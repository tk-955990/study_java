// RMI�ɂ��NetClock�v���O�����̎�����
// (2)�����[�g�T�[�r�X�̎����̃N���X�t�@�C��

// ClockImpl.java
// ���̃N���X�́A�����[�g�T�[�r�X�̎����N���X�ł�
// NetClock��RMI�ŃV�X�e���ɂ�����@�\���L�q���܂�
// ��̓I�ɂ́A������Ԃ����\�b�hputTime()��񋟂��܂�

// ���C�u�����̗��p
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.Date ;

// ClockImpl�N���X
// ���̃N���X��Clock�C���^�t�F�[�X�̎�����^���܂�
public class ClockImpl extends UnicastRemoteObject
	implements Clock{

	// �R���X�g���N�^ClockImpl()
	public ClockImpl() throws RemoteException{
		super();
	}

	// putTime���\�b�h
	// ���݂̎�����Ԃ��܂�
	public String putTime(){
		Date d = new Date() ;
		return d.toString() ;
	}
}