package com.twitter.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TwitterLoginPage extends AbstractPage {

    private String url;

    //LOCATORS
    private By loginForm = By.cssSelector(".front-signin.js-front-signin"),
            emailField = By.id("signin-email"),
            passField = By.id("signin-password"),
            phoneField = By.id("challenge_response"),
            wall = By.id("timeline");

    public TwitterLoginPage(String url, WebDriver driver) {
        super(driver);
        this.url = url;
    }

    public TwitterLoginPage open() {
        driver.get(url);
        return this;
    }

    public TwitterLoginPage isLoginForm() {
        assertThat(isElementPresent(loginForm)).isEqualTo(true);
        return this;
    }

    public TwitterLoginPage loginTwitter() {

        webDriverUtils.waitForExpectedCondition(ExpectedConditions.presenceOfElementLocated(emailField));
        webDriverUtils.waitForExpectedCondition(ExpectedConditions.presenceOfElementLocated(passField));

        typeText(emailField, TestData.getEMAIL());
        typeText(passField, TestData.getPASS());
        driver.findElement(passField).submit();

        webDriverUtils.waitForExpectedCondition(ExpectedConditions.presenceOfElementLocated(phoneField));
        typeText(phoneField, TestData.getPHONE());
        driver.findElement(phoneField).submit();

        return this;
    }

    public TwitterPersonalWallPage isLoggedIn() {
        assertThat(isElementPresent(wall)).isEqualTo(true);
        return new TwitterPersonalWallPage(driver);
    }

}
