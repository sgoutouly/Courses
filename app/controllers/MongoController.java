package controllers;

import org.bson.types.ObjectId;

import play.libs.F.Either;
import play.libs.F.Function0;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MongoClientHolder;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 * Contrôleur Play chargé des interactions avec MongoDB en Asynchrone
 * 
 * @author DTI/MDV/IDV/ADMC
 * @version 1.0
 * @date 31 oct. 2013
 * @copyright La Poste 2013
 */
public class MongoController extends Controller {

	/**
	 * Appel de MongoDB
	 * 
	 * @param matricule Le matricule de l'agent à rechercher
	 * @return Promise<Result> Le résultat différé du traitement
	 */
	public static Promise<Result> service(final String numClient) {		
    	/*
    	 *  Exécution de traitement en asynchrone et envoi d'un promesse de Either en retour.
    	 *  Le Either permet de renvoyer un objet contenant soit la donnée soit l'exception 
    	 */
		Promise<Either<Exception, DBObject>> promiseMongo = Promise.promise(
			/*
			 *  Scénario nominal, on récupère des données que l'on placer dans le paramètre
			 *  droit du Either (le databean) 
			 */
			new Function0<Either<Exception, DBObject>>() {
				@Override
				public Either<Exception, DBObject> apply() throws Throwable {
					DB db = MongoClientHolder.getInstance().getDB();
					DBCollection collection = db.getCollection("KTT0AL35");
					try {
						DBObject doc = collection.findOne(new BasicDBObject("_id", new ObjectId(numClient)));
						if (doc == null) {
							throw new Exception("Document introuvable");
						}
						return Either.Right(doc);
					}
					catch (IllegalArgumentException e) { // Si le format de l'_id est incorrect
						throw new Exception("Idenfiant de document incorrect");
					}					
				}
			}
		).recover(
			/*
			 * Scénario d'erreur, on récupère une exception que l'on place dans le paramètre
			 * gauche du Either (Exception)
			 */
			new MongoRecover()
		);
		
		return promiseMongo.map(
			/*
			 *  Exploitation de la promesse de Either pour renvoyer le résultat après un map
			 *  en fonction de la présence ou non de données : traitement d'exception 
			 */
			new MongoMapper()
		);	

	}

}
