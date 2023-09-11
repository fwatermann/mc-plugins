package de.finn.CloudSystem.Wrapper.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CloudLogger extends PrintStream{

	private boolean isError = false;
	private PrintWriter writer = null;
	
	public CloudLogger(OutputStream out, boolean isError) {
		super(out);
		this.isError = isError;
		File file = new File("./log.log");
		try {
			if(!file.exists()) file.createNewFile();
			writer = new PrintWriter(new FileWriter(file, true), true);
		} catch(IOException ex) {}
	}
	
	@Override
	public void println(String ln) {
		if(!isError) {
			super.println("[" + getDate() + "/Info]: " + ln);
			writer.println("[" + getDate() + "/Info]: " + ln);
		} else {
			super.println("[" + getDate() + "/Error]: " + ln);
			writer.println("[" + getDate() + "/Error]: " + ln);
		}
	}

	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date());
	}
	
	
	
	
	
}
