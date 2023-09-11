package de.finn.CloudSystem.Signs.Signs;

public class Server {

	private String servername;
	private String map;
	private int maxPlayer;
	private int onPlayer;
	private String status;
	
	public Server(String servername, String map, int maxPlayer, int onPlayer, String status) {
		this.servername = servername;
		this.map = map;
		this.maxPlayer = maxPlayer;
		this.onPlayer = onPlayer;
		this.status = "waiting";
	}

	public String getServername() {
		return servername;
	}

	public void setServername(String servername) {
		this.servername = servername;
	}

	public String getMap() {
		return map;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public int getMaxPlayer() {
		return maxPlayer;
	}

	public void setMaxPlayer(int maxPlayer) {
		this.maxPlayer = maxPlayer;
	}

	public int getOnPlayer() {
		return onPlayer;
	}

	public void setOnPlayer(int onPlayer) {
		this.onPlayer = onPlayer;
	}
}
