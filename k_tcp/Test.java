import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Test {
  public static void main(String[] args) throws IOException {

    try (Socket socket = new Socket("www.kkaneko.jp", 80);
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter output =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); ) {

      output.write("GET /index.html HTTP/1.0\r\n");
      output.write("Host: www.kkaneko.jp:80\r\n");
      output.write("\r\n");
      output.flush();

      System.out.println(input.readLine());
      System.out.println(input.readLine());
      System.out.println(input.readLine());
      System.out.println(input.readLine());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
