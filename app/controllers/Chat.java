package controllers;

import models.GraphInteractionManagerActor;
import play.mvc.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * RealTime
 * 
 * @author SGY
 * @version 1.0
 * @date 12 nov. 2013
 * @copyright La Poste 2013
 */

@Security.Authenticated(Secured.class)
public class Chat extends Controller {
	
	/**
	 * graphStream
	 * @param graphIds
	 * @return WebSocket<JsonNode>
	 */
	public static WebSocket<JsonNode> graphStreamServer(final String username) {
		
		return new WebSocket<JsonNode>() {
			public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                try { 
                	GraphInteractionManagerActor.create(username, in, out);
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
			}
		};

	}
		
	/**
	 * graphStream
	 * @param graphIds
	 * @return WebSocket<JsonNode>
	 */
	public static WebSocket<JsonNode> graphStreamClient(final String username, final String owner) {

		return new WebSocket<JsonNode>() {
			public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                try { 
                	GraphInteractionManagerActor.join(owner, username, in, out);
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
			}			
		};

	}

}
