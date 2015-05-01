import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.uncommons.reportng.HTMLReporter;

@Listeners({HTMLReporter.class})
public class timeoutTests {

	protected ThreadLocal<RemoteWebDriver> threadDriver = null;

	public int TIMEOUT = 250000;
	String browserName;
	String hubURL;
	String automationUrl;
	String applicationName;
	String testName;
	DesiredCapabilities dc;
	String sessionId;

	public WebDriver getDriver() {
		return threadDriver.get();
	}

	@BeforeClass	
	public void setUp() {

		this.browserName = "firefox";
		this.hubURL = "http://10.244.172.178:4444/wd/hub";
		this.automationUrl = "http://www.google.com";

		threadDriver = new ThreadLocal<RemoteWebDriver>();
		if (this.browserName.equalsIgnoreCase("firefox")) {
			dc = DesiredCapabilities.firefox();
			dc.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
			FirefoxProfile fp = new FirefoxProfile();
			fp.setAcceptUntrustedCertificates(true);
			dc.setCapability(FirefoxDriver.PROFILE, fp);
			dc.setBrowserName(DesiredCapabilities.firefox().getBrowserName());
		}	
		dc.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);		
		dc.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

		try {
			threadDriver.set(new RemoteWebDriver(new URL(this.hubURL), dc));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			Assert.fail("MalformedURLException @ setup - ", e.getCause());
		}

		getDriver().get(this.automationUrl);

		this.sessionId = threadDriver.get().getSessionId().toString();

		System.out.println("SETUP - " 
				+  this.sessionId);
	}
	
	@Test(description = "Suite Timeout Log and Report Test")
	public void testSuite() {		
		try {
			Thread.sleep(this.TIMEOUT); // Sleep longer than suite level timeout
		} catch (InterruptedException e) {
			Assert.fail("Sleep Interrupt: We most likely hit the testng timeout set by suite.windows.timeout in build.properties", e.getCause());
		}	
	}
	
	@AfterClass()
	public void tearDown() {
		Reporter.log("<p>" + "TEARDOWN - " + this.sessionId + "</p>");
		System.out.println("TEARDOWN - " + this.sessionId);
		getDriver().quit();
	}
}