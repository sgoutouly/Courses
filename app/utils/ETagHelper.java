package utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import play.mvc.Controller;

/**
 * PlaySAHelper.java
 * 
 * @author SGY
 * @version 1.0
 * @date 7 nov. 2013
 */
public class ETagHelper {

	/**
	 * isITagValid
	 * @param key
	 * @return
	 */
	public static boolean isETagValid(final String key) {
		final String eTag = (String) play.cache.Cache.get(key);
		return eTag != null && eTag.equals(Controller.request().getHeader(Controller.IF_NONE_MATCH)) ? true : false;
	}
	
	/**
	 * cacheETag
	 * @param key
	 * @param eTag
	 */
	public static void cacheETag(final String key, final String eTag) {
		play.cache.Cache.set(key, eTag);	
	}
	
	
	/**
	 * getMd5Digest
	 * 
	 * @param bytes
	 * @return String
	 */
	public static String getMd5Digest(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(bytes);
			final BigInteger number = new BigInteger(1, messageDigest);
			// prepend a zero to get a "proper" MD5 hash value
			final StringBuffer sb = new StringBuffer('0');
			sb.append(number.toString(16));
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 cryptographic algorithm is not available.", e);
		}

	}

	/**
	 * longToBytes
	 * 
	 * @param l
	 * @return
	 */
	public static byte[] longToBytes(long l) {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		while (l != 0) {
			bytes.add((byte) (l % (0xff + 1)));
			l = l >> 8;
		}
		byte[] bytesp = new byte[bytes.size()];
		for (int i = bytes.size() - 1, j = 0; i >= 0; i--, j++) {
			bytesp[j] = bytes.get(i);
		}
		return bytesp;
	}
}
