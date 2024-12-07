// �}���`�L���X�g�Ń`���b�g�v���O����Chat.java
// ���̃v���O������,�}���`�L���X�g���g���ă`���b�g�@�\��񋟂��܂�
// �g����java Chat [�|�[�g�ԍ�]

// ���C�u�����̗��p
import java.net.*;
import java.io.*;

// Chat�N���X
// Chat�N���X��,��M�X���b�h���쐬�E���s��,���M��S�����܂�
public class Chat {
	final byte TTL = 1 ;//����Z�O�����g�����̂ݓ��B�\�Ƃ��܂�
	final String MULTICASTADDRESS = ("224.0.0.1") ;
				// �}���`�L���X�g�A�h���X224.0.0.1��,
				// ���[�^�𒴂��Ȃ��ꍇ�̃A�h���X�ł�
	int port = 6000 ;
				// �`���b�g�p�̃|�[�g�ԍ�,�w�肪�Ȃ����6000 �ԂƂ��܂�
	byte[] buff = new byte[1024] ;//���M�p�o�b�t�@
	String myname ="" ;// ���p�Җ�
	int nameLength = 0 ;//���p�Җ��̒���
	MulticastSocket soc = null ; // �}���`�L���X�g�\�P�b�g
	InetAddress chatgroup = null ; //�`���b�g�p�A�h���X

	// �R���X�g���N�^���p�Җ��Ȃǂ�ݒ肵�܂�
	public Chat(int portno){
	port = portno ; //�|�[�g�ԍ��̐ݒ�
	BufferedReader lineread
	  = new BufferedReader(new InputStreamReader(System.in)) ;
	System.out.println("�����O���ǂ���") ;
	try{
		myname = lineread.readLine() ;
	}catch(Exception e){
		e.printStackTrace() ;
		System.exit(1) ;
	}
	System.out.println("�悤����" + myname + "����.") ;
	myname = myname + ">" ;
	nameLength = (myname.getBytes()).length ;
	for(int i = 0;i < nameLength;++i)
		buff[i] = (myname.getBytes())[i] ;
	}

	// makeMulticastSocket���\�b�h
	//MULTICASTADDRESS�ɑ΂��ă}���`�L���X�g�\�P�b�g���쐬���܂�
	public void makeMulticastSocket()
	{
		try{
			chatgroup
				= InetAddress.getByName(MULTICASTADDRESS) ;
			soc = new MulticastSocket(port) ;
			soc.joinGroup(chatgroup) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// startLintener���\�b�h
	// �X���b�h�p�N���XListenPacket�̃I�u�W�F�N�g�𐶐���,�N�����܂�
	public void startListener()
	{
		try{
			ListenPacket lisner =
				new ListenPacket(soc);
			Thread lisner_thread = new Thread(lisner);
			lisner_thread.start();//��M�X���b�h�̊J�n
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// sendMsgs���\�b�h
	// �}���`�L���X�g�p�P�b�g�̑��M��S�����܂�
	public void sendMsgs()
	{
		try{
			// ���M���[�v
			while(true){
				int n = System.in.read(
						buff,nameLength ,1024 - nameLength ) ;
				if(n > 0){
					DatagramPacket dp
						= new DatagramPacket(
						buff,n + nameLength,chatgroup,port) ;
					soc.send(dp) ;
				}
				else break ;// ���[�v�I��
			}
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// quitGroup���\�b�h
	// �ڑ����I�����܂�
	public void quitGroup()
	{
		try{
			// �ڑ��̏I��
			System.out.println("�ڑ��I��") ;
			soc.leaveGroup(chatgroup) ;
			System.exit(0) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// main���\�b�h
	public static void main(String[] arg){
		Chat c = null ;
		int portno = 6000 ;
		if(arg.length >= 1) portno = Integer.parseInt(arg[0]) ;
		c = new Chat(portno) ;
		c.makeMulticastSocket() ;
		c.startListener() ;
		c.sendMsgs() ;
		c.quitGroup() ;
	}
}

// ListenPacket�N���X
// �}���`�L���X�g�p�P�b�g����M���܂�
class ListenPacket implements Runnable {
	MulticastSocket s = null;
	// �R���X�g���N�^�}���`�L���X�g�X���b�h���󂯎��܂�
	public ListenPacket(MulticastSocket soc){
		s = soc;
	}
	// �����̖{��
	public void run(){
		byte[] buff = new byte[1024] ;
		try{
			while(true){
				DatagramPacket recv
					= new DatagramPacket(buff,buff.length) ;
				s.receive(recv) ;
				if(recv.getLength() > 0){
					System.out.write(buff,0,recv.getLength()) ;
				}
			}
		}catch(Exception e){
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}
}