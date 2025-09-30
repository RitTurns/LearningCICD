import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import java.util.Random;

public class AddCustomerPage {
    
    // Page object reference
    private Page page;
    
    // Store customer data for verification
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerMobile;
    
    // Configuration constants
    private static final String DEFAULT_STORE = "PayRange Laundry";
    private static final String DEFAULT_PRICE_LIST = "Existing price List";
    
    // Constructor
    public AddCustomerPage(Page page) {
        this.page = page;
    }
    
    // ============= NAVIGATION METHODS =============
    
    /**
     * Navigate to customer creation page by clicking Customer menu and Add button
     */
    public void navigateToCustomerCreation() {
        System.out.println("\n[Navigation] Going to customer creation page...");
        
        // Click on Customer menu item
        page.locator("a[data-menu-key='customer']").click();
        page.waitForTimeout(1000);
        
        // Click on the Add (+) button
        page.locator(".bi-plus-lg").first().click();
        page.waitForTimeout(1000);
        
        System.out.println("✓ Customer creation form loaded");
    }
    
    // ============= FORM FILLING METHODS =============
    
    /**
     * Fill all mandatory fields with random data
     */
    public void fillMandatoryFields() {
        System.out.println("\n[Form] Filling mandatory fields...");
        
        // Generate and fill First Name
        customerFirstName = generateRandomFirstName();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First name *"))
            .fill(customerFirstName);
        System.out.println("  → First Name: " + customerFirstName);
        
        // Generate and fill Mobile Number
        customerMobile = generateRandomMobile();
        page.locator("input[placeholder='Enter phone number']").fill(customerMobile);
        System.out.println("  → Mobile: +1 " + customerMobile);
    }
    
    /**
     * Fill optional fields (Last Name and Email)
     */
    public void fillOptionalFields() {
        System.out.println("\n[Form] Filling optional fields...");
        
        // Fill Last Name
        customerLastName = generateRandomLastName();
        try {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last name"))
                .fill(customerLastName);
            System.out.println("  → Last Name: " + customerLastName);
        } catch (Exception e) {
            System.out.println("  ⚠ Last Name field not found");
        }
        
        // Fill Email
        customerEmail = generateEmailFromName(customerFirstName, customerLastName);
        try {
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email"))
                .fill(customerEmail);
            System.out.println("  → Email: " + customerEmail);
        } catch (Exception e) {
            System.out.println("  ⚠ Email field not found");
        }
    }
    
    /**
     * Select store from dropdown
     */
    public void selectStore(String storeName) {
        selectFromDropdown("Select Store *", storeName);
        System.out.println("  → Store: " + storeName);
    }
    
    /**
     * Select store with default value
     */
    public void selectStore() {
        selectStore(DEFAULT_STORE);
    }
    
    /**
     * Select price list from dropdown
     */
    public void selectPriceList(String priceListName) {
        selectFromDropdown("Select Price List *", priceListName);
        System.out.println("  → Price List: " + priceListName);
    }
    
    /**
     * Select price list with default value
     */
    public void selectPriceList() {
        selectPriceList(DEFAULT_PRICE_LIST);
    }
    
    /**
     * Fill address fields with random US address
     */
    public void fillAddressFields() {
        System.out.println("\n[Form] Filling address fields...");
        
        try {
            // Fill Address field and wait for suggestions
            String address1 = generateRandomAptNumber();
            page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Address"))
                .fill(address1);
            System.out.println("  → Address: " + address1);
            
            // Wait for address suggestions to appear
            Locator suggestions = page.locator("ul.list-group.card.shadow-0.bg-white > li.list-group-item");
            suggestions.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(3_000));
            
        } catch (Exception e) {
            System.out.println("  ⚠ Address field handling: " + e.getMessage());
        }
    }
    
    /**
     * Submit the customer creation form
     */
    public void submitForm() {
        System.out.println("\n[Form] Submitting...");
        
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create"))
            .click();
        
        page.waitForTimeout(2000);
        System.out.println("✓ Form submitted");
    }
    
    // ============= VERIFICATION METHODS =============
    
    /**
     * Check if success message appears after customer creation
     * @return true if success message is found, false otherwise
     */
    public boolean isSuccessMessageDisplayed() {
        System.out.println("\n[Verification] Checking for success message...");
        
        try {
            Locator successMessage = page.locator("text=/.*success.*/i").first();
            
            if (successMessage.isVisible()) {
                String messageText = successMessage.textContent();
                System.out.println("✓ Success message found: " + messageText);
                return true;
            }
        } catch (Exception e) {
            System.out.println("⚠ Success message not found");
        }
        
        return false;
    }
    
    /**
     * Search for a customer using the search bar
     */
    public void searchCustomer(String searchTerm) {
        System.out.println("\n[Search] Searching for: " + searchTerm);
        
        try {
            // Find search box
            Locator searchBox = page.locator("input[placeholder*='Search...']").first();
            
            if (!searchBox.isVisible()) {
                searchBox = page.locator("input[type='search']").first();
            }
            
            // Clear and search
            searchBox.clear();
            searchBox.fill(searchTerm);
            searchBox.press("Enter");
            
            page.waitForTimeout(2000);
            System.out.println("✓ Search completed");
            
        } catch (Exception e) {
            System.out.println("⚠ Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Search for the created customer using stored first name
     */
    public void searchCreatedCustomer() {
        searchCustomer(customerFirstName);
    }
    
    /**
     * Check if customer appears in the list
     * @param customerName The name to look for
     * @return true if customer is found, false otherwise
     */
    public boolean isCustomerInList(String customerName) {
        System.out.println("\n[Verification] Looking for customer: " + customerName);
        
        try {
            Locator customerInList = page.locator("text=" + customerName).first();
            
            if (customerInList.isVisible()) {
                System.out.println("✓ Customer found in list");
                return true;
            }
        } catch (Exception e) {
            System.out.println("✗ Customer not found in list");
        }
        
        return false;
    }
    
    /**
     * Check if the created customer appears in the list
     * @return true if customer is found, false otherwise
     */
    public boolean isCreatedCustomerInList() {
        return isCustomerInList(customerFirstName);
    }
    
    // ============= HELPER METHODS =============
    
    /**
     * Select an option from React Select dropdown
     */
    private void selectFromDropdown(String dropdownLabel, String optionText) {
        try {
            // Find and click the dropdown
            Locator dropdown = page.locator("div.react-select__control")
                .filter(new Locator.FilterOptions()
                    .setHas(page.locator("text=" + dropdownLabel)));
            dropdown.click();
            
            page.waitForTimeout(500);
            
            // Select the option
            page.locator("div.react-select__option", new Page.LocatorOptions()
                .setHasText(optionText)).first().click();
            
        } catch (Exception e) {
            System.out.println("⚠ Dropdown selection failed: " + e.getMessage());
        }
    }
    
    // ============= DATA GENERATION METHODS =============
    
    /**
     * Generate a random first name
     */
    private String generateRandomFirstName() {
        String[] names = {"John", "Jane", "Mike", "Sarah", "David", "Emma", 
                         "Chris", "Lisa", "Tom", "Anna", "Robert", "Mary"};
        Random random = new Random();
        return names[random.nextInt(names.length)];
    }
    
    /**
     * Generate a random last name
     */
    private String generateRandomLastName() {
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", 
                             "Garcia", "Miller", "Davis", "Wilson", "Martinez",
                             "Anderson", "Taylor", "Thomas", "Moore", "Jackson"};
        Random random = new Random();
        return lastNames[random.nextInt(lastNames.length)];
    }
    
    /**
     * Generate email based on customer name initials
     */
    private String generateEmailFromName(String firstName, String lastName) {
        Random random = new Random();
        String initials = firstName.substring(0, 1).toUpperCase() + 
                         lastName.substring(0, 1).toUpperCase();
        int randomNumber = 1000 + random.nextInt(9000);
        return initials + randomNumber + "@test.com";
    }
    
    /**
     * Generate a random 10-digit mobile number
     */
    private String generateRandomMobile() {
        Random random = new Random();
        StringBuilder mobile = new StringBuilder();
        
        // First digit (2-9)
        mobile.append(2 + random.nextInt(8));
        
        // Remaining 9 digits
        for (int i = 0; i < 9; i++) {
            mobile.append(random.nextInt(10));
        }
        
        return mobile.toString();
    }
    
    /**
     * Generate random apartment/suite number
     */
    private String generateRandomAptNumber() {
        Random random = new Random();
        String[] types = {"Apt", "Suite", "Unit"};
        String type = types[random.nextInt(types.length)];
        
        int aptNumber = 1 + random.nextInt(999);
        
        // Sometimes add a letter (20% chance)
        String letter = "";
        if (random.nextInt(5) == 0) {
            letter = String.valueOf((char)('A' + random.nextInt(4)));
        }
        
        return type + " " + aptNumber + letter;
    }
    
    // ============= GETTER METHODS (for assertions in test) =============
    
    public String getCustomerFirstName() {
        return customerFirstName;
    }
    
    public String getCustomerLastName() {
        return customerLastName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public String getCustomerMobile() {
        return customerMobile;
    }
}
