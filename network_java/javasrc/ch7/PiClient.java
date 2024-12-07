// RMI�ɂ�镪�U�����v���O�����̎�����
// (5)�N���C�A���g�v���Z�X�����̃N���X�t�@�C��

// PiClient.java
// ���̃N���X�́A�N���C�A���g�v���Z�X�̃N���X�ł�
// ���U�����V�X�e���ɂ�����N���C�A���g�̋@�\���L�q���܂�
// RMI���W�X�g���ɂ����閼�O�̎擾��A�T�[�o�ւ̎d���̈˗����s���܂�
// �g�p���@
// java PiClient �T�[�o��
// �Ȃ��A�N���C�A���g�N���̑O�ɁA�T�[�o�ƃ��W�X�g�����N�����Ă�������

// ���C�u�����̗��p
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.* ;

//PiClient�N���X
public class PiClient{

	// main���\�b�h
	public static void main(String args[]){
		long result ;//�T�[�o�̕Ԃ��l
		long millis ;//�o�ߎ���
		long maxloopcount=10000000 ;//��������_�̌�

		try{
			//�v�Z�J�n
			System.out.println("Start") ;
			//���݂̎����i�~���b�j
			millis = System.currentTimeMillis() ;
			//rmiregistry�ɂ��T�[�o�̌���
			Pi p = (Pi)Naming.lookup("//"+args[0]+"/PiService") ;
			//�T�[�o��putPI���\�b�h�ɂ��΂̌v�Z
			result=p.putPi(maxloopcount);
			//�v�Z�I���A�o�ߎ��Ԃ̑���
			//�o�ߎ��Ԃɂ̓l�b�g���[�N�������܂܂�܂�
			millis = System.currentTimeMillis() - millis ;

			//�΂̒l�ƌo�ߎ��Ԃ̏o��
			System.out.println((double)result/maxloopcount*4) ;
			System.out.println((double)millis/1000 + "sec") ;
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}