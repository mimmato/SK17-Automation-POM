package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import page.object.Header;
import page.object.homePage;
import page.object.loginPage;
import page.object.profilePage;

import java.time.Duration;

public class loginTests_object {
    private final String PASSWORD = "TGdd7EDby83jdAC";
    private WebDriver webDriver;

    @BeforeSuite
    protected final void setupTestSuite() {
        WebDriverManager.chromedriver().setup();
        WebDriverManager.firefoxdriver().setup();
        WebDriverManager.edgedriver().setup();
    }

    @BeforeMethod
    protected final void setUpTest() {
        this.webDriver = new ChromeDriver();
        //this.webDriver = new EdgeDriver();
        this.webDriver.manage().window().maximize();
        this.webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        this.webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        this.webDriver.navigate().to("http://training.skillo-bg.com:4200/users/login");
    }

    @AfterMethod
    private final void tearDownTest() {
        if (this.webDriver != null) {
            this.webDriver.close();
        }
    }

    @DataProvider(name = "getUsers")
    public Object[][] getUsers() {
        return new Object[][]{
                {"blasfsafasfabla", 1234, "User not found"},
                {"p0li0m", 5508, "Successful login!"},
                {"dbsdhsh", 1512, "Ivalid password"}};
    }

    @Test(dataProvider = "getUsers")
    public void LoginUser(String username, int userId, String errorMessage) {
        // initial page objects definition
        loginPage login = new loginPage(this.webDriver);
        profilePage profile = new profilePage(this.webDriver);
        Header header = new Header(this.webDriver);
        homePage home = new homePage(this.webDriver);

        // validate initial loading of page
        Assert.assertTrue(login.isUrlLoaded(), "The login page is not loaded");

        // validate the login form is loaded
        String singInTitle = login.getSignInElementText();
        Assert.assertEquals(singInTitle, "Sign in", "Login form is not loaded");

        // fill login form
        login.populatePassword(this.PASSWORD);
        login.populateUsername(username);
        login.clickSignIn();

        // hint for homework - add validation of error/success message of login
        String toastMessageSuccess = login.verifySuccessfulLoginToast();
        Assert.assertEquals(toastMessageSuccess,"Successful login!", errorMessage);

        // validate home page is loaded after login
        Assert.assertTrue(home.isUrlLoaded(), "Home page is not loaded");

        // navigate to profile page
        header.clickProfileLink();

        // validate profile page is loaded
        Assert.assertTrue(profile.isUrlLoaded(userId), "The user profile page is not loaded");

        // validate username is correct
        Assert.assertTrue(profile.isUsernameAsExpected(username), "The username is not as expected");
    }
}


