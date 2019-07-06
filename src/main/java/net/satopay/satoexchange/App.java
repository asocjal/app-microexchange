package net.satopay.satoexchange;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import java.nio.file.Paths;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class App {
	
	private final static ApiModule apiModule = new ApiModule();


    public static void main(final String[] args) {
    	System.out.println("Undertow");
    	
        Undertow server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(path()
                        .addPrefixPath("/socket", websocket(new WebSocketConnectionCallback() {

                            @Override
                            public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                                channel.getReceiveSetter().set(new AbstractReceiveListener() {

                                    @Override
                                    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                                        final String messageData = message.getData();
                                        for (WebSocketChannel session : channel.getPeerConnections()) {
                                        	String ret = apiModule.onReceived(messageData);
                                            WebSockets.sendText(ret, session, null);
                                        }
                                    }
                                });
                                channel.resumeReceives();
                            }

                        }))
                        .addPrefixPath("/", resource(new PathResourceManager(Paths.get("/home/cd/git/app-microexchange/src/main/resources/web"), 100))
                                .setDirectoryListingEnabled(true)))
                .build();

//        server.start();

    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//    	
//    	
//        Undertow server = Undertow.builder()
//                .addHttpListener(80, "localhost")
//                .setHandler(resource(new PathResourceManager(Paths.get("/home/cd/git/app-microexchange/src/main/resources/web"), 100))
//                        .setDirectoryListingEnabled(true))
//                .build();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.print("Stopping web server...");
			server.stop();
			System.out.println("Done");
		}));
		
		
        server.start();
    }

}
