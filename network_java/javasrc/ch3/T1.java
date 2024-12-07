// telnet�̌��`�ƂȂ�v���O����T1.java
// ���̃v���O������,�w�肳�ꂽ�A�h���X�̃|�[�g�ɕW�����o�͂�ڑ����܂�
// �g����java T1 �T�[�o�A�h���X�|�[�g�ԍ�
// �N���̗�java T1 kiku.fuis.fukui-u.ac.jp 80
// �I���ɂ̓R���g���[��C ����͂��Ă�������

// ���C�u�����̗��p
import java.net.*;
import java.io.*;

// T1�N���X
// T1�N���X��,�l�b�g���[�N�ڑ��̊Ǘ����s���܂�
// StreamConnector�N���X��p���ăX���b�h�������s���܂�
public class T1 {
	// �\�P�b�g�̏���
	protected Socket serverSocket;//�ڑ��p�\�P�b�g
	public OutputStream serverOutput;//�l�b�g���[�N�o�͗p�X�g���[��
	public BufferedInputStream serverInput;// �����͗p�X�g���[��

	// openConnection���\�b�h
	//�A�h���X�ƃ|�[�g�ԍ�����\�P�b�g�����X�g���[�����쐬���܂�
	public void openConnection(String host,int port)
		throws IOException,UnknownHostException
	{
		serverSocket = new Socket(host, port);
		serverOutput = serverSocket.getOutputStream();
		serverInput
			= new BufferedInputStream(serverSocket.getInputStream());
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

	// main���\�b�h
	// TCP �R�l�N�V�������J���ď������J�n���܂�
	public static void main(String[] arg){
		try {
			T1 t = null;
			t = new T1();
			t.openConnection(arg[0], Integer.parseInt(arg[1]));
			t.main_proc();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}
}

// StreamConnector�N���X
// �X�g���[�����󂯎��C���҂��������ăf�[�^���󂯓n���܂�
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