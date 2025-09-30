import org.junit.jupiter.api.*;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CashRegisterTest extends BaseTest {
    
    private CashRegisterPage cash;
    
    @BeforeEach
    void setUp() {
        // Call parent's createPage() first
        super.createPage();
        
        // Login before each test
        login();
        
        // Initialize CashRegisterPage with the page from BaseTest
        cash = new CashRegisterPage(page);
        System.out.println("Cash Register setup complete!");
    }
    
    
    @Test
    @Order(1)
    @DisplayName("Handle Register - Open or Close")
    public void HandleRegister() throws InterruptedException {
        System.out.println("TEST: Checking Register State");
        
        // Navigate to Cash Register page
        cash.navigateToCashRegister();
        
        // Check what button is visible
        try {
            // If Open Register button is visible - open it
            if (page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Open Register")).isVisible()) {
                
                System.out.println("Found Open Register button - Opening register...");
                
                // Click Open Register
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Open Register")).click();
                
                Thread.sleep(1000);
                
                // Fill amount and notes
                page.getByPlaceholder("Enter Amount").fill("50.00");
                page.getByPlaceholder("Enter Notes").fill("Opening register");
                
                // Click Open Register in modal
                page.locator("button:has-text('Open Register')").last().click();
                
                Thread.sleep(2000);
                System.out.println("✓ Register opened!");
                          
        }
     } catch(Exception e) {
     
        
        try {
            // If Close Register button is visible - close it
            if (page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Close Register")).isVisible()) {
                
                System.out.println("Found Close Register button - Closing register...");
                
                // Click Close Register
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Close Register")).click();
                
                Thread.sleep(1000);
                
                // Add notes
                page.getByPlaceholder("Enter Notes").fill("Closing register");
                
                // Click Close Register in modal
                page.locator("button:has-text('Close Register')").last().click();
                
                Thread.sleep(2000);
                System.out.println("✓ Register closed!");
                
                
            }
 
       } catch (Exception ex) {
          // Close Register not found
       }
   }
    }
   
    @Test
    @Order(2)
    @DisplayName("Cash In")
    public void CashIn() throws InterruptedException {
        System.out.println("TEST: Cash In");
        
        // First open register if needed
        cash.OpenFreshRegister();
        
        // Then add cash
      cash.CashIn();
        
        
    }
    
    @Test
    @Order(3)
    @DisplayName("Cash Out")
    public void CashOut() throws InterruptedException {
        System.out.println("TEST: Cash Out");
        
        // First open register if needed
        cash.OpenFreshRegister();
        
        // Then remove cash
        cash.CashOut();
        
       Thread.sleep(2000);
        
        // Take final screenshot showing all transaction entries
        System.out.println("Taking final screenshot with all transaction entries...");
        page.screenshot(new Page.ScreenshotOptions()
            .setPath(java.nio.file.Paths.get("target/All_Transactions_Final.png"))
            .setFullPage(true));
        
        
    }
    
    
}