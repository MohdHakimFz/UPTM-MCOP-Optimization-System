package src.algorithms;

// Import classes yang diperlukan
import src.core.*;      // Untuk MCOPSolver interface dan MCOPResult
import java.util.*;     // Untuk StringBuilder, Arrays, dll

/**
 * BACKTRACKING SOLVER - Mencari laluan terpendek menggunakan teknik Backtracking
 * 
 * APA ITU BACKTRACKING?
 * Backtracking adalah teknik yang mencuba SEMUA kemungkinan laluan.
 * Macam kita cuba semua jalan yang mungkin, kalau jalan buntu, kita backtrack (undur)
 * dan cuba jalan lain.
 * 
 * KELEBIHAN: Pasti dapat laluan yang paling pendek (optimal)
 * KEKURANGAN: Sangat lambat untuk banyak location (O(n!))
 * 
 * UNTUK 4 LOCATION: 4! = 24 kemungkinan laluan sahaja (cepat)
 * UNTUK 10 LOCATION: 10! = 3.6 JUTA kemungkinan (sangat lambat!)
 * 
 * @author Hakim & Group
 */
public class BacktrackingSolver implements MCOPSolver {
    
    // ==================== VARIABLES (Data yang disimpan) ====================
    
    /**
     * dist - Matrix kos perjalanan (sama dengan cost matrix yang diinput)
     * Contoh: dist[0][1] = 15 (kos dari UPTM ke City B)
     */
    private final int[][] dist;
    
    /**
     * locations - Nama-nama tempat (UPTM, City B, City C, City D)
     */
    private final String[] locations;
    
    /**
     * n - Bilangan location (dalam kes ini, 4)
     */
    private final int n;
    
    /**
     * bestCost - Kos terendah yang ditemui setakat ini
     * Mula-mula set kepada nilai yang sangat besar (Integer.MAX_VALUE)
     * Nanti akan update bila jumpa kos yang lebih rendah
     */
    private int bestCost;
    
    /**
     * bestRoute - Laluan terbaik yang ditemui setakat ini
     * Contoh: "UPTM -> City B -> City C -> City D"
     */
    private String bestRoute;
    
    /**
     * visited - Tandakan location mana yang sudah dilawati
     * visited[0] = true bermaksud UPTM sudah dilawati
     * visited[1] = false bermaksud City B belum dilawati
     */
    private boolean[] visited;
    
    // ==================== CONSTRUCTOR (Tempat Setup Data) ====================
    
    /**
     * Constructor - Dipanggil bila kita create object BacktrackingSolver
     * 
     * @param distanceMatrix - Matrix kos perjalanan (dari Main.java)
     * @param locations - Nama-nama location (dari Main.java)
     */
    public BacktrackingSolver(int[][] distanceMatrix, String[] locations) {
        this.dist = distanceMatrix;      // Simpan matrix kos
        this.locations = locations;      // Simpan nama location
        this.n = distanceMatrix.length;  // Kira berapa location (4)
    }
    
    // ==================== SOLVE METHOD (Tempat Utama) ====================
    
    /**
     * solve() - Method utama untuk cari laluan terpendek
     * 
     * CARA KERJA:
     * 1. Rekod masa mula (untuk ukur performance)
     * 2. Set bestCost kepada nilai sangat besar (belum jumpa apa-apa)
     * 3. Tandakan UPTM (index 0) sebagai sudah dilawati
     * 4. Panggil method backtrack() untuk mula mencari
     * 5. Selepas selesai, rekod masa akhir
     * 6. Return hasil dalam bentuk MCOPResult
     * 
     * @return MCOPResult - Objek yang mengandungi laluan, kos, masa, dll
     */
    @Override
    public MCOPResult solve() {
        
        // STEP 1: Rekod masa mula (dalam nanosecond untuk lebih tepat)
        long startTime = System.nanoTime();
        
        // STEP 2: Initialize variables untuk pencarian
        bestCost = Integer.MAX_VALUE;  // Set kepada nilai paling besar
        bestRoute = "";                // Kosongkan laluan terbaik
        visited = new boolean[n];      // Buat array visited (semua false)
        
        // STEP 3: Sediakan laluan bermula dari UPTM
        StringBuilder currentRoute = new StringBuilder();  // Untuk bina laluan
        visited[0] = true;              // Tandakan UPTM sudah dilawati
        currentRoute.append(locations[0]);  // Tambah "UPTM" ke laluan
        
        // STEP 4: MULA BACKTRACKING!
        // Parameter:
        // - 0: location semasa (UPTM)
        // - 1: bilangan location yang sudah dilawati (1 = UPTM sahaja)
        // - 0: kos setakat ini (belum ada kos)
        // - currentRoute: laluan setakat ini
        backtrack(0, 1, 0, currentRoute);
        
        // STEP 5: Selepas backtrack selesai, kita ada laluan terbaik
        // Tambah kembali ke UPTM (balik ke tempat mula)
        String finalRoute = bestRoute + " -> " + locations[0];
        
        // STEP 6: Rekod masa akhir
        long endTime = System.nanoTime();
        
        // STEP 7: Return hasil dalam bentuk object MCOPResult
        return new MCOPResult.Builder()
            .route(finalRoute)                    // Laluan lengkap
            .totalCost(bestCost)                  // Kos terendah yang ditemui
            .algorithmName(getAlgorithmName())    // Nama algorithm
            .executionTimeNano(endTime - startTime)  // Masa execution
            .citiesVisited(n)                     // Bilangan location (4)
            .isOptimal(true)                      // Backtracking PASTI optimal
            .build();
    }
    
    // ==================== BACKTRACK METHOD (Recursive Search) ====================
    
    /**
     * backtrack() - Method recursive yang mencuba semua kemungkinan laluan
     * 
     * APA ITU RECURSIVE?
     * Method yang panggil DIRI SENDIRI. Macam tangga: setiap step panggil step seterusnya.
     * 
     * CARA KERJA:
     * 1. Kalau kos setakat ini sudah lebih besar dari kos terbaik, BERHENTI (pruning)
     * 2. Kalau semua location sudah dilawati, kira kos balik ke UPTM
     * 3. Kalau kos ini lebih baik, simpan sebagai bestCost
     * 4. Kalau belum habis, cuba semua location yang belum dilawati
     * 5. Untuk setiap location, pergi ke sana dan panggil backtrack() lagi
     * 6. Selepas selesai, UNDUR (backtrack) dan cuba location lain
     * 
     * @param current - Index location semasa (0=UPTM, 1=City B, dll)
     * @param count - Bilangan location yang sudah dilawati (1 hingga n)
     * @param currentCost - Kos perjalanan setakat ini
     * @param route - Laluan setakat ini (contoh: "UPTM -> City B")
     */
    private void backtrack(int current, int count, int currentCost, StringBuilder route) {
        
        // ========== PRUNING (Potong cabang yang tak perlu) ==========
        /**
         * PRUNING: Kalau kos setakat ini sudah LEBIH BESAR dari kos terbaik,
         * TAK PERLU TERUSKAN! Sebab pasti takkan jadi lebih baik.
         * 
         * Contoh: Kos terbaik setakat ini = 100
         *         Kos setakat ini = 120 (sudah lebih besar)
         *         Maka berhenti, tak perlu cuba lagi.
         * 
         * Ini menjimatkan masa dengan banyak!
         */
        if (currentCost >= bestCost) {
            return;  // BERHENTI! Tak perlu teruskan
        }
        
        // ========== BASE CASE (Sudah Lawat Semua Location) ==========
        /**
         * BASE CASE: Kalau count == n, bermakna semua location sudah dilawati
         * 
         * Contoh: n = 4, count = 4 (UPTM, City B, City C, City D sudah dilawati)
         * 
         * Sekarang kira kos untuk BALIK ke UPTM (location 0)
         * totalCost = currentCost + kos dari location semasa ke UPTM
         */
        if (count == n) {
            // Kira kos balik ke UPTM
            int totalCost = currentCost + dist[current][0];
            
            /**
             * Kalau kos total ini LEBIH RENDAH dari kos terbaik,
             * update bestCost dan bestRoute
             */
            if (totalCost < bestCost) {
                bestCost = totalCost;                    // Simpan kos terbaik
                bestRoute = route.toString();            // Simpan laluan terbaik
            }
            return;  // Selesai untuk cabang ini
        }
        
        // ========== RECURSIVE CASE (Cuba Semua Location Yang Belum Dilawati) ==========
        /**
         * Untuk setiap location yang BELUM dilawati:
         * 1. Tandakan sebagai sudah dilawati
         * 2. Tambah location ke dalam laluan
         * 3. Panggil backtrack() untuk location baru ini
         * 4. Selepas selesai, UNDUR (backtrack) dan cuba location lain
         */
        
        // Loop melalui semua location (0, 1, 2, 3)
        for (int next = 0; next < n; next++) {
            
            // Hanya process jika location ini BELUM dilawati
            if (!visited[next]) {
                
                // STEP 1: Tandakan location ini sebagai sudah dilawati
                visited[next] = true;
                
                // STEP 2: Simpan panjang laluan sekarang (untuk undo nanti)
                int prevLen = route.length();
                
                // STEP 3: Tambah location ke dalam laluan
                // Contoh: "UPTM -> City B" jadi "UPTM -> City B -> City C"
                route.append(" -> ").append(locations[next]);
                
                // STEP 4: PANGGIL DIRI SENDIRI (recursive call)
                // Pergi ke location baru, tambah count, tambah kos
                backtrack(next, 
                          count + 1, 
                          currentCost + dist[current][next], 
                          route);
                
                // STEP 5: BACKTRACK (Undur) - ini yang bagi nama "Backtracking"!
                // Tandakan location ini sebagai BELUM dilawati lagi
                visited[next] = false;
                
                // STEP 6: Kembalikan laluan ke keadaan sebelum tambah location tadi
                // Contoh: "UPTM -> City B -> City C" jadi "UPTM -> City B"
                route.setLength(prevLen);
                
                /**
                 * Kenapa perlu backtrack?
                 * Sebab kita dah selesai cuba satu cabang, sekarang nak cuba cabang lain.
                 * Kalau tak backtrack, nanti kita ingat location tu masih dilawati.
                 */
            }
        }
    }
    
    // ==================== GETTER METHODS (Untuk Dapatkan Maklumat Algorithm) ====================
    
    /**
     * getAlgorithmName() - Return nama algorithm untuk ditunjukkan dalam output
     * 
     * @return Nama algorithm
     */
    @Override
    public String getAlgorithmName() { 
        return "Backtracking (Branch & Bound)"; 
    }
    
    /**
     * getTimeComplexity() - Return time complexity algorithm
     * 
     * O(n!) bermaksud masa bertambah secara faktorial dengan bilangan location
     * Untuk n=4, 4! = 24 operasi
     * Untuk n=5, 5! = 120 operasi
     * Untuk n=10, 10! = 3,628,800 operasi (sangat lambat!)
     * 
     * @return Time complexity notation
     */
    @Override
    public String getTimeComplexity() { 
        return "O(n!) worst case"; 
    }
    
    /**
     * getSpaceComplexity() - Return space complexity algorithm
     * 
     * O(n) bermakna ruang memori bertambah secara linear dengan bilangan location
     * Untuk n=4, guna ~4 unit memori
     * Untuk n=100, guna ~100 unit memori (ok sahaja)
     * 
     * @return Space complexity notation
     */
    @Override
    public String getSpaceComplexity() { 
        return "O(n)"; 
    }
}