import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {
  public static void main(String[] args) throws IOException {
    Socket socket = new Socket("localhost", 4444);
    OutputStreamWriter output = new OutputStreamWriter(socket.getOutputStream());
    output.write("Hello World!!");
    output.close();
    socket.close();
  }
}
