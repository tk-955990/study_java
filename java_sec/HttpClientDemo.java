import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class HttpClientDemo {

  public static void main(String[] args) {

    String url = "http://localhost:8080";

    try {
      URL obj = new URL(url);

      HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      System.out.println("Response Code : " + responseCode);

      BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      String inputLine;
      StringBuilder Response = new StringBuilder();

      while ((inputLine = input.readLine()) != null) {
        Response.append(inputLine);
      }
      input.close();

      System.out.println(Response.toString());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
