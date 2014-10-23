package models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.ObjectIdDeserializer;
import uk.co.panaxiom.playjongo.PlayJongo;
import utils.JSONResultHandler;


import java.util.List;

/**
 * DAO Jongo des Listes
 */
public class Liste {

    @JsonSerialize(using = utils.ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)
    public String _id; 
    public String dateRedaction;
    public String dateCourse;
    public List<Course> courses;


    // Méthodes exposées directement par le DAO
    public static Liste findById(String id) {
        return listes().findOne("{_id:#}", new ObjectId(id)).as(Liste.class);
    }

    // Méthodes exposées directement par le DAO
    public static String findByIdAsString(String id) {
        return listes().findOne("{_id:#}", new ObjectId(id)).map(new JSONResultHandler());

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
    /*public WriteResult update(String modifier) {
        return listes().update(this.id).with(modifier);
    }
    public void remove() {
        listes().remove(this.id);
    }*/

    // Méthodes de commodité
    private static MongoCollection listes() {
        return PlayJongo.getCollection("listes");
    }

}
