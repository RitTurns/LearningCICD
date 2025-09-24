
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;

public class TurnsAppLogin {
    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions()
                .setHeadless(false)); // Set to true for headless mode
            
            BrowserContext context = browser.newContext();
            Page page = context.newPage();
            
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
            
            // Keep browser open for a few seconds to see the result
            Thread.sleep(3000);
            
            System.out.println("Login process completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during login process: " + e.getMessage());
            e.printStackTrace();
        }
    }
}