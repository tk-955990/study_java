// Wrnet.java
// �l�b�g���[�N��̃T�[�o�ɐڑ���,�f�[�^�𑗂�܂�
// ���̌�T�[�o����f�[�^���󂯎��,���̂܂܉�ʂɏo�͂��܂�
// �g����java Wrnet DNS ���|�[�g�ԍ�
// ��java Wrnet kiku.fuis.fukui-u.ac.jp 80

//���C�u�����̗��p
import java.io.*;
import java.net.* ;

// Wrnet�N���X
public class Wrnet {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//�z��̒�`
		Socket wrsocket = null ;// �T�[�o�ڑ��p�\�P�b�g
		InputStream instr = null;// �f�[�^�ǂݎ��p�I�u�W�F�N�g
		OutputStream outstr = null;// �f�[�^�o�͗p�I�u�W�F�N�g
		boolean cont = true ;
		// �w��̃|�[�g�ɑ΂���,�\�P�b�g���쐬���܂�
		// ���o�͂̃X�g���[�������,�f�[�^�ǂݏo�����������܂�
		try{
			wrsocket
				= new Socket(args[0],Integer.parseInt(args[1])) ;
			instr = wrsocket.getInputStream() ;
			outstr = wrsocket.getOutputStream() ;
		}
		catch(Exception e){
			System.err.println("�l�b�g���[�N�G���[�ł�") ;
			System.exit(1) ;
		}
		//
		while (cont) {
			try {
				// System.in����̓ǂݍ���
				int n = System.in.read(buff);
				// System.out�ւ̏����o��
				// System.out.write(buff, 0, n) ;
				// �s���s���I�h�̌��o
				if(buff[0] == '.') cont = false ;
				else outstr.write(buff,0,n) ;
			}
			// �ȉ��͗�O�����ł�
			catch(Exception e){
				// ��O���̓v���O�������I�����܂�
				System.exit(1);
			}
		}
		// �f�[�^�̏I���܂�,�ȉ��̃��[�v���J��Ԃ��܂�
		cont = true ;
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
