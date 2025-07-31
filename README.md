#Spring Boot service for Melbourne weather.

Run below cmd's to test this application.

1. git clone https://github.com/YOUR_USERNAME/weather-app.git
2. cd weather-app
3. Add your API keys in application.properties
    weatherstack.api.key=...
    openweathermap.api.key=...
4. run   ./gradlew bootRun
5. run    curl "http://localhost:8080/v1/weather?city="
