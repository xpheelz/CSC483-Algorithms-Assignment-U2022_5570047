import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * CSC 483.1 – Algorithms Analysis and Design
 * Assignment: Algorithm Design, Analysis, and Optimization for Real-World Systems
 *
 * Student : Nwaneri Stephen Osinachi
 * ID      : U2022/5570047
 * Session : 2025/2026 – First Semester
 *
 * TechMartSearchApp.java  (Question 1 – Part B, Task 3)
 *
 * Test driver that:
 *  1. Generates 100,000 products with unique random IDs from 1 to 200,000.
 *  2. Benchmarks sequential search and binary search for best, average, and
 *     worst cases using repeated timing to average out JVM noise.
 *  3. Benchmarks the hybrid system's name search and insert operations.
 *  4. Prints a formatted performance analysis table matching the sample output.
 *
 * Compile & run:
 *   javac Product.java SearchAlgorithms.java HybridSearchSystem.java TechMartSearchApp.java
 *   java  TechMartSearchApp
 */
public class TechMartSearchApp {

    // ─── Constants ────────────────────────────────────────────────────────────
    private static final int    N               = 100_000;
    private static final int    ID_MAX          = 200_000;
    private static final int    TIMING_REPS     = 100;   // repetitions per benchmark
    private static final int    WARMUP_REPS     = 10;    // JVM warm-up iterations
    private static final long   SEED            = 42L;   // reproducible results

    private static final String[] CATEGORIES = {
        "Electronics", "Laptops", "Phones", "Tablets",
        "Audio", "Cameras", "Accessories", "Gaming", "Networking", "Storage"
    };

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {

        System.out.println("Generating " + N + " products – please wait...");

        // ── 1. Generate dataset ───────────────────────────────────────────────
        Product[] products = generateDataset(N);

        // ── 2. Create sorted copy for binary search ───────────────────────────
        Product[] sorted = Arrays.copyOf(products, products.length);
        Arrays.sort(sorted);                          // O(n log n)

        // ── 3. Build hybrid system ────────────────────────────────────────────
        HybridSearchSystem hybrid = new HybridSearchSystem(products);

        // ── 4. Select test targets ────────────────────────────────────────────
        // Best case  – sequential: ID at index 0; binary: ID at mid position
        int seqBestId   = products[0].getProductId();           // first in unsorted
        int binBestId   = sorted[sorted.length / 2].getProductId(); // exact mid of sorted

        // Average case – pick a random ID known to exist
        int avgCaseId   = sorted[N / 3].getProductId();

        // Worst case – ID guaranteed absent from both arrays
        int worstId     = -999;

        // Hybrid: sample an existing name
        String sampleName = products[N / 2].getProductName();

        // ── 5. JVM warm-up (discarded) ────────────────────────────────────────
        warmUp(products, sorted, hybrid, seqBestId, binBestId, avgCaseId, sampleName);

        // ── 6. Benchmark ──────────────────────────────────────────────────────
        double seqBest  = timeSequential(products, seqBestId,  TIMING_REPS);
        double seqAvg   = timeSequential(products, avgCaseId,  TIMING_REPS);
        double seqWorst = timeSequential(products, worstId,    TIMING_REPS);

        double binBest  = timeBinary(sorted, binBestId, TIMING_REPS);
        double binAvg   = timeBinary(sorted, avgCaseId, TIMING_REPS);
        double binWorst = timeBinary(sorted, worstId,   TIMING_REPS);

        double hybNameAvg   = timeHybridName(hybrid, sampleName, TIMING_REPS);
        double hybInsertAvg = timeHybridInsert(hybrid, TIMING_REPS);

        // ── 7. Print results ──────────────────────────────────────────────────
        printReport(seqBest, seqAvg, seqWorst,
                    binBest, binAvg, binWorst,
                    hybNameAvg, hybInsertAvg);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  DATASET GENERATION
    // ══════════════════════════════════════════════════════════════════════════
    /**
     * Generates {@code count} products with unique random productIds in [1, ID_MAX].
     * Uses a seeded Random for reproducibility across runs.
     */
    private static Product[] generateDataset(int count) {
        Random      rng    = new Random(SEED);
        Set<Integer> used  = new HashSet<>(count * 2);
        Product[]    data  = new Product[count];
        int          index = 0;

        while (index < count) {
            int id = rng.nextInt(ID_MAX) + 1;          // 1 … 200,000
            if (used.add(id)) {                         // only unique IDs
                String name     = "Product_" + id;
                String category = CATEGORIES[id % CATEGORIES.length];
                double price    = Math.round((50 + rng.nextDouble() * 1950) * 100.0) / 100.0;
                int    stock    = rng.nextInt(1000) + 1;
                data[index++]   = new Product(id, name, category, price, stock);
            }
        }
        return data;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  BENCHMARK HELPERS
    // ══════════════════════════════════════════════════════════════════════════
    /** Returns average sequential-search time in milliseconds over {@code reps} runs. */
    private static double timeSequential(Product[] arr, int id, int reps) {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            long t0 = System.nanoTime();
            SearchAlgorithms.sequentialSearchById(arr, id);
            total += System.nanoTime() - t0;
        }
        return nanosToMs(total / reps);
    }

    /** Returns average binary-search time in milliseconds over {@code reps} runs. */
    private static double timeBinary(Product[] sorted, int id, int reps) {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            long t0 = System.nanoTime();
            SearchAlgorithms.binarySearchById(sorted, id);
            total += System.nanoTime() - t0;
        }
        return nanosToMs(total / reps);
    }

    /** Returns average hybrid name-search time in milliseconds. */
    private static double timeHybridName(HybridSearchSystem h, String name, int reps) {
        long total = 0;
        for (int i = 0; i < reps; i++) {
            long t0 = System.nanoTime();
            h.searchByName(name);
            total += System.nanoTime() - t0;
        }
        return nanosToMs(total / reps);
    }

    /** Returns average hybrid insert time in milliseconds. */
    private static double timeHybridInsert(HybridSearchSystem h, int reps) {
        long total = 0;
        // IDs well above ID_MAX to avoid collisions with existing data
        for (int i = 0; i < reps; i++) {
            Product dummy = new Product(300_000 + i, "BenchmarkProduct_" + i,
                                        "Test", 1.00, 1);
            long t0 = System.nanoTime();
            h.addProduct(null, dummy);
            total += System.nanoTime() - t0;
        }
        return nanosToMs(total / reps);
    }

    /** Warm-up pass: results are discarded to let the JIT compile hot methods. */
    private static void warmUp(Product[] arr, Product[] sorted, HybridSearchSystem h,
                               int seqId, int binId, int avgId, String name) {
        for (int i = 0; i < WARMUP_REPS; i++) {
            SearchAlgorithms.sequentialSearchById(arr,    seqId);
            SearchAlgorithms.sequentialSearchById(arr,    avgId);
            SearchAlgorithms.binarySearchById    (sorted, binId);
            SearchAlgorithms.binarySearchById    (sorted, avgId);
            h.searchByName(name);
        }
    }

    /** Converts nanoseconds (long) to milliseconds (double). */
    private static double nanosToMs(long nanos) {
        return nanos / 1_000_000.0;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  OUTPUT FORMATTING
    // ══════════════════════════════════════════════════════════════════════════
    private static void printReport(double seqBest, double seqAvg, double seqWorst,
                                    double binBest, double binAvg, double binWorst,
                                    double hybSearch, double hybInsert) {

        String line = "=".repeat(65);
        System.out.println();
        System.out.println(line);
        System.out.printf("  TECHMART SEARCH PERFORMANCE ANALYSIS (n = %,d products)%n", N);
        System.out.println(line);

        System.out.println("\nSEQUENTIAL SEARCH:");
        System.out.printf("  Best  Case (ID found at position 0)   : %.6f ms%n", seqBest);
        System.out.printf("  Average Case (random existing ID)     : %.6f ms%n", seqAvg);
        System.out.printf("  Worst Case  (ID not found)            : %.6f ms%n", seqWorst);

        System.out.println("\nBINARY SEARCH:");
        System.out.printf("  Best  Case (ID at mid of array)       : %.6f ms%n", binBest);
        System.out.printf("  Average Case (random existing ID)     : %.6f ms%n", binAvg);
        System.out.printf("  Worst Case  (ID not found)            : %.6f ms%n", binWorst);

        double speedup = (binAvg > 0) ? seqAvg / binAvg : Double.NaN;
        System.out.printf("%nPERFORMANCE IMPROVEMENT: Binary search is ~%.0fx faster on average%n",
                          speedup);

        System.out.println("\nHYBRID NAME SEARCH:");
        System.out.printf("  Average search time : %.6f ms%n", hybSearch);
        System.out.printf("  Average insert time : %.6f ms%n", hybInsert);
        System.out.println();
        System.out.println(line);

        // ── Theoretical analysis ───────────────────────────────────────────────
        double seqTheoreticalAvg = N / 2.0;
        double binTheoreticalAvg = Math.log(N) / Math.log(2);
        System.out.println("\nTHEORETICAL COMPARISON COUNT (n = 100,000):");
        System.out.printf("  Sequential average    : %.0f comparisons%n", seqTheoreticalAvg);
        System.out.printf("  Binary average        : %.2f comparisons%n",  binTheoreticalAvg);
        System.out.printf("  Theoretical speed-up  : %.0fx%n", seqTheoreticalAvg / binTheoreticalAvg);

        // ── Complexity summary table ───────────────────────────────────────────
        System.out.println("\nCOMPLEXITY SUMMARY:");
        System.out.printf("%-34s %-10s %-12s %-10s%n", "Algorithm", "Best", "Average", "Worst");
        System.out.println("-".repeat(68));
        row("Sequential Search (by ID)",   "O(1)", "O(n)",      "O(n)");
        row("Binary Search (by ID)",        "O(1)", "O(log n)",  "O(log n)");
        row("Name Search (sequential)",     "O(1)", "O(n)",      "O(n)");
        row("Hybrid – searchByName",        "O(1)", "O(1)",      "O(1)");
        row("Hybrid – searchById",          "O(1)", "O(log n)",  "O(log n)");
        row("Hybrid – addProduct",          "O(log n)","O(n)",   "O(n)");
        System.out.println("=".repeat(68));
    }

    private static void row(String algo, String best, String avg, String worst) {
        System.out.printf("%-34s %-10s %-12s %-10s%n", algo, best, avg, worst);
    }
}
