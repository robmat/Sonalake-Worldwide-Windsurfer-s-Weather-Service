# Sonalake-Worldwide-Windsurfer-s-Weather-Service
Sample app, spring showcase.

## Buidling the Application
### Prerequisites
- Gradle 9.0.0 requires a Java Virtual Machine (JVM) version 17 or higher to run the Gradle Daemon.
- docker (tested: Docker version 28.4.0) (for macos use: `sudo ln -s $HOME/.colima/default/docker.sock /var/run/docker.sock`)
- docker-compose (tested: Docker Compose version 2.23.1)
### Build and Run Steps
1. Clone the repository
2. Run `./gradlew build` to build the application
3. Run `WEATHERBIT_API_KEY={KEY} docker-compose up --build -d` to start the application along with its dependencies
4. Test the application by navigating to `http://localhost:8080/actuator/health` in your web browser or using a tool like curl or Postman.

## Surf weather api
### Getting the JWT
To access the Surf Weather API, you need to obtain a JWT (JSON Web Token) for authentication. Follow these steps to get your JWT:
```bash
curl --location 'http://localhost:9000/default/token' --header 'Content-Type: application/x-www-form-urlencoded' --header 'Authorization: Basic Zm9vOmJhcg==' --header 'Cookie: JSESSIONID=E2A6404F4121BC901993174F0F11DB77' --data-urlencode 'grant_type=client_credentials' --data-urlencode 'client_id=foo' --data-urlencode 'client_secret=bar' --data-urlencode 'scope=weather.read' | jq -r .access_token
```
### Using the JWT to access the Weather Endpoint
Use the obtained JWT to access the weather endpoint as follows:
```bash
curl --location 'localhost:8080/api/weather/best-location/2025-10-30' --fail --header 'Authorization: Bearer TOKEN'
```
### All in one command
```bash
ACCESS_TOKEN=$(curl -s --location 'http://localhost:9000/default/token' \
  --header 'Content-Type: application/x-www-form-urlencoded' \
  --header 'Authorization: Basic Zm9vOmJhcg==' \
  --data-urlencode 'grant_type=client_credentials' \
  --data-urlencode 'client_id=foo' \
  --data-urlencode 'client_secret=bar' \
  --data-urlencode 'scope=weather.read' \
  | jq -r .access_token) && curl --location --fail 'http://localhost:8080/api/weather/best-location/2025-10-31'   --header "Authorization: Bearer ${ACCESS_TOKEN}"
```
### Postman Collection
You can import the API endpoints using the provided [windsurf.postman_collection.json](windsurf.postman_collection.json) file.
Click on the `windsurf` collection to open it, select `Auth` tab and then `Get new access token`. The token will be user in other requests.

## Grafana Dashboard
A Grafana dashboard is available to visualize JVM data. 
Access it at `http://localhost:3000` with the default credentials (admin/admin).
Then got to `http://localhost:3000/dashboards` and select `JVM (Micrometer)`