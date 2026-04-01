🎯 UPTM Marketing Campaign Optimization System (MCOP)

This project solves the Traveling Salesman Problem (TSP) variant for UPTM's international marketing campaign. The system finds optimal routes to visit four locations (UPTM Headquarters, City B Education Fair, City C Recruitment Hub, City D Partner Institution) while minimizing travel costs.

✨ Features:
- 4 Different TSP Algorithms (Greedy, Dynamic Programming, Backtracking, Divide & Conquer)
- Performance Tracking & Comparison
- Min-Heap & Splay Tree Implementation
- CSV Export for Results Analysis
- Professional Package Structure

📊 Algorithms Implemented:
1. Greedy (Nearest Neighbor + Multi-Start)
2. Dynamic Programming (Held-Karp)
3. Backtracking (Branch & Bound)
4. Divide & Conquer

📦 Data Structures:
- Min-Heap (Priority Queue)
- Splay Tree (Self-adjusting BST)

🔧 Technologies:
- Java 11+
- Object-Oriented Design
- Strategy Pattern
- Performance Metrics Logging

👥 Group Members:
- Hakim (Coding Lead)
- Aqwwa
- Aijaz
- Erfan
- Aiman

📅 Course: SWC3524/SWC4423 - Algorithmic Data Structures
🏫 University: Universiti Poly-Tech Malaysia (UPTM)

# 🎯 UPTM Marketing Campaign Optimization System (MCOP)

![Java](https://img.shields.io/badge/Java-11%2B-orange)
![License](https://img.shields.io/badge/License-MIT-green)
![Version](https://img.shields.io/badge/Version-2.0-blue)
![Build](https://img.shields.io/badge/Build-Passing-brightgreen)

## 📌 Project Overview

The **Marketing Campaign Optimization Problem (MCOP)** is a real-world challenge where a marketing team must visit a set of international locations, conducting promotional activities at each destination exactly once, and return to the starting location while minimizing the total cost (travel distance, time, or budget).

This project implements multiple algorithmic approaches to solve the MCOP for **Universiti Poly-Tech Malaysia (UPTM)** with 4 target locations:

| Location | Description |
|----------|-------------|
| UPTM | Headquarters (Start/End Point) |
| City B | Education Fair |
| City C | Recruitment Hub |
| City D | Partner Institution |

---

## 🚀 Features

| Feature | Description |
|---------|-------------|
| **4 TSP Algorithms** | Greedy, Dynamic Programming, Backtracking, Divide & Conquer |
| **Performance Tracking** | Execution time measurement for each algorithm |
| **Result Comparison** | Automatic comparison table with optimality check |
| **CSV Export** | Export results to CSV for further analysis |
| **Min-Heap** | Priority queue implementation for campaign scheduling |
| **Splay Tree** | Self-adjusting BST for student database lookup |
| **Professional Structure** | Multi-package architecture with Strategy Pattern |

---

## 📊 Cost Matrix

The following cost matrix represents travel costs between locations (in arbitrary units):

| Location | UPTM | City B | City C | City D |
|----------|------|--------|--------|--------|
| **UPTM** | 0 | 15 | 25 | 35 |
| **City B** | 15 | 0 | 30 | 28 |
| **City C** | 25 | 30 | 0 | 20 |
| **City D** | 35 | 28 | 20 | 0 |

---

## 🧠 Algorithms Implemented

### 1. Greedy Algorithm (Nearest Neighbor + Multi-Start)
- **Time Complexity:** O(n²)
- **Space Complexity:** O(n)
- **Approach:** Always choose the nearest unvisited city
- **Optimization:** Tries starting from all cities for better results

### 2. Dynamic Programming (Held-Karp)
- **Time Complexity:** O(n²·2ⁿ)
- **Space Complexity:** O(n·2ⁿ)
- **Approach:** Bitmasking with memoization
- **Guarantee:** Always finds optimal solution

### 3. Backtracking (Branch & Bound)
- **Time Complexity:** O(n!) worst case
- **Space Complexity:** O(n)
- **Approach:** Exhaustive search with pruning
- **Guarantee:** Always finds optimal solution

### 4. Divide & Conquer
- **Time Complexity:** O(n²)
- **Space Complexity:** O(n)
- **Approach:** Recursively finds nearest neighbor
- **Guarantee:** Fast but not optimal guaranteed

---

## 📦 Project Structure
UPTM_MCOP_Project/
│
├── src/
│   ├── Main.java
│   │
│   ├── core/
│   │   ├── MCOPSolver.java
│   │   └── MCOPResult.java
│   │
│   ├── algorithms/
│   │   ├── GreedySolver.java
│   │   ├── DynamicProgrammingSolver.java
│   │   ├── BacktrackingSolver.java
│   │   └── DivideAndConquerSolver.java
│   │
│   ├── datastructures/
│   │   ├── MinHeap.java
│   │   └── SplayTree.java
│   │
│   └── utils/
│       ├── SortSearchUtils.java
│       └── PerformanceLogger.java
│
├── results.csv
├── README.md
└── .gitignore


---

## 🛠️ Prerequisites

| Requirement | Version |
|-------------|---------|
| Java JDK | 11 or higher |
| IDE | Eclipse / IntelliJ IDEA / NetBeans |
| Git | (Optional, for cloning) |

---

## ⚙️ Installation & Setup

### Clone the Repository

```bash
git clone https://github.com/MohdHakimFz/UPTM-MCOP-Optimization-System.git
cd UPTM-MCOP-Optimization-System

Compile the Program
javac -d bin src/**/*.java

java -cp bin src.Main
