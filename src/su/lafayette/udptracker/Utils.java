package su.lafayette.udptracker;

import org.apache.log4j.Logger;

import java.util.Random;

public class Utils {
	private static final Logger logger = Logger.getLogger(Utils.class);

	public static String getHexString(byte[] buffer) throws Exception {
		String result = "";
		for (byte b : buffer) {
			result += Integer.toString((b & 0xff) + 0x100, 16).substring(1) + " ";
		}
		return result;
	}

	public final static Random random = new Random();

}
