
import play.Application;
import play.GlobalSettings;
import play.Logger;
import utils.MongoClientHolder;

/**
 * Global
 *
 * @author DTI/MDV/IDV/ADMC
 * @version 1.0
 * @date 5 nov. 2013 
 * @copyright La Poste 2013
 */
public class Global extends GlobalSettings {

    /** 
     * onStop
     * @param app
     * @see play.GlobalSettings#onStop(play.Application)
     */
    public void onStop(Application app) {
    	
    	Logger.info("------------------------------------------------------------");
    	Logger.info("Arrêt de l'application");
        Logger.info("------------------------------------------------------------");
        MongoClientHolder.close();
    	Logger.info("------------------------------------------------------------");
    
    }
	
}
