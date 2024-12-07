import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

class Client {

  public static void main(String[] args) {

    try (Socket socket = new Socket("localhost", 4444); ) {
      BufferedReader inputMessage = new BufferedReader(new InputStreamReader(System.in));

      BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      InetAddress address = socket.getInetAddress();

      System.out.println("Connect to : " + address.getHostAddress());

      String sendMessage;

      while (true) {
        System.out.println("Enter message... ");
        sendMessage = inputMessage.readLine();

        if (sendMessage.equals("exit")) {
          break;
        }

        output.write(sendMessage);
        output.newLine();
        output.flush();

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String returnMessage = input.readLine();
        if (returnMessage != null) {
          System.out.println("returnMessage : " + returnMessage);
          File file = new File("./test.txt");
          BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
          writer.write(returnMessage);
       writer.newLine();
       writer.flush();
          writer.close();
        } else {
          break;
        }
      }
      System.out.println("Closing connection");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
