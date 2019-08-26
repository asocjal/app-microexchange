package net.satopay.satoexchange.web;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import javax.net.ssl.SSLContext;

import bittech.lib.utils.exceptions.StoredException;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class WebModule implements AutoCloseable {

	private final static ApiModule apiModule = new ApiModule();
	private static Undertow server;

	public WebModule() {

		try {
			System.out.println("Starting microex Web Module");

			SSLContext sslContext = KeystoreLoader.createSSLContext(
					KeystoreLoader.loadKeyStore("/web-inf/microex.keystore", "ElaMaKaca"), "ElaMaKaca");

			server = Undertow.builder()/* .addHttpListener(80, "0.0.0.0") */
					.addHttpsListener(443, "0.0.0.0", sslContext)

					.setHandler(path()

							.addPrefixPath("/api", websocket(new WebSocketConnectionCallback() {

								@Override
								public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
									channel.getReceiveSetter().set(new AbstractReceiveListener() {

										@Override
										protected void onFullTextMessage(WebSocketChannel channel,
												BufferedTextMessage message) {
											final String messageData = message.getData();
											String ret = apiModule.onReceived(messageData);
											WebSockets.sendText(ret, channel, null);
										}
									});
									channel.resumeReceives();
								}

							}))

							.addPrefixPath("/", resource(new ClassPathResourceManager(WebModule.class.getClassLoader(),
									WebModule.class.getPackage())).setDirectoryListingEnabled(false)))
					.build();

			new Thread(() -> { server.start(); }).start();

		} catch (Exception ex) {
			throw new StoredException("Cannot create Web module", ex);
		}
	}

	@Override
	public void close() {
		System.out.print("Stopping web module...");
		if (server != null) {
			server.stop();
		}
		
		apiModule.close();
		
		System.out.println("Web module stopped");
	}

}
