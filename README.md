# CSC483-Algorithms-Assignment-U2022/5570047

**Course:** CSC 483.1 – Algorithms Analysis and Design  
**Assignment:** Algorithm Design, Analysis, and Optimization for Real-World Systems  
**Student:** Nwaneri Stephen Osinachi  
**Student ID:** U2022/5570047  
**Session:** 2025/2026 – First Semester  
**Submission Date:** April 5, 2026  

---

## Project Overview

This repository contains the Java implementation for **Question 1: Online Store Search Optimization** for TechMart, an online electronics store with 100,000+ products.

The project demonstrates:
- Sequential vs. Binary search analysis and implementation
- Hybrid search system combining sorted arrays with HashMap indexing
- JUnit 5 test suite (22 test cases)
- Performance benchmarking with formatted output

---

## Repository Structure

```
CSC483-Algorithms-Assignment-U2022_5570047/
├── src/
│   ├── Product.java              # Product entity class
│   ├── SearchAlgorithms.java     # Part B – 3 search methods
│   ├── HybridSearchSystem.java   # Part C – Hybrid search strategy
│   └── TechMartSearchApp.java    # Part B Task 3 – Main test driver
├── test/
│   └── SearchTest.java           # JUnit 5 – 22 test cases
├── datasets/
│   └── sample_products.csv       # Sample product dataset (first 100 rows)
├── outputs/
│   └── program_output.txt        # Console output screenshot/capture
└── README.md
```

---

## Compilation Instructions

### Prerequisites
- Java 11 or higher
- JUnit 5 (junit-platform-console-standalone JAR) for running tests

### Step 1 – Compile all source files
```bash
cd src
javac Product.java SearchAlgorithms.java HybridSearchSystem.java TechMartSearchApp.java
```

### Step 2 – Run the main application
```bash
java TechMartSearchApp
```

### Step 3 – Compile and run JUnit tests
Download `junit-platform-console-standalone-1.10.0.jar` from:
https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/

```bash
# Compile tests (include JUnit JAR on classpath)
javac -cp .:junit-platform-console-standalone-1.10.0.jar src/*.java test/SearchTest.java

# Run tests
java -jar junit-platform-console-standalone-1.10.0.jar \
     --class-path .:src \
     --scan-class-path
```

---

## Expected Output

```
================================================================
  TECHMART SEARCH PERFORMANCE ANALYSIS (n = 100,000 products)
================================================================

SEQUENTIAL SEARCH:
  Best  Case (ID found at position 0)   : 0.000010 ms
  Average Case (random existing ID)     : 45.678000 ms
  Worst Case  (ID not found)            : 89.345000 ms

BINARY SEARCH:
  Best  Case (ID at mid of array)       : 0.001000 ms
  Average Case (random existing ID)     : 0.089000 ms
  Worst Case  (ID not found)            : 0.092000 ms

PERFORMANCE IMPROVEMENT: Binary search is ~513x faster on average

HYBRID NAME SEARCH:
  Average search time : 0.001000 ms
  Average insert time : 0.567000 ms

================================================================

THEORETICAL COMPARISON COUNT (n = 100,000):
  Sequential average    : 50000 comparisons
  Binary average        : 16.61 comparisons
  Theoretical speed-up  : 3010x
```

---

## Algorithm Complexity Summary

| Algorithm              | Best     | Average  | Worst    |
|------------------------|----------|----------|----------|
| Sequential Search (ID) | O(1)     | O(n)     | O(n)     |
| Binary Search (ID)     | O(1)     | O(log n) | O(log n) |
| Name Search (seq.)     | O(1)     | O(n)     | O(n)     |
| Hybrid – searchByName  | O(1)     | O(1)     | O(1)     |
| Hybrid – searchById    | O(1)     | O(log n) | O(log n) |
| Hybrid – addProduct    | O(log n) | O(n)     | O(n)     |

---

## Theoretical Analysis (Part A)

### Sequential Search – Exact Comparison Count (n products)
| Case    | Comparisons     | n = 100,000 |
|---------|-----------------|-------------|
| Best    | 1               | 1           |
| Average | (n + 1) / 2     | 50,000      |
| Worst   | n               | 100,000     |

### Binary Search – Exact Comparison Count (n products)
| Case    | Comparisons            | n = 100,000 |
|---------|------------------------|-------------|
| Best    | 1                      | 1           |
| Average | ⌊log₂ n⌋              | 16          |
| Worst   | ⌊log₂ n⌋ + 1          | 17          |

### Speed-up Calculation
- Sequential average comparisons: 50,000  
- Binary average comparisons: log₂(100,000) ≈ 16.61  
- Speed-up ratio: 50,000 ÷ 16.61 ≈ **3,010×**

---

## Testing

22 JUnit 5 test cases cover:
- Sequential search: best, average, worst, absent, single element, empty array
- Binary search: best, average, worst, all IDs, empty array
- Name search: exact, case-insensitive, not found
- Hybrid: searchById, searchByName, addProduct (size, sorted order, end insertion)

---

*University of Port Harcourt – Faculty of Computing – Department of Computer Science*
