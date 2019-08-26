package net.satopay.satoexchange;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import net.satopay.satoexchange.bankbots.BankBotsModule;
import net.satopay.satoexchange.web.WebModule;

public class App {

	public static void main(final String[] args) throws Exception {
//		satoClick();
		startServer();
	}

	private static void startServer() {
		WebModule webModule = new WebModule();
		BankBotsModule bankBotsModule = new BankBotsModule(false);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			webModule.close();
			bankBotsModule.close();

			Runtime.getRuntime().halt(0); // TODO: Needed because webModule do not want to stop properly
		}));
	}

	private static void satoClick() throws Exception {
		System.setProperty("webdriver.gecko.driver",
				"geckodriver");

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		FirefoxBinary fb = firefoxOptions.getBinary();
		fb.addCommandLineOptions("--headless");
		firefoxOptions.setBinary(fb);

		
		WebDriver driver = new FirefoxDriver(firefoxOptions);
		try {
			driver.get("https://tbtc.bitaps.com/");
			WebElement input = driver.findElement(By.id("faucet-address"));
			input.sendKeys("2NBMEXnU3untFp1gJd4xLvvUhUdr5gv1qfp");
			
			WebElement button = driver.findElement(By.id("receive"));
			for (;;) {
				Thread.sleep(10000);
				button.click();
			}
		} finally {
			driver.close();
		}
	}

}
