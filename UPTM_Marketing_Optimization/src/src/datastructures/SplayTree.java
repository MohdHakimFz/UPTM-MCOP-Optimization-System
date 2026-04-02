package src.datastructures;

/**
 * SPLAY TREE - Struktur data Binary Search Tree yang self-adjusting
 * 
 * APA ITU SPLAY TREE?
 * Splay Tree adalah Binary Search Tree (BST) yang mempunyai keistimewaan:
 * Setiap kali kita mengakses (search/insert) sesuatu node, node tersebut akan
 * "di-splay" (dibawa) ke root (paling atas) menggunakan operasi rotasi.
 * 
 * KENAPA PERLU SPLAY?
 * - Element yang sering diakses akan sentiasa berada dekat dengan root
 * - Ini memberikan performance yang lebih baik untuk akses berulang
 * - Amortized time complexity = O(log n) (purata masa yang baik)
 * 
 * OPERASI SPLAY (Tiga Jenis Rotasi):
 * 1. ZIG: Rotasi single (apabila parent adalah root)
 * 2. ZIG-ZIG: Dua rotasi ke arah yang sama (kiri-kiri atau kanan-kanan)
 * 3. ZIG-ZAG: Dua rotasi ke arah bertentangan (kiri-kanan atau kanan-kiri)
 * 
 * KEGUNAAN DALAM MARKETING:
 * - Database student yang kerap diakses (frequently accessed records)
 * - Cache untuk recent searches (student ID yang baru dicari)
 * - Student registration system (student aktif akan cepat dicari)
 * 
 * @author Hakim & Group
 */
public class SplayTree {
    
    // ==================== INNER CLASS: NODE ====================
    
    /**
     * NODE CLASS - Setiap node dalam Splay Tree
     * 
     * Setiap node mengandungi:
     * - data: nilai yang disimpan (contoh: student ID)
     * - left: rujukan ke child kiri (node dengan nilai lebih kecil)
     * - right: rujukan ke child kanan (node dengan nilai lebih besar)
     * - parent: rujukan ke parent node (penting untuk splaying!)
     */
    private class Node {
        int data;           // Nilai yang disimpan
        Node left;          // Child kiri (nilai lebih kecil)
        Node right;         // Child kanan (nilai lebih besar)
        Node parent;        // Parent node (untuk splay operation)
        
        /**
         * Constructor - Cipta node baru
         * @param data - Nilai untuk node ini
         */
        Node(int data) {
            this.data = data;
            left = null;      // Mula-mula tiada child kiri
            right = null;     // Mula-mula tiada child kanan
            parent = null;    // Mula-mula tiada parent
        }
    }
    
    // ==================== FIELD ====================
    
    /**
     * root - Node paling atas (root) dalam tree
     * 
     * Dalam Splay Tree, root sentiasa berubah selepas setiap operasi splay.
     * Element yang baru diakses akan menjadi root.
     */
    private Node root;
    
    // ==================== CONSTRUCTOR ====================
    
    /**
     * Constructor - Cipta Splay Tree kosong
     */
    public SplayTree() {
        root = null;  // Tree bermula kosong
    }
    
    // ==================== PUBLIC METHODS ====================
    
    /**
     * insert() - Masukkan nilai baru ke dalam tree
     * 
     * CARA KERJA:
     * 1. Cari tempat yang sesuai (mengikut peraturan BST)
     * 2. Masukkan node baru di tempat tersebut
     * 3. SPLAY node baru ke root (supaya cepat diakses nanti)
     * 
     * @param data - Nilai yang hendak dimasukkan
     * 
     * CONTOH:
     * insert(20): tree = [20]
     * insert(10): tree = [20] → [10] di-splay ke root → [10, 20]
     * insert(30): tree = [10, 20] → cari tempat 30 → [10, 20, 30] → splay 30 ke root
     */
    public void insert(int data) {
        // STEP 1: Cipta node baru
        Node newNode = new Node(data);
        
        // STEP 2: Kalau tree kosong, node baru jadi root
        if (root == null) {
            root = newNode;
            return;  // Selesai, tiada splay diperlukan
        }
        
        // STEP 3: Cari tempat untuk masukkan node baru (BST property)
        Node current = root;   // Mula dari root
        Node parent = null;    // Parent akan di-update dalam loop
        
        // Loop untuk cari tempat yang sesuai
        while (current != null) {
            parent = current;  // Simpan parent sebelum turun
            
            // Kalau data lebih kecil, pergi ke kiri
            if (data < current.data) {
                current = current.left;
            } 
            // Kalau data lebih besar atau sama, pergi ke kanan
            else {
                current = current.right;
            }
        }
        
        // STEP 4: Masukkan node baru sebagai child dari parent
        newNode.parent = parent;  // Set parent node baru
        
        // Tentukan sama ada jadi left child atau right child
        if (data < parent.data) {
            parent.left = newNode;   // Jadi left child
        } else {
            parent.right = newNode;  // Jadi right child
        }
        
        // STEP 5: SPLAY node baru ke root!
        // Ini yang membuat Splay Tree istimewa
        splay(newNode);
    }
    
    /**
     * search() - Cari nilai dalam tree
     * 
     * CARA KERJA:
     * 1. Cari node dengan nilai yang dicari (BST search)
     * 2. Jika jumpa, SPLAY node tersebut ke root dan return true
     * 3. Jika tidak jumpa, SPLAY node terakhir yang dikunjungi
     * 
     * @param data - Nilai yang hendak dicari
     * @return true jika jumpa, false jika tidak
     * 
     * CONTOH:
     * tree = [20, 10, 30]
     * search(10): jumpa → splay 10 ke root → [10, 20, 30] → return true
     * search(25): tak jumpa → splay node terakhir (20 atau 30) → return false
     */
    public boolean search(int data) {
        // Kalau tree kosong, terus return false
        if (root == null) {
            return false;
        }
        
        Node current = root;   // Mula dari root
        Node last = null;      // Simpan node terakhir yang dikunjungi
        
        // STEP 1: BST Search - cari node dengan nilai yang dicari
        while (current != null) {
            last = current;  // Simpan node semasa sebagai last
            
            // Kalau jumpa!
            if (data == current.data) {
                // SPLAY node ini ke root (penting!)
                splay(current);
                return true;  // Jumpa!
            } 
            // Kalau data lebih kecil, pergi ke kiri
            else if (data < current.data) {
                current = current.left;
            } 
            // Kalau data lebih besar, pergi ke kanan
            else {
                current = current.right;
            }
        }
        
        // STEP 2: Tak jumpa, tapi tetap splay node terakhir yang dikunjungi
        // Ini membantu untuk akses masa depan (locality of reference)
        if (last != null) {
            splay(last);
        }
        
        return false;  // Tidak jumpa
    }
    
    // ==================== ROTATION METHODS ====================
    
    /**
     * rotateLeft() - Rotasi ke kiri (Left Rotation)
     * 
     * VISUALISASI:
     * 
     * Sebelum rotateLeft(x):        Selepas rotateLeft(x):
     *        x                           y
     *         \                         / \
     *          y         →             x   ?
     *         / \                       \
     *        ?   ?                       ?
     * 
     * @param x - Node yang hendak di-rotate (parent)
     */
    private void rotateLeft(Node x) {
        Node y = x.right;  // y adalah right child dari x
        
        // STEP 1: Anak kiri y menjadi anak kanan x
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;  // Update parent
        }
        
        // STEP 2: y mengambil tempat x
        y.parent = x.parent;
        
        // STEP 3: Update parent x untuk merujuk kepada y
        if (x.parent == null) {
            root = y;  // x adalah root, sekarang y jadi root
        } else if (x == x.parent.left) {
            x.parent.left = y;  // x adalah left child
        } else {
            x.parent.right = y;  // x adalah right child
        }
        
        // STEP 4: x menjadi left child dari y
        y.left = x;
        x.parent = y;
    }
    
    /**
     * rotateRight() - Rotasi ke kanan (Right Rotation)
     * 
     * VISUALISASI:
     * 
     * Sebelum rotateRight(x):       Selepas rotateRight(x):
     *        x                           y
     *       /                           / \
     *      y             →             ?   x
     *     / \                             /
     *    ?   ?                           ?
     * 
     * @param x - Node yang hendak di-rotate (parent)
     */
    private void rotateRight(Node x) {
        Node y = x.left;  // y adalah left child dari x
        
        // STEP 1: Anak kanan y menjadi anak kiri x
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;  // Update parent
        }
        
        // STEP 2: y mengambil tempat x
        y.parent = x.parent;
        
        // STEP 3: Update parent x untuk merujuk kepada y
        if (x.parent == null) {
            root = y;  // x adalah root, sekarang y jadi root
        } else if (x == x.parent.left) {
            x.parent.left = y;  // x adalah left child
        } else {
            x.parent.right = y;  // x adalah right child
        }
        
        // STEP 4: x menjadi right child dari y
        y.right = x;
        x.parent = y;
    }
    
    // ==================== SPLAY METHOD (Yang Paling Penting!) ====================
    
    /**
     * splay() - Operasi utama Splay Tree: bawa node ke root
     * 
     * CARA KERJA:
     * Node x akan dibawa ke root menggunakan gabungan rotasi.
     * Terdapat 3 kes:
     * 
     * 1. ZIG (Single Rotation):
     *    - Apabila parent x adalah root
     *    - Cukup satu rotasi sahaja
     * 
     * 2. ZIG-ZIG (Two Rotations Same Direction):
     *    - Apabila x dan parent berada di arah yang sama
     *    - Contoh: x adalah left child, parent juga left child
     *    - Lakukan rotasi kanan pada grandparent, kemudian rotasi kanan pada parent
     * 
     * 3. ZIG-ZAG (Two Rotations Opposite Direction):
     *    - Apabila x dan parent berada di arah bertentangan
     *    - Contoh: x adalah right child, parent adalah left child
     *    - Lakukan rotasi kiri pada parent, kemudian rotasi kanan pada grandparent
     * 
     * @param x - Node yang hendak di-splay ke root
     */
    private void splay(Node x) {
        // Loop sehingga x menjadi root (parent = null)
        while (x.parent != null) {
            Node parent = x.parent;
            Node grandparent = parent.parent;
            
            // ========== CASE 1: ZIG (Parent adalah Root) ==========
            if (grandparent == null) {
                // Parent adalah root, cukup satu rotasi
                if (x == parent.left) {
                    // x adalah left child → rotate right
                    rotateRight(parent);
                } else {
                    // x adalah right child → rotate left
                    rotateLeft(parent);
                }
            } 
            // ========== CASE 2: ZIG-ZIG (Sama Arah) ==========
            else if (x == parent.left && parent == grandparent.left) {
                // Kedua-dua adalah left child
                // Lakukan dua rotasi kanan
                rotateRight(grandparent);  // Rotasi pada grandparent
                rotateRight(parent);        // Rotasi pada parent
            } 
            // ========== CASE 3: ZIG-ZIG (Sama Arah - Kanan) ==========
            else if (x == parent.right && parent == grandparent.right) {
                // Kedua-dua adalah right child
                // Lakukan dua rotasi kiri
                rotateLeft(grandparent);   // Rotasi pada grandparent
                rotateLeft(parent);         // Rotasi pada parent
            } 
            // ========== CASE 4: ZIG-ZAG (Berlawanan Arah) ==========
            else if (x == parent.right && parent == grandparent.left) {
                // x right child, parent left child
                // Rotasi kiri pada parent, kemudian rotasi kanan pada grandparent
                rotateLeft(parent);          // Rotasi pada parent
                rotateRight(grandparent);    // Rotasi pada grandparent
            } 
            // ========== CASE 5: ZIG-ZAG (Berlawanan Arah - Mirror) ==========
            else {
                // x left child, parent right child
                // Rotasi kanan pada parent, kemudian rotasi kiri pada grandparent
                rotateRight(parent);         // Rotasi pada parent
                rotateLeft(grandparent);     // Rotasi pada grandparent
            }
        }
        // Selepas loop, x adalah root
    }
}