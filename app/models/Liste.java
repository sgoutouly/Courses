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
public class Liste {

    @Id
    public ObjectId id;
    public String dateRedaction;
    public String dateCourse;
    public List<Course> courses;

    /** Comment convertir l'ID Mongo en String lors du marshalling ! */
    public String getId() {
        return this.id.toString();
    }

    // Méthodes exposées directement par le DAO
    public static Liste findById(String id) {
        return listes().findOne("{_id:#}", new ObjectId(id)).as(Liste.class);
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

    public static Iterable<Liste> all() {
        return listes().find().as(Liste.class);
    }


    // Modèle de données
    /** Course */
    public static class Course {
        public String designation;
        public int qte;
        public String unite;
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
