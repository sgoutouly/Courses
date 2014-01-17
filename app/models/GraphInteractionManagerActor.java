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

import com.fasterxml.jackson.databind.JsonNode;

/**
 * GraphInteractionManager
 *
 * @author DISFE/DTI/MDV/IDV
 * @version 1.0
 * @date 13 nov. 2013 
 * @copyright La Poste 2013
 */
public class GraphInteractionManagerActor extends UntypedActor {
    
    /** RESULT_KO */
    public static final String RESULT_KO = "KO";
	/** RESULT_OK */
    public static final String RESULT_OK = "OK";
	/** Manager des interactions */
    static ActorRef interactionManager = Akka.system().actorOf(Props.create(GraphInteractionManagerActor.class));   
    /** Liste des interactions en cours */
    static final Map<String, ActorRef> interactions = new Hashtable<String, ActorRef>();
        
    /**
     * Rejoindre le manager : demander la création d'un interaction et s'y connecter
     */
    public static void create(final String username, WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) throws Exception {
        Logger.debug("GraphInteractionManager : connexion de " + username + " ...");    	
        String result = (String) Await.result(ask(interactionManager, new Create(username, in, out), 1000), Duration.create(1, SECONDS));
        if(RESULT_OK.equals(result)) {	      
            // Gestion de la fermeture de la socket par le client
        	Logger.debug("GraphInteractionManager : connexion de " + username + " reussie !");    
            in.onClose( new Callback0() { public void invoke() { interactionManager.tell(new Quit(username, username), null); } } );
        } 
        else {
            // La connexion à l'interaction a échoué, on renvoi un flux d'erreur sur la socket de l'utilisateur
        	Logger.debug("GraphInteractionManager : connexion de " + username + " echouee !");    
            out.write(Json.newObject().put("error", result));
        }
    }    
    
    /**
     * Se connecter sur un interaction existante
     */
    public static void join(final String owner, final String username, WebSocket.In<JsonNode> in, WebSocket.Out<JsonNode> out) throws Exception {
        Logger.debug("GraphInteractionManager : " + username + " demande à rejoindre l'interaction cree par " + owner + " ...");    	
        String result = (String) Await.result(ask(interactionManager, new Join(username, owner, in, out), 1000), Duration.create(1, SECONDS));
        if(RESULT_OK.equals(result)) {	      
            // Gestion de la fermeture de la socket par le client
        	Logger.debug("GraphInteractionManager : connexion de " + username + " reussie !");    
            in.onClose( new Callback0() { public void invoke() { interactionManager.tell(new Quit(username, owner), null); } } );
        } 
        else {
            // La connexion à l'interaction a échoué, on renvoi un flux d'erreur sur la socket de l'utilisateur
        	Logger.debug("GraphInteractionManager : connexion de " + username + " echouee !");    
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
            if(!interactions.containsKey(join.owner)) {
                getSender().tell("Pas d'interaction en cours pour " + join.owner, getSelf());
            }             	
            else {
            	final String result = (String) Await.result(ask(interactions.get(join.owner), join, 1000), Duration.create(1, SECONDS));
            	if (RESULT_OK.equals(result)) {    		
                    Logger.debug("GraphInteractionManager : interaction rejointe par " + join.username + " !");
                   	getSender().tell(RESULT_OK, getSelf());
            	}   
            	else {
            		getSender().tell(RESULT_KO, getSelf());
            	}
            }
        } 
    	else if(message instanceof Create) {     
    		final Create create = (Create) message;
            if(interactions.containsKey(create.owner)) {
                getSender().tell("This username is already used", getSelf());
            } 
            else {
            	Logger.debug("GraphInteractionManager debut de creation d'une nouvelle interaction pour " + create.owner);
            	// On créé l'acteur pour l'interaction et on connecte l'utilisateur
            	final ActorRef interaction = Akka.system().actorOf(Props.create(GraphInteractionActor.class));
            	final String result = (String) Await.result(ask(interaction, create, 1000), Duration.create(1, SECONDS));
            	if (RESULT_OK.equals(result)) {    		
                    Logger.debug("GraphInteractionManager : creation d'une nouvelle interaction pour " + create.owner + " reussie !");
                   	interactions.put(create.owner, interaction);
                   	getSender().tell(RESULT_OK, getSelf());
            	}   
            	else {
            		getSender().tell(RESULT_KO, getSelf());
            	}
            }    		
    	}
        else if(message instanceof Quit)  {
            Quit quit = (Quit) message;
            Logger.debug("GraphInteractionManager : debut deconnexion de " + quit.username);
            interactions.get(quit.owner).tell(quit, null);
            if (interactions.containsKey(quit.username)) {
            	interactions.remove(quit.username);
            }
            Logger.debug("GraphInteractionManager : fin de deconnexion de " + quit.username);
        } 
        else {
            unhandled(message);
        }
    }
    
    
    
    // -- Messages
    
    /**
     * Create
     */
    public static class Create {
        final String owner;
        final WebSocket.Out<JsonNode> channel;
        final WebSocket.In<JsonNode> input;
           
        public Create(String owner, WebSocket.In<JsonNode> input, WebSocket.Out<JsonNode> channel) {
            this.owner = owner;
            this.channel = channel;
            this.input = input;
        }
    }    
    
    /**
     * Join
     */
    public static class Join extends Create {
        final String username;           
        public Join(String username, String owner, WebSocket.In<JsonNode> input, WebSocket.Out<JsonNode> channel) {
        	super(owner, input, channel);
            this.username = username;
        }
    }
    
    /**
     * Quit
     */
    public static class Quit {        
        final String username;
        final String owner;
        
        public Quit(String username, String owner) {
            this.username = username;
            this.owner = owner;
        }        
    }
    
}
