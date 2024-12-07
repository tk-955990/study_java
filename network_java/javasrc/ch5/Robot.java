// ��C�Q�[����N���C�A���g�v���O����Robot.java
// ���̃v���O������,�C�Q�[���̃N���C�A���g�v���O�����ł�
// ���߂�ꂽ�菇�ŊC�Q�[�����v���C���܂�
// �g����java Robot �ڑ���T�[�o�A�h���X�Q�[���Q���Җ�
// �N����,�w�肵���T�[�o�Ɛڑ���,�����I�ɃQ�[�����s���܂�
// �N����,�w��񐔂̌J��Ԃ��̌�,logout���܂�
// ���̃v���O������logout�R�}���h������܂���
// �v���O������r���Œ�~����ɂ�,�ȉ��̎菇�𓥂�ł�������
// �i�P�j�R���g���[��C ����͂���Robot�v���O�������~���܂�
// �i�Q�jT1.java�v���O�����Ȃ�,�ʂ̃N���C�A���g���g����Robot�Ɠ������O��login���܂�
// �i�R�jlogout���܂�
// �ʃN���C�A���g�����logout��Ƃ��ȗ������,�T�[�o��ɏ�񂪎c���Ă��܂��܂�

// ���C�u�����̗��p
import java.net.*;// �l�b�g���[�N�֘A
import java.io.*;
import java.util.*;

// Robot�N���X
public class Robot {
	// ���{�b�g�̓���^�C�~���O���K�肷��ϐ�sleeptime
	int sleeptime = 50 ;
	// ���{�b�g��logout����܂ł̎��Ԃ��K�肷��ϐ�timeTolive
	int timeTolive = 50 ;
	// �R���X�g���N�^
	public Robot (String[] args)
	{
		login(args[0],args[1]) ;
		try{
			for(;timeTolive > 0; -- timeTolive){
				System.out.println("����" + timeTolive + "��") ;
				// 10 ��ɓn��,sleeptime*100�~���b������left�R�}���h�𑗂�܂�
				for(int i = 0;i < 10;++i){
					Thread.sleep(sleeptime * 100) ;
					out.println("left");
					out.flush() ;
				}
				// 10 ��ɓn��,sleeptime�b������right�R�}���h�𑗂�܂�
				for(int i = 0;i < 10;++i){
					Thread.sleep(sleeptime * 100) ;
					out.println("right");
					out.flush() ;
				}
				// up�R�}���h��1 �񑗂�܂�
				out.println("up");
				out.flush() ;
			}
			// logout����
			out.println("logout") ;
			out.flush() ;
			server.close() ;
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// login�֘A�̃I�u�W�F�N�g
	Socket server;// �Q�[���T�[�o�Ƃ̐ڑ��\�P�b�g
	int port = 10000;// �ڑ��|�[�g
	BufferedReader in;// ���̓X�g���[��
	PrintWriter out;// �o�̓X�g���[��
	String name;// �Q�[���Q���҂̖��O

	// login���\�b�h
	// �T�[�o�ւ�login�������s���܂�
	void login(String host, String name){
		try {
			// �T�[�o�Ƃ̐ڑ�
			this.name = name;
			server = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
			  server.getInputStream()));
			out = new PrintWriter(server.getOutputStream());

			// login�R�}���h�̑��t
			out.println("login " + name);
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// main���\�b�h
	// Robot���N�����܂�
	public static void main(String[] args){
		new Robot(args);
	}
}