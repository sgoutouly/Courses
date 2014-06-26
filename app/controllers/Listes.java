package controllers;

import com.mongodb.*;
import models.Liste;
import models.Parametre;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.ETagHelper;

import static play.libs.Json.*;

/**
 * Contrôleur Play! de Gestion CRUD des listes basé sur un DAO Jongo
 * 
 * @author SGY
 * @version 1.0
 * @date 31 oct. 2013
 */
@Security.Authenticated(Secured.class)
public class Listes extends Controller {

	/**
     * Ajout d'une liste
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result add() {
		try {
			final String id = Liste.insertWithId(request().body().asJson().toString());
			response().setHeader(LOCATION, controllers.routes.Listes.liste(id).url());
			return created();
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

    /**
     * Récupération de toutes les listes
     */
    public static Result listes() {
        try {
            return ok(toJson(Liste.all()));
        }
        catch (Exception e) {
            e.printStackTrace();
            return internalServerError(e.getMessage());
        }
    }

	/**
	 * Récupération d'une liste
	 */
	public static Result liste(String docID) {
		try {
             //Liste liste = Liste.findById(docID);
             final String liste = Liste.findByIdAsString(docID);
             return liste == null ? notFound("Document introuvable") : okJson(liste);
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
    }

    private static Result okJson(String json) {
        response().setHeader(CONTENT_TYPE, "application/json");
        return ok(json);
    }

	/**
	 * Maj d'une liste
	 */
	public static Result update(String docID) {
		try {
            final WriteResult wr = Liste.updateById(docID, request().body().asJson().toString());
			return wr.getN() == 0 ? notFound("Document introuvable") : ok();
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Supression d'une liste
	 */
	public static Result delete(String docID) {
		try {
  			final WriteResult wr =  Liste.remove(docID);
            return wr.getN() == 0 ? notFound("Document introuvable") : ok("Document supprimé avec succès");
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

    /**
     * Renvoi les Parametres
     */
    public static Result parametres() {
        final Parametre parametre = Parametre.findOne();
        return parametre == null ? notFound("Paramètres introuvables") : ok(toJson(parametre));
    }

    /* Traitement ETAG */
    private static Result etag(String doc) {
        final String eTag = ETagHelper.getMd5Digest(doc.getBytes());
        if (eTag != null && eTag.equals(request().getHeader(IF_NONE_MATCH))) {
            return status(NOT_MODIFIED);
        }
        else {
            response().setHeader(ETAG, eTag);
            return ok(toJson(doc));
        }
    }

}
