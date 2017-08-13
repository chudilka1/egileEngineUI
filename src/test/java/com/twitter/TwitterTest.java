package com.twitter;

import com.twitter.core.WebDriverTestBase;
import com.twitter.pages.AbstractPage;
import com.twitter.pages.TestData;
import com.twitter.pages.TwitterLoginPage;
import com.twitter.pages.TwitterPersonalWallPage;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({com.twitter.core.TestListener.class})
public class TwitterTest extends WebDriverTestBase {

    private final String PERSONAL_WALL_URL = TestData.BASE_URL.concat("/AlexY99017469");
    private String text;

    @BeforeTest
    public void loginTwitter() {

        TwitterLoginPage twitter = new TwitterLoginPage(TestData.BASE_URL, driver);

        twitter.open()
                .isLoginForm()
                .loginTwitter()
                .isLoggedIn();
    }

    @Test
    public void postingTweetPositiveTest() throws InterruptedException {

        text = AbstractPage.getUniqueText("Test тест");

        TwitterPersonalWallPage twitter = new TwitterPersonalWallPage(driver);
        twitter.openPersonalWallTwitter(PERSONAL_WALL_URL)
                .isPersonalWallTwitter()
                .clickTweetButtonTwitter()
                .enterTextTwitter(text)
                .clickPostButtonTwitter()
                .isNewTweetPublishedTwitter(text);
    }

    @Test (dependsOnMethods = "postingTweetPositiveTest")
    public void duplicateMessageNegativeTest() throws Exception {
        TwitterPersonalWallPage twitter = new TwitterPersonalWallPage(driver);
        twitter.openPersonalWallTwitter(PERSONAL_WALL_URL)
                .isPersonalWallTwitter()
                .clickTweetButtonTwitter()
                .enterTextTwitter(text)
                .clickPostButtonTwitter()
                .isDuplicateMessageTwitter();
    }

    @Test (dependsOnMethods = "duplicateMessageNegativeTest")
    public void retweetPositiveTest() throws InterruptedException {
        TwitterPersonalWallPage twitter = new TwitterPersonalWallPage(driver);
        twitter.openPersonalWallTwitter(PERSONAL_WALL_URL)
                .isPersonalWallTwitter()
                .clickRetweetButtonTwitter()
                .clickRetweetButtonInDialogTwitter()
                .isRetweetCounterChangedTwitter();
    }

    @Test (dependsOnMethods = "retweetPositiveTest")
    public void deleteTweetPositive() throws InterruptedException{
        TwitterPersonalWallPage twitter = new TwitterPersonalWallPage(driver);
        twitter.openPersonalWallTwitter(PERSONAL_WALL_URL)
                .isPersonalWallTwitter()
                .getTextFromTheFirstPostTwitter()
                .clickDropdownButtonInTheFirstPostTwitter()
                .clickDeleteTweetButtonInDropdownMenuTwitter()
                .clickDeleteButtonInDeleteDialogTwitter()
                .isTweetDeletedTwitter();
    }

}
