import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {
  public static void main(String[] args) {

    try (Socket socket = new Socket("localhost", 4444);
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter output =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader response =
            new BufferedReader(new InputStreamReader(socket.getInputStream())); ) {
      System.out.println("Enter Message ...");

      String inputMessage = input.readLine();

      output.write(inputMessage);
      output.newLine();
      output.flush();

      System.out.println("responseMessage : " + response.readLine());

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
