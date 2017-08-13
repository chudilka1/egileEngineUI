package com.twitter.core;


import com.twitter.util.PropertiesCache;
import com.twitter.util.WebDriverUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final String SCREENSHOTS_FOR_SUCCESS_PATH = "screenshots.path.success";
    private static final String SCREENSHOTS_FOR_FAILED_PATH = "screenshots.path.failed";
    private static final Logger LOG = LogManager.getLogger(TestListener.class);
    private WebDriverUtils webDriverUtils;
    private WebDriver driver;

    @Override
    public void onTestStart(ITestResult result) {
        driver = ((WebDriverTestBase) result.getInstance()).driver;
        webDriverUtils = new WebDriverUtils(driver);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        makeScreenShot(result, PropertiesCache.getInstance().getProperty(SCREENSHOTS_FOR_SUCCESS_PATH));
        LOG.info("Screenshooted on method success (" + PropertiesCache.getInstance().getProperty(SCREENSHOTS_FOR_SUCCESS_PATH) + ")");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        makeScreenShot(result, PropertiesCache.getInstance().getProperty(SCREENSHOTS_FOR_FAILED_PATH));
        LOG.warn("Screenshooted on method failure (" + PropertiesCache.getInstance().getProperty(SCREENSHOTS_FOR_FAILED_PATH) + ")");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOG.warn("Method skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        LOG.warn("test failed but within success % ");
    }

    @Override
    public void onStart(ITestContext context) {
        LOG.info("On start of test " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("on finish of test " + context.getName());
    }

    private void makeScreenShot(ITestResult result, String screenShotPath) {
        webDriverUtils.saveScreenshot(driver, screenShotPath, result.getMethod().getMethodName());
    }
}
