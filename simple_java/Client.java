import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

class Client {
  public static void main(String args[]) {
    // ソケットと出力用のバッファライタを自動的に閉じるためのtry-with-resourcesを使用
    try (Socket socket = new Socket("192.168.0.7", 4444); // サーバーに接続
        BufferedWriter output =
            new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); ) { // サーバーにデータを送信するための出力ストリームを作成

      // ユーザーにメッセージを入力するように促す
      System.out.println("Please enter your message...");

      // 標準入力からデータを読み取るためのバッファリーダを作成
      BufferedReader inputData = new BufferedReader(new InputStreamReader(System.in));

      // ユーザーからの入力を1行読み取る
      String sendData = inputData.readLine();

      // 読み取ったデータをサーバーに送信
      output.write(sendData);
      output.flush(); // バッファに溜まっているデータを強制的に送信する

      // 送信したメッセージをコンソールに表示
      System.out.println("[" + sendData + "]を送信しました。");

    } catch (IOException e) {
      // 接続エラーや入出力エラーが発生した場合に例外をキャッチしてスタックトレースを表示
      e.printStackTrace();
    }
  }
}

