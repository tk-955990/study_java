import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class WebClient {
  public static void main(String[] args) throws IOException {

    String domain = "example.com";

    try (Socket socket = new Socket(domain, 80);
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader input =
            new BufferedReader(new InputStreamReader(socket.getInputStream())); ) {
      writer.println("GET /index.html HTTP/1.1");
      writer.println("Host: " + domain);
      writer.println();
      writer.flush();

      input.lines().limit(18).forEach(System.out::println);
    }
  }
}
