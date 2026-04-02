package src.utils;

// Import classes yang diperlukan
import src.core.MCOPResult;  // Untuk object hasil algorithm
import java.util.*;          // Untuk List, ArrayList, Comparator

/**
 * PERFORMANCE LOGGER - Utiliti untuk log dan analisis performance algorithm
 *
 * APA ITU PerformanceLogger?
 * Class ini bertindak sebagai "jurnal" atau "buku rekod" untuk menyimpan
 * semua hasil yang diperoleh dari setiap algorithm TSP.
 * Selepas semua algorithm dijalankan, kita boleh print ringkasan dan bandingkan.
 *
 * KENAPA PERLU CLASS INI?
 * Daripada print hasil setiap algorithm secara berasingan,
 * PerformanceLogger kumpulkan semua hasil dalam satu tempat, kemudian:
 * - Paparkan dalam bentuk jadual yang kemas
 * - Cari algorithm dengan kos terbaik
 * - Cari algorithm yang paling cepat
 *
 * CARA GUNA:
 *
 * PerformanceLogger logger = new PerformanceLogger();
 * logger.logResult(greedyResult);
 * logger.logResult(dpResult);
 * logger.printSummary();   // Paparkan jadual
 * logger.printComparison(); // Paparkan pemenang
 *
 * @author Hakim & Group
 */
public class PerformanceLogger {

    // ==================== FIELD (Data yang disimpan) ====================

    /**
     * results - Senarai semua hasil algorithm yang telah dilog
     *
     * Guna List<MCOPResult> supaya boleh simpan sebarang bilangan hasil.
     * Setiap kali logResult() dipanggil, hasil baru ditambah ke senarai ini.
     *
     * Contoh isi senarai:
     * [0] = MCOPResult untuk Greedy
     * [1] = MCOPResult untuk Dynamic Programming
     * [2] = MCOPResult untuk Backtracking
     * [3] = MCOPResult untuk Divide & Conquer
     */
    private final List<MCOPResult> results;

    // ==================== CONSTRUCTOR (Tempat Setup Data) ====================

    /**
     * Constructor - Cipta PerformanceLogger yang baru dan kosong
     *
     * ArrayList dipilih kerana:
     * - Senang tambah elemen baru (logResult)
     * - Senang loop untuk print (printSummary)
     * - Saiz boleh membesar secara dinamik
     */
    public PerformanceLogger() {
        this.results = new ArrayList<>();  // Buat senarai kosong
    }

    // ==================== PUBLIC METHODS ====================

    /**
     * logResult() - Simpan satu hasil algorithm ke dalam senarai
     *
     * CARA KERJA:
     * Tambah objek MCOPResult yang diberikan ke hujung senarai results.
     * Urutan penyimpanan bergantung pada urutan panggilan method ini.
     *
     * @param result - Objek MCOPResult yang mengandungi laluan, kos, masa, dll.
     *
     * CONTOH PENGGUNAAN:
     * logger.logResult(greedySolver.solve());
     * logger.logResult(dpSolver.solve());
     */
    public void logResult(MCOPResult result) {
        results.add(result);  // Tambah ke hujung senarai
    }

    /**
     * printSummary() - Paparkan ringkasan semua hasil dalam bentuk jadual
     *
     * CARA KERJA:
     * 1. Print header jadual dengan sempadan "="
     * 2. Print nama lajur: Algorithm, Cost, Time (ms), Optimal?
     * 3. Loop melalui setiap MCOPResult dalam senarai dan print maklumatnya
     * 4. Print sempadan bawah jadual
     *
     * FORMAT OUTPUT:
     * ======================================================================
     *                     PERFORMANCE SUMMARY
     * ======================================================================
     * Algorithm                           Cost       Time (ms)    Optimal?
     * ----------------------------------------------------------------------
     * Greedy (Nearest Neighbor...)        88         0.021        ✓
     * Dynamic Programming (Held-Karp)     88         0.015        ✓
     * ...
     * ======================================================================
     *
     * NOTA FORMAT:
     * %-35s = String kiri-justify dalam lebar 35 aksara
     * %-10d = Integer kiri-justify dalam lebar 10 aksara
     * %-12.3f = Float dengan 3 tempat perpuluhan, lebar 12
     * %-10s = String kiri-justify dalam lebar 10 aksara
     */
    public void printSummary() {

        // STEP 1: Print baris sempadan atas dan tajuk
        System.out.println("\n" + "=".repeat(70));
        System.out.println("                    PERFORMANCE SUMMARY");
        System.out.println("=".repeat(70));

        // STEP 2: Print header lajur
        // %-35s = lajur "Algorithm" lebar 35 aksara, kiri-justify
        System.out.printf("%-35s %-10s %-12s %-10s\n",
            "Algorithm", "Cost", "Time (ms)", "Optimal?");

        // STEP 3: Print sempadan tengah
        System.out.println("-".repeat(70));

        // STEP 4: Loop dan print setiap hasil
        for (MCOPResult result : results) {

            /**
             * String.format() digunakan untuk susun maklumat dalam lajur yang kemas:
             * - getAlgorithmName() = nama algorithm (String)
             * - getTotalCost()     = kos perjalanan (int)
             * - getExecutionTimeMs() = masa dalam millisecond (double, 3 d.p.)
             * - isOptimal()        = true → "✓", false → "✗"
             */
            System.out.printf("%-35s %-10d %-12.3f %-10s\n",
                result.getAlgorithmName(),
                result.getTotalCost(),
                result.getExecutionTimeMs(),
                result.isOptimal() ? "✓" : "✗");   // Ternary operator untuk simbol
        }

        // STEP 5: Print baris sempadan bawah
        System.out.println("=".repeat(70));
    }

    /**
     * printComparison() - Paparkan perbandingan ringkas antara algorithm
     *
     * CARA KERJA:
     * 1. Guna Stream API untuk cari MCOPResult dengan kos terendah (bestCost)
     * 2. Guna Stream API untuk cari MCOPResult dengan masa tercepat (fastest)
     * 3. Print kedua-dua pemenang
     *
     * FORMAT OUTPUT:
     * ----------------------------------------------------------------------
     * ALGORITHM COMPARISON
     * ----------------------------------------------------------------------
     * Best Cost: Dynamic Programming (Held-Karp) (88 units)
     * Fastest: Greedy (Nearest Neighbor + Multi-Start) (0.015 ms)
     *
     * APA ITU STREAM API?
     * Stream API adalah cara moden untuk proses senarai dalam Java.
     * Contoh: results.stream()
     *             .min(Comparator.comparingInt(...))  ← cari nilai terkecil
     *             .orElse(null)                       ← kalau senarai kosong, return null
     */
    public void printComparison() {

        // Print header bahagian perbandingan
        System.out.println("\n" + "-".repeat(70));
        System.out.println("ALGORITHM COMPARISON");
        System.out.println("-".repeat(70));

        // STEP 1: Cari algorithm dengan KOS TERENDAH
        /**
         * results.stream()
         *     .min(Comparator.comparingInt(MCOPResult::getTotalCost))
         *     .orElse(null)
         *
         * Ini bermaksud:
         * - Tukar senarai kepada stream (aliran data)
         * - Cari elemen dengan nilai getTotalCost() paling kecil
         * - Kalau senarai kosong, return null
         *
         * MCOPResult::getTotalCost = method reference (singkatan untuk r -> r.getTotalCost())
         */
        MCOPResult bestCost = results.stream()
            .min(Comparator.comparingInt(MCOPResult::getTotalCost))
            .orElse(null);

        // STEP 2: Cari algorithm dengan MASA TERCEPAT
        /**
         * Sama seperti di atas, tapi bandingkan menggunakan getExecutionTimeMs()
         * comparingDouble() digunakan kerana masa adalah nilai double (bukan int)
         */
        MCOPResult fastest = results.stream()
            .min(Comparator.comparingDouble(MCOPResult::getExecutionTimeMs))
            .orElse(null);

        // STEP 3: Print hasil - semak null dahulu untuk keselamatan
        if (bestCost != null) {
            System.out.println("Best Cost: " + bestCost.getAlgorithmName() +
                " (" + bestCost.getTotalCost() + " units)");
        }

        if (fastest != null) {
            // String.format("%.3f", ...) = format double kepada 3 tempat perpuluhan
            System.out.println("Fastest: " + fastest.getAlgorithmName() +
                " (" + String.format("%.3f", fastest.getExecutionTimeMs()) + " ms)");
        }
    }
}