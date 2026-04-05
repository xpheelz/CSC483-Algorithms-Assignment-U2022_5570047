/**
 * CSC 483.1 – Algorithms Analysis and Design
 * Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems
 *
 * Student : Nwaneri Stephen Osinachi
 * ID      : U2022/5570047
 * Session : 2025/2026 – First Semester
 *
 * SearchAlgorithms.java
 * Contains the three search methods required by Question 1 – Part B, Task 2.
 *
 * ┌─────────────────────────────┬──────────┬──────────┬──────────┐
 * │ Method                      │ Best     │ Average  │ Worst    │
 * ├─────────────────────────────┼──────────┼──────────┼──────────┤
 * │ sequentialSearchById        │ O(1)     │ O(n)     │ O(n)     │
 * │ binarySearchById            │ O(1)     │ O(log n) │ O(log n) │
 * │ searchByName                │ O(1)     │ O(n)     │ O(n)     │
 * └─────────────────────────────┴──────────┴──────────┴──────────┘
 */
public class SearchAlgorithms {

    // ══════════════════════════════════════════════════════════════════════════
    //  1.  SEQUENTIAL SEARCH BY ID
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Scans the product array from left to right until the target ID is found
     * or the entire array has been examined.
     *
     * <p><b>Complexity analysis</b></p>
     * <ul>
     *   <li><b>Best case  O(1)</b>  – target is at index 0; only 1 comparison.</li>
     *   <li><b>Average O(n/2) ≡ O(n)</b>  – on average the target sits near
     *       the middle; approximately n/2 comparisons are needed.</li>
     *   <li><b>Worst case O(n)</b>  – target is at the last position or absent;
     *       exactly n comparisons are performed.</li>
     * </ul>
     *
     * <p><b>Exact comparison count</b> for an array of n products:</p>
     * <ul>
     *   <li>Best  : 1</li>
     *   <li>Average: (n + 1) / 2  ≈  n/2</li>
     *   <li>Worst : n</li>
     * </ul>
     *
     * @param products  array of Product objects (order does not matter)
     * @param targetId  the productId to search for
     * @return the matching Product, or {@code null} if not found
     */
    public static Product sequentialSearchById(Product[] products, int targetId) {
        for (int i = 0; i < products.length; i++) {
            // Each iteration costs exactly one comparison
            if (products[i].getProductId() == targetId) {
                return products[i];          // found – stop scanning
            }
        }
        return null;                         // exhausted array without a match
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  2.  BINARY SEARCH BY ID
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Uses divide-and-conquer to locate a product in a <em>sorted</em> array.
     *
     * <p><b>Pre-condition (mandatory)</b>: {@code products} must be sorted in
     * <em>ascending</em> order of {@code productId}.  Violating this will produce
     * incorrect results without any warning.</p>
     *
     * <p><b>Complexity analysis</b></p>
     * <ul>
     *   <li><b>Best case  O(1)</b>  – target equals the very first mid element;
     *       1 comparison suffices.</li>
     *   <li><b>Average  O(log n)</b> – the search space is halved each iteration;
     *       approximately ⌊log₂ n⌋ + 1 comparisons on average.</li>
     *   <li><b>Worst case O(log n)</b> – target is absent or at a leaf of the
     *       virtual search tree; ⌊log₂ n⌋ + 1 comparisons are made.</li>
     * </ul>
     *
     * <p><b>Why binary search is more efficient</b><br>
     * Sequential search checks each element linearly; binary search eliminates
     * half the remaining candidates with every comparison.  For n = 100,000
     * the worst-case comparisons are 100,000 vs ⌊log₂(100,000)⌋ + 1 = 17.
     * That is roughly a 5,882× reduction in comparisons.</p>
     *
     * @param products  product array sorted by productId in ascending order
     * @param targetId  the productId to search for
     * @return the matching Product, or {@code null} if not found
     */
    public static Product binarySearchById(Product[] products, int targetId) {
        int low  = 0;
        int high = products.length - 1;

        while (low <= high) {
            // mid = low + (high - low) / 2  avoids integer overflow
            int mid = low + (high - low) / 2;

            if (products[mid].getProductId() == targetId) {
                return products[mid];        // ── found ──

            } else if (products[mid].getProductId() < targetId) {
                low = mid + 1;              // target lies in the RIGHT half

            } else {
                high = mid - 1;            // target lies in the LEFT half
            }
        }
        return null;                        // low > high → not found
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  3.  SEARCH BY NAME (sequential – names are unsorted)
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Performs a case-insensitive sequential scan for a product by its name.
     *
     * <p>Binary search cannot be applied here because the array is sorted by
     * {@code productId}, <em>not</em> by name.  Maintaining a separate sorted
     * name array (or a HashMap index) is the job of the hybrid approach in Part C.</p>
     *
     * <p><b>Complexity analysis</b></p>
     * <ul>
     *   <li><b>Best case  O(1)</b>  – name matches the first element.</li>
     *   <li><b>Average  O(n)</b>  – name matches near the middle.</li>
     *   <li><b>Worst case O(n)</b>  – name matches the last element or is absent.</li>
     * </ul>
     *
     * @param products    array of Product objects (any order)
     * @param targetName  the product name to locate (case-insensitive)
     * @return the first matching Product, or {@code null} if not found
     */
    public static Product searchByName(Product[] products, String targetName) {
        for (Product product : products) {
            if (product.getProductName().equalsIgnoreCase(targetName)) {
                return product;
            }
        }
        return null;
    }
}
