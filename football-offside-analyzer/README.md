# Football Offside Analyzer

This project is designed to analyze football offside situations from a top view using OpenCV. It provides a graphical user interface (GUI) for loading images and processing them to determine offside positions based on player and ball locations.

## Project Structure

```
football-offside-analyzer
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── pann
│   │   │   │   ├── OpenCVSwingFrame.java
│   │   │   │   └── Main.java
│   │   └── resources
│   └── test
│       ├── java
│       └── resources
├── lib
│   └── opencv-<version>.jar
├── .gitignore
├── build.gradle
└── README.md
```

## Requirements

- Java Development Kit (JDK)
- OpenCV library (included in the `lib` directory)

## Setup Instructions

1. **Clone the Repository**: 
   ```
   git clone <repository-url>
   cd football-offside-analyzer
   ```

2. **Open the Project**: Open the project in your preferred IDE.

3. **Add OpenCV Library**: Ensure that the OpenCV library (`opencv-<version>.jar`) is correctly placed in the `lib` directory.

4. **Build the Project**: Use Gradle to build the project. Run the following command in the project root:
   ```
   ./gradlew build
   ```

5. **Run the Application**: Execute the `Main` class to start the application.

## Features

- **Load and Display Image**: Users can load a football field image from their file system.
- **Analyze Offside Situations**: The application processes the loaded image to identify players and the ball, determining offside positions.
- **Team Identification**: The application can identify players' teams based on color.
- **Possession Check**: It checks which team has possession of the ball.
- **Last Defender Identification**: The application identifies the last defender of the defending team.
- **Offside Detection**: It checks if any players are in an offside position and marks them accordingly.

## Usage

1. Launch the application.
2. Click on "Charger une Image" to load a football field image.
3. Use additional buttons to trigger analysis functions such as TeamChecker, TeamPossession, SensChecker, LastDefender, and Offside.

## Testing

Unit tests can be added in the `src/test/java` directory to validate the functionality of the methods implemented in the application.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.