package de.finn.CloudSystem.Master.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CloudLogger extends PrintStream{

	private boolean isError = false;
	
	public CloudLogger(OutputStream out, boolean isError) {
		super(out);
		this.isError = isError;
	}
	
	@Override
	public void println(String ln) {
		if(!isError) {
			super.println("[" + getDate() + "/Info]: " + ln);
		} else {
			super.println("[" + getDate() + "/Error]: " + ln);
		}
	}

	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		return format.format(new Date());
	}
	
	
	
	
	
}
