package writers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Writer {
	public static void write(String fileName, String str) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(fileName, true));
			wr.write(str);
			wr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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
