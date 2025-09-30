import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

import java.nio.file.Paths;

import org.junit.jupiter.api.*;

public class BaseTest {
    
    // These are shared by all test classes
    static Playwright playwright;
    static Browser browser;
    Page page;
    BrowserContext context;
    
    @BeforeAll
    static void launchBrowser() {
        // Create playwright and browser once for all tests
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(false)  // Set to true to hide browser
            .setSlowMo(500));     // Slows down actions so you can see them
    }
    
    @AfterAll
    static void closeBrowser() {
        // Clean up after all tests
        browser.close();
        playwright.close();
    }
    
    @BeforeEach
    void createPage() {
        // Before each test, create a fresh page
        context = browser.newContext();
        page = context.newPage();
    }
    
    @AfterEach
    void closePage() {
        // After each test, close the page
        context.close();
    }
    
    // Simple login method that all tests can use
    void login() {
        System.out.println("Logging in...");
        
        // Step 1: Go to website
        page.navigate("https://admin.turnsapp.com");
        
        // Step 2: Enter business ID
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Business Id"))
            .fill("ritika");
        
        // Step 3: Click Proceed
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed"))
            .click();
        
        // Step 4: Wait for login page
        page.waitForURL("**/account/login");
        
        // Step 5: Enter username
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username *"))
            .fill("admin");
        
        // Step 6: Enter password  
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *"))
            .fill("1234");
        
        // Step 7: Click Log In
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log In"))
            .click();
        
        // Step 8: Wait for home page
        page.waitForURL("**/home");
        
        System.out.println("Login successful!");
    }
    
    void takeScreenshot(String fileName) {
        try {
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(Paths.get("screenshots/" + fileName + ".png"))
                .setFullPage(true));
            System.out.println("📸 Screenshot: " + fileName + ".png");
        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}
