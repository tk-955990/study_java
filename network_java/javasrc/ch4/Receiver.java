// SMTP �ɂ�郁�[����M�v���O����Receiver.java
// ���̃v���O������,��M�p���[���T�[�o�Ƃ��ē��삵�܂�
// �g����java Receiver
// �N���̗�java Receiver
// ���̃v���O������,�����瑗��ꂽ���[����W���o�͂ɏ����o���܂�

// ���C�u�����̗��p
import java.net.*;
import java.io.*;
import java.util.*;

// Receiver�N���X
// Receiver�N���X��,Receiver�v���O�����̖{�̂ł�
public class Receiver {
	final int SMTP_PORT = 25;// SMTP �ڑ��p�|�[�g�ԍ�(25 ��)

	// sendResult���\�b�h
	// �N���C�A���g�ɑ΂��ă��b�Z�[�W�𑗂�܂�
	public void sendResult(Socket smtp, BufferedReader smtp_in,
						   PrintWriter smtp_out,
						   String command)
		throws IOException
	{
		smtp_out.print(command + "\r\n");//���b�Z�[�W�̑��M
		smtp_out.flush();
		System.out.println("send> " + command);//���M���e�̕\��
	}

	// getCommand���\�b�h
	// �N���C�A���g����̃R�}���h��ǂݎ��܂�
	// �R�}���h�͋󔒂ŋ�؂���,String�̔z��Ƃ��܂�
	public String[] getCommand(Socket smtp, BufferedReader smtp_in,
							   PrintWriter smtp_out)
		throws IOException
	{
		String res = smtp_in.readLine();//�ԓ��̓ǂݎ��
		System.out.println("recv> " + res);
		//�󔒂ŋ�؂�,���ʂ��Ăяo�����ɕԂ��܂�
		StringTokenizer st = new StringTokenizer(res) ;
		String[] results = new String[st.countTokens()] ;
		for(int i = 0;st.hasMoreTokens();++i){
			results[i] = st.nextToken() ;
		}
		return results ;
	}

	// procCommand���\�b�h
	// �N���C�A���g����̃R�}���h���������ǂ����𒲂ׂ܂�
	// ���̌��ʂ��N���C�A���g�ɕԂ��܂�
	// �����R�}���h���s���Ȃ�,�ڑ����������܂�
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

	// receive���\�b�h
	// SMTP �̃Z�b�V������i�߂܂�
	public void receive(Socket smtp)
		throws IOException
	{
		try{
			String[] commands = null ;
			// CONNECT����
			BufferedReader smtp_in = new BufferedReader(
				new InputStreamReader (smtp.getInputStream()));
			PrintWriter smtp_out = new PrintWriter(
				smtp.getOutputStream());

			String myname = InetAddress.getLocalHost().getHostName();
			sendResult(smtp, smtp_in, smtp_out,
					   "220 " + myname + " SMTP");
			// HELO�R�}���h�̏���
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"HELO",
				"250 " + "Hello " + commands[1] + ", pleased to meet you") ;

			// MAIL FROM�R�}���h�̏���
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"MAIL","250 "
				+ commands[1].substring(commands[1].indexOf(":") + 1)
				+ "... Sender ok") ;
			// RCPT TO�R�}���h�̏���
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],
				"RCPT","250 Recipient ok") ;
			// DATA�R�}���h�̏���
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out,commands[0],"DATA",
				"354 Enter mail, end with \".\" on a line by itself") ;

			//���b�Z�[�W�e�s�̎�M
			String res = smtp_in.readLine() ;
			while(!(res.equals("."))){
				System.out.println(res) ;
				res = smtp_in.readLine() ;
			}
			sendResult(smtp, smtp_in, smtp_out,
				"250 Message accepted for delivery") ;
			// QUIT�R�}���h�̏���
			commands = getCommand(smtp, smtp_in, smtp_out) ;
			procCommand(smtp, smtp_in, smtp_out, commands[0],"QUIT","221 "
				+ myname + " closing connection") ;
		}catch(Exception e){
		}
		// �R�l�N�V�����̃N���[�Y
		smtp.close();
	}

	// mainproc���\�b�h
	// ServerSocket�����,�ڑ���҂��󂯂܂�
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

	// main���\�b�h
	// Receiver�N���X�̃I�u�W�F�N�g�𐶐����܂�
	public static void main(String[] args){
		Receiver r = new Receiver() ;
		r.mainproc() ;
	}
}