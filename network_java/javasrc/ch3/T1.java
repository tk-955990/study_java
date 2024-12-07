// telnetの原形となるプログラムT1.java
// このプログラムは,指定されたアドレスのポートに標準入出力を接続します
// 使い方java T1 サーバアドレスポート番号
// 起動の例java T1 kiku.fuis.fukui-u.ac.jp 80
// 終了にはコントロールC を入力してください

// ライブラリの利用
import java.net.*;
import java.io.*;

// T1クラス
// T1クラスは,ネットワーク接続の管理を行います
// StreamConnectorクラスを用いてスレッド処理を行います
public class T1 {
	// ソケットの準備
	protected Socket serverSocket;//接続用ソケット
	public OutputStream serverOutput;//ネットワーク出力用ストリーム
	public BufferedInputStream serverInput;// 同入力用ストリーム

	// openConnectionメソッド
	//アドレスとポート番号からソケットを作りストリームを作成します
	public void openConnection(String host,int port)
		throws IOException,UnknownHostException
	{
		serverSocket = new Socket(host, port);
		serverOutput = serverSocket.getOutputStream();
		serverInput
			= new BufferedInputStream(serverSocket.getInputStream());
	}

	// main_procメソッド
	// ネットワークとのやりとりをするスレッドをスタートさせます
	public void main_proc()
		throws IOException
	{
		try {
			// スレッド用クラスStreamConnectorのオブジェクトを生成します
			StreamConnector stdin_to_socket =
				new StreamConnector(System.in, serverOutput);
			StreamConnector socket_to_stdout =
				new StreamConnector(serverInput, System.out);
			// スレッドを生成します
			Thread input_thread = new Thread(stdin_to_socket);
			Thread output_thread = new Thread(socket_to_stdout);
			// スレッドを起動します
			input_thread.start();
			output_thread.start();
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}

	// mainメソッド
	// TCP コネクションを開いて処理を開始します
	public static void main(String[] arg){
		try {
			T1 t = null;
			t = new T1();
			t.openConnection(arg[0], Integer.parseInt(arg[1]));
			t.main_proc();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

// StreamConnectorクラス
// ストリームを受け取り，両者を結合してデータを受け渡します
// StreamConnectorクラスはスレッドを構成するためのクラスです

class StreamConnector implements Runnable {
	InputStream src = null;
	OutputStream dist = null;
	// コンストラクタ入出力ストリームを受け取ります
	public StreamConnector(InputStream in, OutputStream out){
		src = in;
		dist = out;
	}
	// 処理の本体
	// ストリームの読み書きを無限に繰り返します
	public void run(){
		byte[] buff = new byte[1024];
		while (true) {
			try {
				int n = src.read(buff);
				if (n > 0)
					dist.write(buff, 0, n);
			}
			catch(Exception e){
				e.printStackTrace();
				System.err.print(e);
				System.exit(1);
			}
		}
	}
}