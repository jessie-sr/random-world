# Random World

## Overview

This project is dedicated to build a 2D tile-based world exploration engine, representing a comprehensive software engineering effort that involves ideation, design, implementation, and presentation. It emphasizes the development of a pseudorandomly generated world with distinct rooms and hallways, interactive features, and persistent game states.

### Key Features

- **Tile-Based World**: A grid of tiles for the creation of diverse worlds.
- **Random World Generation**: Distinct rooms and hallways generated with pseudorandom algorithms.
- **Interactive Avatar**: Control an avatar with W, A, S, and D keys for exploration.
- **Persistent Game States**: Save and load game states, maintaining the continuity of the game experience.
- **User Interface**: An engaging user interface with a Heads Up Display (HUD).
- **Additional Features**: Lighting effects, multiple save slots, and customizable avatar appearances.

## Getting Started

### Running the Program

- Start the program by running the `Main` class.
- Use command line arguments for non-interactive mode (`-s inputString`) or no arguments for interactive mode.

### Interactive Mode

- **Main Menu**: Navigate using N (New World), L (Load), and Q (Quit).
- **New World**: Enter a seed and press S to generate a world.
- **Exploration**: Use W, A, S, and D for movement. Press “:Q” to save and quit.

### Non-Interactive Mode

- Provide an input string following `-s` flag to generate a world.
- Example: `java Main -s "N3412S"` to start a game with seed 3412.

## Features

### World Generation

- The world consists of a 2D grid, pseudorandomly generated with random rooms, hallways, and outdoor spaces.
- Each run generates a substantially different world, ensuring a unique experience.

### Interactivity

- Control an avatar to explore the world, with deterministic behaviors based on the seed.
- Real-time mechanics are not used, ensuring consistent experiences across different sessions.

### UI and HUD

- After entering a seed, the world is displayed along with a HUD.
- The HUD shows useful information, like descriptions of tiles under the mouse pointer.

### Saving and Loading

- The game state can be saved with “:Q” and loaded with the L option in the Main Menu.
- The saved state includes the random number generator's state for consistent experiences.

### Advanced Features

- **Lighting Effects**: Toggle light sources on and off for dynamic world rendering.
- **Multiple Save Slots**: Additional save slots accessible via a new menu option.
- **Instant World Generation**: Create a new world without restarting the program by pressing “o”.
- **Customizable Avatar**: Change the avatar's appearance through a menu option.

## Technical Details

- The project uses `byow.TileEngine` for rendering and `byow.Core` for core functionalities.
- `RandomUtils.java` and `persistenceUtils.java` are used for randomness and file operations, respectively.
- The program is built to meet all requirements and restrictions outlined in the project spec, including the Ambition categories.

## Contributing

This project is part of an academic course and is not open for external contributions. However, feedback and suggestions are welcome.

## Acknowledgments

Special thanks to the 61BL course staff and contributors who provided guidance and resources for this project.

---

Enjoy exploring the Random World!
