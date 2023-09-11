package de.finn.CloudSystem.Master.Security;

public class Encryptor {

	public static String encrypt(String input, String key) {
		if(input == null) {
			return null;
		}
		String ret = "";
		for(Character all : input.toCharArray()) {
			char c = 97;
			for(Character bll : key.toCharArray()) {
				c = moveChar(all, (int)bll);
			}
			ret += c;
		}
		return ret;
	}
	
	public static String decrypt(String input, String key) {
		if(input == null) {
			return null;
		}
		String ret = "";
		for(Character all : input.toCharArray()) {
			char c = 97;
			for(Character bll : key.toCharArray()) {
				c = moveChar(all, -bll);
			}
			ret += c;
		}
		return ret;
	}
	
	private static char moveChar(char i, int b) {
		return (char)(i + b);
	}
	
	
	
	
	
}
