// Writefile.java
// �L�[�{�[�h����̓��͂��󂯎��,���̂܂܉�ʂɏo�͂��܂�
// �܂�,�t�@�C���ɓ��͂����Ɋi�[���܂�
// �g����java Writefile �t�@�C����
// ���̃v���O�������I������ɂ�,�s�̐擪�Ƀs���I�h.����͂��Ă�������
//���C�u�����̗��p
import java.io.*;
// Writefile�N���X
public class Writefile {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//�z��̒�`
		boolean cont = true ;// ���[�v����p�ϐ�
		FileOutputStream outfile = null;
		// �t�@�C���o�͗p�I�u�W�F�N�g
		// �I�u�W�F�N�goutfile�����,�t�@�C���̏o�͂��������܂�
		try{
			outfile = new FileOutputStream(args[0]) ;
		}
		catch(FileNotFoundException e){
			// �t�@�C�������̎��s
			System.err.println("�t�@�C��������܂���") ;
			System.exit(1) ;
		}
		// �s���ւ̃s���I�h�̓��͂܂�,�ȉ��̃��[�v���J��Ԃ��܂�
		while (cont) {
			try {
				// System.in����̓ǂݍ���
				int n = System.in.read(buff);
				// System.out�ւ̏����o��
				System.out.write(buff, 0, n) ;
				// �s���s���I�h�̌��o
				if(buff[0] == '.') cont = false ;
				else outfile.write(buff,0,n) ;
			}
			// �ȉ��͗�O�����ł�
			catch(Exception e){
				// ��O���̓v���O�������I�����܂�
				System.exit(1);
			}
		}
		// �t�@�C������܂�
		try{
			outfile.close() ;
		}
		catch(IOException e){
			// �t�@�C���N���[�Y�̎��s�ł�
			System.err.println("�t�@�C���̃G���[�ł�") ;
			System.exit(1) ;
		}
	}
}