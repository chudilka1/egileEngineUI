package com.twitter.pages;

import com.twitter.util.WebDriverUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class AbstractPage {
    protected WebDriver driver;
    protected WebDriverUtils webDriverUtils;
    protected JavascriptExecutor executor;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        webDriverUtils = new WebDriverUtils(this.driver);
    }

    public void typeText(By elementId, String text) {
        WebElement textBox = driver.findElement(elementId);
        textBox.clear();
        textBox.sendKeys(text);
    }

    public void click(By elementId) {
        WebElement button = driver.findElement(elementId);
        button.click();
    }

    public void jsClick(By elementId) {
        WebElement element = driver.findElement(elementId);
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
    }

    public static String getUniqueText(String text) {
        Random random = new Random();
        int randomNumber = random.nextInt(99) + 1;

        return text.concat(String.valueOf(randomNumber));
    }

    public String getTextFromElement(By elementId) {
        WebElement element = driver.findElement(elementId);
        return element.getText().trim();
    }

    public boolean isElementPresent(By elementId) {
        webDriverUtils.waitForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(elementId));
        List<WebElement> list = driver.findElements(elementId);
        return list.size() != 0 && list.get(0).isDisplayed();
    }
}
