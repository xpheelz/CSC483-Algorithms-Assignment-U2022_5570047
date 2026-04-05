import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

/**
 * CSC 483.1 – Algorithms Analysis and Design
 * Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems
 *
 * Student : Nwaneri Stephen Osinachi
 * ID      : U2022/5570047
 * Session : 2025/2026 – First Semester
 *
 * SearchTest.java  –  JUnit 5 test suite
 *
 * Verifies correctness of:
 *  • SearchAlgorithms.sequentialSearchById  (6 tests)
 *  • SearchAlgorithms.binarySearchById      (6 tests)
 *  • SearchAlgorithms.searchByName          (3 tests)
 *  • HybridSearchSystem.searchById          (2 tests)
 *  • HybridSearchSystem.searchByName        (2 tests)
 *  • HybridSearchSystem.addProduct          (3 tests)
 *
 * Total: 22 test cases
 *
 * Compile & run with Maven/Gradle or directly:
 *   javac -cp junit-platform-console-standalone-1.x.jar *.java
 *   java  -jar junit-platform-console-standalone-1.x.jar --class-path . --scan-class-path
 */
@DisplayName("TechMart Search Algorithm Test Suite")
class SearchTest {

    // ─── Shared fixtures created once for all tests ───────────────────────────
    private static Product[] unsorted;   // preserved – sequential & name tests
    private static Product[] sorted;     // sorted by ID – binary search tests

    // Fresh hybrid system created before each test to isolate state
    private HybridSearchSystem hybrid;

    @BeforeAll
    static void buildFixtures() {
        unsorted = new Product[]{
            new Product(5,  "Wireless Mouse",     "Accessories",  29.99, 100),
            new Product(2,  "Mechanical Keyboard","Accessories",  89.99,  50),
            new Product(8,  "4K Monitor",         "Electronics", 399.99,  20),
            new Product(1,  "USB-C Hub",          "Accessories",  49.99,  75),
            new Product(10, "Laptop Stand",       "Accessories",  39.99,  60),
            new Product(3,  "Gaming Headset",     "Audio",        79.99,  40),
            new Product(7,  "Webcam HD",          "Electronics",  59.99,  35),
            new Product(4,  "SSD 1TB",            "Storage",     109.99,  80),
            new Product(9,  "Smart Speaker",      "Audio",        99.99,  25),
            new Product(6,  "Ethernet Cable",     "Networking",    9.99, 200)
        };

        // sorted copy: IDs 1,2,3,4,5,6,7,8,9,10
        sorted = Arrays.copyOf(unsorted, unsorted.length);
        Arrays.sort(sorted);
    }

    @BeforeEach
    void buildHybrid() {
        hybrid = new HybridSearchSystem(unsorted);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SEQUENTIAL SEARCH BY ID
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Seq – best case: target at index 0 is found")
    void sequential_BestCase_TargetAtFirstIndex() {
        // unsorted[0].productId == 5
        Product result = SearchAlgorithms.sequentialSearchById(unsorted, 5);
        assertNotNull(result, "Product with ID 5 must be found");
        assertEquals(5, result.getProductId());
        assertEquals("Wireless Mouse", result.getProductName());
    }

    @Test
    @DisplayName("Seq – average case: target at middle index is found")
    void sequential_AverageCase_TargetAtMiddle() {
        // unsorted[4].productId == 10
        Product result = SearchAlgorithms.sequentialSearchById(unsorted, 10);
        assertNotNull(result);
        assertEquals(10, result.getProductId());
    }

    @Test
    @DisplayName("Seq – worst case: target at last index is found")
    void sequential_WorstCase_TargetAtLastIndex() {
        // unsorted[9].productId == 6
        Product result = SearchAlgorithms.sequentialSearchById(unsorted, 6);
        assertNotNull(result);
        assertEquals(6, result.getProductId());
        assertEquals("Ethernet Cable", result.getProductName());
    }

    @Test
    @DisplayName("Seq – absent ID returns null")
    void sequential_AbsentId_ReturnsNull() {
        assertNull(SearchAlgorithms.sequentialSearchById(unsorted, 999));
    }

    @Test
    @DisplayName("Seq – single-element array: found")
    void sequential_SingleElement_Found() {
        Product[] one = { new Product(42, "Solo", "Test", 1.00, 1) };
        assertNotNull(SearchAlgorithms.sequentialSearchById(one, 42));
    }

    @Test
    @DisplayName("Seq – empty array returns null without exception")
    void sequential_EmptyArray_ReturnsNull() {
        assertNull(SearchAlgorithms.sequentialSearchById(new Product[0], 1));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BINARY SEARCH BY ID
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Bin – best case: target is mid element (ID 5 or 6) is found")
    void binary_BestCase_MidElement() {
        // sorted IDs: 1..10, mid ≈ index 4 or 5
        Product result = SearchAlgorithms.binarySearchById(sorted, 5);
        assertNotNull(result, "ID 5 must be found in sorted array");
        assertEquals(5, result.getProductId());
    }

    @Test
    @DisplayName("Bin – first element (ID 1) is found")
    void binary_FirstElement_Found() {
        Product result = SearchAlgorithms.binarySearchById(sorted, 1);
        assertNotNull(result);
        assertEquals(1, result.getProductId());
        assertEquals("USB-C Hub", result.getProductName());
    }

    @Test
    @DisplayName("Bin – last element (ID 10) is found")
    void binary_LastElement_Found() {
        Product result = SearchAlgorithms.binarySearchById(sorted, 10);
        assertNotNull(result);
        assertEquals(10, result.getProductId());
    }

    @Test
    @DisplayName("Bin – absent ID returns null (worst case)")
    void binary_AbsentId_ReturnsNull() {
        assertNull(SearchAlgorithms.binarySearchById(sorted, 999));
    }

    @Test
    @DisplayName("Bin – all 10 IDs are individually retrievable")
    void binary_AllIds_AreFound() {
        for (int id = 1; id <= 10; id++) {
            Product p = SearchAlgorithms.binarySearchById(sorted, id);
            assertNotNull(p, "ID " + id + " must be found");
            assertEquals(id, p.getProductId());
        }
    }

    @Test
    @DisplayName("Bin – empty array returns null without exception")
    void binary_EmptyArray_ReturnsNull() {
        assertNull(SearchAlgorithms.binarySearchById(new Product[0], 1));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SEARCH BY NAME (sequential, unsorted)
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Name – exact match is found")
    void nameSearch_ExactMatch_Found() {
        Product result = SearchAlgorithms.searchByName(unsorted, "4K Monitor");
        assertNotNull(result);
        assertEquals(8, result.getProductId());
    }

    @Test
    @DisplayName("Name – match is case-insensitive")
    void nameSearch_CaseInsensitive() {
        Product r1 = SearchAlgorithms.searchByName(unsorted, "4k monitor");
        Product r2 = SearchAlgorithms.searchByName(unsorted, "4K MONITOR");
        assertNotNull(r1);
        assertNotNull(r2);
        assertEquals(8, r1.getProductId());
        assertEquals(8, r2.getProductId());
    }

    @Test
    @DisplayName("Name – non-existent name returns null")
    void nameSearch_NotFound_ReturnsNull() {
        assertNull(SearchAlgorithms.searchByName(unsorted, "Flying Carpet"));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HYBRID SEARCH SYSTEM
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Hybrid searchById – returns correct product")
    void hybrid_SearchById_Found() {
        Product result = hybrid.searchById(8);
        assertNotNull(result);
        assertEquals("4K Monitor", result.getProductName());
    }

    @Test
    @DisplayName("Hybrid searchById – absent ID returns null")
    void hybrid_SearchById_NotFound() {
        assertNull(hybrid.searchById(999));
    }

    @Test
    @DisplayName("Hybrid searchByName – HashMap O(1) lookup succeeds")
    void hybrid_SearchByName_Found() {
        Product result = hybrid.searchByName("Smart Speaker");
        assertNotNull(result);
        assertEquals(9, result.getProductId());
    }

    @Test
    @DisplayName("Hybrid searchByName – case-insensitive lookup succeeds")
    void hybrid_SearchByName_CaseInsensitive() {
        assertNotNull(hybrid.searchByName("SMART SPEAKER"));
        assertNotNull(hybrid.searchByName("smart speaker"));
    }

    @Test
    @DisplayName("Hybrid addProduct – size increases and new product is searchable")
    void hybrid_AddProduct_IncreasesSize_AndSearchable() {
        int before = hybrid.size();
        Product vr = new Product(11, "VR Headset", "Gaming", 299.99, 15);
        hybrid.addProduct(null, vr);

        assertEquals(before + 1, hybrid.size(), "Size must increment by 1");
        assertNotNull(hybrid.searchById(11),          "New ID must be found by searchById");
        assertNotNull(hybrid.searchByName("VR Headset"), "New name must be found by searchByName");
    }

    @Test
    @DisplayName("Hybrid addProduct – sorted order is preserved after insertion")
    void hybrid_AddProduct_MaintainsSortedOrder() {
        // Insert a product with ID 0 (smaller than all existing IDs)
        hybrid.addProduct(null, new Product(0, "Lowest", "Test", 0.01, 1));
        Product[] arr = hybrid.getSortedById();
        for (int i = 1; i < arr.length; i++) {
            assertTrue(arr[i - 1].getProductId() < arr[i].getProductId(),
                       "Array must remain strictly ascending after insertion at index " + i);
        }
    }

    @Test
    @DisplayName("Hybrid addProduct – insertion at end keeps sorted order")
    void hybrid_AddProduct_AtEnd_Sorted() {
        hybrid.addProduct(null, new Product(100, "HighEnd Device", "Electronics", 999.99, 5));
        Product[] arr = hybrid.getSortedById();
        assertEquals(100, arr[arr.length - 1].getProductId(),
                     "Highest ID should land at the last position");
    }
}
