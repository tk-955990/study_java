// ���񂿂�HTTP�T�[�oPhttpd.java
// ���̃v���O�����̓|�[�g�ԍ�80�Ԃœ��삷��T�[�o�ł�
// �g����java Phttp �f�[�^�t�@�C����
// WWW�N���C�A���g����̐ڑ��ɑ΂�,�����Ŏw�肵���t�@�C����Ԃ��܂�

// ���C�u�����̗��p
import java .io.* ;
import java.net.* ;
import java.util.* ;

// Phttpd�N���X
class Phttpd{
	public static void main(String args[]){
		// �T�[�o�\�P�b�g�̏���
		ServerSocket servsock = null ;
		Socket sock ;
		// ���o�͂̏���
		OutputStream out ;
		BufferedReader in ;
		FileInputStream infile = null;
		byte[] buff = new byte[1024];
		// ���̑��̕ϐ�
		boolean cont = true ;
		int i ;

		try{
			// �T�[�o�p�\�P�b�g�̍쐬�i�|�[�g�ԍ�80�ԁj
			servsock = new ServerSocket(80,300) ;
			while(true){
				sock = servsock.accept() ;// �ڑ��v���̎�t
				// �ڑ���̕\��
				System.out.println("�ڑ��v��"
					+ (sock.getInetAddress()).getHostName()) ;
				// �I�u�W�F�N�ginfile�����,�t�@�C�����������܂�
				try{
					infile = new FileInputStream(args[0]) ;
				}
				catch(Exception e){
					// �t�@�C�������̎��s
					System.err.println("�t�@�C��������܂���") ;
					System.exit(1) ;
				}
				// �ǂݏ����p�I�u�W�F�N�g�̐���
				in = new BufferedReader(new
				InputStreamReader(sock.getInputStream()));
				out = sock.getOutputStream() ;
				// �Ƃ肠�������s���Q�ǂݔ�΂�
				for(i = 0; i < 2;++i)
					in.readLine() ;
				// �t�@�C���̏o��
				cont = true ;
				while(cont){
					// �t�@�C������̓ǂݍ��݂ƃl�b�g���[�N�o��
					try{
						int n = infile.read(buff);
						out.write(buff,0,n) ;
					}
					catch(Exception e){
						cont = false ;
					}
				}
				// �ڑ��I��
				sock.close() ;
				infile.close() ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}