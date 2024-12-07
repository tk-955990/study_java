// メール送受信プログラムMMGui.java用ファイルMailManager.java
// このファイルには,メールの送受信機能を実現するクラスが格納されています
// MMGui.javaのコンパイルには,MailManager.javaファイルが必要です

// ライブラリの利用
import java.util.*;
import java.io.*;
import java.net.*;

// MailManagerクラス
public class MailManager {
	// MailManagerの機能を監視するdebug機能を使う場合には
	// 変数debugをtrueにセットしてください
	static boolean debug = true;

	// メール送受信に必要なオブジェクトの宣言
	Properties property;
	String home;
	String cf;
	File mailDir;
	String fsep;

	String username;
	String password;
	String fromString;

	String popServ;
	String smtpServ;

	boolean keepmail = false;
	String codeNet;
	String codeFile;

	int lastMail_NO = 0;

	// コンストラクタ
	// サーバの設定やユーザ名の設定など,メールクライアントで必要となる
	// 各種の設定を行います
	public MailManager ()
	{
		Properties prop = System.getProperties();
		home = prop.getProperty("user.home");
		fsep = prop.getProperty("file.separator");
		cf = home + fsep + "jet.cf";

		property = new Properties();
		try {
			property.load(new FileInputStream(cf));
		}catch (FileNotFoundException ex){
			System.err.println("file not found:" + cf);
			System.exit(1);
		}catch (IOException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		username = property.getProperty("username");
		password = property.getProperty("password");
		popServ = property.getProperty("popserver");
		smtpServ = property.getProperty("smtpserver");
		fromString = property.getProperty("from");
		if (fromString == null){
			fromString = username + "@" + popServ;
		}
		String dummy = property.getProperty("keepmail");
		keepmail =
			((dummy == null)? false:
			(("off".equalsIgnoreCase(dummy))? false: true));
		codeNet = property.getProperty("code.net", "JIS");
		codeFile = property.getProperty("code.file", "SJIS");

		String propMailDir = property.getProperty("maildir");
		mailDir = new File(propMailDir);
		if (!mailDir.isAbsolute()){
			mailDir = new File(home + fsep + propMailDir);
		}

		if (mailDir.exists()){
			if (!mailDir.isDirectory()){
				System.err.println("not a directory:"
					+ mailDir.toString());
				System.exit(1);
			}
		}else{
			System.err.println("not found mail directory:"
				+ mailDir.toString());
			System.err.println("and create it.");
			mailDir.mkdir();
		}

		String[] files = mailDir.list();
		for (int i = 0; i < files.length; i++){
			try {
				int num = Integer.parseInt(files[i]);
				if (lastMail_NO < num){
					lastMail_NO = num;
				}
			}catch (NumberFormatException ex){
				;
			}
		}
	}

	// listメソッド
	// 受信済みのメール一覧を表示します
	public String[] list(){
		Vector v = new Vector();
		for (int i = 1; i <= lastMail_NO; i++){
			try {
				if (debug){
					System.out.println("MM.list: scan file " + i);
				}
				Mail mail = new Mail(i);
				String space10 = "          ";

				String from = mail.from + space10 + space10;
				from = from.substring(0, 20);
				String subject =
					mail.subject + space10 + space10 + space10 + space10;
				subject = subject.substring(0, 40);
				String num = i + space10;
				v.addElement(
					num.substring(0, 8) + "[" + from + "]" + subject);
			}catch (FileNotFoundException ex){
			}
		}
		String[] res = new String[v.size()];
		v.copyInto(res);
		return res;
	}

	// readMailメソッド
	// メッセージの本文を返します
	public String readMail(int i){
		try {
			Mail mail = new Mail(i);
			return mail.message;
		}catch (FileNotFoundException ex){
			return null;
		}
	}

	// sendmailメソッド
	// SmtpClientクラスのオブジェクトを使ってメールを送信します
	public void sendmail(
		String to, String subject, String message){
		SmtpClient smtp = new SmtpClient(smtpServ, codeNet);
		String[] dummy = new String[1];
		dummy[0] = to;
		smtp.sendmail(fromString, dummy, subject, message);
	}

	// getmailメソッド
	// PopClientクラスのオブジェクトを使ってpop サーバからメールを受信します
	public int getmail(){
		int n = 0;
		PopClient pop = new PopClient(popServ, username, password,
									  codeNet);
		pop.login();
		String res = pop.stat();
		StringTokenizer st = new StringTokenizer(res);
		if ("+OK".equals(st.nextToken())){
			n = Integer.parseInt(st.nextToken());
			for (int i = 1; i <= n; i++){
				try {
					new Mail(pop.retr(i));
					if (!keepmail){
					pop.dele(i);
				}
				}catch (IOException ex){
					ex.printStackTrace();
					System.exit(1);
				}
			}
		}
		pop.quit();
		return n;
	}

	// Mailクラス
	// メール管理のための機能を提供します
	public class Mail {
		String message;

		String subject;
		String to;
		String from;
		String replyto;
		File file;

		// コンストラクタ
		// メッセージを引数としてMailクラスのオブジェクトを作成します
		// pop3 サーバから受信したメッセージの処理に使います
		Mail(String msg) throws IOException{
			message = msg;
			lastMail_NO++;
			file = new File(mailDir.getPath() + fsep +
							lastMail_NO);
			PrintWriter mail =
				new PrintWriter(new OutputStreamWriter
							(new FileOutputStream(file), codeFile));
			mail.print(msg);
			mail.flush();
			mail.close();
			parse();
		}

		// コンストラクタ
		// 受信済みファイルに対応する番号を引数として
		// Mailクラスのオブジェクトを作成します
		Mail(int i) throws FileNotFoundException{
			try {
				file = new File(mailDir.getPath() + fsep + i);
				InputStreamReader mailIn =
					new InputStreamReader(new FileInputStream(file),
										  codeFile);
				char[] cbuf = new char[1024];
				StringBuffer sb = new StringBuffer();
				while (true){
					int n = mailIn.read(cbuf, 0, 1024);
					if (n == -1){
						break;
					}
					sb.append(cbuf, 0, n);
				}
				message = sb.toString();
			}catch (IOException ex){
				if (ex instanceof FileNotFoundException){
					throw (FileNotFoundException) ex;
				}else{
					ex.printStackTrace();
					System.exit(1);
				}
			}
			parse();
		}

		// parseメソッド
		// parseHeaderメソッドを用いてメッセージのヘッダを解析します
		void parse(){
			from = parseHeader("From:");
			to = parseHeader("To:");
			subject = parseHeader("Subject:");
			replyto = parseHeader("Reply-To:");
		}

		// parseHeaderメソッド
		// メッセージの中から指定されたヘッダの情報を探し出します
		String parseHeader(String header){
			int begin = 0;
			int end = 0;
			for (end = message.indexOf("\n", begin); end >= 0 &&
				 begin >= 0; begin = end + 1, end
				 = message.indexOf("\n", begin)){
				String line = message.substring(begin, end);
				if ("".equals(line)){
					break;
				}
				if (line.startsWith(header))
					return line.substring(header.length());
			}
			return null;
		}
	}
}

// NetClientクラス
// SmtpClientクラスとPopClientクラスの共通部分を与えます
class NetClient {
	static boolean debug = MailManager.debug;

	Socket s;
	BufferedReader from_server;
	PrintWriter to_server;
	String encode = "JIS";

	// コンストラクタ
	// 文字コードを指定する場合
	NetClient(String enc){
		encode = enc;

		if (debug) {
			System.out.println("NetClient.encode:" + encode);
		}
	}

	// コンストラクタ
	// 文字コードがデフォルトの場合
	NetClient(){
		this("JIS");
	}

	// connectメソッド
	// サーバとの接続用ソケットとストリームを作成します
	public void connect(String server, int port){
		try {
			s = new Socket(server, port);
			from_server = new BufferedReader(
			new InputStreamReader(s.getInputStream(),encode));
			to_server = new PrintWriter(
			new OutputStreamWriter(s.getOutputStream(),encode));
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// disconnectメソッド
	// サーバとの接続を切断します
	public void disconnect(){
		try {
			s.close();
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// receiveメソッド
	// サーバから行を読み取ります
	public String receive(){
		String res = null;
		try {
			res = from_server.readLine();
			if (debug) {
				System.out.println("RECV> " + res);
			}
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		return res;
	}

	// sendメソッド
	// サーバに１行データを送ります
	public void send(String msg){
		to_server.println(msg);
		to_server.flush();
		if (debug) {
			System.out.println("SEND> " + msg);
		}
	}
}

// PopClientクラス
// NetClientを継承して,Pop3 受信機能を実現します
class PopClient extends NetClient{
	static final int POP3_PORT = 110;
	String server;
	String username;
	String password;
	boolean loginFlag = false;

	// コンストラクタ
	// 受信に必要な情報をセットします
	PopClient(String serv, String user, String pass, String enc){
		super(enc);
		username = user;
		password = pass;
		server = serv;
	}

	// loginメソッド
	// POP3 サーバにloginします
	public boolean login(){
		if (loginFlag)
			return loginFlag;
		connect(server, POP3_PORT);
		String res = receive();
		if (res.startsWith("-ERR")){
			System.err.println("connect failed.:" + res);
			disconnect();
			return false;
		}
		send("user " + username);
		res = receive();
		if (res.startsWith("-ERR")){
			System.err.println("login user failed.:" + res);
			disconnect();
			return false;
		}
		send("pass " + password);
		res = receive();
		if (res.startsWith("-ERR")){
			System.err.println("login pass failed.:" + res);
			disconnect();
			return false;
		}
		return loginFlag = true;
	}

	// statメソッド
	// サーバにstatコマンドを送付します
	public String stat(){
		send("stat");
		String res = "";
		res = receive();
		if (res.startsWith("-ERR")){
			System.err.println(res);
		}
		return res;
	}

	// listメソッド
	// サーバにlistコマンドを送付します
	public String list(){
		String res = "";
		send("list");
		String line = receive();
		if (line.startsWith("-ERR")){
			System.err.println(line);
			res = line;
		}else{
		while (true){
			line = receive();
			if (".".equals(line)){
				break;
			}
			res += line + "\n";
			}
		}
		return res;
	}

	// retrメソッド
	// サーバにretrコマンドを送付します
	public String retr(int i){
		String res = "";
		send("retr " + i);
		String line = receive();
		if (line.startsWith("-ERR")){
			System.err.println(line);
			res = line;
		}else{
			while (true){
				line = receive();
				if (".".equals(line)){
					break;
				}
				res += line + "\n";
			}
		}
		return res;
	}

	// deleメソッド
	// サーバにdeleコマンドを送付します
	public String dele(int i){
		send("dele " + i);
		String res = receive();
		if (res.startsWith("-ERR")){
			System.err.println(res);
		}
		return res;
	}

	// quitメソッド
	// サーバにquitコマンドを送付します
	public void quit(){
		if (loginFlag){
			send("quit");
			loginFlag = false;
		}
	}
}

// SmtpClientクラス
// NetClientを継承して,SMTP 受信機能を実現します
class SmtpClient extends NetClient{
	int SMTP_PORT = 25;
	String server;

	// コンストラクタ
	SmtpClient(String serv, String enc){
		super(enc);
		server = serv;
	}

	// sendCommandAndResultCheckメソッド
	// サーバにコマンドを送付し,その結果をresultCheckメソッドでチェックします
	void sendCommandAndResultCheck(String command, int successCode){
		send(command);
		resultCheck(successCode);
	}

	// resultCheckメソッド
	// サーバからの返答コードを解析し,エラーなら通信を終了します
	void resultCheck(int successCode){
		String res = receive();
		if (Integer.parseInt(res.substring(0,3)) != successCode){
			disconnect();
			throw new RuntimeException(res);
		}
	}

	// sendmailメソッド
	// SMTPを用いて,サーバにメッセージを送信します
	public void sendmail(String from, String[] to,
						 String subject, String message){
		// CONNECT
		connect(server, SMTP_PORT);
		resultCheck(220);

		// HELO
		try {
			String myname = InetAddress.getLocalHost().getHostName();
			sendCommandAndResultCheck("HELO " + myname, 250);
		}catch (UnknownHostException ex){
			ex.printStackTrace();
			System.exit(1);
		}
		// MAIL FROM
		sendCommandAndResultCheck("MAIL FROM: " + from, 250);
		// RCPT TO
		for (int i = 0; i < to.length; i++){
			sendCommandAndResultCheck("RCPT TO: " + to[i], 250);
		}

		// DATA
		sendCommandAndResultCheck("DATA", 354);
		send("Subject: " + subject);
		send("From: " + from);
		String toString = to[0];
		for (int i = 1; i < to.length; i++){
			toString += "," + to[i];
		}
		send("To: " + toString);
		send("");
		send(message);
		send(".");
		resultCheck(250);

		// QUIT
		sendCommandAndResultCheck("QUIT", 221);
		// CLOSE
		disconnect();
	}
}