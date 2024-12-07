// Pop3 によるメール受信プログラムPop.java
// このプログラムは,指定されたPOPサーバからメールを取得します
// 使い方java Pop
// 起動後,POPサーバのアドレスを聞いてきます
// 対話の例
// POP サーバのアドレスを入力してください
// kiku.fuis.fukui-u.ac.jp
// POPサーバのアドレス：kiku.fuis.fukui-u.ac.jp
// 以上でよろしいですか？(y/n)
// y
// +OK QPOP (version 2.53) at kiku.fuis.fukui-u.ac.jp starting.
// <4752.923273295@kiku.fuis.fukui-u.ac.jp>
// その後,POP のユーザ名とパスワードを聞いてきます
// 対話の例
// user nameを入力してください
// odaka
// Passwordを入力してください
// XXXXXXXX
// user name：odaka
// Password：XXXXXXXX
// 以上でよろしいですか？(y/n)

// ライブラリの利用
import java.net.*;
import java.io.*;
import java.util.*;

// Popクラス
// Popクラスは,Pop プログラムの本体です
public class Pop {
	final int POP_PORT = 110;// POP接続用ポート番号(110 番)
	BufferedReader pop_in = null ;// 読み出し用ストリーム
	PrintWriter pop_out = null ;// 書き込み用ストリーム
	Socket pop = null ;// ソケット

	// transactionメソッド
	// POP3のセッションを進めます
	public void transaction()
		throws IOException
	{
		String buf = "" ;
		BufferedReader lineread // 標準入力読み取り用
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;

		while(cont){ //入力のループ
			System.out.println(
				"Command : L)ist R)etrieve D)elete Q)uit") ;
			buf = lineread.readLine() ;
			// QUIT
			if(buf.equalsIgnoreCase("Q")) cont = false ;
			// LIST
			else if(buf.equalsIgnoreCase("L")) getLines("LIST") ;
			// RETR
			else if(buf.equalsIgnoreCase("R")) {
				System.out.println("Number? : ") ;
				buf = lineread.readLine() ;
				getLines("RETR " + buf) ;
			}
			// DELE
			else if("D".equals(buf) || "d".equals(buf)){
				System.out.println("Number? : ") ;
				buf = lineread.readLine() ;
				getSingleLine("DELE " + buf) ;
			}
		}
	}

	// getLinesメソッド
	// 回答が複数行に渡るコマンドの処理を行います
	public void getLines(String command)
		throws IOException
	{
		boolean cont = true ;
		String buf = null ;
		// コマンドの送付
		pop_out.print(command +"\r\n");
		pop_out.flush() ;
		String res = pop_in.readLine();//返答の読み取り
		System.out.println(res) ;
		// もし返答コードが+OKでなければ・・・
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// コネクションを閉じます
			throw new RuntimeException(res);
		}
		while(cont){// 複数行の読み取り
			buf = pop_in.readLine();//１行読み取り
			System.out.println(buf) ;
			// 行頭の単独ピリオドで終了
			if(".".equals(buf)) cont = false ;
		}
	}

	// getSingleLineメソッド
	// 返答が１行となるコマンドの処理を行います
	public void getSingleLine(String command)
		throws IOException
	{
		// コマンドの送付
		pop_out.print(command +"\r\n");
		pop_out.flush() ;
		System.out.println(command) ;
		String res = pop_in.readLine();//返答の読み取り
		System.out.println(res) ;
		// もし返答コードが+OKでなければ・・・
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// コネクションを閉じます
			throw new RuntimeException(res);
		}
	}

	// authorizationメソッド
	// TCPコネクションの設定と認証を行います
	public void authorization()
		throws IOException
	{
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;
		String pop_server = null ;
		String username = null ;
		String password = null ;

		//POPサーバのアドレスの設定
		while(cont){ //入力のループ
			System.out.println("POP サーバのアドレスを入力してください") ;
			pop_server = lineread.readLine() ;
			System.out.println(" POP サーバのアドレス：" + pop_server) ;
			System.out.println("以上でよろしいですか？(y/n)") ;
			buf = lineread.readLine() ;
			if("y".equals(buf)) cont = false ;
		}
		// サーバとの接続
		pop = new Socket(pop_server, POP_PORT);
		pop_in = new BufferedReader(
				 new InputStreamReader(pop.getInputStream()));
		pop_out = new PrintWriter(pop.getOutputStream());
		// メッセージの取得
		String res = pop_in.readLine();//返答の読み取り
		System.out.println(res) ;
		// もし返答コードが+OKでなければ・・・
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// コネクションを閉じます
			throw new RuntimeException(res);
		}

		// 認証情報の取得
		cont = true ;
		while(cont){ //入力のループ
			System.out.println("user nameを入力してください") ;
			username = lineread.readLine() ;
			System.out.println("Passwordを入力してください") ;
			password = lineread.readLine() ;
			System.out.println(" user name：" + username) ;
			System.out.println(" Password：" + password) ;
			System.out.println("以上でよろしいですか？(y/n)") ;
			buf = lineread.readLine() ;
			if("y".equals(buf)) cont = false ;
		}
		// USERコマンドとPASSコマンドによる認証作業
		getSingleLine("USER " + username) ;
		getSingleLine("PASS " + password) ;
	}

	// updateメソッド
	// POP3 のセッションを終了します
	public void update()
	throws IOException
	{
		// QUIT
		getSingleLine("QUIT");
		pop.close();// コネクションを閉じます
	}

	// mainprocメソッド
	// POP の各処理メソッドを呼び出します
	public void mainproc(String[] args)
		throws IOException
	{
		if(args.length == 0){
			authorization() ;
			transaction() ;
			update() ;
		}else{
			System.out.println("usage: java Pop ");
		}
	}

	// mainメソッド
	// Popクラスのオブジェクトを生成します
	public static void main(String[] args)
		throws IOException
	{
		Pop p = new Pop() ;
		p.mainproc(args) ;
	}
}