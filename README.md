# Java-Swing Tetris

A robust Java Tetris implementation built with Swing, designed to refine OOP principles and game architecture. This
project follows the official Tetris guidelines and serves as the technical foundation for a future **Tetris-ARPG**
hybrid.

---

## 📝 Description

This project focuses on building a modular, scalable Tetris engine. By implementing the **Standard Rotation System (SRS)
** and a clean separation of concerns, the goal is to create a codebase that can easily transition into an ARPG
featuring skill trees, bonus blocks, and character progression.

---

## 📥 Installation & Running
### Require at least java 21

<p >
  <a href="#">
    <img src="https://img.shields.io/badge/Java-21-red?style=for-the-badge&logo=java" alt="Java Version" />
  </a>
</p>
1. Clone the repository.
2. Compile the source files using `javac` or IDE.
3. Run the `Main` class to start the game.

---

## 🎮 Controls

The controls are designed for responsive play, following the logic of the Official Tetris Guidelines while allowing for
custom counter-clockwise mapping.

| Action                       | Input Key              |
|:-----------------------------|:-----------------------|
| **Start Game / Pause**       | `Enter` ↵              |
| **Restart (Game Over)**      | `Enter` ↵              |
| **Move Left / Right**        | `Left` / `Right` ⬅️ ➡️ |
| **Soft Drop (Faster)**       | `Down` ⬇️              |
| **Rotate Clockwise**         | `Up` ⬆️                |
| **Rotate Counter-Clockwise** | `A` 🅰️                |

---

## 🗺️ Roadmap

The table below tracks the development progress, prioritized from core gameplay mechanics to advanced UI and "
ARPG-ready" systems.

### Order of features doesnt mean order of development

| Current & Future Goals<br/>(High to Low Priority)                               | Finished Steps                                |
|:--------------------------------------------------------------------------------|:----------------------------------------------|
| **- Ghost piece:** Visual guide for piece landing                               | ✅ SRS system for rotation (wall-kicks)        |
| **- Hard drop:**                                                                |                                               |
| **- Preview window:** Display of upcoming pieces                                | ✅Basic scoring system                         |
| **- Auto-increasing difficulty:** Gravity/speed scaling                         | ✅ Implement all standard tetrominoes          |
| **- Advanced scoring:** T-Spins, combos, and Back-to-Back                       | ✅ Proper basic rotation logic                 |
| **- Main menu:** Game entry point and navigation                                | ✅ Moving and redrawing logic in JPanel        |
| **- Options menu:** Custom key rebinding and settings                           | ✅ Collision detection (walls and pieces)      |
| **- About screen:** Project information and credits                             | ✅ Simple game loop timer                      |
| **- Saving system:** Local high scores and user settings                        | ✅ Rendering logic after game start            |
| **- Better graphics:** Custom sprites and polished UI                           | ✅ Keyboard input handling (Key Bindings)      |
| **- Separated Threads:** Decoupling logic from rendering                        | ✅ Basic shape rendering and board layout      |
| **- Modular Refactoring:** Using inheritance/composition                        | ✅ Tracking pieces already placed on the board |
| **- Local multiplayer:** 2-player split-screen                                  | ✅ Active tetromino tracking                   |
| **- Simple AI:** Automated opponent                                             | ✅ simple pause / gameOver system              |
| **- Network multiplayer:** 2-4 player online battles                            |                                               |
| **- Bonus blocks:** Specialized blocks with power Ups                           |                                               |
| **- more ARPG features later on:** **(monsters / stages / skill tree / etc..)** |                                               |


---

## 🛠️ Technical Focus

* **Scalability:** Refactoring the engine to use modular composition, allowing for the easy addition of "ARPG" features
  like skill trees and power-ups.
* **Accuracy:** Ensuring all rotations, wall-kicks, and lock-out timings strictly follow official Tetris guidelines.
* **Performance:** Moving toward a multi-threaded architecture to ensure smooth 60FPS rendering independent of game
  logic calculations.

---

## 🚀 Future Vision: Tetris-ARPG

Once the core competitive Tetris mechanics are perfected, the project will expand into a full **Tetris-ARPG**. This
transition will include:

* **Progression Systems:** Leveling up and unlocking abilities through line clears.
* **Skill Trees:** Modular upgrades that change how blocks behave or provide defensive buffs.
* **Power Ups:** different power Ups appear on tetrominoes through the progression
* **monsters and bosses** by cleaning rows will be fighting against monsters and latter on bosses with different abilities