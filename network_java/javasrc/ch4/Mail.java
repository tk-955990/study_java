// SMTP によるメール送信プログラムMail.java
// このプログラムは,指定されたメールサーバに対してメッセージを送信します
// 使い方java Mail [-s subject] 送信相手のアドレス
// 使用上の注意
// subjectは空白なしの一語としてください
// 送信相手のアドレスは複数指定できます
// 起動の例java Mail -s Hello odaka@kiku.fuis.fukui-u.ac.jp
// 起動後,送信を依頼するメールサーバのアドレスと,送信者のアドレスを聞いてきます
// それぞれ,アドレスを答えてください
// アドレス確認の例
// メールサーバのアドレスを入力してください
// kiku.fuis.fukui-u.ac.jp
// あなたのメールアドレスを入力してください
// odaka@take.fuis.fukui-u.ac.jp
// メールサーバのアドレス：kiku.fuis.fukui-u.ac.jp
// あなたのメールアドレス：odaka@take.fuis.fukui-u.ac.jp
// 以上でよろしいですか？(y/n)

// ライブラリの利用
import java.net.*;
import java.io.*;
import java.util.*;

// Mailクラス
// Mailクラスは,Mail プログラムの本体です
public class Mail {
	final int SMTP_PORT = 25;// SMTP 接続用ポート番号(25 番)
	String smtp_server = ""; // 送信用サーバ
	String my_email_addr = "" ; // 自分のメールアドレス

	//sendCommandAndResultCheckメソッド
	//SMTP コマンドをサーバに送信し,返答コードの確認を行います
	public void sendCommandAndResultCheck(Socket smtp,
										  BufferedReader smtp_in,
										  PrintWriter smtp_out,
										  String command,
										  int success_code)
		throws IOException
	{
		smtp_out.print(command + "\r\n");//コマンドの送信
		smtp_out.flush();
		System.out.println("send> " + command);//送信内容の表示
		resultCheck(smtp, smtp_in, smtp_out, success_code);
													//結果のチェック
	}

	// resultCheckメソッド
	// 返答コードをチェックして,エラーならばコネクションを閉じます
	public void resultCheck(Socket smtp, BufferedReader smtp_in,
							PrintWriter smtp_out,
							int success_code)
		throws IOException
	{
		String res = smtp_in.readLine();//返答コードの読み取り
		System.out.println("recv> " + res);
		// もし返答コードが期待されるコード(success_code)でなければ・・・
		if (Integer.parseInt(res.substring(0,3)) != success_code){
			smtp.close();// コネクションを閉じます
			throw new RuntimeException(res);
		}
	}

	// sendメソッド
	// SMTP のセッションを進めます
	public void send(String subject, String[] to,String[] msgs)
		throws IOException
	{
		// CONNECT
		Socket smtp = new Socket(smtp_server, SMTP_PORT);
		BufferedReader smtp_in = new BufferedReader(
			new InputStreamReader(smtp.getInputStream()));
		PrintWriter smtp_out = new PrintWriter(
			smtp.getOutputStream());

		resultCheck(smtp, smtp_in, smtp_out, 220);

		// HELOコマンドの送付
		String myname = InetAddress.getLocalHost().getHostName();
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "HELO " + myname, 250);
		// MAIL FROMコマンドの送付
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "MAIL FROM:" + my_email_addr, 250);
		// RCPT TOコマンドの送付
		for (int i = 0; i < to.length; i++){
			sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
									  "RCPT TO:" + to[i], 250);
		}

		// DATAコマンドによるメールの送付
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "DATA", 354);
		//Subjectヘッダの送付
		smtp_out.print("Subject:" + subject + "\r\n");
		System.out.println("send> " + "Subject:" + subject) ;
		smtp_out.print("\r\n");
		//メッセージ各行の送付
		for(int i = 0;i < msgs.length - 1;++i) {
			smtp_out.print(msgs[i]+"\r\n");
			System.out.println("send> " + msgs[i]) ;
		}
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "\r\n.", 250);

		// QUITコマンドの送付
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "QUIT", 221);
		// コネクションのクローズ
		smtp.close();
	}

	// setAddressメソッド
	// 送信に利用するメールサーバや利用者のメールアドレスをセットします
	public void setAddress(){
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;

		try{
			while(cont){ //アドレス入力のループ
				System.out.println("メールサーバのアドレスを入力してください") ;
				smtp_server = lineread.readLine() ;
				System.out.println("あなたのメールアドレスを入力してください") ;
				my_email_addr = lineread.readLine() ;
				System.out.println(
					" メールサーバのアドレス：" + smtp_server) ;
				System.out.println(
					" あなたのメールアドレス：" + my_email_addr) ;
				System.out.println("以上でよろしいですか？(y/n)") ;
				buf = lineread.readLine() ;
				if("y".equals(buf)) cont = false ;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// setMsgsメソッド
	// 送信メッセージを取り込みます
	public String[] setMsgs(){
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;
		Vector msgs_list = new Vector();
		String[] msgs = null ;

		try{
			System.out.println("送信したいメッセージを入力してください") ;
			System.out.println(" (ピリオドで終了)") ;
			while(cont){ // メッセージの入力
				buf = lineread.readLine() ;
				msgs_list.addElement(buf);
				if(".".equals(buf)) cont = false ;
			}
			msgs = new String[msgs_list.size()];
			msgs_list.copyInto(msgs);
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return(msgs) ;
	}

	// mainprocメソッド
	// 引数の処理などを行います
	public void mainproc(String[] args){
		String usage =
			"java Mail [-s subject] to-addr ...";
		String subject = "";
		Vector to_list = new Vector();

		// サブジェクトやあて先の処理
		for (int i = 0; i < args.length; i++){
			if ("-s".equals(args[i])){
				i++;
				subject = args[i];
			}else{
				to_list.addElement(args[i]);
			}
		}
		// 入力された引数の文字列へ変換
		if (to_list.size() > 0){
			try {
				String[] to = new String[to_list.size()];
				to_list.copyInto(to);
				setAddress() ;
				String[] msgs = setMsgs() ;
				send(subject, to,msgs);
			}catch(IOException e){
				e.printStackTrace();
			}
		}else{
			System.out.println("usage: " + usage);
		}
	}

	// mainメソッド
	// Mail クラスのオブジェクトを生成します
	public static void main(String[] args){
		Mail m = new Mail() ;
		m.mainproc(args) ;
	}
}