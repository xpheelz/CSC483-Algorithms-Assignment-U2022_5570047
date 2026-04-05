import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * CSC 483.1 – Algorithms Analysis and Design
 * Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems
 *
 * Student : Nwaneri Stephen Osinachi
 * ID      : U2022/5570047
 * Session : 2025/2026 – First Semester
 *
 * HybridSearchSystem.java  (Question 1 – Part C)
 *
 * ═══════════════════════════════════════════════════════════════════
 *  HYBRID SEARCH STRATEGY DESIGN
 * ═══════════════════════════════════════════════════════════════════
 *
 *  The two weaknesses of the Part-B setup are:
 *    1. Name search is always O(n) because names are unsorted.
 *    2. Adding a product to a sorted array naively requires full re-sort O(n log n).
 *
 *  This class addresses both by combining two complementary structures:
 *
 *    ┌──────────────────────────────────────────────────────────┐
 *    │  Structure 1: Product[] sortedById                       │
 *    │  – Array maintained in ascending productId order         │
 *    │  – Supports O(log n) binary search by ID                 │
 *    │  – New insertions placed at correct position via binary  │
 *    │    search for position + element shift  → O(n) insert    │
 *    ├──────────────────────────────────────────────────────────┤
 *    │  Structure 2: HashMap<String, Product> nameIndex         │
 *    │  – Maps lowercase(productName) → Product                 │
 *    │  – O(1) average lookup by name (hash-based)              │
 *    │  – O(1) amortised insert (just one HashMap.put call)     │
 *    └──────────────────────────────────────────────────────────┘
 *
 *  ┌───────────────────────┬──────────────┬───────────────────────┐
 *  │ Operation             │ Complexity   │ Data structure used   │
 *  ├───────────────────────┼──────────────┼───────────────────────┤
 *  │ searchById(id)        │ O(log n)     │ sortedById array      │
 *  │ searchByName(name)    │ O(1) avg     │ nameIndex HashMap     │
 *  │ addProduct(product)   │ O(n)         │ both structures       │
 *  │   – find insert pos   │   O(log n)   │   binary search       │
 *  │   – shift elements    │   O(n)       │   System.arraycopy    │
 *  │   – HashMap put       │   O(1) amort │   HashMap             │
 *  └───────────────────────┴──────────────┴───────────────────────┘
 *
 *  NOTE: The O(n) insert cost is dominated by array-element shifting.
 *  For workloads with very high insert frequency a self-balancing BST
 *  (e.g. TreeMap) or skip list would reduce insert to O(log n) at the
 *  cost of slightly higher constant factors for search.
 *
 * ═══════════════════════════════════════════════════════════════════
 *  PSEUDOCODE – addProduct(products, newProduct)
 * ═══════════════════════════════════════════════════════════════════
 *  1. IF backing array is full THEN double its capacity
 *  2. pos ← binarySearchForInsertionPoint(newProduct.productId)
 *       low ← 0 ; high ← size – 1
 *       WHILE low ≤ high DO
 *           mid ← low + (high – low) / 2
 *           IF sortedById[mid].productId < newProduct.productId
 *               THEN low ← mid + 1
 *               ELSE high ← mid – 1
 *       RETURN low                        // insertion index
 *  3. Shift sortedById[pos .. size-1] one position to the right
 *  4. sortedById[pos] ← newProduct
 *  5. nameIndex.put(toLowerCase(newProduct.name), newProduct)
 *  6. size ← size + 1
 *  7. RETURN updated array slice [0 .. size-1]
 * ═══════════════════════════════════════════════════════════════════
 */
public class HybridSearchSystem {

    // ─── Internal state ───────────────────────────────────────────────────────
    private Product[]            sortedById;    // sorted ascending by productId
    private Map<String, Product> nameIndex;     // name (lowercase) → Product
    private int                  size;          // current number of products
    private static final int     INITIAL_CAP = 32;

    // ══════════════════════════════════════════════════════════════════════════
    //  CONSTRUCTORS
    // ══════════════════════════════════════════════════════════════════════════

    /** Creates an empty hybrid system. */
    public HybridSearchSystem() {
        this.sortedById = new Product[INITIAL_CAP];
        this.nameIndex  = new HashMap<>();
        this.size       = 0;
    }

    /**
     * Builds a hybrid system from an existing (possibly unsorted) array.
     * Sorts once in O(n log n) and builds the name index in O(n).
     *
     * @param products source array (not modified)
     */
    public HybridSearchSystem(Product[] products) {
        // Deep-copy to avoid mutating the caller's array
        this.sortedById = Arrays.copyOf(products, Math.max(products.length, INITIAL_CAP));
        Arrays.sort(this.sortedById, 0, products.length); // sort by compareTo → productId
        this.size       = products.length;

        // Build name index
        this.nameIndex = new HashMap<>(size * 2);
        for (int i = 0; i < size; i++) {
            nameIndex.put(sortedById[i].getProductName().toLowerCase(), sortedById[i]);
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  SEARCH OPERATIONS
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Searches by product ID using binary search on the sorted array.
     *
     * <p>Time complexity: <b>O(log n)</b></p>
     *
     * @param targetId the productId to locate
     * @return matching Product or {@code null}
     */
    public Product searchById(int targetId) {
        int low  = 0;
        int high = size - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = Integer.compare(sortedById[mid].getProductId(), targetId);

            if      (cmp == 0) return sortedById[mid];  // found
            else if (cmp < 0)  low  = mid + 1;
            else               high = mid - 1;
        }
        return null;  // not found
    }

    /**
     * Searches by product name using the HashMap index.
     * Match is case-insensitive.
     *
     * <p>Time complexity: <b>O(1) average</b> (hash lookup)</p>
     *
     * @param targetName the product name to locate
     * @return matching Product or {@code null}
     */
    public Product searchByName(String targetName) {
        if (targetName == null) return null;
        return nameIndex.get(targetName.toLowerCase());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INSERT OPERATION
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Inserts {@code newProduct} while preserving sorted order in the ID array
     * so that binary search remains valid after every insertion.
     *
     * <p><b>Algorithm</b> (matches the pseudocode in the class header)</p>
     * <ol>
     *   <li>Grow the backing array if it is at capacity (amortised O(1)).</li>
     *   <li>Use binary search to find the correct insertion index in O(log n).</li>
     *   <li>Shift existing elements one position right to open a slot in O(n).</li>
     *   <li>Place the new product and update the name index in O(1).</li>
     * </ol>
     *
     * <p>Time complexity: <b>O(n)</b> dominated by element shifting.<br>
     * Space: amortised <b>O(1)</b> – array doubles when full.</p>
     *
     * @param products   the caller's product array reference (kept for API compatibility;
     *                   the hybrid system manages its own internal sorted copy)
     * @param newProduct the product to add
     * @return current sorted array snapshot (defensive copy, length == size)
     */
    public Product[] addProduct(Product[] products, Product newProduct) {
        // Step 1 – grow if necessary
        ensureCapacity();

        // Step 2 – binary search for insertion index
        int pos = findInsertionIndex(newProduct.getProductId());

        // Step 3 – shift elements right to make room
        System.arraycopy(sortedById, pos, sortedById, pos + 1, size - pos);

        // Step 4 – insert into sorted array and name index
        sortedById[pos] = newProduct;
        nameIndex.put(newProduct.getProductName().toLowerCase(), newProduct);
        size++;

        // Return updated snapshot
        return Arrays.copyOf(sortedById, size);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HELPER / UTILITY METHODS
    // ══════════════════════════════════════════════════════════════════════════

    /** Returns the current number of products stored. */
    public int size() { return size; }

    /** Returns a defensive copy of the internal sorted array. */
    public Product[] getSortedById() { return Arrays.copyOf(sortedById, size); }

    /**
     * Finds the index at which {@code targetId} should be inserted to keep
     * the array sorted.  Uses binary search; returns the leftmost valid
     * insertion point.
     *
     * @param targetId the productId to place
     * @return insertion index in [0, size]
     */
    private int findInsertionIndex(int targetId) {
        int low = 0, high = size - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (sortedById[mid].getProductId() < targetId) low  = mid + 1;
            else                                           high = mid - 1;
        }
        return low; // correct insertion point
    }

    /** Doubles the backing array if it has reached capacity. */
    private void ensureCapacity() {
        if (size == sortedById.length) {
            sortedById = Arrays.copyOf(sortedById, sortedById.length * 2);
        }
    }

    @Override
    public String toString() {
        return String.format("HybridSearchSystem{size=%d, nameIndexSize=%d}",
                             size, nameIndex.size());
    }
}
