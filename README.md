<p align="center">
  <img src="https://img.icons8.com/tetris" alt="Tetris Logo" />
</p>

<h1 align="center">🧩 Sehes_TetrisEngine</h1>

<p align="center">
  <strong>A high-performance, modular Tetris engine built with Java & Swing.</strong><br />
  Designed as a technical foundation for a future <b>Tetris-ARPG hybrid</b>.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=java&logoColor=white" alt="Java Version" />
  <img src="https://img.shields.io/badge/Maven-0.9-blue?style=for-the-badge&logo=apache-maven&logoColor=white" alt="Maven Version" />
  <img src="https://img.shields.io/badge/License-Open--Source-brightgreen?style=for-the-badge" alt="License" />
  <img src="https://img.shields.io/badge/Standard-SRS-orange?style=for-the-badge" alt="SRS Standard" />
</p>

---

## 📖 Overview

**SimpleTetris** is more than just a clone; it is a robust implementation focusing on clean OOP principles and game architecture. By strictly adhering to the **Official Tetris Guidelines** (including the **Super Rotation System**), this engine provides a smooth and competitive gameplay experience.

The ultimate goal is to evolve this project into a **Tetris-ARPG**, introducing progression, skill trees, and monster-battling mechanics through row clears.

---

## ✨ Key Features

- 🏗️ **Standard Rotation System (SRS):** Full implementation of wall-kicks and rotation logic.
- ⚡ **Responsive Input:** Low-latency keyboard handling using Swing Key Bindings.
- 📐 **Modular Architecture:** Clean separation of Model, View, and Controller (MVC).
- 🧩 **Standard Tetrominoes:** All 7 official shapes (I, J, L, O, S, T, Z) with correct colors.
- 📊 **Scoring System:** Basic scoring to track your performance.
- ⏸️ **Game Flow:** Integrated Pause and Game Over states.

---

## 🎮 Controls

| Action | Input Key | Icon |
| :--- | :--- | :---: |
| **Start / Pause / Restart** | `Enter` | ↵ |
| **Move Left** | `Left Arrow` | ⬅️ |
| **Move Right** | `Right Arrow` | ➡️ |
| **Rotate Clockwise** | `Up Arrow` | ⬆️ |
| **Rotate Counter-Clockwise** | `A` Key | 🅰️ |
| **Soft Drop** | `Down Arrow` | ⬇️ |

---

## 🗺️ Roadmap

The development journey from a core engine to an ARPG-ready platform.

| ✅ Completed Steps | 🚀 Future & Current Goals |
| :--- | :--- |
| **Core Mechanics** | **Enhanced Gameplay** |
| ✔️ SRS Wall-kick system | ⬜ Ghost piece (Visual guide) |
| ✔️ All 7 standard tetrominoes | ⬜ Hard drop implementation |
| ✔️ Collision detection (Walls & Pieces) | ⬜ Preview window for next pieces |
| ✔️ Basic scoring logic | ⬜ Auto-increasing difficulty (Gravity) |
| ✔️ Simple Pause / Game Over system | ⬜ Advanced Scoring (T-Spins, Combos) |
| **Technical Foundation** | **Menus & UI** |
| ✔️ Keyboard input handling | ⬜ Main Menu & Options |
| ✔️ Board state & Active tracking | ⬜ About & Credits screen |
| ✔️ Basic Shape Rendering | ⬜ Local Saving (High scores) |
| ✔️ Game loop (Timer-based) | ⬜ Professional Sprites & Visual Effects |
| ✔️ Moving & Redrawing logic | ⬜ Separated Threads (Logic vs Render) |
| **ARPG Vision (Planned)** | **Multiplayer & AI** |
| 🚧 Modular Refactoring (In Progress) | ⬜ Local & Network Multiplayer |
| | ⬜ Simple AI Opponent |
| | ⬜ **ARPG:** Monsters, Bosses, Skill Trees |

---

## 🛠️ Technical Focus

*   **Scalability:** Refactoring the engine to use modular composition, allowing for the easy addition of "ARPG" features like skill trees and power-ups.
*   **Accuracy:** Ensuring all rotations, wall-kicks, and lock-out timings strictly follow official Tetris guidelines.
*   **Performance:** Moving toward a multi-threaded architecture to ensure smooth 60FPS rendering independent of game logic calculations.

---

## 🚀 How to Run

### 📋 Prerequisites
*   **Java 21** or higher.
*   **Maven** (optional, for building from source).

### ⚙️ Execution
1.  **Clone the Repo:**
    ```bash
    git clone https://github.com/your-username/JavaSwingTetris.git
    ```
2.  **Build with Maven:**
    ```bash
    mvn clean package
    ```
3.  **Run the JAR:**
    ```bash
    java -jar target/TetrisEngine.jar
    ```
    *Alternatively, run `org.sehes.tetris.Main` directly from your IDE.*

---

## 🛡️ License

This project is currently available as **Open Source**. Feel free to explore, learn, and contribute!

---
<p align="center">
  Developed by <b>sehes</b>
</p>
