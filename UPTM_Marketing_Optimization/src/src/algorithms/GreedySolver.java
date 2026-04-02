package src.algorithms;

// Import classes yang diperlukan
import src.core.*;      // Untuk MCOPSolver interface dan MCOPResult
import java.util.*;     // Untuk Arrays, StringBuilder, dll

/**
 * GREEDY SOLVER - Mencari laluan menggunakan algoritma Greedy (Nearest Neighbor)
 * 
 * APA ITU GREEDY ALGORITHM?
 * Greedy = "Tamak" / "Rakus"
 * 
 * Setiap langkah, kita pilih pilihan yang TERBAIK pada masa itu,
 * tanpa memikirkan kesan untuk masa depan.
 * 
 * DALAM KONTEKS MCOP (TSP):
 * Setiap kali kita berada di satu city, kita akan pergi ke city TERDEKAT
 * yang BELUM dilawati. Ini dipanggil "Nearest Neighbor" heuristic.
 * 
 * KELEBIHAN: Sangat cepat dan mudah (O(n²))
 * KEKURANGAN: Tidak pasti dapat laluan paling pendek (tidak optimal)
 * 
 * MULTI-START IMPROVEMENT:
 * Greedy biasa start dari UPTM sahaja. Tapi kadang-kadang start dari city lain
 * boleh bagi hasil yang lebih baik. Jadi kita cuba START DARI SEMUA CITY,
 * dan pilih yang terbaik. Ini dipanggil "Multi-Start Greedy".
 * 
 * @author Hakim & Group
 */
public class GreedySolver implements MCOPSolver {
    
    // ==================== VARIABLES (Data yang disimpan) ====================
    
    /**
     * distanceMatrix - Matrix kos perjalanan
     * Contoh: distanceMatrix[0][1] = 15 (kos dari UPTM ke City B)
     */
    private final int[][] distanceMatrix;
    
    /**
     * locations - Nama-nama tempat (UPTM, City B, City C, City D)
     */
    private final String[] locations;
    
    /**
     * numCities - Bilangan location (dalam kes ini, 4)
     */
    private final int numCities;
    
    /**
     * tryAllStarts - Flag untuk tentukan sama ada nak cuba start dari semua city
     * 
     * true = Cuba start dari semua city, ambil yang terbaik
     * false = Start dari UPTM sahaja (lebih cepat tapi mungkin kurang baik)
     */
    private final boolean tryAllStarts;
    
    // ==================== CONSTRUCTORS (Tempat Setup Data) ====================
    
    /**
     * Constructor 1 - Guna multi-start secara default (tryAllStarts = true)
     * 
     * @param distanceMatrix - Matrix kos perjalanan (dari Main.java)
     * @param locations - Nama-nama location (dari Main.java)
     */
    public GreedySolver(int[][] distanceMatrix, String[] locations) {
        // Panggil constructor lain dengan tryAllStarts = true
        this(distanceMatrix, locations, true);
    }
    
    /**
     * Constructor 2 - Boleh pilih sama ada nak guna multi-start atau tidak
     * 
     * @param distanceMatrix - Matrix kos perjalanan
     * @param locations - Nama-nama location
     * @param tryAllStarts - true = cuba start dari semua city, false = start dari UPTM sahaja
     */
    public GreedySolver(int[][] distanceMatrix, String[] locations, boolean tryAllStarts) {
        this.distanceMatrix = distanceMatrix;  // Simpan matrix kos
        this.locations = locations;            // Simpan nama location
        this.numCities = distanceMatrix.length; // Kira berapa location (4)
        this.tryAllStarts = tryAllStarts;      // Simpan flag multi-start
    }
    
    // ==================== SOLVE METHOD (Tempat Utama) ====================
    
    /**
     * solve() - Method utama untuk cari laluan menggunakan Greedy
     * 
     * CARA KERJA:
     * 1. Rekod masa mula
     * 2. Tentukan berapa banyak starting points nak cuba
     *    - Jika tryAllStarts = true: cuba semua city (4 kali)
     *    - Jika tryAllStarts = false: cuba UPTM sahaja (1 kali)
     * 3. Untuk setiap starting point, panggil solveFromStart()
     * 4. Simpan hasil yang terbaik (kos paling rendah)
     * 5. Rekod masa akhir dan return hasil terbaik
     * 
     * @return MCOPResult - Objek yang mengandungi laluan, kos, masa, dll
     */
    @Override
    public MCOPResult solve() {
        
        // STEP 1: Rekod masa mula (dalam nanosecond)
        long startTime = System.nanoTime();
        
        // STEP 2: Sediakan variable untuk track hasil terbaik
        MCOPResult bestResult = null;          // Hasil terbaik setakat ini
        int bestCost = Integer.MAX_VALUE;      // Kos terbaik (mula dengan nilai besar)
        
        // STEP 3: Tentukan berapa starting points nak cuba
        // Jika tryAllStarts = true, cuba semua city (numCities)
        // Jika false, cuba hanya UPTM (index 0) sahaja
        int startCities = tryAllStarts ? numCities : 1;
        
        // STEP 4: Loop melalui setiap starting point
        for (int start = 0; start < startCities; start++) {
            
            // Cuba cari laluan bermula dari city 'start'
            MCOPResult result = solveFromStart(start);
            
            /**
             * Kalau kos dari starting point ini LEBIH RENDAH dari kos terbaik,
             * update bestResult dan bestCost
             */
            if (result.getTotalCost() < bestCost) {
                bestCost = result.getTotalCost();
                bestResult = result;
            }
        }
        
        // STEP 5: Rekod masa akhir
        long endTime = System.nanoTime();
        
        // STEP 6: Return hasil terbaik
        return new MCOPResult.Builder()
            .route(bestResult.getRoute())                      // Laluan terbaik
            .totalCost(bestResult.getTotalCost())              // Kos terbaik
            .algorithmName(getAlgorithmName())                 // Nama algorithm
            .executionTimeNano(endTime - startTime)            // Masa execution
            .citiesVisited(numCities)                          // Bilangan location
            .isOptimal(bestResult.getTotalCost() <= 88)        // Greedy TIDAK OPTIMAL
            .build();
    }
    
    // ==================== SOLVE FROM SPECIFIC START ====================
    
    /**
     * solveFromStart() - Cari laluan menggunakan Greedy bermula dari city tertentu
     * 
     * CARA KERJA:
     * 1. Tandakan starting city sebagai sudah dilawati
     * 2. Untuk setiap step (sehingga semua city dilawati):
     *    a. Cari city TERDEKAT yang BELUM dilawati
     *    b. Pergi ke city tersebut
     *    c. Tambah kos
     * 3. Selepas semua city dilawati, balik ke starting city
     * 4. Normalize laluan supaya UPTM di awal (untuk consistency)
     * 
     * @param startCity - Index city tempat kita mula (0=UPTM, 1=City B, dll)
     * @return MCOPResult - Hasil untuk starting point ini
     */
    private MCOPResult solveFromStart(int startCity) {
        
        // STEP 1: Sediakan tracking untuk city yang sudah dilawati
        boolean[] visited = new boolean[numCities];  // Semua false pada mulanya
        
        // STEP 2: Sediakan laluan
        StringBuilder route = new StringBuilder();    // Untuk bina laluan
        
        // STEP 3: Mula dari startCity
        int currentCity = startCity;                  // City semasa
        int totalCost = 0;                            // Kos setakat ini
        
        // STEP 4: Tandakan startCity sebagai sudah dilawati
        route.append(locations[currentCity]);         // Tambah nama city ke laluan
        visited[currentCity] = true;                  // Tandakan sudah dilawati
        
        // STEP 5: Lawati baki city (numCities - 1 kali)
        for (int step = 0; step < numCities - 1; step++) {
            
            // Cari city terdekat yang belum dilawati
            int nextCity = findNearestUnvisited(currentCity, visited);
            
            // Kalau tak jumpa (sepatutnya tak berlaku), berhenti
            if (nextCity == -1) break;
            
            // STEP 5a: Tambah kos ke city tersebut
            totalCost += distanceMatrix[currentCity][nextCity];
            
            // STEP 5b: Tambah city ke laluan
            route.append(" -> ").append(locations[nextCity]);
            
            // STEP 5c: Tandakan city ini sebagai sudah dilawati
            visited[nextCity] = true;
            
            // STEP 5d: Update current city
            currentCity = nextCity;
        }
        
        // STEP 6: Selepas semua city dilawati, BALIK ke starting city
        totalCost += distanceMatrix[currentCity][startCity];
        route.append(" -> ").append(locations[startCity]);
        
        // STEP 7: Normalize laluan (pastikan UPTM di awal)
        String finalRoute = normalizeRoute(route.toString(), startCity);
        
        // STEP 8: Return hasil untuk starting point ini
        return new MCOPResult.Builder()
            .route(finalRoute)                         // Laluan yang ditemui
            .totalCost(totalCost)                      // Kos untuk laluan ini
            .algorithmName(getAlgorithmName())         // Nama algorithm
            .citiesVisited(numCities)                  // Bilangan location
            .isOptimal(false)                          // Greedy TIDAK OPTIMAL
            .build();
    }
    
    // ==================== HELPER METHOD: Cari City Terdekat ====================
    
    /**
     * findNearestUnvisited() - Cari city yang BELUM dilawati dengan kos PALING RENDAH
     * 
     * CARA KERJA:
     * 1. Loop melalui semua city
     * 2. Untuk setiap city yang BELUM dilawati:
     *    a. Cek kos dari city semasa ke city tersebut
     *    b. Jika kos lebih kecil dari kos terkecil setakat ini, update
     * 3. Return index city terdekat (atau -1 jika tiada)
     * 
     * @param current - Index city semasa
     * @param visited - Array tracking city yang sudah dilawati
     * @return Index city terdekat, atau -1 jika tiada
     */
    private int findNearestUnvisited(int current, boolean[] visited) {
        
        int nearest = -1;                    // Index city terdekat (mulakan dengan -1)
        int minDist = Integer.MAX_VALUE;    // Kos terkecil (mulakan dengan nilai besar)
        
        // Loop melalui semua city (0, 1, 2, 3)
        for (int i = 0; i < numCities; i++) {
            
            /**
             * Syarat untuk city yang boleh dipilih:
             * 1. Belum dilawati (!visited[i])
             * 2. Ada kos yang positif (distanceMatrix[current][i] > 0)
             *    - Kos 0 hanya untuk diri sendiri, jadi tak boleh pilih
             * 3. Kos lebih kecil dari kos terkecil setakat ini
             */
            if (!visited[i] && distanceMatrix[current][i] > 0 && distanceMatrix[current][i] < minDist) {
                minDist = distanceMatrix[current][i];  // Update kos terkecil
                nearest = i;                           // Simpan index city ini
            }
        }
        
        return nearest;  // Return index city terdekat
    }
    
    // ==================== HELPER METHOD: Normalize Route ====================
    
    /**
     * normalizeRoute() - Pastikan laluan bermula dengan UPTM
     * 
     * KENAPA PERLU?
     * Kalau kita start dari city lain (contoh City B), laluan mungkin:
     * "City B -> City D -> City C -> UPTM -> City B"
     * 
     * Kita nak pastikan semua laluan dalam format yang sama:
     * "UPTM -> ... -> UPTM"
     * 
     * CARA KERJA:
     * 1. Split laluan menjadi array (contoh: ["City B", "City D", "City C", "UPTM", "City B"])
     * 2. Cari index di mana UPTM berada
     * 3. Rotate array supaya UPTM menjadi element pertama
     * 4. Gabungkan semula menjadi string
     * 
     * @param route - Laluan yang belum dinormalize
     * @param startCity - City tempat kita mula (untuk rujukan)
     * @return Laluan yang sudah dinormalize (UPTM di awal)
     */
    private String normalizeRoute(String route, int startCity) {
        
        // Kalau start dari UPTM, laluan dah betul, terus return
        if (startCity == 0) return route;
        
        /**
         * STEP 1: Split laluan menjadi array
         * Contoh: "City B -> City D -> City C -> UPTM -> City B"
         * parts = ["City B", "City D", "City C", "UPTM", "City B"]
         */
        String[] parts = route.split(" -> ");
        
        /**
         * STEP 2: Cari index di mana UPTM berada
         * locations[0] = "UPTM"
         */
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals(locations[0])) {
                
                /**
                 * STEP 3: Rotate array supaya UPTM jadi element pertama
                 * 
                 * Contoh: parts = ["City B", "City D", "City C", "UPTM", "City B"]
                 * i = 3 (UPTM di index 3)
                 * 
                 * rotated[0] = parts[(3+0)%5] = parts[3] = "UPTM"
                 * rotated[1] = parts[(3+1)%5] = parts[4] = "City B"
                 * rotated[2] = parts[(3+2)%5] = parts[0] = "City B"  <-- duplicate?
                 * 
                 * Sebenarnya untuk TSP, laluan cyclic, jadi ada duplicate city.
                 * Kita tak perlu risau, yang penting UPTM di awal.
                 */
                String[] rotated = new String[parts.length];
                for (int j = 0; j < parts.length; j++) {
                    rotated[j] = parts[(i + j) % parts.length];
                }
                
                // STEP 4: Gabungkan semula menjadi string
                return String.join(" -> ", rotated);
            }
        }
        
        // Kalau UPTM tak jumpa (tak sepatutnya), return route asal
        return route;
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * getAlgorithmName() - Return nama algorithm
     * 
     * Nama berubah bergantung kepada sama ada guna multi-start atau tidak
     * 
     * @return Nama algorithm
     */
    @Override
    public String getAlgorithmName() {
        // Jika tryAllStarts = true: "Greedy (Nearest Neighbor + Multi-Start)"
        // Jika false: "Greedy (Nearest Neighbor)"
        return "Greedy (Nearest Neighbor" + (tryAllStarts ? " + Multi-Start)" : ")");
    }
    
    /**
     * getTimeComplexity() - Return time complexity algorithm
     * 
     * O(n²) bermakna masa bertambah secara kuadratik dengan bilangan location
     * 
     * Kenapa O(n²)?
     * - Loop luar: lawati n city (n step)
     * - Loop dalam: cari city terdekat (n city)
     * - Total: n × n = n²
     * 
     * Untuk n=4: 16 operasi (sangat cepat)
     * Untuk n=100: 10,000 operasi (masih cepat)
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
     * 
     * Kenapa O(n)?
     * - Array visited: n element
     * - StringBuilder route: n element
     * - Variable lain: constant
     * - Total: O(n)
     * 
     * Untuk n=4: guna ~4 unit memori
     * Untuk n=100: guna ~100 unit memori
     * 
     * @return Space complexity notation
     */
    @Override
    public String getSpaceComplexity() { 
        return "O(n)"; 
    }
}