import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

class SimpleHttpServer {

  public static void main(String[] args) throws IOException {

    InetSocketAddress address = new InetSocketAddress("localhost", 8080);
    HttpServer server = HttpServer.create(address, 0);

    server.createContext("/", new MyHandler());

    System.out.println("Starting server at http://localhost:8080");
    server.start();
  }

  static class MyHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

      String response = "Hello, this is a simple HTTP server!";
      exchange.sendResponseHeaders(200, response.length());

      OutputStream output = exchange.getResponseBody();
      output.write(response.getBytes());
      output.close();
    }
  }
}
