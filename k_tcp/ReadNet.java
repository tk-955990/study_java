import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.file.*;

// ReadNet クラスは、指定されたサーバーへのHTTPリクエストを送信し、レスポンスをファイルに保存します。
class ReadNet {

  public static void main(String[] args) {

long startTime = System.currentTimeMillis();

    // try-with-resourcesを使用してSocketとI/Oストリームを自動的にクローズします。
    try (Socket socket = new Socket("localhost", 80); // localhostの80番ポートに接続
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream())); // サーバーからのレスポンスを読み取るためのBufferedReader
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // サーバーにデータを送信するためのBufferedWriter
    ) {

      // サーバーに対してHTTP GETリクエストを送信します
      output.write("GET /blog/index.php/2024/10/01/hello-wordpress/ HTTP/1.1\r\n"); // HTTP GETリクエストのパスを指定
      output.write("Host: localhost\r\n"); // ホストヘッダーを追加

      // 空行を送信してHTTPリクエストの終了を示します
      output.write("\r\n");
      output.flush(); // BufferedWriterの内容をフラッシュして、サーバーに送信します

      // レスポンスの内容を保存するためのファイルパスを指定します（test.txt）
      Path path = Path.of("test.txt");
      String line;

      // サーバーからのレスポンスを1行ずつ読み取ります
      while ((line = input.readLine()) != null) {
        System.out.println(line); // 受信したレスポンスをコンソールに出力します

        // 受信した内容をファイルに書き込みます。ファイルが存在しない場合は作成されますが、既存のファイルがあれば上書きされます。
        Files.writeString(path, line + System.lineSeparator(), StandardOpenOption.CREATE);
      }

    } catch (IOException e) {
      e.printStackTrace(); // 入出力例外が発生した場合、その詳細を表示します
    }
    long endTime = System.currentTimeMillis();

    System.out.println("開始時間:" + startTime + "ms");
    System.out.println("終了時間:" + endTime + "ms");
    System.out.println("処理時間:" + (endTime - startTime) / 1000 + "ms");
    
    
    
    

  }
}

