import org.junit.jupiter.api.Test;

public class QuickOrderTest extends BaseTest {
	
    @Test
    public void testCreateQuickOrderAndVerifyInHub() throws InterruptedException {
        // Step 1: Login to application
        login();
        
        // Step 2: Create page object
        QuickOrderPage quickOrderPage = new QuickOrderPage(page);
        
        // Step 3: Navigate to Quick Order
        System.out.println("\n--- Creating Quick Order ---");
        quickOrderPage.navigateToQuickOrder();
        
        // Step 4: Search and select customer
        quickOrderPage.searchAndSelectFirstCustomer();
        
        // Step 5: Fill order details
        
       quickOrderPage.fillOrderDetails("5", "10", "15");
        
        // Step 6: Click Done
        quickOrderPage.clickDoneButton();
        
        // Step 7: Create order and get order ID
        String orderId = quickOrderPage.createQuickOrder();
        
        // Step 8: Proceed to Orders Hub
        quickOrderPage.clickProceedButton();
        
        // Step 9: Search for order in hub
        quickOrderPage.searchOrderInHub(orderId);
        
        // Step 10: Take screenshot
        takeScreenshot("Quick_Order_Created_Is_Visible");
    }
}