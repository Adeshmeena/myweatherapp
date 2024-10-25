package mypack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/weatherservlet")
public class WeatherServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "63bbbbec8bd207822a5d25a20e241878";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/weather_monitoring";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Jazzy@123";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String city = request.getParameter("city");
        
        if (city == null || city.isEmpty()) {
            response.getWriter().write("Please provide a city parameter.");
            return;
        }

        try {
            // Get weather data for the given city
            String weatherData = getWeatherData(city);
            response.setContentType("text/plain");
            response.getWriter().write(weatherData);
        } catch (Exception e) {
            response.getWriter().write("Error fetching weather data: " + e.getMessage());
        }
    }

    private String getWeatherData(String city) throws IOException, SQLException {
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        // Debug: Print response code
        System.out.println("Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Debug: Print the raw JSON response
            String jsonResponseString = response.toString();
            System.out.println("JSON Response: " + jsonResponseString);

            try {
                JSONObject jsonResponse = new JSONObject(jsonResponseString);

                // Check if "main" is available in the JSON response
                if (jsonResponse.has("main")) {
                    JSONObject main = jsonResponse.getJSONObject("main");

                    // Check if "temp" exists in "main"
                    if (main.has("temp")) {
                        double tempKelvin = main.getDouble("temp");
                        double tempCelsius = tempKelvin - 273.15; // Convert Kelvin to Celsius
                        String weatherDescription = jsonResponse.getJSONArray("weather").getJSONObject(0).getString("description");

                        // Store the weather data in the database
                        saveWeatherDataToDatabase(city, tempCelsius, weatherDescription);

                        return "Temperature: " + String.format("%.2f", tempCelsius) + "°C, Weather: " + weatherDescription;
                    } else {
                        return "Temperature data not available in the 'main' section.";
                    }
                } else {
                    return "Main data not available in the response.";
                }
            } catch (Exception e) {
                return "Error parsing JSON response: " + e.getMessage() + ": " + jsonResponseString;
            }
        } else {
            throw new IOException("Error fetching data from OpenWeatherMap: " + responseCode);
        }
    }

    private void saveWeatherDataToDatabase(String city, double temperature, String weatherCondition) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO weather_data (city, temperature, weather_condition) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, city);
                statement.setDouble(2, temperature);
                statement.setString(3, weatherCondition);
                statement.executeUpdate();
            }
        }
    }
}
