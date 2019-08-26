package net.satopay.seatoexhcnage.banks;

import java.math.BigDecimal;
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

import bittech.lib.utils.Utils;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.satopay.satoexchange.bankbots.BankBotsModule;
import net.satopay.satoexchange.bankbots.BankTxReceivedEvent;

/**
 * Unit test for simple App.
 */
public class TMobileTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public TMobileTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(TMobileTests.class);
	}
	
	public void testLipny() {
		
	}

	public void testBasic() throws Exception {

		try(BankBotsModule banks = new BankBotsModule(false)) {
			banks.registerBankTxReceivedListener(new BankTxReceivedEvent() {

				@Override
				public void onBankTxReceived(String title, BigDecimal amount) {
					System.out.println("Received: " + title + " -> " + amount);
				}
				
			});
			Thread.sleep(1000000);
		}
	}

}
