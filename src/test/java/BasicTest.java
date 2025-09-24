import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

public class BasicTest {
    
    @Test
    @DisplayName("Simple test that always passes")
    public void testAlwaysPasses() {
        System.out.println("======================================");
        System.out.println("BASIC TEST IS RUNNING!");
        System.out.println("======================================");
        assertTrue(true, "This should always pass");
        assertEquals(4, 2 + 2, "Basic math check");
    }
    
    @Test
    @DisplayName("Another simple test")
    public void testStringOperations() {
        String text = "Hello GitHub Actions";
        assertTrue(text.contains("GitHub"));
        assertNotNull(text);
        System.out.println("String test passed: " + text);
    }
}