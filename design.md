# BYOW Design Document

## Classes and Data Structures

### Class 1: `RoomGenerator`

#### Overview

Responsible for creating and managing rooms within a 2D virtual world.

#### Fields

- `private Random random`: For creating rooms with varying dimensions.
- `private int numRooms`: Total number of rooms to generate.
- `private int minWidth, maxWidth, minHeight, maxHeight`: Range of room dimensions.
- `public int playerX, playerY`: Player's position.
- `public Room closestRoom`: Nearest room to the player.
- `public int[][] board`: Game board representation.
- `public TETile[][] world`: Visual state of the game world.
- `public TETile userAppearance`: User's avatar tile.
- `public boolean isLightOn`: Lighting effect status.
- `public static TETile[] appearanceBindings`: Array of user appearance options.
- `private Map<Integer, Room> roomMap`: Mapping of room IDs to `Room` objects.

#### Constructors

- `public RoomGenerator(TETile[][] world, long seed)`: For pre-existing world and seed.
- `public RoomGenerator(long seed)`: For new world grid and seed.

#### Methods

- `public void initUserPosition()`: Initializes player position.
- `public void updateUserPosition()`: Updates player position.
- `public void changeUserAppearance(int option)`: Changes player appearance.
- `public void drawRooms()`: Draws rooms on the world.
- `public boolean changeWall(int i, int j)`: Changes wall tiles.
- `public void generateRooms()`: Generates rooms.
- `private boolean isAreaAvailable(int x, int y, int width, int height)`: Checks for room placement availability.
- `private void occupyArea(int x, int y, int width, int height)`: Marks area as occupied by a room.
- `public void connectRooms()`: Connects all rooms with hallways.
- `private void generateHallway(int x1, int y1, int x2, int y2)`: Generates a hallway.
- `public void lightOn()`: Activates lighting effects.
- `public void lightOff()`: Deactivates lighting effects.

### Class 2: `Room`

#### Overview

Represents individual rooms in the game world.

#### Fields

- `private int id`: Room's unique identifier.
- `private int width, height`: Room dimensions.
- `private int x, y`: Top-left corner coordinates of the room.
- `private Light light`: Lighting in the room.
- `private TETile[][] world`: Reference to the game world.

#### Constructor

- `public Room(TETile[][] world, int id, int width, int height, int x, int y)`: Initializes a room with dimensions and location.

#### Methods

- `public int getX()`: Gets x-coordinate.
- `public int getY()`: Gets y-coordinate.
- `public int getWidth()`: Gets width.
- `public int getHeight()`: Gets height.
- `public void addLight(int[][] board)`: Adds a light source.
- `public Light getLight()`: Retrieves the light object.

### Class 3: `Light`

#### Overview

Manages lighting effects within a room.

#### Fields

- `private int x, y`: Coordinates of the light source.
- `private TETile[][] world`: Reference to the game world.
- `private int[][] board`: Board for light placement.

#### Constructor

- `public Light(TETile[][] world, int[][] board, int x, int y)`: Initializes the light object.

#### Methods

- `public void On()`: Activates light.
- `public void Off()`: Deactivates light.

### Class 4: `DrawFrame`

#### Overview

Handles rendering of graphical interfaces using `edu.princeton.cs.algs4.StdDraw`.

#### Fields

- `public static int WIDTH, HEIGHT`: Frame dimensions.
- `private static File savedWorlds, savedWorlds2`: Save slot files.

#### Methods

- `static void drawInputStringToFrame(String inputSeed)`: Displays input seed.
- `public void drawMouse(int x, int y)`: Shows mouse position.
- `static void drawSavedSubFrame()`: Renders save slots status.
- `static void drawAppearanceSelection()`: Renders avatar selection.
- `public static void drawFrame()`: Renders the main game frame.

### Class 5: `Engine`

#### Overview

Central class for managing the game loop and processing inputs.

#### Fields

- `public static final int WIDTH, HEIGHT`: World dimensions.
- `private static final Color DEFAULTCOLOR`: Default GUI color.
- `private static String[] boardToWorldMap`: Board to world mapping.
- `private static int GUINUM`: Number of GUI elements.
- `TERenderer teRender`: Renderer object.
- `private boolean gameOver`: Game over status.
- `private long seed`: World generation seed.
- `private RoomGenerator currGenerator`: Room generator instance.
- `private TETile[][] backWorld`: World representation.
- `private TETile[] GUI`: GUI elements.
- `private int prevMouseX, prevMouseY`: Previous mouse coordinates.
- `private Character preKeyPress`: Previous key pressed.
- `private File savedWorlds, savedWorlds2`: Save files.

#### Constructor

- `public Engine()`: Initializes engine with GUI elements.

#### Methods

- `public static void main(String[] args)`: Main method.
- `public void interactWithKeyboard()`: Handles keyboard interaction.
- `private String keyboardInit()`: Initializes game with keyboard.
- `private void checkKeyBoard(String ch)`: Processes keyboard inputs.
- `private void moveUp(Character w, Character currKey)`: Moves player up.
- `private void moveLeft(Character a, Character currKey)`: Moves player left.
- `private void moveDown(Character s, Character currKey)`: Moves player down.
- `private void moveRight(Character d, Character currKey)`: Moves player right.
- `private void light(Character currKey)`: Toggles game lighting.
- `private void saveTheWorld()`: Saves the game to default slot.
- `private void saveTheWorld2()`: Saves the game to an additional slot.
- `private void setupFiles()`: Sets up save files.
- `public void checkMouse(int mouseX, int mouseY)`: Checks mouse position.
- `private void resumePrevWorld(boolean keyBoardStart)`: Resumes saved game.
- `private void createNewWorld(String inputHistory, String inputSeed, Boolean keyBoardStart)`: Creates new game world.
- `public String solicitSeed()`: Solicits seed from user.
- `public String solicitNCharsInput(int n)`: Solicits character input.
- `public TETile[][] interactWithInputString(String input)`: Processes input string.

### Class `Main`

#### Overview

Entry point of the program, parses command-line inputs.

#### Method

- `public static void main(String[] args)`: Parses arguments and initializes engine.

### Other assistant classes:

- `persistenceUtils`: Manages file IO and object serialization.
- `RandomUtils`: Generates pseudo-random numbers and performs random operations.

## Algorithms

### `RoomGenerator`

- Generates a specified number of rooms with random dimensions and locations.
- Connects rooms with hallways using `generateHallway`.
- Implements lighting effects that can be toggled on/off.

### `Room`

- Represents a single room.
- Manages room dimensions and location.
- Handles adding a light source to the room.

### `Light`

- Controls light activation and deactivation.
- Alters the appearance of tiles based on lighting status.

### `DrawFrame`

- Renders different UI frames like main menu, input prompts, and avatar selection.
- Manages display of mouse position and save slots status.

### `Engine`

- Main loop for game interaction and rendering.
- Handles keyboard and mouse inputs.
- Manages game state, including saving and resuming games.
- Processes input strings to replicate keyboard interactions.

### `Main`

- Determines the mode of interaction based on command-line arguments.
- Initializes and starts the game engine.

## Persistence

- Game state saved in `.txt` files.
- Supports multiple save slots.
- Deterministic gameplay allows resuming from saved state.
- `persistenceUtils` used for file IO operations and object serialization.
