package com.twitter.core;

import com.twitter.util.PropertiesCache;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.os.WindowsUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;

import java.net.MalformedURLException;
import java.net.URL;

import static org.openqa.selenium.remote.BrowserType.CHROME;
import static org.openqa.selenium.remote.BrowserType.FIREFOX;

public abstract class WebDriverTestBase {
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String BROWSER = System.getProperty("browser");
    private static final Logger LOG = LogManager.getLogger(WebDriverTestBase.class);
    protected WebDriver driver;

    private DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

    @BeforeSuite
    public void setUp() throws MalformedURLException {
        if (isWindows()) {
            if (isBrowserSetUpFor(CHROME, BROWSER)) {
                ChromeDriverManager.getInstance().setup();
                LOG.info("Win + Chrome");
            } else if (isBrowserSetUpFor(FIREFOX, BROWSER)) {
                FirefoxDriverManager.getInstance().setup();
                LOG.info("Win + Firefox");
            }
        } else if (isUnix()) {
            if (isBrowserSetUpFor(FIREFOX, BROWSER)) {
                ChromeDriverManager.getInstance().setup();
                LOG.info("Unix + Chrome");
            } else if (isBrowserSetUpFor(FIREFOX, BROWSER)) {
                FirefoxDriverManager.getInstance().setup();
                LOG.info("Unix + Firefox");
            }
        }
        initializeWebDriver();
        LOG.info("Driver was initialized");
    }

    private void initializeWebDriver() {
        try {
            if (isBrowserSetUpFor(CHROME, BROWSER)) {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-notifications");
                options.addArguments("--disable-infobars");
                options.addArguments("--disable-translate");
                options.addArguments("--disable-web-security");
                options.addArguments("--disable-save-password-bubble");
                options.addArguments("--start-maximized");
                DesiredCapabilities caps = DesiredCapabilities.chrome();
                caps.setCapability(ChromeOptions.CAPABILITY, options);
                driver = new ChromeDriver(caps);
                LOG.info("Chrome driver is initialized");

            } else if (isBrowserSetUpFor(FIREFOX, BROWSER)) {
                driver = new FirefoxDriver();
                LOG.info("Firefox driver is initialized");
            }
        } catch (WebDriverException e) {
            LOG.error(e.getMessage());
            WindowsUtils.killByName(desiredCapabilities.getBrowserName() + "driver" + (isWindows() ? ".exe" : ""));
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private boolean isBrowserSetUpFor(String browserName, String browserSystemVariable) {
        return StringUtils.isEmpty(BROWSER) || browserName.equalsIgnoreCase(browserSystemVariable);
    }

    private boolean isWindows() {
        return OS.contains("win");
    }

    private boolean isUnix() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    private String getProperty(String key) {
        return PropertiesCache.getInstance().getProperty(key);
    }

    private String getPath(String s) {
        String path = null;
        URL url = WebDriverTestBase.class.getClassLoader().getResource(s);
        if (url != null) {
            path = url.getPath();
        } else {
            LOG.error("Resource not found");
        }
        return path;
    }
}
