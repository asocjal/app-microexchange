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
//		testHomePage();
//		satoClick();
		startServer();
	}

	private static void startServer() {
		WebModule webModule = new WebModule();
		BankBotsModule bankBotsModule = new BankBotsModule(false);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			webModule.close();
			bankBotsModule.close();
			System.exit(0);
		}));
	}

	private static void satoClick() throws Exception {
		System.setProperty("webdriver.gecko.driver",
				"/home/cd/satoprojects/satoexchange/geckodriver-v0.24.0-linux64/geckodriver");

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
		System.setProperty("webdriver.gecko.driver", "geckodriver");

		FirefoxOptions firefoxOptions = new FirefoxOptions();
		FirefoxBinary fb = firefoxOptions.getBinary();
		fb.addCommandLineOptions("--headless");
		firefoxOptions.setBinary(fb);

		WebDriver driver = new FirefoxDriver(firefoxOptions);
		try {
			driver.get("https://system.t-mobilebankowe.pl/web/login");

			{ // LOGIN
				WebElement login = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[2]/div/div[2]/div/div/div[3]/div[2]/input"));
				login.sendKeys("70116045\n"); // send also a "\n"

				WebElement button = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[3]/div/button/div/div/span/span"));
				Thread.sleep(100);
				button.click();
				Thread.sleep(1000);
			}

			{ // HASLO
				WebElement passwd = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[2]/div/div[3]/div/div/div[3]/div[2]/input"));
				passwd.sendKeys("w2011PLaserJet!");

				Thread.sleep(100);
				WebElement button = driver.findElement(By.xpath(
						"//*[@id=\"app\"]/div/div[2]/div[2]/div[1]/div[1]/div/div/div/div/div[3]/div/button/div/div/span"));
				button.click();
				Thread.sleep(10000);
			}

			{
				WebElement history = driver.findElement(By.xpath(
						"/html/body/div/div/div[4]/div/div/div[2]/div/div[3]/div/div[1]/div/div/div[3]/div/div/div[2]/div/span"));
				history.click();
				Thread.sleep(10000);
			}

			List<WebElement> elements = driver.findElements(By.className("RjVx6R"));

			for (WebElement el : elements) {
				System.out.println(el.getText());
			}

			Thread.sleep(1000000);

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
