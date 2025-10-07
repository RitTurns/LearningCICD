import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Locator;

public class QuickOrderTest extends BaseTest {
	private static final int DEFAULT_TIMEOUT = 10000;
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
        page.waitForTimeout(2000);
    	Locator firstInput=page.locator("input[type='number'].form-control.text-center").first();
        firstInput.waitFor(new Locator.WaitForOptions()
                .setTimeout(DEFAULT_TIMEOUT));
    	firstInput.fill("5");

       Locator secondInput = page.locator("input[type='number'].form-control.text-center").nth(1);
        secondInput.waitFor(new Locator.WaitForOptions().setTimeout(DEFAULT_TIMEOUT));
        secondInput.fill("10");
        secondInput.press("Tab");

        Locator thirdInput = page.locator("input[type='number'].form-control.text-center").nth(2);
        thirdInput.waitFor(new Locator.WaitForOptions().setTimeout(DEFAULT_TIMEOUT));
        thirdInput.fill("5");
        thirdInput.press("Tab");
     //   quickOrderPage.fillOrderDetails("5", "10", "15");
        
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