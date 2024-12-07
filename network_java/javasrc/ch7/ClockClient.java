// RMI�ɂ��NetClock�v���O�����̎�����
// (5)�N���C�A���g�v���Z�X�����̃N���X�t�@�C��

// ClockClient.java
// ���̃N���X�́A�N���C�A���g�v���Z�X�̃N���X�ł�
// NetClock��RMI�ŃV�X�e���ɂ�����N���C�A���g�̋@�\���L�q���܂�
// RMI���W�X�g���ɂ����閼�O�̎擾��A�T�[�o�ւ̎d���̈˗����s���܂�
// �g�p���@
// java ClockClient
// �Ȃ��A�N���C�A���g�N���̑O�ɁA�T�[�o�ƃ��W�X�g�����N�����Ă�������

// ���C�u�����̗��p
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

// ClockClient�N���X
public class ClockClient{
	// main���\�b�h
	public static void main(String args[]){
		try{
			Clock c = (Clock)Naming.lookup("//localhost/ClockService") ;
			System.out.println(c.putTime()) ;
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}