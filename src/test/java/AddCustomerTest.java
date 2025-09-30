import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

/**
 * Test class for Customer Creation functionality
 * This class extends BaseTest to inherit browser setup and common methods
 */
public class AddCustomerTest extends BaseTest {
    
    // Configuration variables - Change these as needed
    private static final String STORE_NAME = "PayRange Laundry";
    private static final String PRICE_LIST_NAME = "Existing price List";
    
    @Test
    @DisplayName("Test: Create new customer with mandatory fields only")
    public void testCreateCustomerWithMandatoryFields() {
        // Arrange - Login and initialize page object
        login();
        AddCustomerPage customerPage = new AddCustomerPage(page);
        
        // Act - Create customer with mandatory fields only
        customerPage.navigateToCustomerCreation();
        customerPage.fillMandatoryFields();
        customerPage.selectStore(STORE_NAME);
        customerPage.selectPriceList(PRICE_LIST_NAME);
        customerPage.submitForm();
        
        // Assert - Verify customer was created successfully
        Assertions.assertTrue(customerPage.isSuccessMessageDisplayed(), 
            "Success message should be displayed after customer creation");
        
        // Search and verify customer appears in list
        customerPage.searchCreatedCustomer();
        Assertions.assertTrue(customerPage.isCreatedCustomerInList(), 
            "Created customer should appear in the customer list");
    }
    
    @Test
    @DisplayName("Test: Create new customer with all fields")
    public void testCreateCustomerWithAllFields() {
        // Arrange - Login and initialize page object
        login();
        AddCustomerPage customerPage = new AddCustomerPage(page);
        
        // Act - Create customer with all fields
        customerPage.navigateToCustomerCreation();
        customerPage.fillMandatoryFields();
        customerPage.fillOptionalFields();
        customerPage.selectStore(STORE_NAME);
        customerPage.selectPriceList(PRICE_LIST_NAME);
        customerPage.fillAddressFields();
        customerPage.submitForm();
        
        // Assert - Verify customer was created successfully
        Assertions.assertTrue(customerPage.isSuccessMessageDisplayed(), 
            "Success message should be displayed after customer creation");
        
        // Search and verify customer appears in list
        customerPage.searchCreatedCustomer();
        Assertions.assertTrue(customerPage.isCreatedCustomerInList(), 
            "Created customer should appear in the customer list");
        
        // Optional: Take screenshot for documentation
        takeScreenshot("customer_created_" + customerPage.getCustomerFirstName());
    }
    
    @Test
    @DisplayName("Test: Create customer with default store and price list")
    public void testCreateCustomerWithDefaults() {
        // Arrange - Login and initialize page object
        login();
        AddCustomerPage customerPage = new AddCustomerPage(page);
        
        // Act - Create customer using default values
        customerPage.navigateToCustomerCreation();
        customerPage.fillMandatoryFields();
        customerPage.selectStore();  // Uses default store
        customerPage.selectPriceList();  // Uses default price list
        customerPage.submitForm();
        
        // Assert - Verify customer was created successfully
        Assertions.assertTrue(customerPage.isSuccessMessageDisplayed(), 
            "Success message should be displayed after customer creation");
        
        // Search and verify customer appears in list
        customerPage.searchCreatedCustomer();
        Assertions.assertTrue(customerPage.isCreatedCustomerInList(), 
            "Created customer should appear in the customer list");
    }
    
    @Test
    @DisplayName("Test: Verify customer data after creation")
    public void testVerifyCustomerData() {
        // Arrange - Login and initialize page object
        login();
        AddCustomerPage customerPage = new AddCustomerPage(page);
        
        // Act - Create customer and store data
        customerPage.navigateToCustomerCreation();
        customerPage.fillMandatoryFields();
        customerPage.fillOptionalFields();
        customerPage.selectStore(STORE_NAME);
        customerPage.selectPriceList(PRICE_LIST_NAME);
        customerPage.submitForm();
        
        // Store customer data for verification
        String firstName = customerPage.getCustomerFirstName();
        String lastName = customerPage.getCustomerLastName();
        String email = customerPage.getCustomerEmail();
        String mobile = customerPage.getCustomerMobile();
        
        // Assert - Basic validations
        Assertions.assertNotNull(firstName, "First name should not be null");
        Assertions.assertNotNull(lastName, "Last name should not be null");
        Assertions.assertNotNull(email, "Email should not be null");
        Assertions.assertNotNull(mobile, "Mobile should not be null");
        
        // Verify email format
        Assertions.assertTrue(email.contains("@test.com"), 
            "Email should have correct domain");
        
        // Verify mobile number length
        Assertions.assertEquals(10, mobile.length(), 
            "Mobile number should be 10 digits");
        
        // Verify customer was created
        Assertions.assertTrue(customerPage.isSuccessMessageDisplayed(), 
            "Success message should be displayed");
        
        // Search and verify in list
        customerPage.searchCreatedCustomer();
        Assertions.assertTrue(customerPage.isCreatedCustomerInList(), 
            "Customer should be in the list");
        
        // Log customer details for debugging
        System.out.println("\n===== Created Customer Details =====");
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Email: " + email);
        System.out.println("Mobile: " + mobile);
        System.out.println("===================================\n");
    }
    
    @Test
    @DisplayName("Test: Search functionality after customer creation")
    public void testSearchCustomerFunctionality() {
        // Arrange - Login and initialize page object
        login();
        AddCustomerPage customerPage = new AddCustomerPage(page);
        
        // Act - Create a customer first
        customerPage.navigateToCustomerCreation();
        customerPage.fillMandatoryFields();
        customerPage.selectStore(STORE_NAME);
        customerPage.selectPriceList(PRICE_LIST_NAME);
        customerPage.submitForm();
        
        // Store the customer name for searching
        String customerName = customerPage.getCustomerFirstName();
        
        // Assert creation was successful
        Assertions.assertTrue(customerPage.isSuccessMessageDisplayed(), 
            "Customer should be created successfully");
        
        // Act - Search for the customer
        customerPage.searchCustomer(customerName);
        
        // Assert - Verify customer is found
        Assertions.assertTrue(customerPage.isCustomerInList(customerName), 
            "Customer '" + customerName + "' should be found in search results");
        
        // Act - Search with partial name (first 3 letters)
        String partialName = customerName.substring(0, Math.min(customerName.length(), 3));
        customerPage.searchCustomer(partialName);
        
        // Assert - Verify customer is still found with partial search
        Assertions.assertTrue(customerPage.isCustomerInList(customerName), 
            "Customer should be found with partial name search: " + partialName);
    }
}