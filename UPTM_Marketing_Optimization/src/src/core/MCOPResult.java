package src.core;

/**
 * MCOP RESULT - Objek untuk menyimpan hasil daripada setiap algorithm
 * 
 * APA ITU MCOPResult?
 * Ini adalah class yang bertindak sebagai "kontena" untuk menyimpan semua maklumat
 * tentang hasil yang diperoleh dari setiap algorithm TSP.
 * 
 * KENAPA PERLU CLASS INI?
 * Daripada setiap algorithm return String sahaja, kita return object yang mengandungi:
 * - Laluan (route)
 * - Kos (totalCost)
 * - Nama algorithm (algorithmName)
 * - Masa execution (executionTimeNano)
 * - Bilangan city yang dilawati (citiesVisited)
 * - Status optimal (isOptimal)
 * 
 * INI ADALAH "IMMUTABLE CLASS" = Data tidak boleh diubah selepas object dicipta.
 * Guna keyword "final" pada class dan semua field untuk pastikan data selamat.
 * 
 * DESIGN PATTERN: BUILDER PATTERN
 * Guna Builder pattern untuk memudahkan pembinaan object dengan banyak parameter.
 * 
 * @author Hakim & Group
 */
public final class MCOPResult {
    
    // ==================== FIELDS (Data yang disimpan) ====================
    
    /**
     * route - Laluan lengkap yang ditemui oleh algorithm
     * 
     * Format: "UPTM -> City B -> City D -> City C -> UPTM"
     * 
     * Ini adalah string yang menunjukkan urutan lawatan ke semua location,
     * bermula dan berakhir di UPTM.
     */
    private final String route;
    
    /**
     * totalCost - Jumlah kos perjalanan untuk laluan tersebut
     * 
     * Untuk cost matrix yang diberikan, kos minimum adalah 88.
     * 
     * Unit: arbitrary units (bukan matawang sebenar)
     * 
     * Contoh: 88
     */
    private final int totalCost;
    
    /**
     * algorithmName - Nama algorithm yang digunakan
     * 
     * Contoh:
     * - "Greedy (Nearest Neighbor + Multi-Start)"
     * - "Dynamic Programming (Held-Karp)"
     * - "Backtracking (Branch & Bound)"
     * - "Divide & Conquer"
     */
    private final String algorithmName;
    
    /**
     * executionTimeNano - Masa yang diambil oleh algorithm untuk menjalankan
     * 
     * Disimpan dalam nanosecond (1 nanosecond = 1/1,000,000,000 saat)
     * 
     * Kenapa guna nanosecond?
     * - Lebih tepat untuk algorithm yang sangat cepat
     * - Boleh tukar ke millisecond bila perlu
     * 
     * Contoh: 33000 (33 microseconds) atau 33000000 (33 milliseconds)
     */
    private final long executionTimeNano;
    
    /**
     * citiesVisited - Bilangan location yang dilawati
     * 
     * Untuk projek ini, sentiasa 4 (UPTM, City B, City C, City D)
     * 
     * Tapi kalau tambah location nanti, nilai ini akan berubah.
     */
    private final int citiesVisited;
    
    /**
     * isOptimal - Menunjukkan sama ada algorithm memberikan hasil yang OPTIMAL
     * 
     * true = Algorithm menjamin hasil yang paling pendek (optimal)
     * false = Algorithm tidak menjamin optimal (hampiran/approximation)
     * 
     * Untuk projek ini:
     * - Greedy: false (tidak optimal)
     * - Dynamic Programming: true (optimal)
     * - Backtracking: true (optimal)
     * - Divide & Conquer: false (tidak optimal)
     */
    private final boolean isOptimal;
    
    // ==================== CONSTRUCTOR (Private) ====================
    
    /**
     * Constructor PRIVATE - Hanya boleh dipanggil dari dalam class ini
     * 
     * Kenapa private?
     * - Untuk memastikan object hanya dicipta melalui Builder
     * - Ini adalah sebahagian dari Builder Pattern
     * 
     * @param builder - Objek Builder yang mengandungi semua data
     */
    private MCOPResult(Builder builder) {
        // Ambil semua data dari builder dan simpan ke dalam fields
        this.route = builder.route;
        this.totalCost = builder.totalCost;
        this.algorithmName = builder.algorithmName;
        this.executionTimeNano = builder.executionTimeNano;
        this.citiesVisited = builder.citiesVisited;
        this.isOptimal = builder.isOptimal;
    }
    
    // ==================== BUILDER CLASS (Untuk Membina Object) ====================
    
    /**
     * BUILDER CLASS - Memudahkan pembinaan object MCOPResult
     * 
     * APA ITU BUILDER PATTERN?
     * Cara alternatif untuk membina object dengan banyak parameter.
     * 
     * Contoh cara guna:
     * 
     * MCOPResult result = new MCOPResult.Builder()
     *     .route("UPTM -> City B -> City D -> City C -> UPTM")
     *     .totalCost(88)
     *     .algorithmName("Greedy")
     *     .executionTimeNano(33000)
     *     .citiesVisited(4)
     *     .isOptimal(false)
     *     .build();
     * 
     * KELEBIHAN:
     * 1. Senang dibaca - setiap parameter ada namanya
     * 2. Fleksibel - tak perlu ingat urutan parameter
     * 3. Boleh tambah parameter baru tanpa patahkan code lama
     */
    public static class Builder {
        
        // ========== BUILDER FIELDS (Sama dengan MCOPResult) ==========
        // Semua fields ini akan diisi step by step melalui method chaining
        
        private String route;               // Laluan
        private int totalCost;              // Kos
        private String algorithmName;       // Nama algorithm
        private long executionTimeNano;     // Masa execution
        private int citiesVisited;          // Bilangan city
        private boolean isOptimal;          // Status optimal
        
        // ========== BUILDER METHODS (Method Chaining) ==========
        
        /**
         * setRoute() - Set laluan
         * 
         * @param route - Laluan lengkap (contoh: "UPTM -> City B -> ...")
         * @return Builder object (untuk method chaining)
         */
        public Builder route(String route) { 
            this.route = route; 
            return this;  // Return this supaya boleh chain method seterusnya
        }
        
        /**
         * setTotalCost() - Set jumlah kos
         * 
         * @param cost - Kos perjalanan (contoh: 88)
         * @return Builder object (untuk method chaining)
         */
        public Builder totalCost(int cost) { 
            this.totalCost = cost; 
            return this; 
        }
        
        /**
         * setAlgorithmName() - Set nama algorithm
         * 
         * @param name - Nama algorithm (contoh: "Greedy")
         * @return Builder object (untuk method chaining)
         */
        public Builder algorithmName(String name) { 
            this.algorithmName = name; 
            return this; 
        }
        
        /**
         * setExecutionTimeNano() - Set masa execution dalam nanosecond
         * 
         * @param time - Masa dalam nanosecond (contoh: 33000)
         * @return Builder object (untuk method chaining)
         */
        public Builder executionTimeNano(long time) { 
            this.executionTimeNano = time; 
            return this; 
        }
        
        /**
         * setCitiesVisited() - Set bilangan city yang dilawati
         * 
         * @param count - Bilangan city (contoh: 4)
         * @return Builder object (untuk method chaining)
         */
        public Builder citiesVisited(int count) { 
            this.citiesVisited = count; 
            return this; 
        }
        
        /**
         * setIsOptimal() - Set status optimal
         * 
         * @param optimal - true jika optimal, false jika tidak
         * @return Builder object (untuk method chaining)
         */
        public Builder isOptimal(boolean optimal) { 
            this.isOptimal = optimal; 
            return this; 
        }
        
        /**
         * build() - Membina object MCOPResult yang sebenar
         * 
         * Method ini dipanggil di akhir untuk mencipta object MCOPResult.
         * 
         * @return MCOPResult object yang lengkap
         */
        public MCOPResult build() {
            // Panggil constructor private MCOPResult dengan builder ini
            return new MCOPResult(this);
        }
    }
    
    // ==================== GETTER METHODS (Untuk Akses Data) ====================
    
    /**
     * getRoute() - Dapatkan laluan
     * 
     * @return String laluan lengkap
     */
    public String getRoute() { 
        return route; 
    }
    
    /**
     * getTotalCost() - Dapatkan jumlah kos
     * 
     * @return int kos perjalanan
     */
    public int getTotalCost() { 
        return totalCost; 
    }
    
    /**
     * getAlgorithmName() - Dapatkan nama algorithm
     * 
     * @return String nama algorithm
     */
    public String getAlgorithmName() { 
        return algorithmName; 
    }
    
    /**
     * getExecutionTimeNano() - Dapatkan masa execution dalam nanosecond
     * 
     * @return long masa dalam nanosecond
     */
    public long getExecutionTimeNano() { 
        return executionTimeNano; 
    }
    
    /**
     * getExecutionTimeMs() - Dapatkan masa execution dalam millisecond
     * 
     * Method ini TIDAK menyimpan nilai, tetapi mengira dari executionTimeNano
     * 
     * Formula: nanosecond / 1,000,000 = millisecond
     * 
     * @return double masa dalam millisecond
     */
    public double getExecutionTimeMs() { 
        return executionTimeNano / 1_000_000.0; 
    }
    
    /**
     * getCitiesVisited() - Dapatkan bilangan city yang dilawati
     * 
     * @return int bilangan city
     */
    public int getCitiesVisited() { 
        return citiesVisited; 
    }
    
    /**
     * isOptimal() - Dapatkan status optimal
     * 
     * @return boolean true jika optimal, false jika tidak
     */
    public boolean isOptimal() { 
        return isOptimal; 
    }
    
    // ==================== TOSTRING METHOD (Untuk Output) ====================
    
    /**
     * toString() - Menghasilkan representasi string untuk object ini
     * 
     * Method ini akan dipanggil secara automatik bila kita print object:
     * System.out.println(result);
     * 
     * Format output:
     * [Greedy] Route: UPTM -> City B -> City D -> City C -> UPTM | Cost: 88 | Time: 0.215 ms | Optimal: true
     * 
     * @return String formatted output
     */
    @Override
    public String toString() {
        /**
         * String.format() - Membina string dengan format tertentu
         * 
         * %s = string (untuk algorithmName dan route)
         * %d = integer (untuk totalCost)
         * %.3f = float dengan 3 decimal places (untuk getExecutionTimeMs())
         * %b = boolean (untuk isOptimal)
         */
        return String.format("[%s] Route: %s | Cost: %d | Time: %.3f ms | Optimal: %s",
            algorithmName,      // Nama algorithm
            route,              // Laluan
            totalCost,          // Kos
            getExecutionTimeMs(), // Masa dalam millisecond
            isOptimal           // Status optimal (true/false)
        );
    }
}