package com.twitter.util;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;

import static com.google.common.io.Files.toByteArray;

public class WebDriverUtils {
    private static final String EXPLICIT_WAIT = "webdriver.explicit.wait";
    private WebDriverWait wait;

    public WebDriverUtils(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Long.valueOf(getProperty(EXPLICIT_WAIT)));
    }

    private String getProperty(String key) {
        return PropertiesCache.getInstance().getProperty(key);
    }

    public void waitForExpectedCondition(ExpectedCondition expectedCondition) {
        wait.until(expectedCondition);
    }

    public byte[] saveScreenshot(WebDriver driver, String pathToFolder, String screenshotName) {
        try {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(pathToFolder + screenshotName + ".png"));
            return toByteArray(scrFile);
        } catch (Exception e) {
            System.out.println("Screenshot was not saved" + e);
        }
        return new byte[0];
    }
}
