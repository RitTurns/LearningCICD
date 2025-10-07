import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class QuickOrderPage {
    
    private Page page;
    private static final String SEARCH_INPUT = "Search Customers by Name, Phone no., Email address";
    private static final int DEFAULT_TIMEOUT = 10000;
    
    public QuickOrderPage(Page page) {
        this.page = page;
    }
    
    public void navigateToQuickOrder() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Quick Order"))
            .click();
        page.waitForURL("**/quickOrder");
        System.out.println("✓ Navigated to Quick Order page");
        page.waitForTimeout(1500);
    }
    
    public void searchAndSelectFirstCustomer() {
        page.getByPlaceholder(SEARCH_INPUT).click();
        System.out.println("✓ Clicked on customer search field");
        
        page.waitForSelector("li.list-group-item", new Page.WaitForSelectorOptions().setTimeout(5000));
        System.out.println("✓ Customer list is now visible");
        page.waitForTimeout(2000);
        
        page.locator("li.list-group-item").first().click();
        System.out.println("✓ Clicked on first customer from search results");
        page.waitForTimeout(2000);
        
        page.waitForTimeout(2000);
        System.out.println("✓ Screen updated with customer details");
        page.waitForTimeout(2000);
    }
    
    public void fillOrderDetails(String bags, String quantity, String lbs) {
    	page.waitForTimeout(2000);
    	page.locator("input[type='number'].form-control.text-center").first()
        .waitFor(new Locator.WaitForOptions()
                .setTimeout(DEFAULT_TIMEOUT));

       Locator secondInput = page.locator("input[type='number'].form-control.text-center").nth(1);
        secondInput.waitFor(new Locator.WaitForOptions().setTimeout(DEFAULT_TIMEOUT));
        secondInput.fill(quantity);
        secondInput.press("Tab");

        Locator thirdInput = page.locator("input[type='number'].form-control.text-center").nth(2);
        thirdInput.waitFor(new Locator.WaitForOptions().setTimeout(DEFAULT_TIMEOUT));
        thirdInput.fill(lbs);
        thirdInput.press("Tab");

System.out.println("✓ Filled order details");
    	
//    	page.waitForTimeout(2000);
//     //   page.locator("input[type='number'].form-control.text-center").first().fill(bags);
//        
//        Locator secondInput = page.locator("input[type='number'].form-control.text-center").nth(1);
//        secondInput.fill(quantity);
//        
//        Locator thirdInput = page.locator("input[type='number'].form-control.text-center").nth(2);
//        thirdInput.fill(lbs);
    }
    
    public void clickDoneButton() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Done")).click();
        System.out.println("✓ Clicked Done button");
        page.waitForTimeout(500);
    }
    
    public String createQuickOrder() {
        Locator createQuickOrder = page.locator("xpath=//button[normalize-space(text())='Create Quick Order']");
        createQuickOrder.click(new Locator.ClickOptions().setForce(true));
        
        page.waitForURL("**/orderNotification");
        
        String orderId = page.locator("text=/^#PLO\\d+/").textContent();
        System.out.println("✓ Order created successfully: " + orderId);
        
        return orderId;
    }
    
    public void clickProceedButton() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Proceed")).click();
        page.waitForURL("**/ordersHub");
        System.out.println("\n--- Verifying Order in Orders Hub ---");
        page.waitForTimeout(2500);
    }
    
    public void searchOrderInHub(String orderId) {
        Locator searchBox = page.locator("input[placeholder*='Search...']").first();
        searchBox.fill(orderId);
    }
}