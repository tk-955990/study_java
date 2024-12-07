// RMI�ɂ��NetClock�v���O�����̎�����
// (4)�T�[�o�v���Z�X�����̃N���X�t�@�C��

// ClockServer.java
// ���̃N���X�́A�T�[�o�v���Z�X�̃N���X�ł�
// NetClock��RMI�ŃV�X�e���ɂ�����T�[�o�̋@�\���L�q���܂�
// RMI���W�X�g���ɂ����閼�O�̓o�^��A�T�[�o�̋N�����s���܂�
// �g�p���@
// java ClockServer
// �Ȃ��A�T�[�o�N���̑O�ɁA���W�X�g�����N�����Ă�������
// RMI���W�X�g���̋N���͈ȉ��̂悤�ɂ��܂�
// rmiregistry

// ���C�u�����̗��p
import java.rmi.Naming;
// ClockServer�N���X
public class ClockServer{
	// �R���X�g���N�^
	public ClockServer(){
		try{
			Clock c = new ClockImpl() ;
			Naming.rebind("//localhost/ClockService",c) ;
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	// main���\�b�h
	public static void main(String args[]){
		new ClockServer() ;
	}
}