package utils;

import play.mvc.Result;

/**
 * PlaySAHelper.java
 *
 * @author SGY
 * @version 1.0
 * @date 7 nov. 2013 
 * @copyright La Poste 2013
 */
public class ExceptionHelper {
	
	/**
	 * handleException
	 * @param coreException
	 * @return
	 */
	public static Result handleException(Exception exception) {
		return null;
		/*final String errorCode = exception.getErrorCode();
		if (errorCode.startsWith("01")) {
			return Controller.badRequest(((Exception) coreException).getMessage() + " (" + errorCode + ")"); 
		}
		else if (errorCode.startsWith("02")) {
			return Controller.notFound(((Exception) coreException).getMessage() + " (" + errorCode + ")");
		}
		else {
			return Controller.internalServerError(((Exception) coreException).getMessage());
		}*/
	}

}
