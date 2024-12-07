// �}���`�L���X�g�p�P�b�g���M�v���O����Msend.java
// ���̃v���O������,�}���`�L���X�g�p�P�b�g�𑗐M���܂�
// �g����java Msend [�}���`�L���X�g�A�h���X] [�|�[�g�ԍ�]

// ���C�u�����̗��p
import java.net.*;
import java.io.*;

// Msend�N���X
public class Msend {

	// �����̖{��
	public static void main(String[] arg){
		String multicastAddress = "224.0.0.1" ;
				// �}���`�L���X�g�A�h���X224.0.0.1��,
				// ���[�^���z���Ȃ��ꍇ�̃A�h���X�ł�
		int port = 6000 ;
				// �`���b�g�p�̃|�[�g�ԍ�,�w�肪�Ȃ����6000 �ԂƂ��܂�
		MulticastSocket s = null;
		byte[] buff = new byte[1024] ;

		if(arg.length >= 1) multicastAddress = arg[0] ;
		if(arg.length >= 2) port = Integer.parseInt(arg[1]) ;
		try{
			InetAddress chatgroup
				= InetAddress.getByName(multicastAddress) ;
			MulticastSocket soc = new MulticastSocket(port) ;
			soc.joinGroup(chatgroup) ;
			while(true){
				int n = System.in.read(buff,0,buff.length) ;
				if(n > 0){
					DatagramPacket dp
						= new DatagramPacket(buff,n,chatgroup,port) ;
					soc.send(dp) ;
				}
				else break ;// ���[�v�I��
			}
		}catch(Exception e){
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}
}