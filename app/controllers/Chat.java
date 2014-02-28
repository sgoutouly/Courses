package controllers;

import models.ChatRoom;
import play.mvc.*;
import play.Logger;
import play.mvc.Controller;
import play.mvc.WebSocket;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Chat
 */
@Security.Authenticated(Secured.class)
public class Chat extends Controller {
	
	/**
	 * graphStream
	 * @param graphIds
	 * @return WebSocket<JsonNode>
	 */
	public static WebSocket<JsonNode> connect() {
		final String username = session().get("email");
		return new WebSocket<JsonNode>() {
			public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                try { 
                	ChatRoom.join(username, in, out);
                } 
                catch (Exception ex) {
                    ex.printStackTrace();
                }
			}			
		};
	}

}
