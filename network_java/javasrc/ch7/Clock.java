// RMI�ɂ��NetClock�v���O�����̎�����
// (1)�����[�g�T�[�r�X�̃C���^�t�F�[�X��`�t�@�C��

// Clock.java Clock�C���^�t�F�[�X
// ���̃C���^�t�F�[�X�́AClockImpl�N���X�̂��߂̃C���^�t�F�[�X�ł�
// ClockImpl���͂��߂Ƃ��āANetClock��RMI�łɕK�{�̃C���^�t�F�[�X�ł�

// ���C�u�����̗��p
import java.rmi.Remote;
import java.rmi.RemoteException ;

// Clock�C���^�t�F�[�X
public interface Clock extends Remote{
	String putTime() throws RemoteException;
}