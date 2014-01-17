package controllers;

import org.bson.types.ObjectId;

import java.util.Set;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.Logger;
import utils.MongoClientHolder;
import utils.ETagHelper;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.BasicDBObject;
import com.mongodb.WriteResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * Contrôleur Play! générique chargé des interactions avec une base MongoDB
 * 
 * @author DTI/MDV/IDV/ADMC
 * @version 1.0
 * @date 31 oct. 2013
 * @copyright La Poste 2013
 */
public class MongoExplorer extends Controller {

	/* Traitement ETAG */
	private static Result etag(String doc) {
		final String eTag = ETagHelper.getMd5Digest(doc.getBytes());
		if (eTag.equals(request().getHeader(IF_NONE_MATCH))) {
			return status(NOT_MODIFIED);
		}
		else {
			response().setHeader(ETAG, eTag);
			return ok(doc);	
		}
	}

	/**
	 * Création d'un ressource dans une collection passée en argument
	 *
	 * @param collectionName Le nom de la collection à enrichir	 
	 * @return Result Le résultat du traitement
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create(String collectionName) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			collection.insert((DBObject) JSON.parse(request().body().asJson().toString()));
			return created();

		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Récupération du premier document de la collection
	 * @param collectionName Le nom de la collection
	 *
	 * @return Result Le résultat du traitement
	 */
	public static Result getInCollection(String collectionName, String docID) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			DBObject doc = collection.findOne(new BasicDBObject("_id", new ObjectId(docID)));
			if (doc == null) {
				return notFound("Document introuvable");
			}
			return etag(doc.toString());
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Récupération du premier document de la collection
	 * @param collectionName Le nom de la collection
	 *
	 * @return Result Le résultat du traitement
	 */
	public static Result updateInCollection(String collectionName, String docID) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			WriteResult wr = collection.update(
				new BasicDBObject("_id", new ObjectId(docID)), 
				(DBObject) JSON.parse(request().body().asJson().toString())
			);
			if (wr.getN() == 0) {
				return notFound("Document introuvable");
			}
			return ok();
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Supression d'un ressource dans une collection passée en argument via son id
	 *
	 * @param collectionName Le nom de la collection à enrichir	 	 
	 * @param docID L'identifiant du document à supprimer
	 *
	 * @return Result Le résultat du traitement
	 */
	public static Result delete(String collectionName, String docID) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			WriteResult wr = collection.remove(new BasicDBObject("_id", new ObjectId(docID)));
			if (wr.getN() == 0) {
				return notFound("Document introuvable");
			}
			return ok("Document supprimé avec succès");
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Récupération du premier document de la collection
	 *
	 * @param collectionName Le nom de la collection
	 *
	 * @return Result Le résultat du traitement
	 */
	public static Result firstInCollection(String collectionName) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			DBObject doc = collection.findOne();
			if (doc == null) {
				return notFound("Document introuvable");
			}
			return etag(doc.toString());
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Récupération de toutes les occurences de la collection
	 *
	 * @param collectionName Le nom de la collection
	 *
	 * @return Result Le résultat du traitement
	 */
	public static Result collection(String collectionName) {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			if (!db.collectionExists(collectionName)) {
				return notFound("Collection introuvable");
			}
			DBCollection collection = db.getCollection(collectionName);
			if (collection.count() > 0) {
				DBCursor cursor = collection.find();
				StringBuffer sb = new StringBuffer("["); 
				int i = 0;
				while (cursor.hasNext()) {
					i ++;
					sb.append(cursor.next().toString());
					if(i < collection.count()) {
						sb.append(",");
					}
				}
				sb.append("]");
				response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
				return etag(sb.toString());
			}
			else {
				return notFound("Collection vide");
			}
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

	/**
	 * Récupération des noms des collections présentent dans la base
 	 *	
	 * @return Result Le résultat du traitement
	 */
	public static Result collections() {
		try {
			DB db = MongoClientHolder.getInstance().getDB();
			Set<String> collections = db.getCollectionNames();
			JsonNode json = Json.toJson(collections);
			return ok(json);
		}
		catch (Exception e) {
			return internalServerError(e.getMessage());
		}
	}

}
