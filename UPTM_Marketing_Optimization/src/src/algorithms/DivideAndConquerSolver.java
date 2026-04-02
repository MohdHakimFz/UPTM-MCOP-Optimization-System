package src.algorithms;

import src.core.*;  // Untuk MCOPSolver interface dan MCOPResult

/**
 * DIVIDE AND CONQUER SOLVER - Mencari laluan menggunakan teknik Divide and Conquer
 * 
 * APA ITU DIVIDE AND CONQUER?
 * Divide and Conquer = "Bahagi dan Selesaikan"
 * 
 * Strategi:
 * 1. DIVIDE (Bahagi): Pecahkan masalah besar kepada masalah-masalah kecil
 * 2. CONQUER (Selesaikan): Selesaikan setiap masalah kecil
 * 3. COMBINE (Gabung): Gabungkan penyelesaian masalah kecil menjadi penyelesaian lengkap
 * 
 * DALAM KONTEKS MCOP:
 * Setiap step, kita cari city terdekat (paling murah) dari city semasa.
 * Kemudian kita "divide" dengan pergi ke city tersebut, dan teruskan proses.
 * Ini macam greedy, tapi guna pendekatan recursive (panggil diri sendiri).
 * 
 * KELEBIHAN: Sangat cepat (O(n²))
 * KEKURANGAN: Tidak pasti dapat laluan paling pendek (tidak optimal)
 * 
 * @author Hakim & Group
 */
public class DivideAndConquerSolver implements MCOPSolver {
    
    // ==================== VARIABLES (Data yang disimpan) ====================
    
    /**
     * dist - Matrix kos perjalanan
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
    
    // ==================== CONSTRUCTOR (Tempat Setup Data) ====================
    
    /**
     * Constructor - Dipanggil bila kita create object DivideAndConquerSolver
     * 
     * @param distanceMatrix - Matrix kos perjalanan (dari Main.java)
     * @param locations - Nama-nama location (dari Main.java)
     */
    public DivideAndConquerSolver(int[][] distanceMatrix, String[] locations) {
        this.dist = distanceMatrix;      // Simpan matrix kos
        this.locations = locations;      // Simpan nama location
        this.n = distanceMatrix.length;  // Kira berapa location (4)
    }
    
    // ==================== SOLVE METHOD (Tempat Utama) ====================
    
    /**
     * solve() - Method utama untuk cari laluan menggunakan Divide and Conquer
     * 
     * CARA KERJA:
     * 1. Rekod masa mula (untuk ukur performance)
     * 2. Tandakan UPTM (index 0) sebagai sudah dilawati
     * 3. Panggil method divideAndConquer() untuk cari laluan
     * 4. Selepas semua city dilawati, kira kos balik ke UPTM
     * 5. Rekod masa akhir dan return hasil
     * 
     * @return MCOPResult - Objek yang mengandungi laluan, kos, masa, dll
     */
    @Override
    public MCOPResult solve() {
        
        // STEP 1: Rekod masa mula (dalam nanosecond)
        long startTime = System.nanoTime();
        
        // STEP 2: Sediakan tracking untuk location yang sudah dilawati
        boolean[] visited = new boolean[n];     // Semua false pada mulanya
        
        // STEP 3: Sediakan laluan bermula dari UPTM
        StringBuilder route = new StringBuilder();      // Untuk bina laluan
        visited[0] = true;                              // Tandakan UPTM sudah dilawati
        route.append(locations[0]);                     // Tambah "UPTM" ke laluan
        
        // STEP 4: MULA DIVIDE AND CONQUER!
        // Panggil method recursive untuk cari laluan seterusnya
        // Parameter:
        // - 0: location semasa (UPTM)
        // - visited: array tracking location yang sudah dilawati
        // - route: laluan setakat ini
        int totalCost = divideAndConquer(0, visited, route);
        
        // STEP 5: CARI KOS UNTUK BALIK KE UPTM
        /**
         * Selepas semua city dilawati, kita kena balik ke UPTM.
         * 
         * Cara:
         * 1. Dapatkan nama city terakhir yang dilawati
         *    Contoh: route = "UPTM -> City B -> City D -> City C"
         *    lastCity = "City C"
         * 2. Cari kos dari city terakhir ke UPTM
         *    dist[3][0] = 35 (dari City D ke UPTM)
         */
        
        // Dapatkan nama city terakhir (selepas " -> " terakhir)
        // lastIndexOf(" -> ") + 4 - cari position " -> " terakhir dan tambah 4
        // +4 sebab " -> " ada 4 character (spasi, -, >, spasi)
        String lastCity = route.substring(route.lastIndexOf(" -> ") + 4);
        
        // Cari index city terakhir dan tambah kos balik ke UPTM
        for (int i = 0; i < n; i++) {
            if (lastCity.equals(locations[i])) {
                totalCost += dist[i][0];  // Tambah kos dari city terakhir ke UPTM
                break;
            }
        }
        
        // STEP 6: TAMBAH UPTM KE HUJUNG LALUAN
        route.append(" -> ").append(locations[0]);  // Contoh: " -> UPTM"
        
        // STEP 7: Rekod masa akhir
        long endTime = System.nanoTime();
        
        // STEP 8: Return hasil dalam bentuk object MCOPResult
        return new MCOPResult.Builder()
            .route(route.toString())                     // Laluan lengkap
            .totalCost(totalCost)                       // Kos yang ditemui
            .algorithmName(getAlgorithmName())          // Nama algorithm
            .executionTimeNano(endTime - startTime)     // Masa execution
            .citiesVisited(n)                           // Bilangan location (4)
            .isOptimal(false)                           // D&C TIDAK OPTIMAL!
            .build();
    }
    
    // ==================== DIVIDE AND CONQUER METHOD (Recursive) ====================
    
    /**
     * divideAndConquer() - Method recursive yang mencari city terdekat setiap step
     * 
     * APA ITU RECURSIVE?
     * Method yang panggil DIRI SENDIRI sehingga semua city dilawati.
     * 
     * CARA KERJA:
     * 1. DIVIDE: Cari city terdekat (paling murah) dari city semasa
     * 2. CONQUER: Tandakan city itu sebagai sudah dilawati
     * 3. RECURSE: Panggil method ini lagi dengan city baru
     * 4. COMBINE: Tambah kos dan teruskan
     * 
     * @param current - Index location semasa
     * @param visited - Array tracking location yang sudah dilawati
     * @param route - Laluan setakat ini (akan diubah)
     * @return Kos perjalanan setakat ini
     */
    private int divideAndConquer(int current, boolean[] visited, StringBuilder route) {
        
        // ========== BASE CASE (Semua City Sudah Dilawati) ==========
        /**
         * BASE CASE: Kalau semua city sudah dilawati, return 0 (kos tambahan = 0)
         * allVisited() akan return true jika semua city sudah dilawati
         */
        if (allVisited(visited)) {
            return 0;  // Tiada kos tambahan, semua sudah dilawati
        }
        
        // ========== DIVIDE STEP: Cari City Terdekat ==========
        /**
         * Cari city yang BELUM dilawati dengan kos PALING RENDAH dari city semasa.
         * Ini adalah step "Divide" - kita bahagikan masalah dengan memilih satu city.
         */
        
        int nearest = -1;                    // Index city terdekat (mulakan dengan -1)
        int minDist = Integer.MAX_VALUE;    // Kos terkecil (mulakan dengan nilai besar)
        
        // Loop melalui semua city (0, 1, 2, 3)
        for (int i = 0; i < n; i++) {
            /**
             * Syarat untuk city yang boleh dipilih:
             * 1. Belum dilawati (!visited[i])
             * 2. Ada kos yang positif (dist[current][i] > 0)
             * 3. Kos lebih kecil dari kos terkecil setakat ini
             */
            if (!visited[i] && dist[current][i] > 0 && dist[current][i] < minDist) {
                minDist = dist[current][i];  // Update kos terkecil
                nearest = i;                 // Simpan index city ini
            }
        }
        
        // ========== CONQUER STEP: Lawati City Terdekat ==========
        /**
         * Jika kita jumpa city terdekat (nearest != -1):
         * 1. Tandakan city itu sebagai sudah dilawati
         * 2. Tambah city ke dalam laluan
         * 3. Tambah kos ke total
         * 4. Panggil method ini LAGI (recursive) dengan city baru
         */
        if (nearest != -1) {
            visited[nearest] = true;                           // Tandakan sudah dilawati
            route.append(" -> ").append(locations[nearest]);   // Tambah ke laluan
            
            /**
             * RECURSIVE CALL (Panggil Diri Sendiri)
             * 
             * minDist = kos dari city semasa ke city terdekat
             * + divideAndConquer(nearest, visited, route) = kos untuk baki city
             * 
             * Contoh: 
             * - minDist = 15 (UPTM → City B)
             * - divideAndConquer() = 28+20 = 48 (City B → City D → City C)
             * - Return = 15 + 48 = 63 (kos sehingga sebelum balik ke UPTM)
             */
            return minDist + divideAndConquer(nearest, visited, route);
        }
        
        /**
         * Kalau nearest = -1, bermakna tiada city yang boleh dilawati.
         * Ini tak sepatutnya berlaku, tapi return 0 untuk safety.
         */
        return 0;
    }
    
    // ==================== HELPER METHOD ====================
    
    /**
     * allVisited() - Semak sama ada SEMUA city sudah dilawati
     * 
     * CARA KERJA:
     * Loop melalui array visited[]. Kalau jumpa satu pun yang false, return false.
     * Kalau semua true, return true.
     * 
     * @param visited - Array tracking location yang sudah dilawati
     * @return true jika semua city sudah dilawati, false jika masih ada yang belum
     */
    private boolean allVisited(boolean[] visited) {
        // Loop melalui setiap element dalam array visited
        for (boolean v : visited) {
            // Kalau ada element yang false, return false (belum habis)
            if (!v) {
                return false;
            }
        }
        // Kalau semua true, return true (sudah habis)
        return true;
    }
    
    // ==================== GETTER METHODS (Untuk Dapatkan Maklumat Algorithm) ====================
    
    /**
     * getAlgorithmName() - Return nama algorithm untuk ditunjukkan dalam output
     * 
     * @return Nama algorithm
     */
    @Override
    public String getAlgorithmName() { 
        return "Divide & Conquer"; 
    }
    
    /**
     * getTimeComplexity() - Return time complexity algorithm
     * 
     * O(n²) bermakna masa bertambah secara kuadratik dengan bilangan location
     * Untuk n=4, 4² = 16 operasi (sangat cepat)
     * Untuk n=10, 10² = 100 operasi (masih cepat)
     * Untuk n=100, 100² = 10,000 operasi (ok sahaja)
     * 
     * @return Time complexity notation
     */
    @Override
    public String getTimeComplexity() { 
        return "O(n²)"; 
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