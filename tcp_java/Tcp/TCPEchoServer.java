package Tcp;

import java.net.*;
import java.io.*;

public class TCPEchoServer {

	private static final int BUFSIZE = 32;

	public static void main(String[] args) throws IOException {

		if (args.length != 1) // 引数の数が正しいかの確認
			throw new IllegalArgumentException("Parameter(s): <Port>");

		int servPort = Integer.parseInt(args[0]);

		// クライアントの接続要求を受け付けるサーバソケットを作成
		ServerSocket servSock = new ServerSocket(servPort);
                System.out.println("Server is running and waiting ...);

		int recvMsgSize; // 受信したメッセージのサイズ
		byte[] byteBuffer = new byte[BUFSIZE]; // 受信バッファ

		for (;;) { // 繰り返し実行され、接続を受け付けて処理

			Socket clntSock = servSock.accept(); // クライアント接続を取得//
			System.out.println("Handiling client at" + clntSock.getInetAddress() + " on port " + clntSock.getPort());

			InputStream in = clntSock.getInputStream();
			OutputStream out = clntSock.getOutputStream();

			// クライアントが接続をクローズし、-1が返されるまで受信
			while ((recvMsgSize = in.read(byteBuffer)) != -1)
				out.write(byteBuffer, 0, recvMsgSize);

			clntSock.close(); // ソケットをクローズする。このクライアントとの接続は終了
		}
		/* この部分には到達しない */
	}

}
