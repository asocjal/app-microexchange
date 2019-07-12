package net.satopay.seatoexhcnage.banks;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}
	
	public void testLipny() {
		
	}

	public void atestHomePage() throws Exception {
		System.setProperty("webdriver.gecko.driver",
				"firefox/geckodriver");
//
//		FirefoxBinary firefoxBinary = new FirefoxBinary();
//	    firefoxBinary.addCommandLineOptions("");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary("firefox/firefox");
		FirefoxBinary fb = firefoxOptions.getBinary();
		fb.addCommandLineOptions("--headless");
		firefoxOptions.setBinary(fb);
//		firefoxOptions.setBinary(firefoxBinary);

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
