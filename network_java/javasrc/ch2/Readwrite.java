// Readwrite.java
// �L�[�{�[�h����̓��͂��󂯎��,���̂܂܉�ʂɏo�͂��܂�
// ���̃v���O�������I������ɂ�,�R���g���[���b����͂��Ă�������
//���C�u�����̗��p
import java.io.*;
// Readwrite�N���X
public class Readwrite {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//�z��̒�`
		// �ȉ��̃��[�v�𖳌��ɌJ��Ԃ��܂�
		//
		while (true) {
			try {
				// System.in����̓ǂݍ���
				int n = System.in.read(buff);
				// System.out�ւ̏����o��
				System.out.write(buff, 0, n);
			}
			// �ȉ��͗�O�����ł�
			catch(Exception e){
				// ��O���̓v���O�������I�����܂�
				System.exit(1);
			}
		}
	}
}