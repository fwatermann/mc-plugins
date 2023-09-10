package de.legitfinn.friend;

public enum FriendMessages {

	sent_request("§7[§4Freunde§7] §aDu hast %player% §aeine Freundschaftsanfrage gesendet."),
	receive_request("§7[§4Freunde§7] %player% §ahat dir eine Freundschaftsanfrage gesendet."),
	norequests("§7[§4Freunde§7] §cDer Spieler %player% §cakzeptiert keine Freundschaftsanfragen."),
	hasAlreadyRequest("§7[§4Freunde§7] %player% hat bereits eine Anfrage von dir erhalten."),
	alreadyFriends("§7[§4Freunde§7] §cDu bist bereits mit %player% §cbefreundet."),
	other_friend_remove("§7[§4Freunde§7] %player% §chat die Freundschaft aufgelößt."),
	friend_remove("§7[§4Freunde§7] §cDu hast die Freundschaft mit %player% §caufgelößt."),
	no_friend_name("§7[§4Freunde§7] §cDu hast keinen Freund mit dem Namen %player%§c."),
	never_on_network("§7[§4Freunde§7] §cDieser Spieler war noch nie auf dem Netzwerk."),
	request_accept("§7[§4Freunde§7] §aDu hast die Freundschaftsanfrage von %player% §aangenommen."),
	other_request_accept("§7[§4Freunde§7] %player% §ahat deine Freundschaftsanfrage angenommen."),
	request_deny("§7[§4Freunde§7] §cDu hast die Freundschaftsanfrage von %player% §cabgelehnt."),
	other_request_deny("§7[§4Freunde§7] %player% §chat deine Freundschaftsanfrage abgelehnt."),
	toggle_on_teleport("§7[§4Freunde§7] §aEs kann nun wieder zu dir ges"),
	setting_party_activ("§7[§4Freunde§7] §aPartyanfragen aktiviert."),
	setting_party_disable("§7[§4Freunde§7] §cPartyanfragen deaktiviert."),
	setting_request_activ("§7[§4Freunde§7] §aFreundschaftsanfragen aktiviert."),
	setting_request_disable("§7[§4Freunde§7] §cFreundschaftsanfragen deaktiviert."),
	setting_msg_activ("§7[§4Freunde§7] §aPrivate Nachrichten aktiviert."),
	setting_msg_disable("§7[§4Freunde§7] §cPrivate Nachrichten deaktiviert."),
	setting_teleport_activ("§7[§4Freunde§7] §aNachjoinen aktiviert."),
	setting_teleport_disable("§7[§4Freunde§7] §cNachjoinen deaktiviert."),
	setting_clan_activ("§7[§4Freunde§7] §aClananfragen aktiviert."),
	setting_clan_disable("§7[§4Freunde§7] §cClananfragen deaktiviert."),
	setting_notify_activ("§7[§4Freunde§7] §aOn-/Offlinenachrichten aktiviert."),
	setting_notify_disable("§7[§4Freunde§7] §cOn-/Offlinenachrichten deaktiviert."),
	teleport("§7[§4Freunde§7] §aDu wurdest zu %player% auf den Server %server% verschoben");
	
	String name;
	private FriendMessages(String text) {
		name = text;
	}
	
	public String getMessage() {
		return name;
	}
	
	
	
	
}
