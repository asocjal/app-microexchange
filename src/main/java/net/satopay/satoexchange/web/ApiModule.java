package net.satopay.satoexchange.web;

import bittech.lib.protocol.ListenersManager;

public class ApiModule {
	
	private final ListenersManager listenersManager = new ListenersManager();
	private final JsonCommandExec jsonCommandExec = new JsonCommandExec(listenersManager);
	private final ApiListener apiListener = new ApiListener();
	
	public ApiModule() {
		listenersManager.registerListener(apiListener);
	}
	
	public String onReceived(String message) {
		return jsonCommandExec.onReceived(message);
	}
	
	

}
