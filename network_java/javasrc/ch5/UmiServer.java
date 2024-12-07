// ��C�Q�[����T�[�o�v���O����UmiServer.java
// ���̃v���O������,�C�Q�[���̃T�[�o�v���O�����ł�
// �g����java UmiServer
// �N�������,�|�[�g�ԍ�10000 �Ԃɑ΂���N���C�A���g����̐ڑ���҂��܂�
// �v���O�������~����ɂ̓R���g���[��C ����͂��Ă�������

// ���C�u�����̗��p
import java.io.*;
import java.net.*;
import java.util.*;

// UmiServer�N���X
// UmiServer�N���X��,UmiServer�v���O�����̖{�̂ł�
public class UmiServer {
	static final int DEFAULT_PORT = 10000;
							//UmiServer�ڑ��p�|�[�g�ԍ�
	static ServerSocket serverSocket;
	static Vector connections;
				//�N���C�A���g�Ƃ̃R�l�N�V������ێ�����Vector�I�u�W�F�N�g
	static Vector energy_v; // �R���^���N�̈ʒu��񃊃X�g
	static Hashtable userTable = null;
				// �N���C�A���g�֘A���o�^�p�e�[�u��
	static Random random = null;

	// addConnection���\�b�h
	// �N���C�A���g�Ƃ̐ڑ���Vector�I�u�W�F�N�gconnections�ɓo�^���܂�
	public static void addConnection(Socket s){
		if (connections == null){//���߂ẴR�l�N�V�����̏ꍇ��,
			connections = new Vector();// connections���쐬���܂�
		}
		connections.addElement(s);
	}

	// deleteConnection���\�b�h
	// �N���C�A���g�Ƃ̐ڑ���connections����폜���܂�
	public static void deleteConnection(Socket s){
		if (connections != null){
			connections.removeElement(s);
		}
	}

	// loginUser���\�b�h
	// login�R�}���h�̏����Ƃ���,���p�҂̖��O��D�̈ʒu��o�^���܂�
	public static void loginUser(String name){
		if (userTable == null){// �o�^�p�e�[�u�����Ȃ���΍쐬���܂�
			userTable = new Hashtable();
		}
		if (random == null){// �����̏��������܂�
			random = new Random();
		}
		// �D�̏����ʒu�𗐐��Ō��肵�܂�
		int ix = Math.abs(random.nextInt()) % 256;
		int iy = Math.abs(random.nextInt()) % 256;

		// �N���C�A���g�̖��O��D�̈ʒu��\�ɓo�^���܂�
		userTable.put(name, new Ship(ix, iy));
		// �T�[�o���̉�ʂɂ��N���C�A���g�̖��O��\�����܂�
		System.out.println("login:" + name);
		System.out.flush();
	}

	// logoutUser���\�b�h
	// �N���C�A���g�̃��O�A�E�g���������܂�
	public static void logoutUser(String name){
		// �T�[�o����ʂɃ��O�A�E�g����N���C�A���g�̖��O��\�����܂�
		System.out.println("logout:" + name);
		System.out.flush();
		// �o�^�p�e�[�u�����獀�ڂ��폜���܂�
		userTable.remove(name);
	}

	// left���\�b�h
	// �������̑D�����ɓ�������,�R���^���N���E���邩�ǂ������肵�܂�
	// ����ɂ�calculation���\�b�h���g���܂�
	public static void left(String name){
		Ship ship = (Ship) userTable.get(name);
		ship.left();
		calculation();
	}

	// right���\�b�h
	// �������̑D���E�ɓ�������,�R���^���N���E���邩�ǂ������肵�܂�
	// ����ɂ�calculation���\�b�h���g���܂�
	public static void right(String name){
		Ship ship = (Ship) userTable.get(name);
		ship.right();
		calculation();
	}

	// up���\�b�h
	// �������̑D����ɓ�������,�R���^���N���E���邩�ǂ������肵�܂�
	// ����ɂ�calculation���\�b�h���g���܂�
	public static void up(String name){
		Ship ship = (Ship) userTable.get(name);
		ship.up();
		calculation();
	}

	// down���\�b�h
	// �������̑D�����ɓ�������,�R���^���N���E���邩�ǂ������肵�܂�
	// ����ɂ�calculation���\�b�h���g���܂�
	public static void down(String name){
		Ship ship = (Ship) userTable.get(name);
		ship.down();
		calculation();
	}

	// calculation���\�b�h
	// �R���^���N�ƑD�̈ʒu�֌W�𒲂ׂ�,�R���^���N���E���邩�ǂ������肵�܂�
	static void calculation(){
		if (userTable != null && energy_v != null){
			// ���ׂẴN���C�A���g�ɂ��Ĕ��肵�܂�
			for (Enumeration users = userTable.keys();
				 users.hasMoreElements();) {
				// ���肷��N���C�A���g�̖��O�ƑD�̈ʒu�����o���܂�
				String user = users.nextElement().toString();
				Ship ship = (Ship) userTable.get(user);
				// �R���^���N���ׂĂɂ���,�D�Ƃ̈ʒu�֌W�𒲂ׂ܂�
				for (Enumeration energys = energy_v.elements();
					 energys.hasMoreElements();) {
					// �R���^���N�̈ʒu�ƑD�̈ʒu�𒲂�,�������v�Z���܂�
					int[] e = (int []) energys.nextElement();
					int x = e[0] - ship.x;
					int y = e[1] - ship.y;
					double r = Math.sqrt(x * x + y * y);
					// ����"10"�ȓ��Ȃ�R���^���N����荞�݂܂�
					if (r < 10) {
						energy_v.removeElement(e);
						ship.point++;
					}
				}
			}
		}
	}

	// statInfo���\�b�h
	// STAT�R�}���h���������܂�
	// �N���C�A���g�ɑ΂���,�D�̏��iship_info)��,
	// �C���Y�����Ă���R���^���N�̏���(energy_info)�𑗐M���܂�
	public static void statInfo(PrintWriter pw){
		// �D�̏��(ship_info)�̑��M
		pw.println("ship_info");
		if (userTable != null){
			for (Enumeration users = userTable.keys();
				 users.hasMoreElements();) {
				String user = users.nextElement().toString();
				Ship ship = (Ship) userTable.get(user);
				pw.println(user + " " + ship.x + " "
								+ ship.y + " " + ship.point);
			}
		}
		pw.println(".");// ship_info�̏I��
		// �R���^���N�̏��ienergy_info�j�̑��M
		pw.println("energy_info");
		if (energy_v != null){
			// ���ׂĂ̔R���^���N�̈ʒu�����N���C�A���g�ɑ��M���܂�
			for (Enumeration energys = energy_v.elements();
				 energys.hasMoreElements();) {
				int[] e = (int []) energys.nextElement();
				pw.println(e[0] + " " + e[1]);
			}
		}
		pw.println(".");// enegy_info�̏I��
		pw.flush();
	}

	// putEnergy���\�b�h
	// �R���^���N���P����,�C��Ƀ����_���ɔz�u���܂�
	public static void putEnergy(){
		if (energy_v == null){// ���߂Ĕz�u����ꍇ�̏���
			energy_v = new Vector();
		}
		if (random == null){// ���߂ė������g���ꍇ�̏���
			random = new Random();
		}
		// �����ňʒu�����߂ĊC��ɔz�u���܂�
		int[] e = new int[2];
		e[0] = Math.abs(random.nextInt()) % 256;
		e[1] = Math.abs(random.nextInt()) % 256;

		energy_v.addElement(e);
	}

	// main���\�b�h
	// �T�[�o�\�P�b�g�̍쐬�ƃN���C�A���g�ڑ��̏���
	// ����ѓK���ȃ^�C�~���O�ł̔R���^���N�̒����ǉ��������s���܂�
	public static void main(String[] arg){
		try {// �T�[�o�\�P�b�g�̍쐬
			serverSocket = new ServerSocket(DEFAULT_PORT);
		}catch (IOException e){
			System.err.println("can't create server socket.");
			System.exit(1);
		}
		// �R���^���N�����ɒǉ�����X���b�het�����܂�
		Thread et = new Thread(){
			public void run(){
				while(true){
					try {
						sleep(10000);// �X���b�het��10000�~���b�x�~�����܂�
					}catch(InterruptedException e){
						break;
					}
					// �C��ɂP�R���^���N��z�u���܂�
					UmiServer.putEnergy();
				}
			}
		};
		// et���X�^�[�g���܂�
		et.start();
		// �\�P�b�g�̎�t��,�N���C�A���g�����v���O�����̊J�n�������s���܂�
		while (true) {// �������[�v
			try {
				Socket cs = serverSocket.accept();
				addConnection(cs);// �R�l�N�V������o�^���܂�
				// �N���C�A���g�����X���b�h���쐬���܂�
				Thread ct = new Thread(new clientProc(cs));
				ct.start();
			}catch (IOException e){
				System.err.println("client socket or accept error.");
			}
		}
	}
}

// clientProc�N���X
// clientProc�N���X��,�N���C�A���g�����X���b�h�̂ЂȌ`�ł�
class clientProc implements Runnable {
	Socket s; // �N���C�A���g�ڑ��p�\�P�b�g
	// ���o�̓X�g���[��
	BufferedReader in;
	PrintWriter out;
	String name = null;// �N���C�A���g�̖��O

	// �R���X�g���N�^clientProc
	// �\�P�b�g���g���ē��o�̓X�g���[�����쐬���܂�
	public clientProc(Socket s) throws IOException {
		this.s = s;
		in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
		out = new PrintWriter(s.getOutputStream());
	}

	// run���\�b�h
	// �N���C�A���g�����X���b�h�̖{�̂ł�
	public void run(){
		try {
			//LOGOUT�R�}���h��M�܂ŌJ��Ԃ��܂�
			while (true) {
				// �N���C�A���g����̓��͂�ǂݎ��܂�
				String line = in.readLine();
				// name����̏ꍇ�ɂ�LOGIN�R�}���h�݂̂��󂯕t���܂�
				if (name == null){
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();
					if ("login".equalsIgnoreCase(cmd)){
						name = st.nextToken();
						UmiServer.loginUser(name);
					}else{
						// LOGIN�R�}���h�ȊO��,���ׂĖ������܂�
					}
				}else{
					// name����łȂ��ꍇ�̓��O�C���ς݂ł�����,�R�}���h���󂯕t���܂�
					StringTokenizer st = new StringTokenizer(line);
					String cmd = st.nextToken();// �R�}���h�̎��o��
					// �R�}���h�𒲂�,�Ή����鏈�����s���܂�
					if ("STAT".equalsIgnoreCase(cmd)){
						UmiServer.statInfo(out);
					} else if ("UP".equalsIgnoreCase(cmd)){
						UmiServer.up(name);
					} else if ("DOWN".equalsIgnoreCase(cmd)){
						UmiServer.down(name);
					} else if ("LEFT".equalsIgnoreCase(cmd)){
						UmiServer.left(name);
					} else if ("RIGHT".equalsIgnoreCase(cmd)){
						UmiServer.right(name);
					} else if ("LOGOUT".equalsIgnoreCase(cmd)){
						UmiServer.logoutUser(name);
						// LOGOUT�R�}���h�̏ꍇ�ɂ͌J��Ԃ����I�����܂�
						break;
					}
				}
			}
			// �o�^�����폜��,�ڑ���ؒf���܂�
			UmiServer.deleteConnection(s);
			s.close();
		}catch (IOException e){
			try {
				s.close();
			}catch (IOException e2){}
		}
	}
}

// Ship�N���X
// �D�̈ʒu��,�l�������R���^���N�̐����Ǘ����܂�
class Ship {
	// �D�̈ʒu���W
	int x;
	int y;
	// �l�������R���^���N�̌�
	int point = 0;

	// �R���X�g���N�^
	// �����ʒu���Z�b�g���܂�
	public Ship(int x, int y){
		this.x = x;
		this.y = y;
	}

	// left���\�b�h
	// �D�����ɓ������܂�
	public void left(){
		x -= 10;
		// ���̕ӂ͉E�̕ӂɂȂ����Ă��܂�
		if (x < 0)
			x += 256;
	}

	// right���\�b�h
	// �D���E�ɓ������܂�
	public void right(){
		x += 10;
		// �E�̕ӂ͍��̕ӂɂȂ����Ă��܂�
		x %= 256;
	}

	// up���\�b�h
	// �D����ɓ������܂�
	public void up(){
		y += 10;
		// ��̕ӂ͉��̕ӂɂȂ����Ă��܂�
		y %= 256;
	}

	// down���\�b�h
	// �D�����ɓ������܂�
	public void down(){
		y -= 10;
		// ���̕ӂ͏�̕ӂɂȂ����Ă��܂�
		if (y < 0)
			y += 256;
	}
}