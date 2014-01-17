package models;

import java.util.Hashtable;
import java.util.Map;

import play.Logger;
import play.libs.F.Callback;
import play.libs.Json;
import play.mvc.WebSocket;
import akka.actor.UntypedActor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * GraphInteraction
 *
 * @author DISFE/DTI/MDV/IDV
 * @version 1.0
 * @date 13 nov. 2013 
 * @copyright La Poste 2013
 */
public class GraphInteractionActor extends UntypedActor {

	/** Participants à l'interaction */
    Map<String, WebSocket.Out<JsonNode>> members = new Hashtable<String, WebSocket.Out<JsonNode>>();        

    /** 
     * Comment l'interaction va traiter les différents types de message
     * @param message
     * @throws Exception
     * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
     */
    public void onReceive(Object message) throws Exception {  
       	if(message instanceof GraphInteractionManagerActor.Join) {            
    		final GraphInteractionManagerActor.Join join = (GraphInteractionManagerActor.Join) message;     
			Logger.debug(join.username + " rejoint l'interaction creee par " + join.owner + " ...");
            // On attache le traitement associé à la réception d'un flux sur la socket de cet utilisateur
            // Dans notre cas, il s'agira d'envoyer un message de type Talk a l'interaction
            join.input.onMessage(
                new Callback<JsonNode>() {
                    public void invoke(JsonNode event) {                   
                        Logger.debug("Interaction : Reception d'un message de l'utilisateur : " + join.username);
                        Logger.debug("Interaction : traitement d'un message talk");
                        getSelf().tell(new Talk(join.username, event.get("text").asText()), null);
                    } 
                }
            );
            members.put(join.username, join.channel);
            notifyAll("talk", join.username, "vient de rejoindre l'interaction");
            Logger.debug(join.owner + " a bien rejoint l'interaction !");
            getSender().tell(GraphInteractionManagerActor.RESULT_OK, getSelf());
        } 
       	else if(message instanceof GraphInteractionManagerActor.Create) {
    		final GraphInteractionManagerActor.Create create = (GraphInteractionManagerActor.Create) message;     
			Logger.debug("Nouvelle interaction en cours d'initialisation pour " + create.owner + " ...");
            // On attache le traitement associé à la réception d'un flux sur la socket de cet utilisateur
            // Dans notre cas, il s'agira d'envoyer un message de type Talk ou Udpdate Ã  l'interaction
            create.input.onMessage(
                new Callback<JsonNode>() {
                    public void invoke(JsonNode event) {                   
                        Logger.debug("Interaction : Reception d'un message de l'utilisateur : " + create.owner);
                        if (event.hasNonNull("update")) {
                            Logger.debug("Interaction : traitement d'un message update");
                            getSelf().tell(new Update(create.owner, event.get("update")), null);
                        }
                        else {
                            Logger.debug("Interaction : traitement d'un message talk");
                            getSelf().tell(new Talk(create.owner, event.get("text").asText()), null);
                        }
                    } 
                }
            );
            members.put(create.owner, create.channel);
            Logger.debug("Nouvelle interaction initialisee pour " + create.owner + " !");
            getSender().tell(GraphInteractionManagerActor.RESULT_OK, getSelf());
    	}
        else if(message instanceof Talk)  {
            Talk talk = (Talk) message;
            notifyAll("talk", talk.username, talk.text);
        } 
        else if(message instanceof Update)  {
        	Update update = (Update) message;
            notifyAll("udpate", update.username, update.data);
        } 
        else if(message instanceof GraphInteractionManagerActor.Quit)  {
        	GraphInteractionManagerActor.Quit quit = (GraphInteractionManagerActor.Quit) message;
            Logger.debug("Interaction : Reception d'un message quit de " + quit.username);
            members.remove(quit.username);
            notifyAll("talk", quit.username, "vient de quitter l'interaction");
            Logger.debug("Interaction : traitement du message quit termine pour " + quit.username);
            getSender().tell(GraphInteractionManagerActor.RESULT_OK, null);
        } 
        else {
            unhandled(message);
        }
    }
    
    // Send a Json event to all members
    /**
     * notifyAll
     * @param kind
     * @param user
     * @param text
     */
    public void notifyAll(String kind, String user, JsonNode data) {
        for(WebSocket.Out<JsonNode> channel: members.values()) {
            ObjectNode event = Json.newObject();
            event.put("kind", kind);
            event.put("user", user);
            event.put("data", data);
            ArrayNode m = event.putArray("members");
            for(String u: members.keySet()) {
                m.add(u);
            }
            channel.write(event);
        }
    }
    
    /**
     * notifyAll
     * @param kind
     * @param user
     * @param text
     */
    public void notifyAll(String kind, String user, String text) {
        for(WebSocket.Out<JsonNode> channel: members.values()) {
            ObjectNode event = Json.newObject();
            event.put("kind", kind);
            event.put("user", user);
            event.put("message", text);
            ArrayNode m = event.putArray("members");
            for(String u: members.keySet()) {
                m.add(u);
            }
            channel.write(event);
        }
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
     * Udpate
     */
    public static class Update {
        final String username;
        final JsonNode data;
        
        public Update(String username, JsonNode data) {
            this.username = username;
            this.data = data;
        }        
    }
        
}
