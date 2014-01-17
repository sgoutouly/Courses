package utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;


/**
 * MongoClientHolder : singleton chargé de contenir l'unique 
 * instance de MongoClient (il s'agit d'un composant thread Safe)
 * Permet de fermer le MongoClient, c'est à dire de libérer les ressources
 * qui lui sont allouées
 *
 * @author SGY
 * @version 1.0
 * @date 5 déc. 2013 
 * @copyright La Poste 2013
 */
public class MongoClientHolder {
	
	/** instance */
	private static final MongoClientHolder instance = new MongoClientHolder();

	private static final String DEFAULT_DBNAME = "courses_db";
	
	/** client */
	private MongoClient client;
	
	/** thrownException */
	private Exception thrownException;

	
	/**
	 * Constructeur privé du Singleton
	 */
	private MongoClientHolder() {
		super();
		try {
			this.client = new MongoClient("192.168.1.105", 27017);					
		}
		catch(Exception t) {
			this.thrownException = t;
		}
	}
	
	/**
	 * getClient
	 * @return MongoClient L'instance de MongoClient stockée dans le holder
	 */
	public MongoClient getClient() throws Exception {
		if (this.thrownException == null) {
			return this.client;
		}
		else {
			throw this.thrownException;
		}
	}

	/**
	 * getDb
	 * @param dbName Le nom de la base
	 * @return DB La base demandée
	 * @throws Exception si labse est absente du serveur ou si la connexion du client a échoué
	 */
	public DB getDB(String dbName) throws Exception {
		if (this.thrownException == null) {
			if (this.client.getDatabaseNames().contains(dbName)) {
				return this.client.getDB(dbName);
			}
			else {
				throw new Exception("La base " + dbName + " est absente du serveur.");
			}
		}
		else {
			throw this.thrownException;
		}
	}
	
	/**
	 * getDb
	 * @return DB La base configurée
	 * @throws Exception si la base est absente du serveur ou si la connexion du client a échoué
	 */
	public DB getDB() throws Exception {
		final String dbName = DEFAULT_DBNAME;
		if (this.thrownException == null) {
			if (this.client.getDatabaseNames().contains(dbName)) {
				return this.client.getDB(dbName);
			}
			else {
				throw new Exception("La base " + dbName + " est absente du serveur.");
			}
		}
		else {
			throw this.thrownException;
		}
	}	
	
	/**
	 * getInstance
	 * @return MongoClientHolder L'instance Unique
	 */
	public static MongoClientHolder getInstance() {
		return instance;
	}
	
	/**
	 * close ferme le client Mongo si il existe
	 */
	public static void close() {
		if (instance.thrownException != null) {
			instance.client.close();
		}		
	}
	
	
}
