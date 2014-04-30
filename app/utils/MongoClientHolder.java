package utils;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;

import java.util.Arrays;


/**
 * MongoClientHolder : singleton chargÃ© de contenir l'unique 
 * instance de MongoClient (il s'agit d'un composant thread Safe)
 * Permet de fermer le MongoClient, c'est Ã  dire de libÃ©rer les ressources
 * qui lui sont allouÃ©es
 *
 * @author SGY
 * @version 1.0
 * @date 5 dÃ©c. 2013 
 */
public class MongoClientHolder {

    /** instance */
    private static final MongoClientHolder instance = new MongoClientHolder();

    private static final String DEFAULT_DBNAME = "gJUVUgAqUFguwFsIldnlzg";

    /** client */
    private MongoClient client;

    /** thrownException */
    private Exception thrownException;


    /**
     * Constructeur privÃ© du Singleton
     */
    private MongoClientHolder() {
        super();
        try {
            this.client = new MongoClient("lennon.mongohq.com", 10059);
            //this.client = new MongoClient(new MongoClientURI("mongodb://cloudbees:7qXiHIqmJqy3c1WrXw0-KLbmWUTN28o770HymsSDvhbBwBUhyUx_dP2M2A_xl1ZJTIxVtdoD1OrnCf7VxjUEXQ@lennon.mongohq.com:10059/gJUVUgAqUFguwFsIldnlzg"));

        }
        catch(Exception t) {
            this.thrownException = t;
        }
    }

    /**
     * getClient
     * @return MongoClient L'instance de MongoClient stockÃ©e dans le holder
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
     * @return DB La base demandÃ©e
     * @throws Exception si labse est absente du serveur ou si la connexion du client a Ã©chouÃ©
     */
    public DB getDB(String dbName) throws Exception {
        if (this.thrownException == null) {
            final DB db = this.client.getDB(dbName);
            db.authenticate("courses_user", "courses_user".toCharArray());
            return db;
        }
        else {
            throw this.thrownException;
        }
    }

    /**
     * getDb
     * @return DB La base configurÃ©e
     * @throws Exception si la base est absente du serveur ou si la connexion du client a Ã©chouÃ©
     */
    public DB getDB() throws Exception {
        final String dbName = DEFAULT_DBNAME;
        if (this.thrownException == null) {
            final DB db = this.client.getDB(dbName);
            db.authenticate("courses_user", "courses_user".toCharArray());
            return db;
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