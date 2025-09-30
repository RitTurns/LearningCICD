import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CashRegisterPage {
    
    private Page page;
    
    // Constructor to initialize the page
    public CashRegisterPage(Page page) {
        this.page = page;
    }
    
    // Method to navigate to Cash Register page
    public void navigateToCashRegister() throws InterruptedException {
        System.out.println("Navigating to Cash Register...");
        
        // Click on Cash Register menu item in sidebar
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Cash Register"))
            .click();
        
        // Wait for page to load
        Thread.sleep(2000);
        page.waitForURL("**/cashRegister");
        
        System.out.println("Cash Register page loaded!");
    }
    
    // Method to open a fresh register (when no register is open)
    public void OpenFreshRegister() throws InterruptedException {
        System.out.println("Opening a fresh register...");
        
        // Navigate to Cash Register first
        navigateToCashRegister();
        
        // Check if register needs to be opened or is already open
        try {
            // Try to find Open Register button
            if (page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Open Register")).isVisible()) {
                
                // Click on Open Register button
                page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                    .setName("Open Register")).click();
                
                // Wait for modal to appear
                Thread.sleep(1000);
                
                // Enter opening balance amount
                page.getByPlaceholder("Enter Amount").fill("50.00");
                
                // Optional: Add notes
                page.getByPlaceholder("Enter Notes").fill("Opening register for the day");
                
                // Click the Open Register button in modal
                page.locator("button:has-text('Open Register')").last().click();
                
                Thread.sleep(2000);
                System.out.println("Register opened successfully!");
            }
        } catch (Exception e) {
            System.out.println("Register might be already open or button not found");
            // If register is already open, continue
        }
    }
    
    // Method to add cash to register
    public void CashIn() throws InterruptedException {
        System.out.println("Adding cash to register...");
        
        // Make sure we're on cash register page
        if (!page.url().contains("cashRegister")) {
            navigateToCashRegister();
        }
        
        // Click Cash In button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cash In"))
            .click();
        
        // Wait for modal to appear
        Thread.sleep(1000);
        
        // Enter amount to add
        page.getByPlaceholder("Enter Amount").fill("100.00");
        
        // Optional: Add notes
        page.getByPlaceholder("Enter Notes").fill("Customer payment received");
        
        // Click Cash In button in modal
        page.locator("button:has-text('Cash In')").last().click();
        
        Thread.sleep(2000);
        System.out.println("Cash added successfully!");
    }
    
    // Method to remove cash from register
    public void CashOut() throws InterruptedException {
        System.out.println("Removing cash from register...");
        
        // Make sure we're on cash register page
        if (!page.url().contains("cashRegister")) {
            navigateToCashRegister();
        }
        
        // Click Cash Out button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cash Out"))
            .click();
        
        // Wait for modal to appear
        Thread.sleep(1000);
        
        // Enter amount to remove
        page.getByPlaceholder("Enter Amount").fill("25.00");
        
        // Try to select expense type if dropdown is present
//        try {
//            // Look for dropdown and select an option
//            page.locator("select").first().selectOption("Other");
//        } catch (Exception e) {
//            System.out.println("Expense type dropdown not found, continuing...");
//        }
        
        // Optional: Add notes
        page.getByPlaceholder("Enter Notes").fill("Purchase of supplies");
        
        // Click Cash out button in modal
        page.locator("button:has-text('Cash out')").last().click();
        
        Thread.sleep(2000);
        System.out.println("Cash removed successfully!");
    }
    
    // Method to close the register
    public void CloseRegister() throws InterruptedException {
        System.out.println("Closing register...");
        
        // Make sure we're on cash register page
        if (!page.url().contains("cashRegister")) {
            navigateToCashRegister();
        }
        
        // Click Close Register button
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
            .setName("Close Register")).click();
        
        // Wait for modal to appear
        Thread.sleep(1000);
        
        // The closing amount should be auto-filled with current balance
        // Enter closing amount if needed (or verify the auto-filled amount)
        try {
            // If there's an amount field, it might be pre-filled
            page.getByPlaceholder("Enter Amount").fill("");  // Clear if needed
        } catch (Exception e) {
            System.out.println("Amount field might be auto-filled or not editable");
        }
        
        // Optional: Add closing notes
        page.getByPlaceholder("Enter Notes").fill("End of day closing");
        
        // Click Close Register button in modal
        page.locator("button:has-text('Close Register')").last().click();
        
        Thread.sleep(2000);
        System.out.println("Register closed successfully!");
    }
    
    // Helper method to select store if needed
    public void selectStore(String storeName) throws InterruptedException {
        System.out.println("Selecting store: " + storeName);
        
        // Wait a bit for dropdown to be ready
        Thread.sleep(1000);
        
        // Click on the store dropdown and select store
        page.locator("text=" + storeName).first().click();
        
        Thread.sleep(1000);
        System.out.println("Store selected: " + storeName);
    }
}