package controllers;

import java.util.*;

import play.Routes;
import play.libs.F;
import play.libs.WS;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;
import controllers.routes;
import views.html.*;

public class Application extends Controller {

    private static Map<String, String> users = new HashMap<String, String>(2);
    static  {
        users.put("sylvain", "sylvain");
        users.put("christine", "christine");
    }

    // -- Authentication
    public static class Login {
        public String email;
        public String password;
        
        public String validate() {
            if(!users.get(email).equals(password)) {
                return "Identifiant ou mot de passe incorrect";
            }
            return null;
        }
    }

    /**
     * Login page.
     */
    public static Result login() {
        return ok(login.render(form(Login.class)));
    }
    
    /**
     * Handle login form submission.
     */
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if(loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        }
        else {
            session("email", loginForm.get().email);
            return redirect(routes.Assets.at("index.html#/listesEnCours"));
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "Vous avez été déconnecté !");
        return redirect(routes.Application.login());
    }

    // -- Javascript routing
    public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("jsRoutes",
                // Routes for Chat
                controllers.routes.javascript.Chat.connect(),
                // Routes for Mongo
                controllers.routes.javascript.MongoExplorer.firstInCollection(),
                // Routes for Listes over Jongo
                controllers.routes.javascript.Listes.listes(),
                controllers.routes.javascript.Listes.liste(),
                controllers.routes.javascript.Listes.add(),
                controllers.routes.javascript.Listes.delete(),
                controllers.routes.javascript.Listes.update()
            )
        );
    }

    /*
    public static F.Promise<Result> reactiveComposition() {


        final F.Promise<WS.Response> twitterPromise = WS.url("http://www.twitter.com").get();
        final F.Promise<WS.Response> typesafePromise = WS.url("http://www.typesafe.com").get();

        return twitterPromise.flatMap((twitter) ->
                typesafePromise.map((typesafe) ->
                        ok(twitter.getBody() + typesafe.getBody())));
    }*/

}
