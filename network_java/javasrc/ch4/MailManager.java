// ���[������M�v���O����MMGui.java�p�t�@�C��MailManager.java
// ���̃t�@�C���ɂ�,���[���̑���M�@�\����������N���X���i�[����Ă��܂�
// MMGui.java�̃R���p�C���ɂ�,MailManager.java�t�@�C�����K�v�ł�

// ���C�u�����̗��p
import java.util.*;
import java.io.*;
import java.net.*;

// MailManager�N���X
public class MailManager {
	// MailManager�̋@�\���Ď�����debug�@�\���g���ꍇ�ɂ�
	// �ϐ�debug��true�ɃZ�b�g���Ă�������
	static boolean debug = true;

	// ���[������M�ɕK�v�ȃI�u�W�F�N�g�̐錾
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

	// �R���X�g���N�^
	// �T�[�o�̐ݒ�⃆�[�U���̐ݒ�Ȃ�,���[���N���C�A���g�ŕK�v�ƂȂ�
	// �e��̐ݒ���s���܂�
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

	// list���\�b�h
	// ��M�ς݂̃��[���ꗗ��\�����܂�
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

	// readMail���\�b�h
	// ���b�Z�[�W�̖{����Ԃ��܂�
	public String readMail(int i){
		try {
			Mail mail = new Mail(i);
			return mail.message;
		}catch (FileNotFoundException ex){
			return null;
		}
	}

	// sendmail���\�b�h
	// SmtpClient�N���X�̃I�u�W�F�N�g���g���ă��[���𑗐M���܂�
	public void sendmail(
		String to, String subject, String message){
		SmtpClient smtp = new SmtpClient(smtpServ, codeNet);
		String[] dummy = new String[1];
		dummy[0] = to;
		smtp.sendmail(fromString, dummy, subject, message);
	}

	// getmail���\�b�h
	// PopClient�N���X�̃I�u�W�F�N�g���g����pop �T�[�o���烁�[������M���܂�
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

	// Mail�N���X
	// ���[���Ǘ��̂��߂̋@�\��񋟂��܂�
	public class Mail {
		String message;

		String subject;
		String to;
		String from;
		String replyto;
		File file;

		// �R���X�g���N�^
		// ���b�Z�[�W�������Ƃ���Mail�N���X�̃I�u�W�F�N�g���쐬���܂�
		// pop3 �T�[�o�����M�������b�Z�[�W�̏����Ɏg���܂�
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

		// �R���X�g���N�^
		// ��M�ς݃t�@�C���ɑΉ�����ԍ��������Ƃ���
		// Mail�N���X�̃I�u�W�F�N�g���쐬���܂�
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

		// parse���\�b�h
		// parseHeader���\�b�h��p���ă��b�Z�[�W�̃w�b�_����͂��܂�
		void parse(){
			from = parseHeader("From:");
			to = parseHeader("To:");
			subject = parseHeader("Subject:");
			replyto = parseHeader("Reply-To:");
		}

		// parseHeader���\�b�h
		// ���b�Z�[�W�̒�����w�肳�ꂽ�w�b�_�̏���T���o���܂�
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

// NetClient�N���X
// SmtpClient�N���X��PopClient�N���X�̋��ʕ�����^���܂�
class NetClient {
	static boolean debug = MailManager.debug;

	Socket s;
	BufferedReader from_server;
	PrintWriter to_server;
	String encode = "JIS";

	// �R���X�g���N�^
	// �����R�[�h���w�肷��ꍇ
	NetClient(String enc){
		encode = enc;

		if (debug) {
			System.out.println("NetClient.encode:" + encode);
		}
	}

	// �R���X�g���N�^
	// �����R�[�h���f�t�H���g�̏ꍇ
	NetClient(){
		this("JIS");
	}

	// connect���\�b�h
	// �T�[�o�Ƃ̐ڑ��p�\�P�b�g�ƃX�g���[�����쐬���܂�
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

	// disconnect���\�b�h
	// �T�[�o�Ƃ̐ڑ���ؒf���܂�
	public void disconnect(){
		try {
			s.close();
		}catch (IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// receive���\�b�h
	// �T�[�o����s��ǂݎ��܂�
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

	// send���\�b�h
	// �T�[�o�ɂP�s�f�[�^�𑗂�܂�
	public void send(String msg){
		to_server.println(msg);
		to_server.flush();
		if (debug) {
			System.out.println("SEND> " + msg);
		}
	}
}

// PopClient�N���X
// NetClient���p������,Pop3 ��M�@�\���������܂�
class PopClient extends NetClient{
	static final int POP3_PORT = 110;
	String server;
	String username;
	String password;
	boolean loginFlag = false;

	// �R���X�g���N�^
	// ��M�ɕK�v�ȏ����Z�b�g���܂�
	PopClient(String serv, String user, String pass, String enc){
		super(enc);
		username = user;
		password = pass;
		server = serv;
	}

	// login���\�b�h
	// POP3 �T�[�o��login���܂�
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

	// stat���\�b�h
	// �T�[�o��stat�R�}���h�𑗕t���܂�
	public String stat(){
		send("stat");
		String res = "";
		res = receive();
		if (res.startsWith("-ERR")){
			System.err.println(res);
		}
		return res;
	}

	// list���\�b�h
	// �T�[�o��list�R�}���h�𑗕t���܂�
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

	// retr���\�b�h
	// �T�[�o��retr�R�}���h�𑗕t���܂�
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

	// dele���\�b�h
	// �T�[�o��dele�R�}���h�𑗕t���܂�
	public String dele(int i){
		send("dele " + i);
		String res = receive();
		if (res.startsWith("-ERR")){
			System.err.println(res);
		}
		return res;
	}

	// quit���\�b�h
	// �T�[�o��quit�R�}���h�𑗕t���܂�
	public void quit(){
		if (loginFlag){
			send("quit");
			loginFlag = false;
		}
	}
}

// SmtpClient�N���X
// NetClient���p������,SMTP ��M�@�\���������܂�
class SmtpClient extends NetClient{
	int SMTP_PORT = 25;
	String server;

	// �R���X�g���N�^
	SmtpClient(String serv, String enc){
		super(enc);
		server = serv;
	}

	// sendCommandAndResultCheck���\�b�h
	// �T�[�o�ɃR�}���h�𑗕t��,���̌��ʂ�resultCheck���\�b�h�Ń`�F�b�N���܂�
	void sendCommandAndResultCheck(String command, int successCode){
		send(command);
		resultCheck(successCode);
	}

	// resultCheck���\�b�h
	// �T�[�o����̕ԓ��R�[�h����͂�,�G���[�Ȃ�ʐM���I�����܂�
	void resultCheck(int successCode){
		String res = receive();
		if (Integer.parseInt(res.substring(0,3)) != successCode){
			disconnect();
			throw new RuntimeException(res);
		}
	}

	// sendmail���\�b�h
	// SMTP��p����,�T�[�o�Ƀ��b�Z�[�W�𑗐M���܂�
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