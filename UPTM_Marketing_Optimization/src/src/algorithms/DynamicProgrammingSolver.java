package src.algorithms;

// Import classes yang diperlukan
import src.core.*;      // Untuk MCOPSolver interface dan MCOPResult
import java.util.*;     // Untuk Arrays.fill()

/**
 * DYNAMIC PROGRAMMING SOLVER - Mencari laluan terpendek menggunakan Dynamic Programming (Held-Karp)
 * 
 * APA ITU DYNAMIC PROGRAMMING?
 * Dynamic Programming = "Simpan hasil pengiraan yang sudah dibuat supaya tak perlu kira semula"
 * 
 * MACAM MANA?
 * Bayangkan kita kena kira semua kemungkinan laluan (24 untuk 4 city)
 * TAPI banyak laluan yang kongsi sub-masalah yang sama.
 * Contoh: Laluan UPTM→City B→City C dan UPTM→City D→City C
 * Kedua-dua berakhir di City C, dan dari City C ke seterusnya adalah SAMA.
 * 
 * DP akan SIMPAN hasil pengiraan untuk "berakhir di City C dengan set city tertentu"
 * Supaya bila jumpa situasi yang sama, boleh terus guna tanpa kira semula.
 * 
 * TEKNIK: Bitmasking + Memoization
 * - Bitmasking: Guna binary number (0/1) untuk track city mana dah dilawati
 * - Memoization: Simpan hasil dalam table (memo) supaya tak perlu kira semula
 * 
 * KELEBIHAN: Pasti dapat laluan paling pendek (OPTIMAL)
 * KEKURANGAN: Perlukan banyak memori (O(n·2ⁿ)) dan hanya sesuai untuk n ≤ 20
 * 
 * @author Hakim & Group
 */
public class DynamicProgrammingSolver implements MCOPSolver {
    
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
    
    /**
     * memo - Table untuk simpan kos minimum dari satu state
     * 
     * Dimensi: [n][1 << n] = [4][16]
     * 
     * memo[pos][mask] = kos minimum untuk melawat semua city yang belum dilawati,
     *                   bermula dari city 'pos' dengan set city yang sudah dilawati 'mask'
     * 
     * Contoh: memo[2][0b0111] = kos minimum dari City C dengan sudah lawat UPTM, City B, City C
     */
    private int[][] memo;
    
    /**
     * next - Table untuk simpan city yang akan dilawati seterusnya (untuk reconstruct path)
     * 
     * Dimensi: [n][1 << n] = [4][16]
     * 
     * next[pos][mask] = city yang patut dilawati seterusnya untuk mencapai kos minimum
     *                   dari state (pos, mask)
     */
    private int[][] next;
    
    /**
     * allVisited - Bitmask yang menunjukkan SEMUA city sudah dilawati
     * 
     * Untuk n=4, allVisited = (1 << 4) - 1 = 16 - 1 = 15
     * Dalam binary: 1111 (4 bit semuanya 1)
     * 
     * Maknanya: city 0,1,2,3 semua sudah dilawati
     */
    private int allVisited;
    
    // ==================== CONSTRUCTOR (Tempat Setup Data) ====================
    
    /**
     * Constructor - Dipanggil bila kita create object DynamicProgrammingSolver
     * 
     * @param distanceMatrix - Matrix kos perjalanan (dari Main.java)
     * @param locations - Nama-nama location (dari Main.java)
     */
    public DynamicProgrammingSolver(int[][] distanceMatrix, String[] locations) {
        this.dist = distanceMatrix;      // Simpan matrix kos
        this.locations = locations;      // Simpan nama location
        this.n = distanceMatrix.length;  // Kira berapa location (4)
    }
    
    // ==================== SOLVE METHOD (Tempat Utama) ====================
    
    /**
     * solve() - Method utama untuk cari laluan terpendek menggunakan DP
     * 
     * CARA KERJA:
     * 1. Rekod masa mula
     * 2. Set allVisited (contoh: untuk 4 city = 15)
     * 3. Buat table memo dan next dengan saiz [n][2ⁿ]
     * 4. Initialize memo dengan -1 (belum dikira)
     * 5. Panggil tsp(0, 1) - mula dari UPTM (pos=0), hanya UPTM dilawati (mask=1)
     * 6. Reconstruct laluan dari table next
     * 7. Rekod masa akhir dan return hasil
     * 
     * @return MCOPResult - Objek yang mengandungi laluan, kos, masa, dll
     */
    @Override
    public MCOPResult solve() {
        
        // STEP 1: Rekod masa mula
        long startTime = System.nanoTime();
        
        // STEP 2: Set allVisited mask (semua bit = 1)
        // (1 << n) = 2ⁿ, tolak 1 dapat n bit semuanya 1
        allVisited = (1 << n) - 1;  // Untuk n=4: 16-1=15 (binary: 1111)
        
        // STEP 3: Buat table memo dan next
        // Saiz: [n][2ⁿ] = [4][16] untuk 4 city
        memo = new int[n][1 << n];
        next = new int[n][1 << n];
        
        // STEP 4: Initialize memo dengan -1 (bermakna belum dikira)
        for (int i = 0; i < n; i++) {
            Arrays.fill(memo[i], -1);
        }
        
        // STEP 5: MULA DYNAMIC PROGRAMMING!
        // tsp(0, 1) bermaksud:
        // - pos = 0 (kita berada di UPTM)
        // - mask = 1 (binary: 0001) bermaksud hanya city 0 (UPTM) sudah dilawati
        int minCost = tsp(0, 1);
        
        // STEP 6: Bina semula laluan dari table next
        String route = reconstructPath();
        
        // STEP 7: Rekod masa akhir
        long endTime = System.nanoTime();
        
        // STEP 8: Return hasil
        return new MCOPResult.Builder()
            .route(route)                             // Laluan lengkap
            .totalCost(minCost)                       // Kos minimum (pasti 88)
            .algorithmName(getAlgorithmName())        // Nama algorithm
            .executionTimeNano(endTime - startTime)   // Masa execution
            .citiesVisited(n)                         // Bilangan location (4)
            .isOptimal(true)                          // DP PASTI OPTIMAL!
            .build();
    }
    
    // ==================== TSP METHOD (Recursive DP with Memoization) ====================
    
    /**
     * tsp() - Method recursive yang mengira kos minimum menggunakan DP
     * 
     * APA ITU BITMASK?
     * Guna binary number untuk track city mana sudah dilawati.
     * 
     * Untuk n=4:
     * - mask = 0001 (1)  = city 0 sudah dilawati (UPTM)
     * - mask = 0011 (3)  = city 0 dan 1 sudah dilawati
     * - mask = 0111 (7)  = city 0,1,2 sudah dilawati
     * - mask = 1111 (15) = semua city sudah dilawati
     * 
     * OPERASI BITWISE:
     * - (1 << city) = 2^city (contoh: 1 << 2 = 4 = 0100)
     * - (mask & (1 << city)) = cek sama ada city sudah dilawati
     * - (mask | (1 << city)) = tandakan city sebagai sudah dilawati
     * 
     * @param pos - Index location semasa (0=UPTM, 1=City B, dll)
     * @param mask - Bitmask tracking city yang sudah dilawati
     * @return Kos minimum dari state (pos, mask) hingga selesai
     */
    private int tsp(int pos, int mask) {
        
        // ========== BASE CASE (Semua City Sudah Dilawati) ==========
        /**
         * BASE CASE: Kalau mask == allVisited, semua city sudah dilawati.
         * Yang perlu dibuat: BALIK ke UPTM (city 0)
         * Return kos dari city semasa (pos) ke UPTM (0)
         */
        if (mask == allVisited) {
            return dist[pos][0];  // Kos balik ke UPTM
        }
        
        // ========== MEMOIZATION (Guna Hasil Yang Sudah Dikira) ==========
        /**
         * Kalau state (pos, mask) sudah pernah dikira, terus guna hasilnya.
         * Ini yang menjadikan DP cepat! Tak perlu kira semula.
         */
        if (memo[pos][mask] != -1) {
            return memo[pos][mask];  // Guna hasil yang sudah disimpan
        }
        
        // ========== RECURSIVE CASE (Kira Kos Minimum) ==========
        /**
         * Untuk setiap city yang BELUM dilawati:
         * 1. Kira kos = kos dari pos ke city + kos dari city ke seterusnya (recursive)
         * 2. Pilih city yang bagi kos paling rendah
         */
        
        int minCost = Integer.MAX_VALUE;  // Mula dengan nilai sangat besar
        int bestNext = -1;                // Simpan city terbaik untuk step ini
        
        // Loop melalui semua city (0, 1, 2, 3)
        for (int city = 0; city < n; city++) {
            
            /**
             * Cek sama ada city ini BELUM dilawati
             * 
             * (mask & (1 << city)) == 0 bermaksud bit untuk city ini adalah 0
             * 
             * Contoh: mask = 0001 (cuma UPTM dilawati)
             * Untuk city = 1: (0001 & 0010) = 0000 → 0, maka BELUM dilawati ✓
             * Untuk city = 0: (0001 & 0001) = 0001 → 1, maka SUDAH dilawati ✗
             */
            if ((mask & (1 << city)) == 0) {
                
                /**
                 * Kira kos untuk pergi ke city ini:
                 * - dist[pos][city] = kos dari city semasa ke city ini
                 * - tsp(city, mask | (1 << city)) = kos dari city ini ke seterusnya
                 *   mask | (1 << city) = tandakan city ini sebagai sudah dilawati
                 */
                int newCost = dist[pos][city] + tsp(city, mask | (1 << city));
                
                /**
                 * Kalau kos baru ini lebih rendah dari kos minimum setakat ini,
                 * update minCost dan simpan city ini sebagai bestNext
                 */
                if (newCost < minCost) {
                    minCost = newCost;
                    bestNext = city;
                }
            }
        }
        
        // ========== SIMPAN HASIL UNTUK GUNAAN NANTI ==========
        /**
         * Simpan hasil pengiraan ke dalam table memo dan next.
         * Supaya kalau jumpa state yang sama lagi, tak perlu kira semula.
         */
        next[pos][mask] = bestNext;      // Simpan city seterusnya (untuk reconstruct)
        memo[pos][mask] = minCost;       // Simpan kos minimum
        
        return minCost;  // Return kos minimum
    }
    
    // ==================== PATH RECONSTRUCTION METHOD ====================
    
    /**
     * reconstructPath() - Bina semula laluan dari table next
     * 
     * CARA KERJA:
     * 1. Mula dari UPTM (pos=0) dengan hanya UPTM dilawati (mask=1)
     * 2. Setiap step, lihat next[pos][mask] untuk tahu city seterusnya
     * 3. Tambah city itu ke laluan
     * 4. Update mask (tandakan city itu sudah dilawati)
     * 5. Update pos ke city itu
     * 6. Ulang sehingga mask == allVisited
     * 7. Akhir sekali, tambah kembali ke UPTM
     * 
     * @return String laluan lengkap (contoh: "UPTM -> City B -> City D -> City C -> UPTM")
     */
    private String reconstructPath() {
        
        // STEP 1: Sediakan StringBuilder untuk bina laluan
        StringBuilder route = new StringBuilder();
        
        // STEP 2: Mula dari UPTM (pos=0) dengan hanya UPTM dilawati (mask=1)
        int mask = 1;           // Binary: 0001 (cuma UPTM dilawati)
        int pos = 0;            // Kita berada di UPTM
        
        // STEP 3: Tambah UPTM ke laluan
        route.append(locations[pos]);  // "UPTM"
        
        // STEP 4: Loop sehingga semua city dilawati
        while (mask != allVisited) {
            
            // Dapatkan city seterusnya dari table next
            int nextCity = next[pos][mask];
            
            // Kalau nextCity = -1, ada masalah (tak sepatutnya berlaku)
            if (nextCity == -1) break;
            
            // STEP 5: Tambah city ini ke laluan
            route.append(" -> ").append(locations[nextCity]);
            
            // STEP 6: Update mask (tandakan city ini sudah dilawati)
            // mask | (1 << nextCity) = set bit untuk nextCity kepada 1
            mask = mask | (1 << nextCity);
            
            // STEP 7: Update pos ke city ini
            pos = nextCity;
        }
        
        // STEP 8: Akhir sekali, tambah balik ke UPTM
        route.append(" -> ").append(locations[0]);
        
        // STEP 9: Return laluan lengkap
        return route.toString();
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * getAlgorithmName() - Return nama algorithm
     */
    @Override
    public String getAlgorithmName() { 
        return "Dynamic Programming (Held-Karp)"; 
    }
    
    /**
     * getTimeComplexity() - Return time complexity
     * 
     * O(n²·2ⁿ) bermakna:
     * - 2ⁿ: Ada 2ⁿ kemungkinan mask (subset)
     * - n²: Untuk setiap mask, kita loop melalui n city (n) dan setiap city ada n pilihan
     * 
     * Untuk n=4: 4² × 16 = 256 operasi (sangat cepat)
     * Untuk n=10: 10² × 1024 = 102,400 operasi (masih ok)
     * Untuk n=20: 20² × 1,048,576 = 419 juta operasi (mula lambat)
     * Untuk n=30: 30² × 1 bilion = terlalu besar (tak praktikal)
     */
    @Override
    public String getTimeComplexity() { 
        return "O(n²·2ⁿ)"; 
    }
    
    /**
     * getSpaceComplexity() - Return space complexity
     * 
     * O(n·2ⁿ) bermakna kita perlukan memori untuk:
     * - memo table: n × 2ⁿ
     * - next table: n × 2ⁿ
     * 
     * Untuk n=4: 4 × 16 = 64 unit memori (sangat kecil)
     * Untuk n=20: 20 × 1,048,576 = 20 juta unit (masih ok)
     * Untuk n=30: 30 × 1 bilion = 30 bilion unit (terlalu besar!)
     */
    @Override
    public String getSpaceComplexity() { 
        return "O(n·2ⁿ)"; 
    }
}