// telnet �v���O����Telnet.java
// ���̃v���O������,�w�肳�ꂽ�A�h���X�̃|�[�g�ɕW�����o�͂�ڑ����܂�
// �ڑ��悪telnet �̃|�[�g(23��)�̏ꍇ,�l�S�V�G�[�V�������s���܂�
// �l�S�V�G�[�V�����ł�,�T�[�o����̗v�������ׂĒf��܂�
// �g�����i�P�j java Telnet �T�[�o�A�h���X�|�[�g�ԍ�
// �g�����i�Q�j java Telnet �T�[�o�A�h���X
// �i�Q�j�̏ꍇ,�|�[�g�ԍ�23��(telnet)�����肵�܂�
// �N���̗�java Telnet kiku.fuis.fukui-u.ac.jp
// �I���ɂ̓R���g���[��C ����͂��Ă�������

// ���C�u�����̗��p
import java.net.*;
import java.io.*;

// Telnet�N���X
// Telnet�N���X��,�l�b�g���[�N�ڑ��̊Ǘ����s���܂�
// StreamConnector�N���X��p���ăX���b�h�������s���܂�
// �R���X�g���N�^�͂Q��ނ���,�g�����́i�P�j�i�Q�j�ɑΉ����Ă��܂�
public class Telnet {
	Socket serverSocket;//�ڑ��p�\�P�b�g
	public OutputStream serverOutput;//�l�b�g���[�N�o�͗p�X�g���[��
	public BufferedInputStream serverInput;// �����͗p�X�g���[��
	String host;// �ڑ���T�[�o�A�h���X
	int port; // �ڑ���T�[�o�|�[�g�ԍ�

	static final int DEFAULT_TELNET_PORT = 23;// telnet �̃|�[�g�ԍ�(23��)

	// �R���X�g���N�^�i�P�j�A�h���X�ƃ|�[�g�̎w�肪����ꍇ
	public Telnet(String host, int port){
		this.host = host;
		this.port = port;
	}

	// �R���X�g���N�^�i�Q�j�A�h���X�̎w��݂̂̏ꍇ
	public Telnet(String host){
		this(host, DEFAULT_TELNET_PORT);// telnet �|�[�g������
	}

	// openConnection���\�b�h
	//�A�h���X�ƃ|�[�g�ԍ�����\�P�b�g�����X�g���[�����쐬���܂�
	public void openConnection()
		throws IOException,UnknownHostException
	{
		serverSocket = new Socket(host, port);
		serverOutput = serverSocket.getOutputStream();
		serverInput = new
			BufferedInputStream(serverSocket.getInputStream());
		// �ڑ��悪telnet �|�[�g�Ȃ�,�l�S�V�G�[�V�������s���܂�
		if (port == DEFAULT_TELNET_PORT){
			negotiation(serverInput, serverOutput);
		}
	}

	// main_proc���\�b�h
	// �l�b�g���[�N�Ƃ̂��Ƃ������X���b�h���X�^�[�g�����܂�
	public void main_proc()
		throws IOException
	{
		try {
			// �X���b�h�p�N���XStreamConnector�̃I�u�W�F�N�g�𐶐����܂�
			StreamConnector stdin_to_socket =
				new StreamConnector(System.in, serverOutput);
			StreamConnector socket_to_stdout =
				new StreamConnector(serverInput, System.out);
			// �X���b�h�𐶐����܂�
			Thread input_thread = new Thread(stdin_to_socket);
			Thread output_thread = new Thread(socket_to_stdout);
			// �X���b�h���N�����܂�
			input_thread.start();
			output_thread.start();
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}

	// �l�S�V�G�[�V�����ɗp����R�}���h�̒�`
	static final byte IAC = (byte) 255;
	static final byte DONT = (byte) 254;
	static final byte DO = (byte) 253;
	static final byte WONT = (byte) 252;
	static final byte WILL = (byte) 251;

	// negotiation���\�b�h
	// NVT �ɂ��ʐM���l�S�V�G�[�g���܂�
	static void negotiation(
		BufferedInputStream in,OutputStream out)
		throws IOException
	{
		byte[] buff = new byte[3];//�R�}���h��M�p�z��
		while(true) {
			in.mark(buff.length);
			if (in.available() >= buff.length) {
				in.read(buff);
				if (buff[0] != IAC){// �l�S�V�G�[�V�����I��
					in.reset();
					return;
				} else if (buff[1] == DO) {//DO�R�}���h�ɑ΂��Ắc
					buff[1] = WONT;// WON'T�ŕԓ����܂�
					out.write(buff);
				}
			}
		}
	}

	// main���\�b�h
	// TCP �R�l�N�V�������J���ď������J�n���܂�
	public static void main(String[] arg){
		try {
			Telnet t = null;
			// �����̌��ɂ���ăR���X�g���N�^���قȂ�܂�
			switch (arg.length){
			case 1:// �T�[�o�A�h���X�݂̂̎w��
				t = new Telnet(arg[0]);
				break;
			case 2:// �A�h���X�ƃ|�[�g�̎w��
				t = new Telnet(arg[0], Integer.parseInt(arg[1]));
				break;
			default:// �g�������Ԉ���Ă���ꍇ
				System.out.println(
					"usage: java Telnet <host name> {<port number>}");
				return;
			}
			t.openConnection();
			t.main_proc();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

// StreamConnector�N���X
// �X�g���[�����󂯎��,���҂��������ăf�[�^���󂯓n���܂�
// StreamConnector�N���X�̓X���b�h���\�����邽�߂̃N���X�ł�
class StreamConnector implements Runnable {
	InputStream src = null;
	OutputStream dist = null;

	// �R���X�g���N�^���o�̓X�g���[�����󂯎��܂�
	public StreamConnector(InputStream in, OutputStream out){
		src = in;
		dist = out;
	}
	// �����̖{��
	// �X�g���[���̓ǂݏ����𖳌��ɌJ��Ԃ��܂�
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