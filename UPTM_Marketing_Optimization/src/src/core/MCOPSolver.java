package src.core;

/**
 * MCOP SOLVER INTERFACE - Kontrak untuk semua algorithm TSP
 * 
 * APA ITU INTERFACE?
 * Interface adalah seperti "kontrak" atau "template" yang mesti dipatuhi oleh semua class.
 * 
 * MACAM MANA?
 * Bayangkan interface seperti "senarai tugasan" yang mesti diselesaikan.
 * Class yang implement interface ini WAJIB ada semua method yang dinyatakan.
 * 
 * KENAPA GUNA INTERFACE?
 * 1. STANDARDISASI - Semua algorithm TSP akan guna method yang sama
 * 2. FLEKSIBEL - Senang nak tukar algorithm, semua guna interface yang sama
 * 3. EXTENSIBLE - Senang nak tambah algorithm baru
 * 4. POLYMORPHISM - Boleh simpan semua algorithm dalam satu list
 * 
 * DESIGN PATTERN: STRATEGY PATTERN
 * Interface ini membolehkan kita menggunakan Strategy Pattern.
 * Setiap algorithm adalah "strategy" yang berbeza untuk menyelesaikan masalah yang sama.
 * 
 * CONTOH PENGGUNAAN:
 * 
 * // Simpan semua algorithm dalam satu list
 * List<MCOPSolver> solvers = Arrays.asList(
 *     new GreedySolver(matrix, locations),
 *     new DynamicProgrammingSolver(matrix, locations),
 *     new BacktrackingSolver(matrix, locations),
 *     new DivideAndConquerSolver(matrix, locations)
 * );
 * 
 * // Loop dan jalankan setiap algorithm
 * for (MCOPSolver solver : solvers) {
 *     MCOPResult result = solver.solve();  // Panggil method yang sama!
 *     System.out.println(result);
 * }
 * 
 * @author Hakim & Group
 */
public interface MCOPSolver {
    
    // ==================== METHOD 1: SOLVE ====================
    
    /**
     * solve() - Method utama untuk menyelesaikan MCOP (Marketing Campaign Optimization Problem)
     * 
     * APA YANG METHOD INI BUAT?
     * Method ini akan mencari laluan terpendek yang melawat semua location
     * dan kembali ke tempat mula (UPTM).
     * 
     * SETIAP ALGORITHM AKAN MELAKSANAKAN DENGAN CARA YANG BERBEZA:
     * - Greedy: Pilih city terdekat setiap step
     * - Dynamic Programming: Guna bitmasking dan memoization
     * - Backtracking: Cuba semua kemungkinan dengan pruning
     * - Divide & Conquer: Recursive nearest neighbor
     * 
     * APA YANG DI-RETURN?
     * Method ini return object MCOPResult yang mengandungi:
     * - route: Laluan lengkap (contoh: "UPTM -> City B -> City D -> City C -> UPTM")
     * - totalCost: Jumlah kos (contoh: 88)
     * - algorithmName: Nama algorithm (contoh: "Greedy")
     * - executionTimeNano: Masa execution (contoh: 33000 nanoseconds)
     * - citiesVisited: Bilangan city (contoh: 4)
     * - isOptimal: Status optimal (contoh: true/false)
     * 
     * @return MCOPResult - Objek yang mengandungi semua maklumat hasil
     */
    MCOPResult solve();
    
    // ==================== METHOD 2: GET ALGORITHM NAME ====================
    
    /**
     * getAlgorithmName() - Dapatkan nama algorithm
     * 
     * KENAPA PERLU?
     * Untuk mengenal pasti algorithm yang sedang digunakan.
     * Nama ini akan dipaparkan dalam output dan laporan.
     * 
     * CONTOH NAMA YANG AKAN DI-RETURN:
     * - "Greedy (Nearest Neighbor + Multi-Start)"
     * - "Dynamic Programming (Held-Karp)"
     * - "Backtracking (Branch & Bound)"
     * - "Divide & Conquer"
     * 
     * @return String - Nama algorithm (contoh: "Greedy (Nearest Neighbor + Multi-Start)")
     */
    String getAlgorithmName();
    
    // ==================== METHOD 3: GET TIME COMPLEXITY ====================
    
    /**
     * getTimeComplexity() - Dapatkan time complexity algorithm
     * 
     * APA ITU TIME COMPLEXITY?
     * Time complexity adalah ukuran berapa cepat algorithm berjalan
     * apabila bilangan input (n) bertambah.
     * 
     * NOTASI BIG-O:
     * - O(1): Masa tetap (sangat cepat)
     * - O(n): Linear (masa bertambah dengan n)
     * - O(n²): Kuadratik (masa bertambah dengan n²)
     * - O(n!): Faktorial (sangat lambat untuk n besar)
     * - O(n²·2ⁿ): Exponential (sesuai untuk n kecil sahaja)
     * 
     * UNTUK SETIAP ALGORITHM:
     * 
     * | Algorithm              | Time Complexity | Penjelasan |
     * |------------------------|-----------------|------------|
     * | Greedy                 | O(n²)           | Loop n city, setiap step cari nearest (n) |
     * | Dynamic Programming    | O(n²·2ⁿ)        | 2ⁿ subset, setiap subset loop n city |
     * | Backtracking           | O(n!)           | Cuba semua permutasi (n factorial) |
     * | Divide & Conquer       | O(n²)           | Sama seperti greedy |
     * 
     * @return String - Time complexity (contoh: "O(n²)")
     */
    String getTimeComplexity();
    
    // ==================== METHOD 4: GET SPACE COMPLEXITY ====================
    
    /**
     * getSpaceComplexity() - Dapatkan space complexity algorithm
     * 
     * APA ITU SPACE COMPLEXITY?
     * Space complexity adalah ukuran berapa banyak memori (RAM) yang diperlukan
     * oleh algorithm apabila bilangan input (n) bertambah.
     * 
     * NOTASI BIG-O UNTUK MEMORI:
     * - O(1): Memori tetap (tidak bergantung pada n)
     * - O(n): Linear (memori bertambah dengan n)
     * - O(n·2ⁿ): Exponential (memori bertambah dengan cepat)
     * 
     * UNTUK SETIAP ALGORITHM:
     * 
     * | Algorithm              | Space Complexity | Penjelasan |
     * |------------------------|------------------|------------|
     * | Greedy                 | O(n)             | Array visited (n) + route (n) |
     * | Dynamic Programming    | O(n·2ⁿ)          | Table memo (n × 2ⁿ) |
     * | Backtracking           | O(n)             | Recursion stack + visited array |
     * | Divide & Conquer       | O(n)             | Recursion stack + visited array |
     * 
     * @return String - Space complexity (contoh: "O(n)")
     */
    String getSpaceComplexity();
}