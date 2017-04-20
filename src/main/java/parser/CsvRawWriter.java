package parser;

import java.io.FileWriter;
import java.io.IOException;

public class CsvRawWriter implements Runnable {
	private String fileName;
	private String raw;

	public CsvRawWriter(String fileName, String raw) {
		this.fileName = fileName;
		this.raw = raw;
	}

	public void run() {
		FileWriter fw;
		try {
			fw = new FileWriter(fileName,true);
			fw.write(raw);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}