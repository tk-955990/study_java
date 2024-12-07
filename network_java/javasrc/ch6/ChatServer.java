// �`���b�g�T�[�oChatServer.java
// ���̃v���O������,�`���b�g�̃T�[�o�v���O�����ł�
// �g����java ChatServer [�|�[�g�ԍ�]
// �|�[�g�ԍ����ȗ������,�|�[�g�ԍ�6000 �Ԃ��g���܂�
// �N���̗�java ChatServer
// �I���ɂ̓R���g���[��C ����͂��Ă�������

// ���̃T�[�o�ւ̐ڑ��ɂ�Telnet.java�Ȃǂ��g���Ă�������
// �ڑ����~�߂����Ƃ��ɂ�,�s����quit�Ɠ��͂��Ă�������

// ���C�u�����̗��p
import java.io.*;
import java.net.*;
import java.util.*;

// ChatServer�N���X
public class ChatServer {
	static final int DEFAULT_PORT = 6000;//�|�[�g�ԍ��ȗ�����6000 �Ԃ��g���܂�
	static ServerSocket serverSocket;
	static Vector connections;

	// sendAll���\�b�h
	// �e�N���C�A���g�Ƀ��b�Z�[�W�𑗂�܂�
	public static void sendAll(String s){
		if (connections != null){// �R�l�N�V����������Ύ��s���܂�
			for (Enumeration e = connections.elements();
					e.hasMoreElements() ;) {
				try {
					PrintWriter pw = new PrintWriter((
					  (Socket) e.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				}catch (IOException ex){}
			}
		}
		System.out.println(s);
	}

	// addConnection���\�b�h
	// �N���C�A���g�Ƃ̐ڑ���ǉ����܂�
	public static void addConnection(Socket s){
		if (connections == null){
			connections = new Vector();
		}
		connections.addElement(s);
	}

	// deleteConnection���\�b�h
	// ����N���C�A���g�Ƃ̃R�l�N�V�������폜���܂�
	public static void deleteConnection(Socket s){
		if (connections != null){
			connections.removeElement(s);
		}
	}

	// main���\�b�h
	// �T�[�o�\�P�b�g�����,�N���C�A���g����̐ڑ���҂��󂯂܂�
	public static void main(String[] arg){
		int port = DEFAULT_PORT ;
		if (arg.length > 0) port = Integer.parseInt(arg[0]) ;
		try {
			serverSocket = new ServerSocket(port);
		}catch (IOException e){
			System.err.println(e);
			System.exit(1);
		}
		while (true) {
			try {
				Socket cs = serverSocket.accept();
				addConnection(cs);
				Thread ct = new Thread(new clientProc(cs));
				ct.start();
			}catch (IOException e){
				System.err.println(e);
			}
		}
	}
}

// clientProc�N���X
// �N���C�A���g�����p�X���b�h�̂ЂȌ`�ł�
class clientProc implements Runnable {
	Socket s;
	BufferedReader in;
	PrintWriter out;
	String name = null;
	ChatServer server = null ;

	//�R���X�g���N�^
	public clientProc(Socket s) throws IOException {
		this.s = s;
		in = new BufferedReader(new InputStreamReader(
		  s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
	}

	// �X���b�h�̖{��
	// �e�N���C�A���g�Ƃ̐ڑ��������s���܂�
	public void run(){
		try {
			while (name == null){
				out.print("�����O�́H: ");
				out.flush();
				name = in.readLine();
			}
			String line = in.readLine();
			while (!"quit".equals(line)){
				ChatServer.sendAll(name + "> " +line);
				line = in.readLine();
			}
			ChatServer.deleteConnection(s);
			s.close();
		}catch (IOException e){
			try {
				s.close();
			}catch (IOException e2){}
		}
	}
}