# JavaLift-ESIEE

JavaLift-ESIEE is a simulation project developed in Java, designed to model and analyze elevator systems. This project aims to provide insights into elevator operations, efficiency, and performance under various conditions.

## Features

- Create and manage multiple elevator systems
- Simulate elevator movements and passenger interactions
- Analyze performance metrics such as wait times and energy consumption
- Working interface usign JavaFX (incomplete)


## Prerequisites

- Java Development Kit (JDK) 24 or higher
- JavaFX SDK (if using the JavaFX interface) support for JDK 25
- Maven with JavaFX support

## Installation

1. Clone the repository or unzip the downloaded project files:

```bash
git clone https://github.com/FLAVl0/JavaLift-ESIEE.git
cd /path/to/JavaLift-ESIEE
```

```bash
unzip JavaLift-ESIEE.zip
cd /path/to/JavaLift-ESIEE
```

2. Build the project using Maven:

```bash
mvn clean install
```

3. Run the simulation:

```bash
mvn javafx:run
```

## Usage

As for now, there is no complete interface. Therefore, configuration of the simulation must be done directly in the code. The current file that is running the simulation is `Demo.java`.

The demonstration file will create a building with 10 floors and 2 lifts. Each floor will have 10 inhabitants that will randomly request elevator services. The requests will be handled by the elevator system through `Call` objects, there shouldn't be any need to create them manually or edit them.

Backend-wise, the simulation can uphold way more inhabitants, floors, and lifts, but the interface has its limits and it was chosen to keep it simple for demonstration purposes.