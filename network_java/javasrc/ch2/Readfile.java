// Readfile.java
// �t�@�C���̓��e��ǂݎ��,���̂܂܉�ʂɏo�͂��܂�
// �g����java Readfile �t�@�C����

//���C�u�����̗��p
import java.io.*;

// Readfile�N���X
public class Readfile {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//�z��̒�`
		boolean cont = true ;// ���[�v����p�ϐ�
		FileInputStream infile = null;// �t�@�C���ǎ�p
		// �I�u�W�F�N�ginfile�����,�t�@�C���ǂݏo�����������܂�
		try{
			infile = new FileInputStream(args[0]) ;
		}
		catch(FileNotFoundException e){
			// �t�@�C�������̎��s
			System.err.println("�t�@�C��������܂���") ;
			System.exit(1) ;
		}

		// �t�@�C���̏I���܂�,�ȉ��̃��[�v���J��Ԃ��܂�
		while (cont) {
			try {
				// �t�@�C������̓ǂݍ���
				int n = infile.read(buff);
				// System.out�ւ̏����o��
				System.out.write(buff, 0, n) ;
			}
			// �ȉ��͗�O�����ł�
			catch(Exception e){
				// �ǂݏo���I�����Ƀ��[�v���I�����܂�
				cont = false ;
			}
		}
		// �t�@�C������܂�
		try{
			infile.close() ;
		}
		catch(IOException e){
			// �t�@�C���N���[�Y�̎��s�ł�
			System.err.println("�t�@�C���̃G���[�ł�") ;
			System.exit(1) ;
		}
	}
}