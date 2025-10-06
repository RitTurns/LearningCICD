import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TurnsAppLoginTest extends BaseTest{
	
	@Test
    @DisplayName("Test TurnsApp Admin Login Flow - Valid Login")
    void testValidLogin() {
        // Use the login() method from BaseTest
        login();
        
        // Verify we're on the home page
        assertTrue(page.url().contains("/home"), 
            "Should be redirected to home page after login");
        
        // Check for success alert (optional)
        try {
            Locator alert = page.locator("[role='alert']");
            if (alert.isVisible()) {
                System.out.println("Success alert: " + alert.textContent());
            }
        } catch (Exception e) {
            // Alert is optional, continue if not found
        }
        
        // Take a screenshot for verification
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("target/login-success-screenshot.png")));
        
        System.out.println("Valid login test passed!");
    }
    
    @Test
    @DisplayName("Test Invalid Login Credentials")
    void testInvalidLogin() {
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
        
        // Step 5: Enter WRONG username and password
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Username *"))
            .fill("wronguser");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *"))
            .fill("wrongpass");
        
        // Step 6: Try to log in
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Log In"))
            .click();
        
        page.waitForTimeout(2000);
        
        Locator errorMessage = page.locator("[role='alert']");
            
        
        if (errorMessage.isVisible()) {
            System.out.println("Error popup found: " + errorMessage.first().textContent());
        }
        
        // Step 7: Should NOT be redirected to home page
        assertFalse(page.url().contains("/home"), 
            "Should not be on home page with invalid credentials");
        
        // Take a screenshot of the error
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("target/login-failure-screenshot.png")));
        
        System.out.println("Invalid login test passed - login was correctly rejected");
    

    }
}