// Getdatad.java
// �f�[�^��M�T�[�o
// �N���C�A���g����f�[�^���󂯎��A���̂܂܉�ʂɏo�͂��܂�
// �����Ń|�[�g�ԍ����w�肵�܂�
// �g����java Getdatad port256 port
// ���ۂ̃|�[�g�ԍ���256*(port256)+port�ɂȂ�܂�

//���C�u�����̗��p
import java.io.*;
import java.net.* ;

// Getdatad�N���X
public class Getdatad {
	// �v���O�����̖{��main
	public static void main(String[] args){
		byte[] buff = new byte[1024]; //�z��̒�`
		ServerSocket servsock = null ;//�T�[�o�\�P�b�g
		Socket sock = null ;// �ڑ��p�\�P�b�g
		InputStream instr = null;// �f�[�^�ǂݎ��p�I�u�W�F�N�g
		boolean cont = true ;

		final int dataport = Integer.parseInt(args[0])*256
		+ Integer.parseInt(args[1]) ; // �f�[�^��M�p�|�[�g

		// ��M�p�T�[�o�\�P�b�g���쐬���܂�
		// ���o�͂̃X�g���[�������,�f�[�^�ǂݏo�����������܂�
		try{
			// �T�[�o�\�P�b�g�̍쐬
			servsock = new ServerSocket(dataport,1) ;
			// �T�[�o�����̌J��Ԃ�
			while(true){
				sock = servsock.accept() ;//�ڑ���t
				cont = true ;
				while(cont){
					try{
						// �f�[�^�̓ǂݍ��݂Əo��
						instr = sock.getInputStream() ;
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
				// �ڑ��I��
				sock.close() ;
				System.out.println("�ڑ��I��") ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}