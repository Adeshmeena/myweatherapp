MyWeatherApp
A Java-based web application for fetching and displaying real-time weather information using the OpenWeatherMap API.

Table of Contents
Project Overview
Features
Technologies Used
Setup and Installation
How to Run
API Key Configuration
Folder Structure
Troubleshooting
Contributing
License
Project Overview
MyWeatherApp is a Java servlet-based web application that retrieves real-time weather data for a specified city using the OpenWeatherMap API. It displays the temperature, weather conditions, and stores data in a MySQL database.

Features
Fetch weather data by city name using the OpenWeatherMap API.
Display the temperature and weather conditions in Celsius.
Store weather information in a MySQL database.
Handle errors gracefully and log useful debug information.
Technologies Used
Java 17: Programming language.
Apache Tomcat 9: Web server for deploying servlets.
MySQL: Database for storing weather data.
JSON: Parsing JSON responses from the API.
Maven: Dependency management and build tool.
Setup and Installation
Prerequisites
JDK 17 installed.
Apache Tomcat 9 installed.
MySQL database set up.
Maven installed for dependency management.
Clone the repository:

bash
Copy code
git clone https://github.com/your-username/myweatherapp.git
cd myweatherapp
Set up MySQL database:

Create a database named weatherservlet.
Run the SQL script to create a table for storing weather data:
sql
Copy code
CREATE TABLE weather (
  id INT AUTO_INCREMENT PRIMARY KEY,
  city VARCHAR(100),
  data TEXT,
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
Update database credentials:

Open WeatherServlet.java and update the DB_URL, DB_USER, and DB_PASSWORD with your MySQL details.
Add API Key:

Replace API_KEY in WeatherServlet.java with your OpenWeatherMap API key.
Build the project with Maven:

bash
Copy code
mvn clean install
Deploy the project on Tomcat:

Copy the myweatherapp.war file from target to the webapps directory of your Tomcat installation.
Start the Tomcat server.
How to Run
Open your web browser and go to:
bash
Copy code
http://localhost:8080/myweatherapp/weatherservlet?city=YourCityName
Replace YourCityName with the desired city to get weather information.
API Key Configuration
Get your API key from OpenWeatherMap.
Update the API_KEY field in WeatherServlet.java:
java
Copy code
private static final String API_KEY = "your_api_key_here";
Folder Structure
css
Copy code
myweatherapp/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── mypack/
│   │   │       └── WeatherServlet.java
│   │   └── resources/
│   ├── test/
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── web.xml
│       └── index.jsp
├── pom.xml
├── README.md
└── .gitignore
Troubleshooting
404 Not Found Error: Verify that the URL matches the servlet mapping and the context path (myweatherapp).
Database Connection Errors: Double-check your DB_URL, DB_USER, and DB_PASSWORD in WeatherServlet.java.
JSON Parsing Errors: Ensure that the response from the OpenWeatherMap API contains the expected fields (main, weather).
Contributing
Contributions are welcome! Please open an issue or submit a pull request for improvements or bug fixes.

License
This project is licensed under the MIT License - see the LICENSE file for details.
