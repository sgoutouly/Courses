/**
 * 
 */
package controllers;

import play.libs.F.Either;
import play.mvc.Controller;
import play.mvc.Result;
import utils.ETagHelper;
import utils.ExceptionHelper;

import com.mongodb.DBObject;


/**
 *  Exploitation de la promesse de Either pour renvoyer le résultat après un map
 *  en fonction de la présence ou non de données : traitement d'exception 
 */
public class MongoMapper implements play.libs.F.Function<Either<Exception, DBObject>, Result> {	
	@Override
	public Result apply(Either<Exception, DBObject> promiseResult) {
		// Traitement nominal : retour du databean en JSON
		if(promiseResult.left == null || !promiseResult.left.isDefined()){
			DBObject doc = promiseResult.right.get();
			// Traitement du ETAG
			final String eTag = ETagHelper.getMd5Digest(doc.toString().getBytes());
			if (eTag.equals(Controller.request().getHeader(Controller.IF_NONE_MATCH)) ) {
				return Controller.status(Controller.NOT_MODIFIED);
			}
			else {
				Controller.response().setHeader(Controller.ETAG, eTag);
				return Controller.ok(doc.toString());	
			}
		}
		else { // Traitement des exceptions
			return ExceptionHelper.handleException((Exception) promiseResult.left.get());
		}
	}	
}
