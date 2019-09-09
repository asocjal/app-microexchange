package net.satopay.seatoexhcnage.banks;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

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
				"/home/cd/satoprojects/satoexchange/geckodriver-v0.24.0-linux64");
//
//		FirefoxBinary firefoxBinary = new FirefoxBinary();
//	    firefoxBinary.addCommandLineOptions("");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary("firefox/firefox");
		FirefoxBinary fb = firefoxOptions.getBinary();
		fb.addCommandLineOptions("--headless");
		firefoxOptions.setBinary(fb);
//		firefoxOptions.setBinary(firefoxBinary);

		WebDriver driver = new FirefoxDriver();
		try {
			driver.get("https://tbtc.bitaps.com/");
			WebElement button = driver.findElement(By.id("receive"));
			for(;;) {
				Thread.sleep(10000);
				button.click();
			}
		} finally {
			driver.close();
		}
	}

}
