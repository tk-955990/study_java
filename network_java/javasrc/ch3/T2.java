// telnet プログラムT2.java
// このプログラムは,指定されたアドレスのポートに標準入出力を接続します
// 接続先がtelnet のポート(23番)の場合,ネゴシエーションを行います
// ネゴシエーションでは,サーバからの要求をすべて断ります
// 使い方java T2 サーバアドレスポート番号
// 起動の例java T2 kiku.fuis.fukui-u.ac.jp 23
// 終了にはコントロールC を入力してください

// ライブラリの利用
import java.net.*;
import java.io.*;

// T2クラス
// T2クラスは,ネットワーク接続の管理を行います
// StreamConnectorクラスを用いてスレッド処理を行います
public class T2 {
	Socket serverSocket; //接続用ソケット
	public OutputStream serverOutput;//ネットワーク出力用ストリーム
	public BufferedInputStream serverInput; // 同入力用ストリーム
	static final int DEFAULT_TELNET_PORT = 23; // telnet のポート番号(23番)

	// openConnectionメソッド
	//アドレスとポート番号からソケットを作りストリームを作成します
	public void openConnection(String host,int port)
		throws IOException,UnknownHostException
	{
		serverSocket = new Socket(host, port);
		serverOutput = serverSocket.getOutputStream();
		serverInput = new
			BufferedInputStream(serverSocket.getInputStream());
		if (port == DEFAULT_TELNET_PORT){
			// 接続先がtelnet ポートなら,ネゴシエーションを行います
			negotiation(serverInput, serverOutput);
		}
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

	// ネゴシエーションに用いるコマンドの定義
	static final byte IAC = (byte) 255;
	static final byte DONT = (byte) 254;
	static final byte DO = (byte) 253;
	static final byte WONT = (byte) 252;
	static final byte WILL = (byte) 251;

	// negotiationメソッド
	// NVT による通信をネゴシエートします
	static void negotiation(BufferedInputStream in,OutputStream out)
		throws IOException
	{
		byte[] buff = new byte[3];//コマンド受信用配列
		while(true) {
			in.mark(buff.length);
			if (in.available() >= buff.length) {
				in.read(buff);
				if (buff[0] != IAC){// ネゴシエーション終了
					in.reset();
					return;
				} else if (buff[1] == DO) {// DOコマンドに対しては…
					buff[1] = WONT; // WON'Tで返答します
					out.write(buff);
				}
			}
		}
	}

	// mainメソッド
	// TCPコネクションを開いて処理を開始します
	public static void main(String[] arg){
		try {
			T2 t = null;
			t = new T2();
			t.openConnection(arg[0], Integer.parseInt(arg[1]));
			t.main_proc();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

// StreamConnectorクラス
// ストリームを受け取り,両者を結合してデータを受け渡します
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