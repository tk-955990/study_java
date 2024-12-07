import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
  public static void main(String[] args) {

    System.out.println("Waitting...");

    try (ServerSocket server = new ServerSocket(4444);
        Socket socket = server.accept();
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter output =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        FileWriter writer = new FileWriter("test.txt", false); ) {

      InetAddress address = InetAddress.getLocalHost();
      System.out.println("connect from : " + address.getHostAddress());

      String recievedMessage = input.readLine();
      System.out.println("recieved Message : " + recievedMessage);

      writer.write(recievedMessage + "\n");
      writer.flush();

      output.write(recievedMessage);
      output.newLine();
      output.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
