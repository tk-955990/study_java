// Pop3 �ɂ�郁�[����M�v���O����Pop.java
// ���̃v���O������,�w�肳�ꂽPOP�T�[�o���烁�[�����擾���܂�
// �g����java Pop
// �N����,POP�T�[�o�̃A�h���X�𕷂��Ă��܂�
// �Θb�̗�
// POP �T�[�o�̃A�h���X����͂��Ă�������
// kiku.fuis.fukui-u.ac.jp
// POP�T�[�o�̃A�h���X�Fkiku.fuis.fukui-u.ac.jp
// �ȏ�ł�낵���ł����H(y/n)
// y
// +OK QPOP (version 2.53) at kiku.fuis.fukui-u.ac.jp starting.
// <4752.923273295@kiku.fuis.fukui-u.ac.jp>
// ���̌�,POP �̃��[�U���ƃp�X���[�h�𕷂��Ă��܂�
// �Θb�̗�
// user name����͂��Ă�������
// odaka
// Password����͂��Ă�������
// XXXXXXXX
// user name�Fodaka
// Password�FXXXXXXXX
// �ȏ�ł�낵���ł����H(y/n)

// ���C�u�����̗��p
import java.net.*;
import java.io.*;
import java.util.*;

// Pop�N���X
// Pop�N���X��,Pop �v���O�����̖{�̂ł�
public class Pop {
	final int POP_PORT = 110;// POP�ڑ��p�|�[�g�ԍ�(110 ��)
	BufferedReader pop_in = null ;// �ǂݏo���p�X�g���[��
	PrintWriter pop_out = null ;// �������ݗp�X�g���[��
	Socket pop = null ;// �\�P�b�g

	// transaction���\�b�h
	// POP3�̃Z�b�V������i�߂܂�
	public void transaction()
		throws IOException
	{
		String buf = "" ;
		BufferedReader lineread // �W�����͓ǂݎ��p
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;

		while(cont){ //���͂̃��[�v
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

	// getLines���\�b�h
	// �񓚂������s�ɓn��R�}���h�̏������s���܂�
	public void getLines(String command)
		throws IOException
	{
		boolean cont = true ;
		String buf = null ;
		// �R�}���h�̑��t
		pop_out.print(command +"\r\n");
		pop_out.flush() ;
		String res = pop_in.readLine();//�ԓ��̓ǂݎ��
		System.out.println(res) ;
		// �����ԓ��R�[�h��+OK�łȂ���΁E�E�E
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// �R�l�N�V��������܂�
			throw new RuntimeException(res);
		}
		while(cont){// �����s�̓ǂݎ��
			buf = pop_in.readLine();//�P�s�ǂݎ��
			System.out.println(buf) ;
			// �s���̒P�ƃs���I�h�ŏI��
			if(".".equals(buf)) cont = false ;
		}
	}

	// getSingleLine���\�b�h
	// �ԓ����P�s�ƂȂ�R�}���h�̏������s���܂�
	public void getSingleLine(String command)
		throws IOException
	{
		// �R�}���h�̑��t
		pop_out.print(command +"\r\n");
		pop_out.flush() ;
		System.out.println(command) ;
		String res = pop_in.readLine();//�ԓ��̓ǂݎ��
		System.out.println(res) ;
		// �����ԓ��R�[�h��+OK�łȂ���΁E�E�E
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// �R�l�N�V��������܂�
			throw new RuntimeException(res);
		}
	}

	// authorization���\�b�h
	// TCP�R�l�N�V�����̐ݒ�ƔF�؂��s���܂�
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

		//POP�T�[�o�̃A�h���X�̐ݒ�
		while(cont){ //���͂̃��[�v
			System.out.println("POP �T�[�o�̃A�h���X����͂��Ă�������") ;
			pop_server = lineread.readLine() ;
			System.out.println(" POP �T�[�o�̃A�h���X�F" + pop_server) ;
			System.out.println("�ȏ�ł�낵���ł����H(y/n)") ;
			buf = lineread.readLine() ;
			if("y".equals(buf)) cont = false ;
		}
		// �T�[�o�Ƃ̐ڑ�
		pop = new Socket(pop_server, POP_PORT);
		pop_in = new BufferedReader(
				 new InputStreamReader(pop.getInputStream()));
		pop_out = new PrintWriter(pop.getOutputStream());
		// ���b�Z�[�W�̎擾
		String res = pop_in.readLine();//�ԓ��̓ǂݎ��
		System.out.println(res) ;
		// �����ԓ��R�[�h��+OK�łȂ���΁E�E�E
		if (!("+OK".equals(res.substring(0,3)))){
			pop.close();// �R�l�N�V��������܂�
			throw new RuntimeException(res);
		}

		// �F�؏��̎擾
		cont = true ;
		while(cont){ //���͂̃��[�v
			System.out.println("user name����͂��Ă�������") ;
			username = lineread.readLine() ;
			System.out.println("Password����͂��Ă�������") ;
			password = lineread.readLine() ;
			System.out.println(" user name�F" + username) ;
			System.out.println(" Password�F" + password) ;
			System.out.println("�ȏ�ł�낵���ł����H(y/n)") ;
			buf = lineread.readLine() ;
			if("y".equals(buf)) cont = false ;
		}
		// USER�R�}���h��PASS�R�}���h�ɂ��F�؍��
		getSingleLine("USER " + username) ;
		getSingleLine("PASS " + password) ;
	}

	// update���\�b�h
	// POP3 �̃Z�b�V�������I�����܂�
	public void update()
	throws IOException
	{
		// QUIT
		getSingleLine("QUIT");
		pop.close();// �R�l�N�V��������܂�
	}

	// mainproc���\�b�h
	// POP �̊e�������\�b�h���Ăяo���܂�
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

	// main���\�b�h
	// Pop�N���X�̃I�u�W�F�N�g�𐶐����܂�
	public static void main(String[] args)
		throws IOException
	{
		Pop p = new Pop() ;
		p.mainproc(args) ;
	}
}