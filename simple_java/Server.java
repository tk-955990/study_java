import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
  public static void main(String args[]) {
    // ソケットやリーダーを自動的に閉じるためのtry-with-resourcesを使用
    try (ServerSocket server = new ServerSocket(4444); // ポート4444でクライアントからの接続を待機
        Socket socket = server.accept(); // クライアントが接続してきたら受け入れる
        BufferedReader input =
            new BufferedReader(new InputStreamReader(socket.getInputStream())); ) { // クライアントからデータを受け取るための入力ストリームを作成

      // クライアントから送られてきたメッセージを1行読み取る
      String message = input.readLine();

      // 受け取ったメッセージをコンソールに表示
      System.out.println("[" + message + "]を受信しました。");

    } catch (IOException e) {
      // 接続エラーや入出力エラーが発生した場合に例外をキャッチしてスタックトレースを表示
      e.printStackTrace();
    }
  }
}

