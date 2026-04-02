package src.utils;

/**
 * SORT SEARCH UTILS - Utiliti untuk operasi Sorting dan Searching
 *
 * APA ITU SortSearchUtils?
 * Class ini menyediakan dua fungsi penting dalam sains komputer:
 * 1. insertionSort() - Susun array dari kecil ke besar
 * 2. binarySearch()  - Cari nilai dalam array yang sudah disusun
 *
 * KENAPA SORTING DAN SEARCHING PENTING DALAM MCOP?
 * - Sorting: Susun kos perjalanan untuk analisis dan paparan
 * - Searching: Cari kos tertentu dengan cepat selepas data disusun
 *
 * NOTA REKA BENTUK:
 * Semua method dalam class ini adalah static.
 * Ini bermaksud kita boleh panggil terus tanpa cipta object:
 *   SortSearchUtils.insertionSort(arr);   // OK - static
 *   SortSearchUtils.binarySearch(arr, 5); // OK - static
 *
 * @author Hakim & Group
 */
public class SortSearchUtils {

    // ==================== METHOD 1: INSERTION SORT ====================

    /**
     * insertionSort() - Susun array integer dari nilai terkecil ke terbesar
     *
     * APA ITU INSERTION SORT?
     * Insertion Sort berfungsi seperti kita menyusun kad dalam tangan:
     * - Ambil satu kad pada satu masa
     * - Sisipkan kad itu ke tempat yang betul dalam set yang sudah tersusun
     * - Ulang sehingga semua kad tersusun
     *
     * CONTOH LANGKAH DEMI LANGKAH:
     *
     * Array asal: [8, 3, 5, 1, 9, 2]
     *
     * i=1, key=3:  [8, _, 5, 1, 9, 2] → 8 > 3, geser → [_, 8, 5, 1, 9, 2] → sisip 3 → [3, 8, 5, 1, 9, 2]
     * i=2, key=5:  [3, 8, _, 1, 9, 2] → 8 > 5, geser → [3, _, 8, 1, 9, 2] → sisip 5 → [3, 5, 8, 1, 9, 2]
     * i=3, key=1:  → geser 8, 5, 3 → sisip 1 → [1, 3, 5, 8, 9, 2]
     * i=4, key=9:  → 8 < 9, terus → [1, 3, 5, 8, 9, 2]
     * i=5, key=2:  → geser 9, 8, 5, 3 → sisip 2 → [1, 2, 3, 5, 8, 9]
     *
     * KOMPLEKSITI MASA:
     * - Kes terbaik  (sudah tersusun):   O(n)    - hanya satu pass
     * - Kes purata (rawak):              O(n²)   - dua gelung bersarang
     * - Kes terburuk (terbalik):         O(n²)   - setiap elemen kena geser penuh
     *
     * KOMPLEKSITI RUANG: O(1) - tidak perlukan array tambahan
     *
     * KELEBIHAN:
     * - Mudah difahami dan dilaksanakan
     * - Cekap untuk data yang hampir tersusun
     * - In-place (tidak perlukan memori tambahan)
     * - Stable sort (elemen yang sama kekal susunannya)
     *
     * KEKURANGAN:
     * - Lambat untuk data yang besar (O(n²))
     * - Tidak sesuai untuk n > 1000 berbanding merge sort atau quick sort
     *
     * @param arr - Array integer yang hendak disusun (diubah secara terus / in-place)
     * @return Array yang sama (sudah disusun dari kecil ke besar)
     *
     * NOTA: Array asal akan diubah secara terus (pass by reference dalam Java)
     */
    public static int[] insertionSort(int[] arr) {

        // VALIDATION: Semak sama ada array sah untuk diproses
        // Kalau null atau saiz <= 1, tak perlu sort, terus return
        if (arr == null || arr.length <= 1) {
            return arr;
        }

        // STEP 1: Dapatkan saiz array
        int n = arr.length;

        // STEP 2: Loop bermula dari elemen KEDUA (index 1)
        // Elemen pertama (index 0) sudah "tersusun" dengan sendirinya
        for (int i = 1; i < n; i++) {

            /**
             * key = elemen yang sedang hendak "disisipkan" ke posisi yang betul
             *
             * Contoh: arr = [3, 8, 5, ...]
             *         i = 2, key = 5
             *         Kita nak sisipkan 5 ke tempat yang betul dalam [3, 8]
             */
            int key = arr[i];

            /**
             * j = index untuk bandingkan key dengan elemen-elemen sebelumnya
             *
             * Mula dari i-1 (elemen sebelum key) dan gerak ke kiri
             */
            int j = i - 1;

            /**
             * INNER LOOP: Geser elemen-elemen yang LEBIH BESAR dari key ke kanan
             *
             * Syarat gelung:
             * - j >= 0 : belum keluar dari sempadan array (kiri)
             * - arr[j] > key : elemen ini lebih besar dari key, perlu digeser
             *
             * Contoh: arr = [3, 8, _, 1, 9, 2], key = 5, j = 1
             * arr[1] = 8 > 5? Ya → arr[2] = arr[1] = 8, j-- → j = 0
             * arr[0] = 3 > 5? Tidak → stop
             * Sisip key: arr[1] = 5 → [3, 5, 8, 1, 9, 2]
             */
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];  // Geser elemen ke kanan
                j--;                  // Gerak ke kiri
            }

            /**
             * STEP 3: Sisipkan key ke posisi yang betul
             *
             * Selepas gelung, j+1 adalah posisi yang betul untuk key
             * kerana arr[j] <= key (atau j = -1, bermaksud key paling kecil)
             */
            arr[j + 1] = key;
        }

        // Return array yang sudah disusun
        return arr;
    }

    // ==================== METHOD 2: BINARY SEARCH ====================

    /**
     * binarySearch() - Cari nilai dalam array yang SUDAH DISUSUN
     *
     * APA ITU BINARY SEARCH?
     * Binary Search berfungsi seperti kita mencari nama dalam buku telefon:
     * - Buka di TENGAH buku
     * - Kalau nama yang dicari ada sebelum tengah, cari di separuh KIRI
     * - Kalau nama yang dicari ada selepas tengah, cari di separuh KANAN
     * - Ulang sehingga jumpa atau habis
     *
     * SYARAT PENTING: Array MESTI sudah disusun (sorted) dahulu!
     * Gunakan insertionSort() dahulu sebelum binarySearch().
     *
     * CONTOH LANGKAH DEMI LANGKAH:
     *
     * Array: [1, 2, 3, 5, 8, 9], target = 5
     *
     * Step 1: left=0, right=5, mid=2 → arr[2]=3 < 5 → cari kanan → left=3
     * Step 2: left=3, right=5, mid=4 → arr[4]=8 > 5 → cari kiri → right=3
     * Step 3: left=3, right=3, mid=3 → arr[3]=5 == 5 → JUMPA! return 3
     *
     * KENAPA CEPAT?
     * Setiap langkah, ruang carian dikurangkan SEPARUH.
     * Untuk 1,000,000 elemen: hanya perlu ~20 langkah sahaja!
     *
     * KOMPLEKSITI MASA:
     * - Kes terbaik  (ada di tengah):  O(1)      - jumpa terus pada step pertama
     * - Kes purata:                    O(log n)  - ruang carian dibahagi 2 setiap step
     * - Kes terburuk (tidak ada):      O(log n)  - perlu habiskan semua pembahagian
     *
     * KOMPLEKSITI RUANG: O(1) - tidak perlukan memori tambahan
     *
     * KELEBIHAN:
     * - Sangat cepat: O(log n) berbanding Linear Search O(n)
     * - Cekap untuk data yang besar
     *
     * KEKURANGAN:
     * - Array MESTI disusun dahulu (ada overhead sorting)
     * - Tidak sesuai untuk data yang selalu berubah
     *
     * @param arr    - Array integer yang SUDAH DISUSUN (sorted ascending)
     * @param target - Nilai yang hendak dicari
     * @return Index target dalam array, atau -1 jika tidak dijumpai
     *
     * NOTA: Method ini menggunakan formula mid = left + (right - left) / 2
     * dan BUKAN (left + right) / 2 untuk elakkan integer overflow apabila
     * left + right melebihi Integer.MAX_VALUE.
     */
    public static int binarySearch(int[] arr, int target) {

        // VALIDATION: Semak sama ada array sah untuk dicari
        // Kalau null atau kosong, terus return -1 (tidak jumpa)
        if (arr == null || arr.length == 0) {
            return -1;
        }

        // STEP 1: Tetapkan sempadan carian awal
        int left = 0;               // Sempadan kiri (index pertama)
        int right = arr.length - 1; // Sempadan kanan (index terakhir)

        // STEP 2: Loop selagi sempadan masih sah (left <= right)
        // Kalau left > right, bermaksud target tidak ada dalam array
        while (left <= right) {

            /**
             * STEP 3: Kira index tengah
             *
             * Formula: left + (right - left) / 2
             *
             * Kenapa bukan (left + right) / 2?
             * Sebab (left + right) boleh OVERFLOW jika kedua-dua nilai besar!
             * Contoh: left = 2,000,000,000, right = 2,000,000,001
             *         left + right = 4,000,000,001 → melebihi Integer.MAX_VALUE (2,147,483,647)
             *         Ini akan menyebabkan integer overflow dan hasil yang salah!
             *
             * Formula yang betul: left + (right - left) / 2
             *         = 2,000,000,000 + (1) / 2
             *         = 2,000,000,000 (selamat!)
             */
            int mid = left + (right - left) / 2;

            // STEP 4: Bandingkan elemen tengah dengan target

            if (arr[mid] == target) {
                // JUMPA! Return index tengah
                return mid;

            } else if (arr[mid] < target) {
                /**
                 * arr[mid] < target:
                 * Target ada di separuh KANAN, abaikan separuh kiri
                 * Pindahkan sempadan kiri ke mid + 1
                 *
                 * Contoh: arr[mid]=3, target=5 → 3 < 5, cari di kanan
                 *         left = mid + 1
                 */
                left = mid + 1;

            } else {
                /**
                 * arr[mid] > target:
                 * Target ada di separuh KIRI, abaikan separuh kanan
                 * Pindahkan sempadan kanan ke mid - 1
                 *
                 * Contoh: arr[mid]=8, target=5 → 8 > 5, cari di kiri
                 *         right = mid - 1
                 */
                right = mid - 1;
            }
        }

        // STEP 5: Loop tamat tanpa jumpa target → return -1
        return -1;
    }
}