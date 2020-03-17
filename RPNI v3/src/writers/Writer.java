package writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Writer class to write an item to a file.
 * @author Romain
 *
 */
public class Writer {
	/**
	 * Adds str to the given file.
	 * @param fileName
	 * @param str
	 */
	public static void write(String fileName, String str) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(fileName, true));
			wr.write(str);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Clears the given file.
	 * @param fileName
	 */
	public static void clear(String fileName) {
		try {
			PrintWriter wr = new PrintWriter(fileName);
			wr.print("");
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
