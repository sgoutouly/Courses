package controllers;

import models.GraphInteractionManagerActor;
import play.Logger;
import play.mvc.Controller;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * RealTime
 * 
 * @author DISFE/DTI/MDV/IDV
 * @version 1.0
 * @date 12 nov. 2013
 * @copyright La Poste 2013
 */
public class TempsReel extends Controller {
	
	/**
	 * graphStream
	 * @param graphIds
	 * @return WebSocket<JsonNode>
	 */
	public static WebSocket<JsonNode> graphStreamServer(final String username) {
		Logger.debug("graphStreamServer");
		return new WebSocket<JsonNode>() {
			/* 
			 * onReady
			 */
			public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                // Join the chat room.
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
		Logger.debug("graphStreamClient");
		return new WebSocket<JsonNode>() {
			/* 
			 * onReady
			 */
			public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                // Join the chat room.
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
