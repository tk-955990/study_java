// Readnet.java
// �l�b�g���[�N��̃T�[�o����f�[�^���󂯎��,���̂܂܉�ʂɏo�͂��܂�
// �g����java Readnet DNS ���|�[�g�ԍ�
// ��java Readnet kiku.fuis.fukui-u.ac.jp 6000

//���C�u�����̗��p
import java.io.*;
import java.net.* ;

// Readnet�N���X
public class Readnet {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//�z��̒�`
		Socket readsocket = null ;// �T�[�o�ڑ��p�\�P�b�g
		InputStream instr = null;// �f�[�^�ǂݎ��p�I�u�W�F�N�g
		boolean cont = true ;
		// �w��̃|�[�g�ɑ΂���,�\�P�b�g���쐬���܂�
		// �I�u�W�F�N�ginstr�����,�f�[�^�ǂݏo�����������܂�
		try{
			readsocket
				= new Socket(args[0],Integer.parseInt(args[1])) ;
			instr = readsocket.getInputStream() ;
		}
		catch(Exception e){
			System.err.println("�l�b�g���[�N�G���[�ł�") ;
			System.exit(1) ;
		}

		// �f�[�^�̏I���܂�,�ȉ��̃��[�v���J��Ԃ��܂�
		while (cont) {
			try {
				// �ǂݍ���
				int n = instr.read(buff);
				// System.out�ւ̏����o��
				System.out.write(buff, 0, n) ;
			}
			// �ȉ��͗�O�����ł�
			catch(Exception e){
				// �ǂݏo���I�����Ƀ��[�v���I�����܂�
				cont = false ;
			}
		}
		// �R�l�N�V��������܂�
		try{
			instr.close() ;
		}
		catch(Exception e){
			// �l�b�g���[�N�N���[�Y���s�ł�
			System.err.println("�l�b�g���[�N�̃G���[�ł�") ;
			System.exit(1) ;
		}
	}
}