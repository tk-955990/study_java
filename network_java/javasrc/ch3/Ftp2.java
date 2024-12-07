// ftp プログラムFtp2.java
// このプログラムは,ftp サーバと接続します
// データ用コネクションを用意し,LISTコマンドを実行します
// ファイル転送機能はありません
// 使い方java Ftp2 サーバアドレス
// 起動の例java Ftp2 kiku.fuis.fukui-u.ac.jp

// ライブラリの利用
import java.net.*;
import java.io.*;

// Ftp2クラス
public class Ftp2 {
	// ソケットの準備
	Socket ctrlSocket;//制御用ソケット
	public PrintWriter ctrlOutput;//制御出力用ストリーム
	public BufferedReader ctrlInput;// 同入力用ストリーム

	final int CTRLPORT = 21 ;// ftp の制御用ポート

	// openConnectionメソッド
	//アドレスとポート番号からソケットを作り制御用ストリームを作成します
	public void openConnection(String host)
		throws IOException,UnknownHostException
	{
		ctrlSocket = new Socket(host, CTRLPORT);
		ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream());
		ctrlInput
			= new BufferedReader(new InputStreamReader
								(ctrlSocket.getInputStream()));
	}

	// closeConnectionメソッド
	//制御用のソケットを閉じます
	public void closeConnection()
		throws IOException
	{
		ctrlSocket.close() ;
	}

	// showMenuメソッド
	// Ftp のコマンドメニューを出力します
	public void showMenu()
	{
		System.out.println(">Command?") ;
		System.out.print("1 login") ;
		System.out.print(" 2 ls") ;
		System.out.print(" 3 cd") ;
		System.out.println(" 9 quit") ;
	}

	// getCommandメソッド
	// 利用者の指定したコマンド番号を読み取ります
	public String getCommand()
	{
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;

		while(buf.length() != 1){// １文字の入力を受けるまで繰り返し
			try{
				buf = lineread.readLine() ;
			}catch(Exception e)
			{
				e.printStackTrace();
				System.exit(1);
			}
		}
		return (buf) ;
	}

	// doLoginメソッド
	// ftp サーバにログインします
	public void doLogin()
	{
		String loginName = "" ;
		String password = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;

		try{
			System.out.println("ログイン名を入力してください") ;
			loginName = lineread.readLine() ;
			// USERコマンドによるログイン
			ctrlOutput.println("USER " + loginName) ;
			ctrlOutput.flush() ;
			// PASSコマンドによるパスワードの入力
			System.out.println("パスワードを入力してください") ;
			password = lineread.readLine() ;
			ctrlOutput.println("PASS " + password) ;
			ctrlOutput.flush() ;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// doQuitメソッド
	// ftp サーバからログアウトします
	public void doQuit()
	{
		try{
			ctrlOutput.println("QUIT ") ;// QUITコマンドの送信
			ctrlOutput.flush() ;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// doCdメソッド
	// ディレクトリを変更します
	public void doCd()
	{
		String dirName = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;

		try{
			System.out.println("ディレクトリ名を入力してください") ;
			dirName = lineread.readLine() ;
			ctrlOutput.println("CWD " + dirName) ;// CWDコマンド
			ctrlOutput.flush() ;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// doLsメソッド
	// ディレクトリ情報を得ます
	public void doLs()
	{
		try{
			int n ;
			byte[] buff = new byte[1024] ;
			// データ用コネクションを作成します
			Socket dataSocket = dataConnection("LIST") ;
			// データ読み取り用ストリームを用意します
			BufferedInputStream dataInput
				= new BufferedInputStream(dataSocket.getInputStream()) ;
			// ディレクトリ情報を読み取ります
			while((n = dataInput.read(buff)) > 0){
				System.out.write(buff,0,n) ;
			}
			dataSocket.close() ;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// dataConnectionメソッド
	// サーバとのデータ交換用にソケットを作ります
	// また,サーバに対してportコマンドでポートを通知します
	public Socket dataConnection(String ctrlcmd)
	{
		String cmd = "PORT " ; //PORTコマンドで送るデータの格納用変数
		int i ;
		Socket dataSocket = null ;// データ転送用ソケット
		try{
			// 自分のアドレスを求めます
			byte[] address = InetAddress.getLocalHost().getAddress() ;
			// 適当なポート番号のサーバソケットを作ります
			ServerSocket serverDataSocket = new ServerSocket(0,1) ;
			// PORTコマンド用の送信データを用意します
			for(i = 0; i < 4; ++i)
				cmd = cmd + (address[i] & 0xff) + "," ;
			cmd = cmd + (((serverDataSocket.getLocalPort()) / 256) & 0xff)
					  + ","
					  + (serverDataSocket.getLocalPort() & 0xff) ;
			// PORTコマンドを制御用ストリームを通して送ります
			ctrlOutput.println(cmd) ;
			ctrlOutput.flush() ;
			// 処理対象コマンド（LIST,RETR,およびSTOR）をサーバに送ります
			ctrlOutput.println(ctrlcmd) ;
			ctrlOutput.flush() ;
			// サーバからの接続を受け付けます
			dataSocket = serverDataSocket.accept() ;
			serverDataSocket.close() ;
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return dataSocket ;
	}

	// execCommandメソッド
	// コマンドに対応する各処理を呼び出します
	public boolean execCommand(String command)
	{
		boolean cont = true ;

		switch(Integer.parseInt(command)){
		case 1 : // login 処理
			doLogin() ;
			break ;
		case 2 : // サーバのディレクトリ表示処理
			doLs() ;
			break ;
		case 3 : // サーバの作業ディレクトリ変更処理
			doCd() ;
			break ;
		case 9 : // 処理の終了
			doQuit() ;
			cont = false ;
			break ;
		default : //それ以外の入力
			System.out.println("番号を選択してください") ;
		}
		return(cont) ;
	}

	// main_procメソッド
	// Ftp のコマンドメニューを出力して,各処理を呼び出します
	public void main_proc()
		throws IOException
	{
		boolean cont = true ;
		try {
			while(cont){
				// メニューを出力します
				showMenu() ;
				// コマンドを受け取り実行します
				cont = execCommand(getCommand()) ;
			}
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}

	// getMsgsメソッド
	// 制御ストリームの受信スレッドを開始します
	public void getMsgs(){
		try {
			CtrlListen listener = new CtrlListen(ctrlInput) ;
			Thread listenerthread = new Thread(listener) ;
			listenerthread.start() ;
		}catch(Exception e){
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}

	// mainメソッド
	// TCPコネクションを開いて処理を開始します
	public static void main(String[] arg){
		try {
			Ftp2 f = null;
			f = new Ftp2();
			f.openConnection(arg[0]);	// 制御用コネクションの設定
			f.getMsgs() ;				// 受信スレッドの開始
			f.main_proc();				// ftp 処理
			f.closeConnection() ;		// コネクションのクローズ
			System.exit(0) ;			// プログラムの終了
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

// CtrlListen クラス
class CtrlListen implements Runnable{
	BufferedReader ctrlInput = null ;
	// コンストラクタ読み取り先の指定
	public CtrlListen(BufferedReader in){
		ctrlInput = in ;
	}

	public void run(){
		while(true){
			try{ // ひたすら行を読み取り,標準出力にコピーします
				System.out.println(ctrlInput.readLine()) ;
			} catch (Exception e){
				System.exit(1) ;
			}
		}
	}
}