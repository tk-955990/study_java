// SMTP によるメール受信プログラムReceiver.java
// このプログラムは,受信用メールサーバとして動作します
// 使い方java Receiver
// 起動の例java Receiver
// このプログラムは,他から送られたメールを標準出力に書き出します

// ライブラリの利用
import java.net.*;
import java.io.*;
import java.util.*;

// Receiverクラス
// Receiverクラスは,Receiverプログラムの本体です
public class Receiver {
	final int SMTP_PORT = 25;// SMTP 接続用ポート番号(25 番)

	// sendResultメソッド
	// クライアントに対してメッセージを送ります
	public void sendResult(Socket smtp, BufferedReader smtp_in,
						   PrintWriter smtp_out,
						   String command)
		throws IOException
	{
		smtp_out.print(command + "\r\n");//メッセージの送信
		smtp_out.flush();
		System.out.println("send> " + command);//送信内容の表示
	}

	// getCommandメソッド
	// クライアントからのコマンドを読み取ります
	// コマンドは空白で区切って,Stringの配列とします
	public String[] getCommand(Socket smtp, BufferedReader smtp_in,
							   PrintWriter smtp_out)
		throws IOException
	{
		String res = smtp_in.readLine();//返答の読み取り
		System.out.println("recv> " + res);
		//空白で区切り,結果を呼び出し側に返します
		StringTokenizer st = new StringTokenizer(res) ;
		String[] results = new String[st.countTokens()] ;
		for(int i = 0;st.hasMoreTokens();++i){
			results[i] = st.nextToken() ;
		}
		return results ;
	}

	// procCommandメソッド
	// クライアントからのコマンドが正当かどうかを調べます
	// その結果をクライアントに返します
	// もしコマンドが不正なら,接続を解除します
	public void procCommand(Socket smtp, BufferedReader smtp_in,
							PrintWriter smtp_out,String command,
							String acceptableCommand,String result)
		throws IOException
	{
		if(acceptableCommand.equalsIgnoreCase(command)){
			sendResult(smtp, smtp_in, smtp_out,result) ;
		}else{
			sendResult(smtp, smtp_in, smtp_out,
					   "421 service not available") ;
			smtp.close() ;
		}
	}

	// receiveメソッド
	// SMTP のセッションを進めます
	public void receive(Socket smtp)
		throws IOException
	{
		try{
			String[] commands = null ;
			// CONNECT処理
			BufferedReader smtp_in = new BufferedReader(
				new InputStreamReader (smtp.getInputStream()));
			PrintWriter smtp_out = new PrintWriter(
				smtp.getOutputStream());

			String myname = InetAddress.getLocalHost().getHostName();
			sendResult(smtp, smtp_in, smtp_out,
					   "220 " + myname + " SMTP");
			// HELOコマンドの処理
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"HELO",
				"250 " + "Hello " + commands[1] + ", pleased to meet you") ;

			// MAIL FROMコマンドの処理
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"MAIL","250 "
				+ commands[1].substring(commands[1].indexOf(":") + 1)
				+ "... Sender ok") ;
			// RCPT TOコマンドの処理
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],
				"RCPT","250 Recipient ok") ;
			// DATAコマンドの処理
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"DATA",
				"354 Enter mail, end with \".\" on a line by itself") ;

			//メッセージ各行の受信
			String res = smtp_in.readLine() ;
			while(!(res.equals("."))){
				System.out.println(res) ;
				res = smtp_in.readLine() ;
			}
			sendResult(smtp, smtp_in, smtp_out,
				"250 Message accepted for delivery") ;
			// QUITコマンドの処理
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out, commands[0],"QUIT","221 "
				+ myname + " closing connection") ;
		}catch(Exception e){
		}
		// コネクションのクローズ
		smtp.close();
	}

	// mainprocメソッド
	// ServerSocketを作り,接続を待ち受けます
	public void mainproc(){
		Socket sock ;
		try {
			ServerSocket serversock = new ServerSocket(SMTP_PORT,1) ;
			while(true){
				sock = serversock.accept() ;
				receive(sock) ;
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	// mainメソッド
	// Receiverクラスのオブジェクトを生成します
	public static void main(String[] args){
		Receiver r = new Receiver() ;
		r.mainproc() ;
	}
}