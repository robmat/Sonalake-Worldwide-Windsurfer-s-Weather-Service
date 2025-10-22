# Sonalake-Worldwide-Windsurfer-s-Weather-Service
Sample app, spring showcase.

## Buidling the Application
### Prerequisites
- Gradle 9.0.0 requires a Java Virtual Machine (JVM) version 17 or higher to run the Gradle Daemon.
- docker (for macos use: `sudo ln -s $HOME/.colima/default/docker.sock /var/run/docker.sock`)
- docker-compose
### Build Steps
1. Clone the repository
2. Run `./gradlew build` to build the application
3. Run `docker-compose up --build` to start the application along with its dependencies
