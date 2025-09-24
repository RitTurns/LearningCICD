import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TurnsAppLoginTest {
	
	
    
	static Playwright playwright;
    static Browser browser;
    BrowserContext context;
    Page page;
    
    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        
        // Check if running in CI/CD or locally
        boolean isCI = System.getenv("CI") != null || System.getProperty("ci") != null;
        boolean headless = isCI ? true : false;  // Headless in CI, visible locally
        
        System.out.println("Running in " + (headless ? "HEADLESS" : "VISIBLE") + " mode");
        
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
            .setHeadless(headless)
            .setSlowMo(headless ? 0 : 500));  // Slow down for debugging when visible
    }
    
    @AfterAll
    static void closeBrowser() {
        browser.close();
        playwright.close();
    }
    
    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }
    
    @AfterEach
    void closeContext() {
        context.close();
    }
    
    @Test
    @DisplayName("Test TurnsApp Admin Login Flow")
    void testTurnsAppLogin() {
        try {
            // Step 1: Navigate to admin.turnsapp.com
            System.out.println("Navigating to admin.turnsapp.com...");
            page.navigate("https://admin.turnsapp.com");
            
            // Step 2: Enter business ID
            System.out.println("Entering business ID...");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Business Id"))
                .fill("ritika");
            
            // Step 3: Click Proceed button
            System.out.println("Clicking Proceed...");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed"))
                .click();
            
            // Step 4: Wait for login page to load
            page.waitForURL("**/account/login");
            System.out.println("Login page loaded");
            
            // Step 5: Enter username
            System.out.println("Entering username...");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username *"))
                .fill("admin");
            
            // Step 6: Enter password
            System.out.println("Entering password...");
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *"))
                .fill("1234");
            
            // Step 7: Click Log In button
            System.out.println("Clicking Log In...");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log In"))
                .click();
            
            // Step 8: Wait for successful login and home page
            page.waitForURL("**/home");
            System.out.println("Login successful! Redirected to home page");
            
            // Verify we're on the home page
            assertTrue(page.url().contains("/home"), "Should be redirected to home page after login");
            
            // Optional: Wait for success alert and close it
            try {
                page.locator("[role='alert']").waitFor(new Locator.WaitForOptions()
                    .setTimeout(5000));
                System.out.println("Success alert appeared: " +
                    page.locator("[role='alert']").textContent());
                
                // Close the alert if close button is present
                if (page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("close"))
                    .isVisible()) {
                    page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("close"))
                        .click();
                    System.out.println("Alert closed");
                }
            } catch (Exception e) {
                System.out.println("No alert found or alert handling failed: " + e.getMessage());
            }
            
            // Optional: Take a screenshot for verification
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("target/login-success-screenshot.png")));
            
            System.out.println("Login process completed successfully!");
            
        } catch (Exception e) {
            // If test fails, take a screenshot for debugging
            page.screenshot(new Page.ScreenshotOptions()
                .setPath(java.nio.file.Paths.get("target/login-failure-screenshot.png")));
            
            fail("Login test failed: " + e.getMessage());
        }
    }
    
    @Test
    @DisplayName("Test Invalid Login Credentials")
    void testInvalidLogin() {
        // Navigate to admin.turnsapp.com
        page.navigate("https://admin.turnsapp.com");
        
        // Enter business ID
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Business Id"))
            .fill("ritika");
        
        // Click Proceed
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed"))
            .click();
        
        // Wait for login page
        page.waitForURL("**/account/login");
        
        // Enter WRONG username and password
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username *"))
            .fill("wronguser");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *"))
            .fill("wrongpass");
        
        // Try to log in
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log In"))
            .click();
        
        // Should NOT be redirected to home page
        assertFalse(page.url().contains("/home"), "Should not be on home page with invalid credentials");
        
        System.out.println("Invalid login test passed - login was correctly rejected");
    }
}