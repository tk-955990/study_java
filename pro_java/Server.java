import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
  public static void main(String[] args) throws IOException {
    ServerSocket server = new ServerSocket(4444);
    System.out.println("Waiting...");
    Socket socket = server.accept();
    System.out.println("connect from " + socket.getInetAddress());
    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    String message = input.readLine();
    System.out.println("Received: " + message);
    server.close();
    input.close();
    socket.close();
  }
}
