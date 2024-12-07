// RMI�ɂ�镪�U�����v���O�����̎�����
// (5)�N���C�A���g�v���Z�X�����̃N���X�t�@�C��

// DPiClient.java
// ���̃N���X�́A�N���C�A���g�v���Z�X�̃N���X�ł�
// ���U�����V�X�e���ɂ�����N���C�A���g�̋@�\���L�q���܂�
// RMI���W�X�g���ɂ����閼�O�̎擾��A�T�[�o�ւ̎d���̈˗����s���܂�
// �g�p���@
// java DPiClient �T�[�o��1 �T�[�o��2 �E�E�E
// �Ȃ��A�N���C�A���g�N���̑O�ɁA�T�[�o�ƃ��W�X�g�����N�����Ă�������

// ���C�u�����̗��p
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.* ;

//DPiClient�N���X
public class DPiClient{

	// main���\�b�h
	public static void main(String args[]){
		long result=0 ;//�T�[�o�̌v�Z���ʂ��i�[����
		long millis ;//�o�ߎ���
		long maxloopcount=10000000 ;//�_�̌�
		int i ;//�T�[�o�̐�

		//�X���b�h���\�����邽�߂̔z���錾����
		// DPiClient�ł́A�T�[�o�̐������X���b�h���쐬���܂�
		// �e�X���b�h�̓T�[�o�̌v�Z�����I����҂��A
		// �I����Result�N���X�̃I�u�W�F�N�g�Ɍ��ʂ�񍐂��܂�
		launchPiServer l[] = new launchPiServer[args.length];
		Thread t[] = new Thread [args.length];

		//�����̖{��
		try{
			//�������Ԍv���J�n
			millis = System.currentTimeMillis() ;
			//�����Ŏw�肳�ꂽ�T�[�o�ɏ������˗����܂�
			for(i=0;i<args.length;++i){
				//�����̌������������J��Ԃ��܂�
				//�e�T�[�o���ƂɃX���b�h�����蓖�Ă�
				l[i] = new launchPiServer(args[i],
				maxloopcount/args.length,millis) ;
				t[i] = new Thread(l[i]) ;
				t[i].start() ;//�����̊J�n
			}
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}

// launchPiServer�N���X
// �T�[�o�ɑ΂��ď������˗����A���ʂ��󂯎��܂�
// �T�[�o����󂯎�����������ʂ́A
//Result�N���X��collect()���\�b�h�ŏW�v���܂�
class launchPiServer implements Runnable{
	String address ;//�T�[�o�̃A�h���X���i�[����
	long maxl ;//�J��Ԃ��̉�(�_�̐�)
	long millis ;//�����J�n�������i�[����

	//�R���X�g���N�^
	public launchPiServer(String name, long maxloopcount,
			long m){
		//�Ăяo��������󂯎�����l���N���X�����ŕێ����܂�
		address = name ;
		maxl = maxloopcount ;
		millis = m ;
	}

	//�X���b�h�̖{��
	public void run(){
		Pi p ;//�T�[�o�p�I�u�W�F�N�g
		try{
			//PiService�̗��p
			p = (Pi)Naming.lookup("//"+address+"/PiService") ;
			//�T�[�o�����������̂ŁA�������˗����܂�
			System.out.println("Start "+ address) ;
			Result.collect(address,maxl,millis,p.putPi(maxl));
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}

// Result�N���X
// �e�T�[�o����Ԃ��ꂽ�l���W�v���܂�
class Result{
	static int i=0 ;
	static long all=0 ;

	//collect���\�b�h
	// �����̃X���b�h����Ăяo����邽�߁A�r�����䂪�K�v�ɂȂ�܂�
	// ���̂��߁Asynchronized�Ƃ����L�[���[�h��t����
	// ���\�b�h��錾���܂�
	// �����̈ꗗ
	// address �T�[�o�̃A�h���X
	// maxloopcount �T�[�o�����������_�̌�
	// misllis �����J�n����
	// res �~�̓����ɓ������_�̌�

	// collect()���\�b�h
	static public synchronized void
	collect(String address,long maxloopcount,long millis,
			long res){
		System.out.println("Finish "+address) ;
		// �~�����΂̋ߎ��l�̌v�Z
		all+=res ;//����܂ł̌��ʂɁA�V���ɓ���ꂽ���ʂ����Z
		++i ;//�Ԏ���Ԃ����T�[�o�̌��𐔂��܂�
		//�΂̋ߎ��l���o�͂��܂�
		System.out.println(" " +
			(double)all/(maxloopcount*i)*4) ;
		//�o�ߎ��Ԃ��o�͂��܂�
		millis = System.currentTimeMillis() - millis ;
		System.out.println(" " + (double)millis/1000 + "sec") ;
	}
}