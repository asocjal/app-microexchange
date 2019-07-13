package net.satopay.satoexchange;

import static io.undertow.Handlers.path;
import static io.undertow.Handlers.resource;
import static io.undertow.Handlers.websocket;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import net.satopay.satoexchange.web.ApiModule;

public class App {

	private final static ApiModule apiModule = new ApiModule();

	public static void main(final String[] args) throws Exception {
//		testHomePage();
//		satoClick();
		startServer();
	}

	private static void startServer() {
		System.out.println("Undertow");

		Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0")
				.setHandler(path().addPrefixPath("/api", websocket(new WebSocketConnectionCallback() {

					@Override
					public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
						channel.getReceiveSetter().set(new AbstractReceiveListener() {

							@Override
							protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
								final String messageData = message.getData();
//								for (WebSocketChannel session : channel.getPeerConnections()) {
									String ret = apiModule.onReceived(messageData);
									WebSockets.sendText(ret, channel, null);
//								}
							}
						});
						channel.resumeReceives();
					}

				}))

						.addPrefixPath("/", resource(
								new ClassPathResourceManager(App.class.getClassLoader(), App.class.getPackage()))
										.setDirectoryListingEnabled(false)))
				.build();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.print("Stopping web server...");
			server.stop();
			System.out.println("Done");
		}));

		server.start();
	}

	private static void satoClick() throws Exception {
		System.setProperty("webdriver.gecko.driver", "/home/cd/satoprojects/satoexchange/geckodriver-v0.24.0-linux64/geckodriver");

		WebDriver driver = new FirefoxDriver();
		try {
			driver.get("https://tbtc.bitaps.com/");
			WebElement button = driver.findElement(By.id("receive"));
			for (;;) {
				Thread.sleep(10000);
				button.click();
			}
		} finally {
			driver.close();
		}
	}

	private static void testHomePage() throws Exception {
		System.setProperty("webdriver.gecko.driver", "firefox/geckodriver");

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary("firefox/firefox");
		FirefoxBinary fb = firefoxOptions.getBinary();
		fb.addCommandLineOptions("--headless");
		firefoxOptions.setBinary(fb);

		WebDriver driver = new FirefoxDriver(firefoxOptions);
		try {
			driver.get("http://www.google.com");
			WebElement element = driver.findElement(By.name("q"));
			element.sendKeys("Cheese!\n"); // send also a "\n"
			element.submit();

			// wait until the google page shows the result
			WebElement myDynamicElement = (new WebDriverWait(driver, 10))
					.until(ExpectedConditions.presenceOfElementLocated(By.id("resultStats")));

			List<WebElement> findElements = driver.findElements(By.xpath("//*[@id='rso']//h3"));

			// this are all the links you like to visit
			for (WebElement webElement : findElements) {
				System.out.println(webElement.getText());
			}
		} finally {
			driver.close();
		}
	}

}
