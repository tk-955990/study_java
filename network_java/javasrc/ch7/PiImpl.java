// RMI�ɂ�镪�U�����v���O�����̎�����
// (2)�����[�g�T�[�r�X�̎����̃N���X�t�@�C��

// PiImpl.java
// ���̃N���X�́A�����[�g�T�[�r�X�̎����N���X�ł�
// ���U�����V�X�e���ɂ�����@�\���L�q���܂�
// ��̓I�ɂ́A�����e�J�����@�ɂ���/4���v�Z���܂�

// ���C�u�����̗��p
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.lang.Math ;

// PiImpl�N���X
public class PiImpl extends UnicastRemoteObject
	implements Pi{

	// �R���X�g���N�^PiImpl
	public PiImpl() throws RemoteException{
		super();
	}

	// putPi���\�b�h
	// �����e�J�����@�ɂ���/4���v�Z���܂�
	public long putPi(long maxloopcount){
		long i ; // �_�����̌J��Ԃ���
		long in=0 ; // ���a�P�̉~���ɓ_�����݂���ꍇ�̐�
		double x,y ; // �����_���ɐ�������_��x,y���W
		for(i=0;i<maxloopcount;++i){
			x=Math.random() ;// x���W
			y=Math.random() ;// y���W
			if((x*x+y*y)<=1.0) // �������a�P�̉~���Ȃ�΁E�E�E
			++in ; // in��1���₷
		}
		return in ;
	}
}