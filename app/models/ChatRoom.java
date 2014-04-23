package models;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Hashtable;
import java.util.Map;

import play.Logger;
import play.libs.Akka;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.WebSocket;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import play.libs.F.Callback;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/** 
 * Acteur gérant la ChatRoom
 */
public class ChatRoom extends UntypedActor {

    /** RESULT_KO */
    public static final String RESULT_KO = "KO";
    /** RESULT_OK */
    public static final String RESULT_OK = "OK";

    // Default room.
    static ActorRef defaultRoom = Akka.system().actorOf(Props.create(ChatRoom.class));

	/** Participants à l'interaction */
    Map<String, WebSocket.Out<JsonNode>> members = new Hashtable<String, WebSocket.Out<JsonNode>>();  

    /**
     * Se connecter sur un interaction existante
     */
    public static void join(final String username, WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) throws Exception {       
        String result = (String) Await.result(
            ask(defaultRoom, new Join(username, in, out), 1000), Duration.create(1, SECONDS)
        );
        if(RESULT_OK.equals(result)) {   
            // Gestion de la fermeture de la socket par le client
            in.onMessage(event -> defaultRoom.tell(new Talk(username, event.get("text").asText()), null));
            in.onClose(() -> defaultRoom.tell(new Quit(username), null));
        } 
        else {
            // La connexion à l'interaction a échoué, on renvoi un flux d'erreur sur la socket de l'utilisateur
            Logger.error("ChatRoom : connexion de " + username + " echouee !");    
            out.write(Json.newObject().put("error", result));
        }
    }

    /** 
     * Comment l'interaction va traiter les différents types de message
     * @param message
     * @throws Exception
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    public void onReceive(Object message) throws Exception {  
       	if(message instanceof Join) {            
    		final Join join = (Join) message;     
            members.put(join.username, join.channel);
            notifyAll("talk", "ChatRoom", join.username + " vient de rejoindre la discussion", false);
            getSender().tell(RESULT_OK, getSelf());
        } 
        else if(message instanceof Talk)  {
            Talk talk = (Talk) message;
            notifyAll("talk", talk.username, talk.text, true);
        } 
        else if(message instanceof Quit)  {
        	Quit quit = (Quit) message;
            members.remove(quit.username);
            notifyAll("talk", quit.username, "vient de quitter la discussion");
            getSender().tell(RESULT_OK, null);
        } 
        else {
            unhandled(message);
        }
    }
    
    /**
     * notifyAll
     * @param kind type de message
     * @param user Envoyeur
     * @param text contenu du message
     * @param excludeSender L'envoyeur est'il exclu de la notification ?
     */
    public void notifyAll(String kind, String user, String text, boolean excludeSender) {
        java.util.Iterator<String> it = members.keySet().iterator();

        while(it.hasNext()) {
            String userTo = it.next();
            if (excludeSender && user.equals(userTo)) { continue; }
            ObjectNode event = Json.newObject().put("kind", kind).put("user", user.toUpperCase())
                    .put("message", text);
            ArrayNode m = event.putArray("members");
            for(String u: members.keySet()) {
                m.add(u);
            }
            members.get(userTo).write(event);
        }
    }

    /**
     * notifyAll
     */
    public void notifyAll(String kind, String user, String text) {
        notifyAll(kind, user, text, false);
    }
    
    // -- Messages
    /**
     * Talk
     */
    public static class Talk {
        final String username;
        final String text;
        
        public Talk(String username, String text) {
            this.username = username;
            this.text = text;
        }        
    }
    
    /**
     * Create
     */
    public static class Join {
        final String username;
        final WebSocket.Out<JsonNode> channel;
        final WebSocket.In<JsonNode> input;
           
        public Join(String username, WebSocket.In<JsonNode> input, WebSocket.Out<JsonNode> channel) {
            this.username = username;
            this.channel = channel;
            this.input = input;
        }
    }  
    
    /**
     * Quit
     */
    public static class Quit {        
        final String username;
        public Quit(String username) {
            this.username = username;
        }        
    }
        
}
