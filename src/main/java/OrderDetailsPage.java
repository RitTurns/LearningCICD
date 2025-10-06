import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class OrderDetailsPage {
private final Page page;
    
    public OrderDetailsPage(Page page) {
        this.page = page;
    }
    
    /**
     * Get the order ID displayed on the page
     * @return Order ID (e.g., "#PLO27")
     */
    public String getOrderId() {
        String orderId = page.locator("text=/^#PLO\\d+/").first().textContent();
        System.out.println("✓ Viewing order details for: " + orderId);
        return orderId;
    }
    
    /**
     * Get customer information displayed on order details
     * @return Customer name
     */
    public String getCustomerName() {
        String customerName = page.locator("text=/.*Thomas.*/").first().textContent();
        System.out.println("✓ Customer name: " + customerName);
        return customerName;
    }
    
    /**
     * Check if Take Payment button is disabled
     * @return true if button is disabled
     */
    public boolean isTakePaymentButtonDisabled() {
        try {
            Locator takePaymentButton = page.getByRole(AriaRole.BUTTON, 
                new Page.GetByRoleOptions().setName("Take Payment"));
            
            // Check if button is disabled via attribute or class
            boolean isDisabled = takePaymentButton.isDisabled() || 
                                takePaymentButton.getAttribute("disabled") != null ||
                                takePaymentButton.getAttribute("class").contains("disabled");
            
            System.out.println(isDisabled ? 
                "✓ Take Payment button is DISABLED" : 
                "✗ Take Payment button is ENABLED");
            return isDisabled;
        } catch (Exception e) {
            System.out.println("✗ Take Payment button not found");
            return false;
        }
    }
    
    /**
     * Check if Edit Order button is visible and enabled
     * @return true if button is clickable
     */
    public boolean isEditOrderButtonVisible() {
        try {
            Locator editButton = page.getByRole(AriaRole.BUTTON, 
                new Page.GetByRoleOptions().setName("Edit Order"));
            boolean isVisible = editButton.isVisible();
            System.out.println(isVisible ? 
                "✓ Edit Order button is visible" : 
                "✗ Edit Order button is NOT visible");
            return isVisible;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verify order preferences are displayed correctly
     * @param starch - Expected starch level
     * @param finish - Expected finish type
     * @return true if preferences match
     */
    public boolean verifyPreferences(String starch, String finish) {
        boolean starchMatch = page.locator("text=" + starch).isVisible();
        boolean finishMatch = page.locator("text=" + finish).isVisible();
        
        boolean preferencesCorrect = starchMatch && finishMatch;
        System.out.println(preferencesCorrect ? 
            "✓ Preferences verified: Starch=" + starch + ", Finish=" + finish : 
            "✗ Preferences do NOT match");
        return preferencesCorrect;
    }
    
    /**
     * Verify order note is displayed
     * @param expectedNote - Expected note text
     * @return true if note matches
     */
    public boolean verifyOrderNote(String expectedNote) {
        boolean noteVisible = page.locator("text=" + expectedNote).isVisible();
        System.out.println(noteVisible ? 
            "✓ Order note verified: " + expectedNote : 
            "✗ Order note NOT found");
        return noteVisible;
    }
    
    /**
     * Verify rack/conveyor number is displayed
     * @param expectedRack - Expected rack number
     * @return true if rack number matches
     */
    public boolean verifyRackConveyor(String expectedRack) {
        boolean rackVisible = page.locator("text=" + expectedRack).isVisible();
        System.out.println(rackVisible ? 
            "✓ Rack/Conveyor verified: " + expectedRack : 
            "✗ Rack/Conveyor NOT found");
        return rackVisible;
    }
    
    /**
     * Get payment status from order details
     * @return Payment status (e.g., "Not Paid", "Paid")
     */
    public String getPaymentStatus() {
        String status = page.locator("text=/Not Paid|Paid/").first().textContent().trim();
        System.out.println("✓ Payment status: " + status);
        return status;
    }
    
    /**
     * Navigate back to Orders Hub
     */
    public void navigateBackToOrdersHub() {
        page.goBack();
        page.waitForURL("**/ordersHub");
        System.out.println("✓ Navigated back to Orders Hub");
    }

}
