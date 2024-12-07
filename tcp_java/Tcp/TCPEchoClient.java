package Tcp;

import java.net.*;
import java.io.*;

public class TCPEchoClient {

	public static void main(String[] args) throws IOException {

		if ((args.length < 2) || (args.length > 3)) // 引数の数が正しいかの確認(引数が２つ又は３つ)
			throw new IllegalArgumentException("Pramater(s): <Server> <Word> [<Port>]");
		// 第一引数 : サーバー名またはIPアドレス
		String server = args[0]; 

		// 第二引数 : デフォルトの文字エンコード方式を使って入力文字列をバイトに変換する
		byte[] byteBuffer = args[1].getBytes();

		// 第三引数 : 使用するポート番号 ※未入力の場合は７番ポート
		int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

		// サーバーの指定されたポートに接続するソケットを作成する
		Socket socket = new Socket(server, servPort);
		System.out.println("Connected to server...sending echo string");

		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		out.write(byteBuffer); // エンコードされた文字列をサーバに送信する

		int totalBytesRcvd = 0; // これまでに受信した合計バイト数
		int bytesRcvd; // 前回の読み込みで受信したバイト数

		// バイトデータが完全に受信されるまで繰り返す
		while (totalBytesRcvd < byteBuffer.length) {
			// 残りのバッファ容量を読み込む
			bytesRcvd = in.read(byteBuffer, totalBytesRcvd, byteBuffer.length - totalBytesRcvd);
			// もし読み込まれたバイト数が -1 ならば、接続が予期せずクローズされたことを意味する
			if (bytesRcvd == -1) {
				throw new SocketException("Connection closed prematurely");
			}
			// これまでに受信した合計バイト数を更新する
			totalBytesRcvd += bytesRcvd;
		}

		System.out.println("Rceived " + "'" +new String(byteBuffer) + "'");

		socket.close(); // ソケットとストリームをクローズする
	}
}
