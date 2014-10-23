package models;

import com.mongodb.WriteResult;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.oid.Id;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.List;

/**
 * DAO Jongo des paramètres
 */
public class Parametre {
    /*@JsonSerialize(using = utils.ObjectIdSerializer.class)
    @JsonDeserialize(using = ObjectIdDeserializer.class)*/
    @Id
    public String _id;
    public List<Produit> produits;
    public List<Unite> unites;

    //---- Méthodes exposées directement par le DAO
    public static Parametre findById(String id) {
        return parametres().findOne("{_id:#}", new ObjectId(id)).as(Parametre.class);
    }
    public static Parametre findOne() {
        return parametres().findOne().as(Parametre.class);
    }

    //---- Méthodes de commodité
    private static MongoCollection parametres() {
        return PlayJongo.getCollection("parametres");
    }

    // --- Modèle
    public static class Produit {
        public String designation;
        public String categorie;
    }
    public static class Unite {
        public String designation;
    }
}
