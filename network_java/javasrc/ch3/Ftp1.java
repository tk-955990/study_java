// ftp プログラムFtp1.java
// このプログラムは,ftp サーバと接続します
// データ用コネクションを扱わないため,ファイル転送はできません
// 使い方java Ftp1 サーバアドレス
// 起動の例java Ftp1 kiku.fuis.fukui-u.ac.jp

// ライブラリの利用
import java.net.*;
import java.io.*;

// Ftp1クラス
public class Ftp1 {
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
	// ftp のコマンドメニューを出力します
	public void showMenu()
	{
		System.out.println(">Command?") ;
		System.out.print("1 login") ;
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

	// execCommandメソッド
	//コマンドに対応する各処理を呼び出します
	public boolean execCommand(String command)
	{
		boolean cont = true ;
		switch(Integer.parseInt(command)){
		case 1 : // login 処理
			doLogin() ;
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
	// ftp のコマンドメニューを出力して,各処理を呼び出します
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
	// TCP コネクションを開いて処理を開始します
	public static void main(String[] arg){
		try {
			Ftp1 f = null;
			f = new Ftp1();
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