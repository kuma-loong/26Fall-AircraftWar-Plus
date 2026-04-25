# Aircraft War
[English](README.md) | [简体中文](README_zh.md)

This is a classic comprehensive laboratory project for the "Software Construction" course at Harbin Institute of Technology (Shenzhen). This project is a vertically scrolling shooter game developed using Java and the Swing framework.

The main objective of the project is to iteratively develop a complete game from scratch, deeply understanding and applying **Object-Oriented Programming** principles, **Classic Design Patterns**, **File I/O and DAO Architecture**, **Java Multithreading**, and **Graphical User Interface (GUI) Design** in practice.

## 🛠 Tech Stack
- **Languages**: Java (JDK 25)
- **GUI Framework**: Java Swing
- **Testing Framework**: JUnit 5
- **Environment**: macOS / Windows / Linux

## 🚀 Iteration Milestones (Lab Features)

This project adopts an agile iterative approach for development and refactoring through laboratory stages. The following are the core design patterns and features implemented in each lab stage:

### Lab 1: Object-Oriented Basics & Singleton Pattern
- Built the basic game main loop and collision detection mechanism.
- Introduced the **Singleton Pattern** to ensure the uniqueness of the hero aircraft (`HeroAircraft`), using Double-Checked Locking (DCL) to ensure thread safety.
- Implemented basic interactions for normal enemies, elite enemies, as well as health and firepower items.

### Lab 2: Factory Pattern
- Applied the **Factory Method Pattern** to decouple the instantiation process of various enemy aircraft, supporting the expansion of different types of enemies (Elite, ElitePlus, Ace, Boss).
- Applied the **Simple Factory Pattern** combined with a strategy drop pool to randomly generate various buff items.

### Lab 3: Strategy Pattern
- Refactored the shooting trajectory system for the hero aircraft and enemy aircraft using the **Strategy Pattern**.
- Implemented multiple trajectory strategies such as straight shooting, multi-line scatter shooting, and Boss's circular shooting. Actively switched firepower at runtime by holding the `ShootStrategy` interface.
- Introduced the Boss mechanism, triggering powerful Bosses and multiple supply drop logic based on the score.

### Lab 4: DAO Pattern & Unit Testing
- Built a local leaderboard system based on TSV text files.
- Adopted the **Data Access Object (DAO) Pattern** to separate business logic (ranking, display) from underlying read/write operations, implementing a file storage mechanism separated by difficulty (Easy/Normal/Hard).
- Introduced JUnit 5, comprehensively covering unit testing for core entity methods (e.g., relocation, bullet generation) and the local DAO read/write chain.

### Lab 5: Swing GUI & Multithreading
- Used `CardLayout` to connect the full-link game interfaces: **Difficulty Selection Main Menu -> Game Main Loop -> Settlement Name Input Popup -> Score Leaderboard Display**.
- Implemented MVC rendering for the leaderboard based on `JTable`, supporting safe deletion synchronization of selected records.
- Introduced **Multithreading Mechanisms**:
  - Audio thread (`Thread`): Implemented BGM looping, seamless switching of Boss exclusive BGM, and asynchronous support for triggering corresponding action sound effects.
  - Timing thread (`Runnable`): Implemented a limited-time effect mechanism for firepower enhancement items, automatically and safely restoring the hero aircraft's trajectory after a triggered period.

## 🎮 Running Instructions

This project is natively adapted to the VS Code development environment. If you open it in VS Code, you can directly use the commands defined in `.vscode/tasks.json`:

1. **Compile Source**:
   Run the `Compile Source` task directly in the command palette or task list.
   *(Or execute manually in the terminal: `javac -encoding UTF-8 -d out/production $(find src/edu/hitsz -name "*.java")` )*

2. **Start Game**:
   Run the `Run Game` task directly, or execute `java -cp out/production edu.hitsz.application.Main` in the terminal.

3. **Run Unit Tests**:
   Run `Compile Tests` first, then execute `Run JUnit5 Tests`.

## 📝 Directory Structure Overview

- `src/edu/hitsz/application`: System startup entry, main scheduling panel, and UI (Frame/Panels) definitions.
- `src/edu/hitsz/aircraft`: Definition of the hero aircraft and all enemy aircraft.
- `src/edu/hitsz/bullet` & `src/edu/hitsz/item`: Abstraction and concrete implementation of ammunition and items.
- `src/edu/hitsz/shootstrategy`: Trajectory algorithm clusters of the strategy pattern.
- `src/edu/hitsz/factory`: Factory collections responsible for the creation of various entities.
- `src/edu/hitsz/dao`: Design of data read/write and leaderboard storage chains.
- `test/`: Unit test source code.
- `docs/`: Detailed workflow records, design explanations, and pitfall notes for each experiment.