package src.datastructures;

import java.util.ArrayList;

/**
 * MIN-HEAP DATA STRUCTURE - Struktur data untuk priority queue (utamakan nilai terkecil)
 * 
 * APA ITU MIN-HEAP?
 * Min-Heap adalah struktur data berbentuk pokok (tree) di mana:
 * - Setiap NODE PARENT sentiasa LEBIH KECIL dari CHILD nodes
 * - NILAI TERKECIL sentiasa berada di ROOT (paling atas)
 * - Ini dipanggil "HEAP PROPERTY"
 * 
 * BENTUK POKOK (Complete Binary Tree):
 * Semua level dipenuhi kecuali level paling bawah, dan diisi dari kiri ke kanan.
 * 
 * CONTOH MIN-HEAP:
 * 
 *          3
 *        /   \
 *       5     8
 *      / \   /
 *     9   7 10
 * 
 * 3 adalah terkecil (root)
 * 5 dan 8 lebih besar dari 3
 * 9, 7, 10 lebih besar dari parent masing-masing
 * 
 * KELEBIHAN:
 * - Insert: O(log n) - sangat cepat
 * - Extract Min: O(log n) - sangat cepat
 * - Peek (lihat nilai terkecil): O(1) - sangat cepat
 * 
 * KEGUNAAN DALAM MARKETING:
 * - Priority queue untuk campaign dengan budget terendah
 * - Urutkan student applications mengikut priority score
 * - Scheduling marketing activities berdasarkan deadline
 * 
 * @author Hakim & Group
 */
public class MinHeap {
    
    // ==================== FIELD ====================
    
    /**
     * heap - ArrayList yang menyimpan elemen-elemen heap
     * 
     * KENAPA GUNA ARRAYLIST?
     * - Complete binary tree boleh disimpan dalam array dengan mudah
     * - Index 0 = root (nilai terkecil)
     * - Index children: left child = 2*i + 1, right child = 2*i + 2
     * - Index parent: (i - 1) / 2
     * 
     * CONTOH PENYIMPANAN:
     * 
     * Tree:          Array:
     *      3         index: 0    1    2    3    4    5
     *    /   \       value: 3    5    8    9    7   10
     *   5     8
     *  / \   /
     * 9   7 10
     */
    private final ArrayList<Integer> heap;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor - Cipta Min-Heap kosong
     * 
     * ArrayList akan mula kosong, tiada elemen.
     */
    public MinHeap() {
        heap = new ArrayList<>();  // Buat ArrayList kosong
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * insert() - Masukkan nilai baru ke dalam heap
     * 
     * CARA KERJA:
     * 1. Tambah nilai baru di hujung array (level paling bawah, paling kanan)
     * 2. "Bubble Up" nilai tersebut ke atas sehingga heap property terpenuhi
     * 
     * @param value - Nilai yang hendak dimasukkan (contoh: 10, 3, 15)
     * 
     * CONTOH:
     * insert(3): heap = [3]
     * insert(15): heap = [3, 15]  (3 < 15, OK)
     * insert(1): heap = [3, 15, 1]
     *            Bubble up: 1 < 3? Ya, swap → [1, 15, 3]
     *                      1 di root, selesai
     */
    public void insert(int value) {
        // STEP 1: Tambah di hujung array
        heap.add(value);
        
        // STEP 2: Bubble up untuk kekalkan heap property
        // Index = heap.size() - 1 (index element baru)
        bubbleUp(heap.size() - 1);
    }
    
    /**
     * extractMin() - Keluarkan dan return nilai terkecil (root)
     * 
     * CARA KERJA:
     * 1. Simpan nilai root (nilai terkecil) untuk di-return
     * 2. Ambil element terakhir dan letakkan di root
     * 3. "Bubble Down" root tersebut ke bawah sehingga heap property terpenuhi
     * 4. Return nilai terkecil yang disimpan tadi
     * 
     * @return Nilai terkecil dalam heap, atau -1 jika heap kosong
     * 
     * CONTOH:
     * heap = [1, 3, 5, 7, 9]
     * extractMin() → return 1
     * heap selepas: [3, 7, 5, 9]
     *               Bubble down: 3 < 7? Ya, 3 < 5? Ya, OK
     */
    public int extractMin() {
        // Kalau heap kosong, return -1
        if (isEmpty()) {
            return -1;
        }
        
        // STEP 1: Simpan nilai root (nilai terkecil)
        int min = heap.get(0);
        
        // STEP 2: Ambil element terakhir
        int last = heap.remove(heap.size() - 1);
        
        // STEP 3: Kalau heap masih ada element, letak last di root
        if (!isEmpty()) {
            heap.set(0, last);
            
            // STEP 4: Bubble down untuk kekalkan heap property
            bubbleDown(0);
        }
        
        // STEP 5: Return nilai terkecil
        return min;
    }
    
    /**
     * peek() - Lihat nilai terkecil tanpa mengeluarkannya
     * 
     * @return Nilai terkecil (root), atau -1 jika heap kosong
     * 
     * CONTOH:
     * heap = [3, 5, 8, 9, 7, 10]
     * peek() → 3 (heap masih sama)
     */
    public int peek() {
        return isEmpty() ? -1 : heap.get(0);
    }
    
    /**
     * size() - Dapatkan bilangan elemen dalam heap
     * 
     * @return Bilangan elemen
     */
    public int size() { 
        return heap.size(); 
    }
    
    /**
     * isEmpty() - Semak sama ada heap kosong
     * 
     * @return true jika kosong, false jika ada elemen
     */
    public boolean isEmpty() { 
        return heap.isEmpty(); 
    }
    
    // ==================== PRIVATE HELPER METHODS ====================
    
    /**
     * bubbleUp() - Naikkan elemen ke atas sehingga heap property terpenuhi
     * 
     * CARA KERJA:
     * 1. Mula dari index yang diberikan (biasanya index terakhir)
     * 2. Bandingkan dengan parent: (index - 1) / 2
     * 3. Jika elemen current < parent, swap
     * 4. Naik ke parent dan ulang
     * 5. Berhenti bila current >= parent atau sudah sampai root
     * 
     * VISUALISASI:
     * 
     * Sebelum bubbleUp (index 2, value 1):
     *     [3]        [3]
     *    /   \  →   /   \
     *  [5]   [1]  [1]   [5]
     * 
     * 1 < 3? Ya, swap:
     *     [1]        [1]
     *    /   \  →   /   \
     *  [5]   [3]  [5]   [3]  (selesai)
     * 
     * @param index - Index elemen yang hendak di-bubble-up
     */
    private void bubbleUp(int index) {
        int current = index;  // Mula dari index yang diberikan
        
        // Loop selagi belum sampai root (index > 0)
        while (current > 0) {
            // Kira index parent: (current - 1) / 2
            int parent = (current - 1) / 2;
            
            /**
             * Jika nilai current LEBIH KECIL dari parent,
             * heap property dilanggar, perlu swap
             */
            if (heap.get(current) < heap.get(parent)) {
                swap(current, parent);   // Tukar tempat
                current = parent;        // Naik ke parent untuk terus check
            } else {
                // Jika current >= parent, heap property OK, berhenti
                break;
            }
        }
    }
    
    /**
     * bubbleDown() - Turunkan elemen ke bawah sehingga heap property terpenuhi
     * 
     * CARA KERJA:
     * 1. Mula dari index yang diberikan (biasanya index 0 selepas extractMin)
     * 2. Bandingkan dengan child kiri dan kanan
     * 3. Cari child yang paling kecil
     * 4. Jika child terkecil < current, swap
     * 5. Turun ke child tersebut dan ulang
     * 6. Berhenti bila current <= kedua-dua child atau sudah sampai leaf
     * 
     * VISUALISASI:
     * 
     * Sebelum bubbleDown (root = 8):
     *       [8]           [3]
     *      /   \    →    /   \
     *    [3]   [5]     [8]   [5]
     * 
     * 8 > 3? Ya, swap:
     *       [3]           [3]
     *      /   \    →    /   \
     *    [8]   [5]     [8]   [5]  (8 < 5? Tidak, berhenti)
     * 
     * @param index - Index elemen yang hendak di-bubble-down
     */
    private void bubbleDown(int index) {
        int current = index;  // Mula dari index yang diberikan
        
        // Loop sehingga tidak ada swap lagi
        while (true) {
            // Kira index child kiri dan kanan
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int smallest = current;  // Anggap current adalah yang terkecil
            
            /**
             * Bandingkan dengan left child
             * Syarat: left < heap.size() (pastikan left child wujud)
             */
            if (left < heap.size() && heap.get(left) < heap.get(smallest)) {
                smallest = left;  // Left child lebih kecil
            }
            
            /**
             * Bandingkan dengan right child
             * Syarat: right < heap.size() (pastikan right child wujud)
             */
            if (right < heap.size() && heap.get(right) < heap.get(smallest)) {
                smallest = right;  // Right child lebih kecil
            }
            
            /**
             * Jika smallest == current, bermakna current sudah lebih kecil
             * dari kedua-dua child, heap property OK, berhenti
             */
            if (smallest != current) {
                // Tukar current dengan child yang lebih kecil
                swap(current, smallest);
                current = smallest;  // Turun ke child tersebut
            } else {
                // Heap property sudah OK
                break;
            }
        }
    }
    
    /**
     * swap() - Tukar dua elemen dalam heap
     * 
     * @param i - Index element pertama
     * @param j - Index element kedua
     */
    private void swap(int i, int j) {
        int temp = heap.get(i);   // Simpan nilai i
        heap.set(i, heap.get(j)); // Letak nilai j ke i
        heap.set(j, temp);        // Letak nilai yang disimpan ke j
    }
}