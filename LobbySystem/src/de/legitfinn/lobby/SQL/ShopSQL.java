package de.legitfinn.lobby.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class ShopSQL {

	public static MySQL mysql = LobbyServerSQL.mysql;
	
	public static void connect() {
		mysql.update("CREATE TABLE IF NOT EXISTS PSCPayments(UUID VARCHAR(100), TYPE VARCHAR(100), PRICE VARCHAR(100), CODE VARCHAR(100), STATUS VARCHAR(100), DATE VARCHAR(100))");
	}
	
	public static void addPayment(UUID uuid, String type, String price, String code, String date){
		mysql.update("INSERT INTO PSCPayments (UUID, TYPE, PRICE, CODE, STATUS, DATE) VALUES ('" + uuid + "','" + type + "','" + price + "','" + code + "','WAITING','" + date + "')");
	}
	
	public static boolean isWaiting(UUID uuid){
		ResultSet rs = mysql.query("SELECT STATUS FROM PSCPayments WHERE UUID = '" + uuid + "'");
		try {
			while(rs.next()){
				if(rs.getString("STATUS").equalsIgnoreCase("waiting")){
					return true;
				}
			}
		} catch (SQLException e) {}
		return false;
	}
	
	public static void acceptPayment(boolean accept, UUID uuid, String date){
		if(accept){
			mysql.update("UPDATE PSCPayments SET STATUS = 'ACCEPTED' WHERE UUID = '" + uuid + "' AND DATE = '" + date + "'");
		}else {
			mysql.update("UPDATE PSCPayments SET STATUS = 'DENIED' WHERE UUID = '" + uuid + "' AND DATE = '" + date + "'");
		}
	}
	
	public static ArrayList<String> getPaymentsDENIED(){
		ArrayList<String> pay = new ArrayList<>();
		ResultSet rs = mysql.query("SELECT * FROM PSCPayments");
		try {
			while(rs.next()){
				if(rs.getString("STATUS").equalsIgnoreCase("denied")){
					String uuid = rs.getString("UUID");
					String type = rs.getString("TYPE");
					String price = rs.getString("PRICE");
					String date = rs.getString("DATE");
					String status = rs.getString("STATUS");
					String code = rs.getString("CODE");
					String add = uuid + "§<-->§" + type + "§<-->§" + price + "§<-->§" + date + "§<-->§" + status + "§<-->§" + code;
					pay.add(add);
				}
			}
		} catch (SQLException e) {}
		return pay;
	}
	
	public static ArrayList<String> getPaymentsACCEPTED(){
		ArrayList<String> pay = new ArrayList<>();
		ResultSet rs = mysql.query("SELECT * FROM PSCPayments");
		try {
			while(rs.next()){
				if(rs.getString("STATUS").equalsIgnoreCase("accepted")){
					String uuid = rs.getString("UUID");
					String type = rs.getString("TYPE");
					String price = rs.getString("PRICE");
					String date = rs.getString("DATE");
					String status = rs.getString("STATUS");
					String code = rs.getString("CODE");
					String add = uuid + "§<-->§" + type + "§<-->§" + price + "§<-->§" + date + "§<-->§" + status + "§<-->§" + code;
					pay.add(add);
				}
			}
		} catch (SQLException e) {}
		return pay;
	}
	
	public static ArrayList<String> getPaymentsWAITING(){
		ArrayList<String> pay = new ArrayList<>();
		ResultSet rs = mysql.query("SELECT * FROM PSCPayments");
		try {
			while(rs.next()){
				if(rs.getString("STATUS").equalsIgnoreCase("waiting")){
					String uuid = rs.getString("UUID");
					String type = rs.getString("TYPE");
					String price = rs.getString("PRICE");
					String date = rs.getString("DATE");
					String status = rs.getString("STATUS");
					String code = rs.getString("CODE");
					String add = uuid + "§<-->§" + type + "§<-->§" + price + "§<-->§" + date + "§<-->§" + status + "§<-->§" + code;
					pay.add(add);
				}
			}
		} catch (SQLException e) {}
		return pay;
	}
	
	public static int waitingamount(UUID uuid){
		ResultSet rs = mysql.query("SELECT STATUS FROM PSCPayments WHERE UUID = '" + uuid + "'");
		try {
			int i = 0;
			while(rs.next()){
				if(rs.getString("STATUS").equalsIgnoreCase("waiting")){
					i++;
				}
			}
			return i;
		} catch (SQLException e) {}
		return 0;
	}
	
}
