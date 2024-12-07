// https://java-code.jp/259
// クライアントクラスの実装

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client {
  public static void main(String[] args) {

    // ソケット、出力用、入力用のリソースをtry-with-resourcesで自動的にクローズ
    try (Socket socket = new Socket("localhost", 8080); // サーバーに接続するためのソケットを作成
        PrintWriter writer =
            new PrintWriter(socket.getOutputStream(), true); // サーバーにメッセージを送るためのPrintWriter
        BufferedReader reader =
            new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream())); // サーバーからのメッセージを受け取るためのBufferedReader
        BufferedReader keyboard =
            new BufferedReader(new InputStreamReader(System.in))) { // キーボード入力を読み取るためのBufferedReader

      // 無限ループでクライアントからの入力をサーバーに送信し、サーバーからの応答を受け取る
      while (true) {
        System.out.print("Input>"); // ユーザーに入力を促すメッセージを表示
        String input = keyboard.readLine(); // キーボードから1行分の入力を受け取る
        writer.println(input); // 入力した内容をサーバーに送信

        // 入力が "," だった場合、ループを終了して接続を切断
        if (input.equals(",")) {
          break;
        }

        // サーバーからの応答を受け取り、表示する
        System.out.println("[Server]" + reader.readLine());
      }
    } catch (Exception e) {
      // 例外が発生した場合はスタックトレースを表示してエラーを確認
      e.printStackTrace();
    }
  }
}
