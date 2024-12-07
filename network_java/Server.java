// https://java-code.jp/261
// サーバークラスの実装

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

class Server {
  public static void main(String[] args) {

    // サーバーソケットを作成し、try-with-resourcesで自動的にクローズされるようにする
    try (ServerSocket server = new ServerSocket()) {

      // サーバーソケットを "localhost" のポート8080にバインド（接続待ちの設定）
      server.bind(new InetSocketAddress("localhost", 8080));

      // クライアントからの接続要求を受け入れる（接続があるまで待機）
      try (Socket socket = server.accept();

          // クライアントからのデータを受け取るためのBufferedReaderを作成
          BufferedReader reader =
              new BufferedReader(new InputStreamReader(socket.getInputStream()));

          // クライアントにデータを送るためのPrintWriterを作成（自動フラッシュ設定: true）
          PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

        // クライアントから送られてくるデータを読み続けるループ
        while (true) {
          String line = reader.readLine(); // クライアントからのメッセージを1行読み込む

          // もしクライアントからのメッセージが "," であれば、ループを抜けて終了
          if (line.equals(",")) {
            break;
          }

          // クライアントに受け取ったメッセージを大文字にして送り返す
          writer.println(line.toUpperCase());

          // 受け取ったメッセージをサーバーのコンソールに表示
          System.out.println(line);
        }
      }
    } catch (Exception e) {
      // 例外が発生した場合は、スタックトレースを出力してエラーを確認
      e.printStackTrace();
    }
  }
}
