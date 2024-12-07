// RMI�ɂ�镪�U�����v���O�����̎�����
// (4)�T�[�o�v���Z�X�����̃N���X�t�@�C��

// PiServer.java
// ���̃N���X�́A�T�[�o�v���Z�X�̃N���X�ł�
// ���U�����V�X�e���ɂ�����T�[�o�̋@�\���L�q���܂�
// RMI���W�X�g���ɂ����閼�O�̓o�^��A�T�[�o�̋N�����s���܂�
// �g�p���@
// java PiServer
// �Ȃ��A�T�[�o�N���̑O�ɁA���W�X�g�����N�����Ă�������
// RMI���W�X�g���̋N���͈ȉ��̂悤�ɂ��܂�
// rmiregistry

// ���C�u�����̗��p
import java.rmi.Naming;

// PiServer�N���X
public class PiServer{

	// �R���X�g���N�^
	public PiServer(){
		try{
			Pi p = new PiImpl() ;
			Naming.rebind("//localhost/PiService",p) ;
		} catch(Exception e) {
			e.printStackTrace() ;
		}
	}

	// main���\�b�h
	public static void main(String args[]){
		new PiServer() ;
	}
}
