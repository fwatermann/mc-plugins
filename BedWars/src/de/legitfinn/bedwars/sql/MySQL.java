package de.legitfinn.bedwars.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class MySQL {

	private String HOST = "";
	private String DATABASE = "";
	private String USER = "";
	private String PASSWORD = "";
	private JavaPlugin instance;

	private Connection con;

	public MySQL(String host, String database, String user, String password, JavaPlugin instance) {
		this.HOST = host;
		this.DATABASE = database;
		this.USER = user;
		this.PASSWORD = password;
		this.instance = instance;
		connect();
		reconnecter();
	}

	public void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoReconnect=true",
					USER, PASSWORD);
			System.out.println("[MySQL] Die Verbindung zur MySQL wurde hergestellt!");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("[MySQL] Die Verbindung zur MySQL ist fehlgeschlagen! Fehler: " + e.getMessage());
		}
	}

	public void close() {
		try {
			if (con != null) {
				con.close();
				System.out.println("[MySQL] Die Verbindung zur MySQL wurde Erfolgreich beendet!");
			}
		} catch (SQLException e) {
			System.out.println("[MySQL] Fehler beim beenden der Verbindung zur MySQL! Fehler: " + e.getMessage());
		}
	}

	public void update(String qry) {
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
	}

	public ResultSet query(String qry) {
		ResultSet rs = null;

		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException e) {
			connect();
			System.err.println(e);
		}
		return rs;
	}

	public void reconnecter() {
		Bukkit.getScheduler().runTaskLater(instance, new Runnable() {

			@Override
			public void run() {
				close();
				connect();
				reconnecter();
			}
		}, 30*20);
	}
}