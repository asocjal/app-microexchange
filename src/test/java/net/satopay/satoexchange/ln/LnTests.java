package net.satopay.satoexchange.ln;

import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class LnTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public LnTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(LnTests.class);
	}

	public void testLipny() {

	}

	public void testVerifyInvoice1() throws Exception {
		try (Ln ln = new Ln()) {
			ln.verifyInvoice(
					"lnbc10u1pwjkjvwpp5uxfgy7pmmqeukmfa945ndqw5harghhsffv858m54rgh2eh023fmsdqqcqzpggk0x84fqpkhalejmu792ac0qa52ze8kdqnwcyanpf4aar39w5l7zdh07cf7y9rzsxm32lahml60jntdkyc53zhkw4gytt9nrqus76vgqpdmyle",
					1000);
		}
	}

	public void testVerifyInvoice2() throws Exception {
		try {
			try (Ln ln = new Ln()) {
				ln.verifyInvoice(
						"lnbc10u1pwjkjvwpp5uxfgy7pmmqiukmfa945ndqw5harghhsffv858m54rgh2eh023fmsdqqcqzpggk0x84fqpkhalejmu792ac0qa52ze8kdqnwcyanpf4aar39w5l7zdh07cf7y9rzsxm32lahml60jntdkyc53zhkw4gytt9nrqus76vgqpdmyle",
						1000);
				Assert.fail("Exceptionnot thrown");
			}
		} catch (Exception ex) {
			// Exception thrown - means success
		}
	}

	public void testVerifyInvoice3() throws Exception {

		try (Ln ln = new Ln()) {
			ln.verifyInvoice(
					"lnbc1pwjkj68pp5qz5d3zx4hw6upeptmdqp3hdus2p8lkqfzl2r250srwdr9426njksdqqcqzpgknnlqvjuasv2gc880ujap9e3sg3e3k4sdx4n3ppuv6vas6jhvccrushnee0hrvdh588q6tuyvpv6kmm4s6eaqpcxyngp7uyu2ve7drcp3tdnw7",
					1000);
			Assert.fail("Exceptionnot thrown");
		}
	}
	
	public void testPayInvoice() throws Exception {

		try (Ln ln = new Ln()) {
			ln.payInvoice(
					"lnbc1pwjkj68pp5qz5d3zx4hw6upeptmdqp3hdus2p8lkqfzl2r250srwdr9426njksdqqcqzpgknnlqvjuasv2gc880ujap9e3sg3e3k4sdx4n3ppuv6vas6jhvccrushnee0hrvdh588q6tuyvpv6kmm4s6eaqpcxyngp7uyu2ve7drcp3tdnw7",
					1000);
			Assert.fail("Exceptionnot thrown");
		}
	}

}
