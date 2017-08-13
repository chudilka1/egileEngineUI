package com.twitter.pages;

import lombok.Getter;
import lombok.Setter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class TwitterPersonalWallPage extends AbstractPage {

    @Getter
    @Setter
    private static String text,
            timeStamp,
            retweetCount;

    private By bigAvatar = By.cssSelector(".ProfileAvatar"),
            tweetButton = By.id("global-new-tweet-button"),
            textField = By.id("tweet-box-global"),
            postButtonInDialog = By.cssSelector("#global-tweet-dialog-dialog button[class*=\"tweet-action\"]"),
            textInTheFirstPostOnTheWall = By.cssSelector("#stream-items-id > li:first-child div[class=\"content\"] div[class=\"js-tweet-text-container\"]"),
            tweetTime = By.cssSelector("#stream-items-id > li:first-child div[class=\"content\"] small[class=\"time\"] > a"),
            retweetButton = By.cssSelector("#stream-items-id > li:first-child div[class=\"content\"] div[class=\"stream-item-footer\"] div[class*=\"ProfileTweet-action--retweet\"] button:first-child"),
            retweetButtonInDialog = By.cssSelector("#retweet-tweet-dialog-dialog div[class=\"tweet-button\"] > button"),
            retweetCounter = By.cssSelector("#stream-items-id > li:first-child div[class=\"content\"] div[class=\"stream-item-footer\"] div[class*=\"ProfileTweet-action--retweet\"] button:last-child > span"),
            dropdownButtonInTheFirstPost = By.cssSelector("#stream-items-id > li:first-child div[class=\"content\"] div[class=\"stream-item-header\"] div[class*=\"ProfileTweet-action\"] .dropdown > button"),
            textInPopupAboutDuplicate = By.cssSelector("#message-drawer div[class=\"message-inside\"] .message-text"),
            deleteTweetButtonInDropdownMenu = By.cssSelector("#timeline #stream-items-id li:first-child .content div[class=\"dropdown open\"] div[class*=\"dropdown-menu\"] > ul li[class*=\"js-actionDelete\"] > button"),
            deleteButtonInDeleteDialog = By.cssSelector("#delete-tweet-dialog-dialog > .modal-content .modal-footer button[class*=\"delete-action\"]");

    private List<WebElement> dropdownElements = driver.findElements(By.cssSelector("#timeline #stream-items-id li:first-child .content div[class=\"dropdown open\"] div[class*=\"dropdown-menu\"] > ul li"));

    public TwitterPersonalWallPage(WebDriver driver) {
        super(driver);
    }

    public TwitterPersonalWallPage openPersonalWallTwitter(String url) {
        driver.get(url);
        return this;
    }

    public TwitterPersonalWallPage isPersonalWallTwitter() {
        assertThat(isElementPresent(bigAvatar)).isEqualTo(true);
        return this;
    }

    public TwitterPersonalWallPage clickTweetButtonTwitter() {
        click(tweetButton);
        return this;
    }

    public TwitterPersonalWallPage enterTextTwitter(String text) {
        typeText(textField, text);
        return this;
    }

    public TwitterPersonalWallPage clickPostButtonTwitter() {
        click(postButtonInDialog);
        return this;
    }

    public TwitterPersonalWallPage getTextFromTheFirstPostTwitter() {
        text = getTextFromElement(textInTheFirstPostOnTheWall);
        return this;
    }

    public TwitterPersonalWallPage isNewTweetPublishedTwitter(String text) throws InterruptedException {
        setTimeStamp(getTimeOfPublication());
        setRetweetCount(getNumberOfRetweets());
        Thread.sleep(1000);
        assertThat(text).containsIgnoringCase(getTextFromElement(textInTheFirstPostOnTheWall));
        return this;
    }

    public TwitterPersonalWallPage isDuplicateMessageTwitter() throws InterruptedException {
        Thread.sleep(1000);
        assertThat(getTextFromElement(textInPopupAboutDuplicate)).isEqualTo("Вы уже отправили этот твит.");
        return this;
    }

    public TwitterPersonalWallPage clickRetweetButtonTwitter() {
        click(retweetButton);
        return this;
    }

    public TwitterPersonalWallPage clickRetweetButtonInDialogTwitter() {
        click(retweetButtonInDialog);
        return this;
    }

    public TwitterPersonalWallPage isRetweetCounterChangedTwitter() throws InterruptedException {
        Thread.sleep(1000);
        assertThat(getTextFromElement(retweetCounter)).isEqualToIgnoringCase("1");
        return this;
    }

    public TwitterPersonalWallPage clickDropdownButtonInTheFirstPostTwitter() {
        click(dropdownButtonInTheFirstPost);
        return this;
    }

    public TwitterPersonalWallPage clickDeleteTweetButtonInDropdownMenuTwitter() {
        webDriverUtils.waitForExpectedCondition(ExpectedConditions.visibilityOfElementLocated(deleteTweetButtonInDropdownMenu));
        click(deleteTweetButtonInDropdownMenu);
        return this;
    }

    public TwitterPersonalWallPage clickDeleteButtonInDeleteDialogTwitter() {
        click(deleteButtonInDeleteDialog);
        return this;
    }

    public TwitterPersonalWallPage isTweetDeletedTwitter() throws InterruptedException {
        Thread.sleep(1000);
        String initialText = text;
        getTextFromTheFirstPostTwitter();
        assertThat(initialText).isNotEqualToIgnoringCase(text);
        return this;
    }

    private String getTimeOfPublication() {
        return driver.findElement(tweetTime).getCssValue("data-original-title");
    }

    private String getNumberOfRetweets() {
        return driver.findElement(retweetCounter).getCssValue("data-tweet-stat-count");
    }

}
