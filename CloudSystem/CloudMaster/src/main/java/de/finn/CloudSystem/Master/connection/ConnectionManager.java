package de.finn.CloudSystem.Master.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.finn.CloudSystem.Master.main.CloudMaster;

public class ConnectionManager {

	public ConnectionManager() {}
	
	public Thread proxyListener;
	public Thread serverListener;
	public Thread wrapperListener;
	
	public ServerSocket proxyServer;
	public ServerSocket serverServer;
	public ServerSocket wrapperServer;
	
	public ArrayList<WrapperConnection> wrapperConnections = new ArrayList<WrapperConnection>();
	public ArrayList<ServerConnection> serverConnections = new ArrayList<ServerConnection>();
	public ArrayList<ProxyConnection> proxyConnections = new ArrayList<ProxyConnection>();
	
	public String players = "";
	
	public void start() {
		wrapperListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					wrapperServer = new ServerSocket(CloudMaster.instance.wrapperPort);
					while(true) {
						Socket client = wrapperServer.accept();
						WrapperConnection con = new WrapperConnection(client);
						wrapperConnections.add(con);
						Thread th = new Thread(con);
						th.setName("WrapperConnection#" + wrapperConnections.size());
						th.start();
					}
				} catch (IOException e) {}
			}
		});
		
		serverListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					serverServer = new ServerSocket(CloudMaster.instance.serverPort);
					while(true) {
						Socket client = serverServer.accept();
						ServerConnection sc = new ServerConnection(client);
						serverConnections.add(sc);
						Thread th = new Thread(sc);
						th.start();
					}
				} catch(IOException ex) {}
			}
		});
		
		proxyListener = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					proxyServer = new ServerSocket(CloudMaster.instance.proxyPort);
					while(true) {
						Socket client = proxyServer.accept();
						ProxyConnection pc = new ProxyConnection(client);
						proxyConnections.add(pc);
						Thread th = new Thread(pc);
						th.start();
					}
				} catch(IOException ex) {}
			}
		});
		
		wrapperListener.setName("WrapperConnectionListener");
		serverListener.setName("ServerConnectionListener");
		proxyListener.setName("ProxyConnectionListener");
		
		wrapperListener.start();
		serverListener.start();
		proxyListener.start();	
	}
	
	
	
	
	
	
}
