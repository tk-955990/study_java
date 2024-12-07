// ��C�Q�[���GUI �N���C�A���g�v���O����UmiClient.java
// ���̃v���O������,�C�Q�[���̃N���C�A���g�v���O�����ł�
// �g����java UmiClient
// �N������login�{�^����������,�ڑ���T�[�o�̖��O�◘�p�҂̖��O��₢���킹�Ă��܂�
// �T�[�o���Ɨ��p�Җ�����͂��Ă�������
// ������OK �{�^����������,�|�[�g�ԍ�10000 �ԂŃT�[�o�Ɛڑ����܂�
//
// �v���O�������~����ɂ�logout �{�^���������Ă�������

// ���C�u�����̗��p
import java.awt.*;// �O���t�B�b�N�X
import java.awt.event.*;// �C�x���g�֘A
import java.net.*;// �l�b�g���[�N�֘A
import java.io.*;
import java.util.*;

// UmiClient�N���X
// UmiClient�v���O�����̒��S�ƂȂ�N���X�ł�
public class UmiClient implements Runnable {
	Frame f;// �N���C�A���g���\���p�E�B���h�E
	Panel p;// �㉺���E�̈ړ��{�^���ƊC�̏�Ԃ�\������p�l��
	Canvas c;// �C�̏�Ԃ�\������L�����o�X

	// �R���X�g���N�^
	// GUI ��ʂ̏����z�u���s���܂�
	public UmiClient ()
	{
		Button b;
		f = new Frame();//�N���C�A���g���E�B���h�E�S�̂̕\��
		p = new Panel();//�C�\�������Ƒ���{�^���̕\��
		p.setLayout(new BorderLayout());

		// up�{�^���̍쐬
		b = new Button("up");
		b.addActionListener(new ActionListener(){
			// up�{�^���������ꂽ��up�R�}���h�𑗐M���܂�
			public void actionPerformed(ActionEvent e){
				sendCommand("up");
			}
		});
		p.add(b, BorderLayout.NORTH);

		// left�{�^���̍쐬
		b = new Button("left");
		b.addActionListener(new ActionListener(){
			// left�{�^���������ꂽ��left�R�}���h�𑗐M���܂�
			public void actionPerformed(ActionEvent e){
				sendCommand("left");
			}
		});
		p.add(b, BorderLayout.WEST);

		// right�{�^���̍쐬
		b = new Button("right");
		b.addActionListener(new ActionListener(){
			// right�{�^���������ꂽ��right�R�}���h�𑗐M���܂�
			public void actionPerformed(ActionEvent e){
				sendCommand("right");
			}
		});
		p.add(b, BorderLayout.EAST);

		// down�{�^���̍쐬
		b = new Button("down");
		b.addActionListener(new ActionListener(){
			// down�{�^���������ꂽ��down�R�}���h�𑗐M���܂�
			public void actionPerformed(ActionEvent e){
				sendCommand("down");
			}
		});
		p.add(b, BorderLayout.SOUTH);

		// �C��̗l�q��\������Canvas���쐬���܂�
		c = new Canvas();
		c.setSize(256,256);// �傫���̐ݒ�
		// �t���[���ɕK�v�ȕ��i�����t���܂�
		p.add(c);
		f.add(p);

		// �t���[��f��login�{�^�������t���܂�
		b = new Button("login");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// login�{�^���������ꂽ�ꍇ�̏���
				// �T�[�o���Z�b�g����Ă��Ȃ����login�������s���܂�
				if(server == null) login();
			}
		});
		f.add(b,BorderLayout.NORTH);

		// �t���[��f��logout�{�^�������t���܂�
		b = new Button("logout");
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				logout();
			}
		});
		f.add(b,BorderLayout.SOUTH);

		// �t���[��f��\�����܂�
		f.setSize(335,345);
		f.show();
	}

	// run���\�b�h
	// 500�~���b���Ƃɉ�ʂ��X�V���܂�
	public void run(){
		while (true){
			try {
				Thread.sleep(500);
			}catch(Exception e){
			}
			// repain���\�b�h��p����,�T�[�o��̏�����ʂɏo�͂��܂�
			repaint();
		}
	}

	// login�����֘A�̃I�u�W�F�N�g
	int sx = 100;
	int sy = 100;
	TextField host, tf_name;
	Dialog d;

	// login���\�b�h
	// login�E�B���h�E��\����,�K�v�ȏ��𓾂܂�
	// ���ۂ�login������,realLogin���\�b�h�ōs���܂�
	void login(){
		// �E�B���h�E�̕\���ƃf�[�^�̓���
		d = new Dialog(f, true);
		host = new TextField(10) ;
		tf_name = new TextField(10) ;
		d.setLayout(new GridLayout(3,2));
		d.add(new Label("host:"));
		d.add(host);
		d.add(new Label("name:"));
		d.add(tf_name);
		Button b = new Button("OK");
		b.addActionListener(new ActionListener(){
			// ���͂�����������,readlLogin���\�b�h���g���ăT�[�o��login���܂�
			public void actionPerformed(ActionEvent e){
				realLogin(host.getText(), tf_name.getText());
				d.dispose();
			}
		});
		d.add(b);
		d.setResizable(true);
		d.setSize(200, 150);
		d.show();
		(new Thread(this)).start();
	}

	// realLogin�֘A�̃I�u�W�F�N�g
	Socket server;// �Q�[���T�[�o�Ƃ̐ڑ��\�P�b�g
	int port = 10000;// �ڑ��|�[�g
	BufferedReader in;// ���̓X�g���[��
	PrintWriter out;// �o�̓X�g���[��
	String name;// �Q�[���Q���҂̖��O

	// realLogin���\�b�h
	// �T�[�o�ւ�login�������s���܂�
	void realLogin(String host, String name){
		try {
			// �T�[�o�Ƃ̐ڑ�
			this.name = name;
			server = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(
			  server.getInputStream()));
			out = new PrintWriter(server.getOutputStream());

			// login�R�}���h�̑��t
			out.println("login " + name);
			out.flush();
			repaint();
		}catch(Exception e){
			e.printStackTrace();
			System.exit(1);
		}
	}

	// logout���\�b�h
	// logout�������s���܂�
	void logout(){
		try {
			// logout�R�}���h�̑��t
			out.println("logout");
			out.flush();
			server.close();
		}catch (Exception e){
			;
		}
		System.exit(0);
	}

	// repaint���\�b�h
	// �T�[�o����Q�[���̏��𓾂�,�N���C�A���g�̉�ʂ�`�������܂�
	void repaint(){
		// �T�[�o��stat�R�}���h�𑗕t��,�Ֆʂ̗l�q�Ȃǂ̏��𓾂܂�
		out.println("stat");
		out.flush();

		try {
			String line = in.readLine();// �T�[�o����̓��͂̓ǂݍ���
			Graphics g = c.getGraphics();// Canvas c�ɊC�̗l�q��`���܂�

			// �C�̕`��(�P�Ȃ���l�p�`�ł�)
			g.setColor(Color.blue);
			g.fillRect(0, 0, 256, 256);

			//ship_info����n�܂�D�̏��̐擪�s��T���܂�
			while (!"ship_info".equalsIgnoreCase(line))
				line = in.readLine();

			// �D�̏��ship_info�̕\��
			// ship_info�̓s���I�h�݂̂̍s�ŏI���ł�
			line = in.readLine();
			while (!".".equals(line)){
				StringTokenizer st = new StringTokenizer(line);
				// ���O��ǂݎ��܂�
				String obj_name = st.nextToken().trim();

				// �����̑D�͐�(red)�ŕ\����,���l�̑D�͗�(green)�ŕ\�����܂�
				if (obj_name.equals(name))//�����̑D
					g.setColor(Color.red);
				else // ���l�̑D
					g.setColor(Color.green);

				// �D�̈ʒu���W��ǂݎ��܂�
				int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;

				// �D��\�����܂�
				g.fillOval(x - 10, 256 - y - 10, 20, 20);
				// ���_��D�̉E���ɕ\�����܂�
				g.drawString(st.nextToken(),x+10,256-y+10) ;
				// ���O��D�̉E��ɕ\�����܂�
				g.drawString(obj_name,x+10,256-y-10) ;

				// ���̂P�s��ǂݎ��܂�
				line = in.readLine();
			}

			// energy_info����n�܂�,�R���^���N�̏���҂��󂯂܂�
			while (!"energy_info".equalsIgnoreCase(line))
				line = in.readLine();

			// �R���^���N�̏��energy_info�̕\��
			// energy_info�̓s���I�h�݂̂̍s�ŏI���ł�
			line = in.readLine();
			while (!".".equals(line)){
				StringTokenizer st = new StringTokenizer(line);

				// �R���^���N�̈ʒu���W��ǂݎ��܂�
				int x = Integer.parseInt(st.nextToken()) ;
				int y = Integer.parseInt(st.nextToken()) ;

				// �R���^���N��,�������̐ԊۂŎ����܂�
				g.setColor(Color.red);
				g.fillOval(x - 5, 256 - y - 5, 10, 10);
				g.setColor(Color.white);
				g.fillOval(x - 3, 256 - y - 3, 6, 6);

				// ���̂P�s��ǂݎ��܂�
				line = in.readLine();
			}
		}catch (Exception e){
		e.printStackTrace();
		System.exit(1);
		}
	}

	// sendCommand���\�b�h
	// �T�[�o�փR�}���h�𑗐M���܂�
	void sendCommand(String s){
		if ("up".equals(s)){
			out.println("up");
		}else if ("down".equals(s)){
			out.println("down");
		}else if ("left".equals(s)){
			out.println("left");
		}else if ("right".equals(s)){
			out.println("right");
		}
		out.flush();
	}

	// main���\�b�h
	// UmiClient���N�����܂�
	public static void main(String[] arg){
		new UmiClient();
	}
}