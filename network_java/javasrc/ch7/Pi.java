// RMI�ɂ�镪�U�����v���O�����̎�����
// (1)�����[�g�T�[�r�X�̃C���^�t�F�[�X��`�t�@�C��

// Pi.java Pi�C���^�t�F�[�X
// ���̃C���^�t�F�[�X�́APiImpl�N���X�̂��߂̃C���^�t�F�[�X�ł�

// ���C�u�����̗��p
import java.rmi.Remote;
import java.rmi.RemoteException ;

// Pi�C���^�t�F�[�X
public interface Pi extends Remote{
	long putPi(long maxloopcount) throws RemoteException;
}