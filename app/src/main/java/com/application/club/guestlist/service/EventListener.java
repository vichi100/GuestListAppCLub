package com.application.club.guestlist.service;

import org.json.JSONException;

public interface EventListener {
	
	public void eventReceived(String message) throws JSONException;

}
