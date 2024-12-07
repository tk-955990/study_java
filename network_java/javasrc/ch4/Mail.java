// SMTP �ɂ�郁�[�����M�v���O����Mail.java
// ���̃v���O������,�w�肳�ꂽ���[���T�[�o�ɑ΂��ă��b�Z�[�W�𑗐M���܂�
// �g����java Mail [-s subject] ���M����̃A�h���X
// �g�p��̒���
// subject�͋󔒂Ȃ��̈��Ƃ��Ă�������
// ���M����̃A�h���X�͕����w��ł��܂�
// �N���̗�java Mail -s Hello odaka@kiku.fuis.fukui-u.ac.jp
// �N����,���M���˗����郁�[���T�[�o�̃A�h���X��,���M�҂̃A�h���X�𕷂��Ă��܂�
// ���ꂼ��,�A�h���X�𓚂��Ă�������
// �A�h���X�m�F�̗�
// ���[���T�[�o�̃A�h���X����͂��Ă�������
// kiku.fuis.fukui-u.ac.jp
// ���Ȃ��̃��[���A�h���X����͂��Ă�������
// odaka@take.fuis.fukui-u.ac.jp
// ���[���T�[�o�̃A�h���X�Fkiku.fuis.fukui-u.ac.jp
// ���Ȃ��̃��[���A�h���X�Fodaka@take.fuis.fukui-u.ac.jp
// �ȏ�ł�낵���ł����H(y/n)

// ���C�u�����̗��p
import java.net.*;
import java.io.*;
import java.util.*;

// Mail�N���X
// Mail�N���X��,Mail �v���O�����̖{�̂ł�
public class Mail {
	final int SMTP_PORT = 25;// SMTP �ڑ��p�|�[�g�ԍ�(25 ��)
	String smtp_server = ""; // ���M�p�T�[�o
	String my_email_addr = "" ; // �����̃��[���A�h���X

	//sendCommandAndResultCheck���\�b�h
	//SMTP �R�}���h���T�[�o�ɑ��M��,�ԓ��R�[�h�̊m�F���s���܂�
	public void sendCommandAndResultCheck(Socket smtp,
										  BufferedReader smtp_in,
										  PrintWriter smtp_out,
										  String command,
										  int success_code)
		throws IOException
	{
		smtp_out.print(command + "\r\n");//�R�}���h�̑��M
		smtp_out.flush();
		System.out.println("send> " + command);//���M���e�̕\��
		resultCheck(smtp, smtp_in, smtp_out, success_code);
													//���ʂ̃`�F�b�N
	}

	// resultCheck���\�b�h
	// �ԓ��R�[�h���`�F�b�N����,�G���[�Ȃ�΃R�l�N�V��������܂�
	public void resultCheck(Socket smtp, BufferedReader smtp_in,
							PrintWriter smtp_out,
							int success_code)
		throws IOException
	{
		String res = smtp_in.readLine();//�ԓ��R�[�h�̓ǂݎ��
		System.out.println("recv> " + res);
		// �����ԓ��R�[�h�����҂����R�[�h(success_code)�łȂ���΁E�E�E
		if (Integer.parseInt(res.substring(0,3)) != success_code){
			smtp.close();// �R�l�N�V��������܂�
			throw new RuntimeException(res);
		}
	}

	// send���\�b�h
	// SMTP �̃Z�b�V������i�߂܂�
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

		// HELO�R�}���h�̑��t
		String myname = InetAddress.getLocalHost().getHostName();
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "HELO " + myname, 250);
		// MAIL FROM�R�}���h�̑��t
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "MAIL FROM:" + my_email_addr, 250);
		// RCPT TO�R�}���h�̑��t
		for (int i = 0; i < to.length; i++){
			sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
									  "RCPT TO:" + to[i], 250);
		}

		// DATA�R�}���h�ɂ�郁�[���̑��t
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "DATA", 354);
		//Subject�w�b�_�̑��t
		smtp_out.print("Subject:" + subject + "\r\n");
		System.out.println("send> " + "Subject:" + subject) ;
		smtp_out.print("\r\n");
		//���b�Z�[�W�e�s�̑��t
		for(int i = 0;i < msgs.length - 1;++i) {
			smtp_out.print(msgs[i]+"\r\n");
			System.out.println("send> " + msgs[i]) ;
		}
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "\r\n.", 250);

		// QUIT�R�}���h�̑��t
		sendCommandAndResultCheck(smtp, smtp_in, smtp_out,
								  "QUIT", 221);
		// �R�l�N�V�����̃N���[�Y
		smtp.close();
	}

	// setAddress���\�b�h
	// ���M�ɗ��p���郁�[���T�[�o�◘�p�҂̃��[���A�h���X���Z�b�g���܂�
	public void setAddress(){
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;

		try{
			while(cont){ //�A�h���X���͂̃��[�v
				System.out.println("���[���T�[�o�̃A�h���X����͂��Ă�������") ;
				smtp_server = lineread.readLine() ;
				System.out.println("���Ȃ��̃��[���A�h���X����͂��Ă�������") ;
				my_email_addr = lineread.readLine() ;
				System.out.println(
					" ���[���T�[�o�̃A�h���X�F" + smtp_server) ;
				System.out.println(
					" ���Ȃ��̃��[���A�h���X�F" + my_email_addr) ;
				System.out.println("�ȏ�ł�낵���ł����H(y/n)") ;
				buf = lineread.readLine() ;
				if("y".equals(buf)) cont = false ;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	// setMsgs���\�b�h
	// ���M���b�Z�[�W����荞�݂܂�
	public String[] setMsgs(){
		String buf = "" ;
		BufferedReader lineread
			= new BufferedReader(new InputStreamReader(System.in)) ;
		boolean cont = true ;
		Vector msgs_list = new Vector();
		String[] msgs = null ;

		try{
			System.out.println("���M���������b�Z�[�W����͂��Ă�������") ;
			System.out.println(" (�s���I�h�ŏI��)") ;
			while(cont){ // ���b�Z�[�W�̓���
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

	// mainproc���\�b�h
	// �����̏����Ȃǂ��s���܂�
	public void mainproc(String[] args){
		String usage =
			"java Mail [-s subject] to-addr ...";
		String subject = "";
		Vector to_list = new Vector();

		// �T�u�W�F�N�g�₠�Đ�̏���
		for (int i = 0; i < args.length; i++){
			if ("-s".equals(args[i])){
				i++;
				subject = args[i];
			}else{
				to_list.addElement(args[i]);
			}
		}
		// ���͂��ꂽ�����̕�����֕ϊ�
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

	// main���\�b�h
	// Mail �N���X�̃I�u�W�F�N�g�𐶐����܂�
	public static void main(String[] args){
		Mail m = new Mail() ;
		m.mainproc(args) ;
	}
}