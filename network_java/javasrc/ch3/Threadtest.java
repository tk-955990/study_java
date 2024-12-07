// �X���b�h�e�X�g�v���O����Threadtest.java
// ���̃v���O������,�O����10 �܂ł̐��l���o�͂���X���b�h����s���삳���܂�
// �g����java Threadtest

// ���C�u�����̗��p
import java.net.*;
import java.io.*;

// Threadtest�N���X
// Threadtest�N���X��,�X���b�h�̍쐬�Ǝ��s���Ǘ����܂�
public class Threadtest {
	// main���\�b�h
	public static void main(String[] arg){
		try {
			// �X���b�h�p�N���XCountTen�̃I�u�W�F�N�g�𐶐����܂�
			CountTen no1 =
			new CountTen("No1");
			CountTen no2 =
			new CountTen("No2");
			// �X���b�h�𐶐����܂�
			Thread no1_thread = new Thread(no1);
			Thread no2_thread = new Thread(no2);
			// �X���b�h���N�����܂�
			no1_thread.start();
			no2_thread.start();
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}
}

// CountTen�N���X
// �O����10 �܂Ő����グ�܂�
class CountTen implements Runnable {
	String myname ;
	// �R���X�g���N�^�X���b�h�̖��O���󂯎��܂�
	public CountTen(String name){
		myname = name;
	}
	// �����̖{��
	public void run(){
		int i ;
		for(i=0;i<=10;++i){
			System.out.println(myname + ":" + i) ;
		}
	}
}