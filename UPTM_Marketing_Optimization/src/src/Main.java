package src;

// Import semua classes dari package yang kita buat
import src.algorithms.*;      // Untuk algorithm TSP (Greedy, DP, Backtracking, D&C)
import src.core.*;            // Untuk interface dan result object
import src.datastructures.*;  // Untuk MinHeap dan Splay Tree
import src.utils.*;           // Untuk utility classes (sorting, search, logger)
import java.util.*;           // Untuk Arrays, List, dll

/**
 * MAIN CLASS - Entry point untuk program UPTM Marketing Campaign Optimization
 * 
 * Class ini adalah tempat program bermula. Semua algorithm akan dijalankan dari sini.
 * 
 * @author Hakim & Group
 * @version 2.0
 * @since 2026
 */
public class Main {
    
    // ==================== DATA INPUT ====================
    
    /**
     * COST_MATRIX - Jadual kos perjalanan antara locations
     * 
     * Baris = location asal, Kolum = location tujuan
     * Contoh: costMatrix[0][1] = 15 (dari UPTM ke City B)
     * 
     * Index locations:
     * 0 = UPTM (starting point)
     * 1 = City B
     * 2 = City C  
     * 3 = City D
     * 
     * Diagonal = 0 (kos dari location ke diri sendiri)
     * Matrix adalah simetri (kos pergi = kos balik)
     */
    private static final int[][] COST_MATRIX = { 
        {0, 15, 25, 35},   // Baris 0: Dari UPTM
        {15, 0, 30, 28},   // Baris 1: Dari City B
        {25, 30, 0, 20},   // Baris 2: Dari City C
        {35, 28, 20, 0}    // Baris 3: Dari City D
    };
    
    /**
     * LOCATIONS - Nama-nama location dalam urutan yang sama dengan matrix
     * 
     * Index 0 = UPTM (tempat mula dan akhir)
     * Index 1 = City B
     * Index 2 = City C
     * Index 3 = City D
     */
    private static final String[] LOCATIONS = {"UPTM", "City B", "City C", "City D"};
    
    // ==================== MAIN METHOD (Tempat Program Bermula) ====================
    
    /**
     * main() - Method utama. Java akan panggil method ini bila program dijalankan.
     * 
     * @param args Argument dari command line (tak digunakan dalam projek ni)
     */
    public static void main(String[] args) {
        
        // STEP 1: Create object untuk track performance semua algorithm
        // PerformanceLogger akan record masa dan hasil setiap algorithm
        PerformanceLogger logger = new PerformanceLogger();
        
        // STEP 2: Print banner pembuka (header yang cantik)
        printBanner();
        
        // ==================== BAHAGIAN 1: TSP ALGORITHMS ====================
        // Di sini kita akan jalankan 4 algorithm TSP dan bandingkan hasilnya
        
        System.out.println("\n=== TSP ALGORITHM RESULTS ===");
        System.out.println("-".repeat(50));  // Print garis pemisah (50 kali "-")
        
        /**
         * STEP 3: Senarai semua algorithm yang akan dijalankan
         * 
         * Arrays.asList() - Buat list yang mengandungi:
         * 1. GreedySolver - Algorithm greedy (pilih yang terdekat setiap kali)
         *    Parameter true = cuba start dari semua location untuk hasil lebih baik
         * 2. DynamicProgrammingSolver - Algorithm DP (gunakan bitmasking)
         * 3. BacktrackingSolver - Algorithm backtracking (cuba semua kemungkinan)
         * 4. DivideAndConquerSolver - Algorithm divide & conquer (bahagi kecil)
         */
        List<MCOPSolver> solvers = Arrays.asList(
            new GreedySolver(COST_MATRIX, LOCATIONS, true),           // Algorithm 1
            new DynamicProgrammingSolver(COST_MATRIX, LOCATIONS),     // Algorithm 2
            new BacktrackingSolver(COST_MATRIX, LOCATIONS),           // Algorithm 3
            new DivideAndConquerSolver(COST_MATRIX, LOCATIONS)        // Algorithm 4
        );
        
        /**
         * STEP 4: Jalankan setiap algorithm satu per satu
         * 
         * for (MCOPSolver solver : solvers) - Loop melalui setiap algorithm
         * solver.solve() - Panggil method solve() untuk dapatkan hasil
         * logger.logResult() - Simpan hasil untuk analysis nanti
         * System.out.println() - Print hasil ke screen
         */
        for (MCOPSolver solver : solvers) {
            MCOPResult result = solver.solve();     // Jalankan algorithm
            logger.logResult(result);               // Simpan ke logger
            System.out.println(result);             // Print ke screen
        }
        
        /**
         * STEP 5: Tunjukkan summary dan perbandingan
         * logger.printSummary() - Print jadual perbandingan semua algorithm
         * logger.printComparison() - Print algorithm terbaik (cost terendah & paling cepat)
         */
        logger.printSummary();      // Jadual perbandingan
        logger.printComparison();   // Algorithm terbaik
        
        // ==================== BAHAGIAN 2: SORTING & SEARCHING ====================
        // Demonstrasi Insertion Sort dan Binary Search
        
        System.out.println("\n=== SORTING & SEARCHING ===");
        System.out.println("-".repeat(50));
        
        /**
         * STEP 6: Sediakan data untuk di-sort
         * scores - Array skor engagement student (random data untuk demo)
         */
        int[] scores = {85, 42, 97, 53, 68, 74, 31, 89, 66, 78};
        System.out.println("Original: " + Arrays.toString(scores));  // Tunjuk data asal
        
        /**
         * STEP 7: Insertion Sort
         * scores.clone() - Buat salinan array asal supaya array asal tak berubah
         * SortSearchUtils.insertionSort() - Panggil method sorting
         */
        int[] sorted = SortSearchUtils.insertionSort(scores.clone());
        System.out.println("Sorted: " + Arrays.toString(sorted));     // Tunjuk hasil sorting
        
        /**
         * STEP 8: Binary Search
         * Cari value 68 dalam array yang sudah di-sort
         * binarySearch() - Return index jika jumpa, -1 jika tak jumpa
         */
        int target = 68;  // Value yang nak dicari
        int index = SortSearchUtils.binarySearch(sorted, target);
        
        // Print hasil pencarian (gunakan ternary operator ? : untuk condition)
        System.out.println("Search " + target + ": " + 
            (index != -1 ? "Found at index " + index : "Not found"));
        
        // ==================== BAHAGIAN 3: DATA STRUCTURES ====================
        // Demonstrasi Min-Heap dan Splay Tree
        
        System.out.println("\n=== DATA STRUCTURES ===");
        System.out.println("-".repeat(50));
        
        // ---------- MIN-HEAP DEMONSTRATION ----------
        /**
         * MinHeap - Struktur data dimana nilai terkecil sentiasa di root
         * Berguna untuk priority queue (utamakan kos terendah)
         */
        
        MinHeap heap = new MinHeap();  // Create object heap baru
        
        /**
         * Budget campaign dalam RM (contoh data)
         * heap.insert() - Masukkan setiap budget ke dalam heap
         * Heap akan susun secara automatik (nilai terkecil di root)
         */
        int[] budgets = {1500, 800, 2000, 500, 1200, 300};
        for (int b : budgets) {
            heap.insert(b);  // Insert setiap budget
        }
        
        /**
         * heap.extractMin() - Keluarkan nilai terkecil dari heap
         * Pertama: 300 (paling kecil)
         * Kedua: 500 (kecil seterusnya)
         */
        System.out.println("Min-Heap Extract Min: " + heap.extractMin());  // Output: 300
        System.out.println("Min-Heap Extract Min: " + heap.extractMin());  // Output: 500
        
        // ---------- SPLAY TREE DEMONSTRATION ----------
        /**
         * Splay Tree - Binary Search Tree yang self-adjusting
         * Element yang selalu diakses akan "naik" ke root untuk akses lebih cepat
         * Berguna untuk database student yang kerap dicari
         */
        
        SplayTree tree = new SplayTree();  // Create object splay tree baru
        
        /**
         * Student IDs (contoh data)
         * tree.insert() - Masukkan setiap ID ke dalam tree
         */
        int[] ids = {2024001, 2023105, 2024156, 2023892, 2024012};
        for (int id : ids) {
            tree.insert(id);  // Insert setiap ID
        }
        
        /**
         * tree.search() - Cari student ID dalam tree
         * Return true jika jumpa, false jika tidak
         */
        System.out.println("Splay Tree Search 2024156: " + 
            (tree.search(2024156) ? "Found" : "Not Found"));  // Output: Found
        
        // STEP 9: Print footer penutup
        printFooter();
    }
    
    // ==================== HELPER METHODS ====================
    // Method-method pembantu untuk print banner dan footer
    
    /**
     * printBanner() - Print header yang cantik untuk program
     * 
     * Guna Unicode box-drawing characters untuk buat border
     * ╔ ╗ ║ ╚ ╝ adalah characters untuk kotak
     */
    private static void printBanner() {
        System.out.println();  // Baris kosong untuk spacing
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     UPTM MARKETING CAMPAIGN OPTIMIZATION SYSTEM (MCOP)    ║");
        System.out.println("║     SWC3524/SWC4423 - Algorithmic Data Structures         ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * printFooter() - Print footer penutup
     * ✓ adalah check mark symbol (Alt + 251)
     */
    private static void printFooter() {
        System.out.println("\n✓ System execution completed successfully");
    }
}