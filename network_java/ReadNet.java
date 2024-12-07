
// ReadNet.java
// ネットワーク上のサーバからデータを受け取り、HTTPリクエストを送信してレスポンスを受け取るプログラム
// 使い方: java ReadNet DNS名 ポート番号
// 例: java ReadNet localhost 80

import java.io.*;
import java.net.*;

// ReadNetクラス
public class ReadNet {
    public static void main(String[] args) {
        // 引数が不足している場合のチェック
        // 実行時にDNS名とポート番号を指定しないとエラーを表示して終了
        if (args.length < 2) {
            System.err.println("使い方: java ReadNet DNS 名 ポート番号");
            System.exit(1);
        }

        // 受信用のバッファを1024バイト（1KB）確保
        byte[] buff = new byte[1024];

        // try-with-resources構文でSocketとストリームを開く
        // Socketは指定したDNS名とポート番号でサーバに接続
        try (Socket readSocket = new Socket(args[0], Integer.parseInt(args[1]));
             // サーバからデータを受け取るためのInputStream
             InputStream instrStream = readSocket.getInputStream();
             // サーバにデータを送信するためのOutputStream
             OutputStream outStream = readSocket.getOutputStream()) {

            // HTTP GETリクエストを作成
            // サーバに「GET」リクエストを送信して、ページの内容を要求する
            String request = "GET / HTTP/1.1\r\n"           // HTTPのGETメソッド
                             + "Host: " + args[0] + "\r\n"  // リクエストするホスト（引数から取得）
                             + "Connection: close\r\n\r\n"; // リクエスト後に接続を閉じる
            // リクエストをバイト配列に変換してサーバに送信
            outStream.write(request.getBytes());
            // 送信が終わったらバッファの内容をフラッシュ（即時送信）
            outStream.flush();

            // サーバからのレスポンスを受信するループ
            boolean cont = true;

            while (cont) {
                // サーバからデータを読み込む（データがなければ-1が返される）
                int n = instrStream.read(buff);
                if (n == -1) {
                    // データが全て受信されたらループを終了
                    cont = false;
                } else {
                    // 受信したデータをそのまま標準出力に表示
                    System.out.write(buff, 0, n);
                }
            }
        } catch (UnknownHostException e) {
            // 指定したホストが見つからなかった場合のエラーメッセージ
            System.err.println("ホストが見つかりません: " + e.getMessage());

        } catch (IOException e) {
            // ネットワークエラーが発生した場合のエラーメッセージ
            System.err.println("ネットワークエラー: " + e.getMessage());
            System.exit(1);
        }
    }
}
