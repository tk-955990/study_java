// �����𓚂���T�[�o�v���O����Netclock.java
// ���̃v���O�����̓|�[�g�ԍ�6000�Ԃœ��삷��T�[�o�ł�
// �N���C�A���g����̐ڑ��ɑ΂�,������Ԃ��܂�
// ���̃v���O�������~������ɂ̓R���g���[��C����͂��Ă�������
// �g����java Netclock

// ���C�u�����̗��p
import java.io.* ;
import java.net.* ;
import java.util.* ;

// Netclock�N���X
class Netclock{
	public static void main(String args[]){
		ServerSocket servsock = null ;// �T�[�o�p�\�P�b�g
		Socket sock ;// �\�P�b�g�̓ǂݏ����p�I�u�W�F�N�g
		OutputStream out ;// �o�̓X�g���[��
		String outstr ;// �o�̓f�[�^���i�[���镶����
		int i ;//�o�͂̌J��Ԃ�����p�ϐ�
		Date d ;// ���t���������p�I�u�W�F�N�g

		try{
			// �T�[�o�\�P�b�g�̍쐬
			servsock = new ServerSocket(6000,300) ;
			// �T�[�o�����̌J��Ԃ�
			while(true){
				sock = servsock.accept() ;//�ڑ���t
				// �o�͗p�f�[�^�̍쐬
				d = new Date() ;
				outstr = "\n"
					+ "Hello, this is Netclock server."
					+ "\n" + d.toString() + "\n"
					+ "Thank you." + "\n";
				// �f�[�^�̏o��
				out = sock.getOutputStream() ;
				for(i = 0; i < outstr.length();++i)
					out.write((int)outstr.charAt(i)) ;
				out.write('\n') ;
				// �ڑ��I��
				sock.close() ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}