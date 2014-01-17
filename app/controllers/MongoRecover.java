package controllers;

import play.libs.F.Either;

import com.mongodb.DBObject;

/**
 * Scénario d'erreur, on récupère une exception que l'on place dans le paramètre
 * gauche du Either (Exception)
 */
public class MongoRecover implements play.libs.F.Function<Throwable, Either<Exception, DBObject>> {	
	@Override
	public Either<Exception, DBObject> apply(Throwable t) {
		return Either.Left((Exception) t);
	}
}
