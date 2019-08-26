package net.satopay.satoexchange.web;

import bittech.lib.protocol.ListenersManager;
import lib.satopay.JsonCommandExec;

public class ApiModule implements AutoCloseable {
	
	private final ListenersManager listenersManager = new ListenersManager();
	private final JsonCommandExec jsonCommandExec = new JsonCommandExec(listenersManager);
	private final ApiListener apiListener = new ApiListener();
	
	public ApiModule() {
		listenersManager.registerListener(apiListener);
	}
	
	public String onReceived(String message) {
		return jsonCommandExec.onReceived(message);
	}

	
	@Override
	public void close() {
		apiListener.close();
	}
	
	
	

}
