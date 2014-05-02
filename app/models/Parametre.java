package models;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.List;

/**
 * DAO Jongo des Listes
 */
public class Parametre {

    @Id
    public ObjectId id;

    /** Comment convertir l'ID Mongo en String lors du marshalling ! */
    public String getId() {
        return this.id.toString();
    }

    // Méthodes exposées directement par le DAO
    public static Parametre findById(String id) {
        return listes().findOne("{_id:#}", new ObjectId(id)).as(Parametre.class);
    }

    public static WriteResult updateById(String id, String modifier) {
        return listes().update(new ObjectId(id)).with(modifier);
    }

    public static WriteResult insert(String modifier) {
        return listes().insert(modifier);
    }

    public static String insertWithId(String modifier) {
        ObjectId id = ObjectId.get();
        listes().insert("{_id:#, " + modifier.substring(1), id);
        return id.toString();
    }

    public static WriteResult remove(String id) {
        return listes().remove(new ObjectId(id));
    }

    public static Iterable<Parametre> all() {
        return listes().find().as(Parametre.class);
    }


    // Méthodes exposées par un instance de Liste (non utilisées)
    public WriteResult insert() {
        return listes().save(this);
    }
    public WriteResult update(String modifier) {
        return listes().update(this.id).with(modifier);
    }
    public void remove() {
        listes().remove(this.id);
    }

    // Méthodes de commodité
    private static MongoCollection listes() {
        return PlayJongo.getCollection("listes");
    }

}
