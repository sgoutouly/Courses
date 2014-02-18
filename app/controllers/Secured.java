package controllers;

import play.mvc.*;
import play.mvc.Http.*;

import models.*;

/* */
public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {

        //return redirect(routes.Application.login());
        return unauthorized("Veuillez vous authentifier pour accéder à cette ressource");
    }
    
}