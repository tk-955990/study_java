import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server {

  public static void main(String[] args) {

    try (ServerSocket server = new ServerSocket(4444); ) {
      System.out.println("waiting...");
      Socket socket = server.accept();
      InetAddress address = socket.getInetAddress();
      System.out.println("Connect to : " + address.getHostAddress());

      BufferedReader receive = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String receiveMessage;

      while ((receiveMessage = receive.readLine()) != null && !(receiveMessage.equals("exit"))) {

        System.out.println("receiveMessage : " + receiveMessage);

        BufferedWriter output =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        output.write(receiveMessage);
        output.newLine();
        output.flush();
      }
      System.out.println("Closing connection");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
