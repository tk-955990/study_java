// Testargs.java
// �R�}���h�ւ̈�������ʂɕ\�����܂�
// �g����java Testarg (�����P) (�����Q) ...

import java.io.* ;
public class Testargs{
	// ���C���v���O����
	public static void main(String[] args){
		// ���b�Z�[�W�̏o��
		int number = 0 ;
		while(number < args.length){
			System.out.println(args[number]);
			++ number ;
		}
	}
	//���C���v���O�����̏I��
}