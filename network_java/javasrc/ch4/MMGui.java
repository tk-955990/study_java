// GUI �Ń��[���N���C�A���g�v���O����MMGui.java
// ���̃v���O������,�O���t�B�J���ȃ��[�U�C���^�t�F�[�X�������
// ���[���N���C�A���g�v���O�����ł�
// �g����java MMGui
// MMGui.java�̃R���p�C���ɂ�,MailManager.java�t�@�C�����K�v�ł�

// ���C�u�����̗��p
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

// MMGui�N���X
public class MMGui {
	static boolean debug = MailManager.debug;
	MailManager mm;

	// �����N���X�ɂ�郁�[���\�������̐ݒ�
	ActionListener listAction = new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String cmd = ae.getActionCommand();
			if (debug) {
				System.out.println("MMGui.listAction:" + cmd);
			}
			StringTokenizer st = new StringTokenizer(cmd);
			readMail(Integer.parseInt(st.nextToken()));
		}
	};

	// �����N���X�ɂ��{�^�������̐ݒ�
	ActionListener buttonAction = new ActionListener(){
		public void actionPerformed(ActionEvent ae){
			String cmd = ae.getActionCommand();
			if (debug) {
				System.out.println("MMGui.buttonAction:" + cmd);
			}
			if ("quit".equalsIgnoreCase(cmd)){
				System.exit(0);
			}else if ("get".equalsIgnoreCase(cmd)){
				mm.getmail();
				list();
			}else if ("send".equalsIgnoreCase(cmd)){
				sendMail();
			}
		}
	};

	TextArea message;
	List list;
	Frame f;

	// �R���X�g���N�^
	// �E�B���h�E�Ȃǂ�ݒ肵�܂�
	public MMGui ()
	{
		f = new Frame();
		f.setSize(600, 480);
		GridBagLayout layout = new GridBagLayout();
		f.setLayout(layout);

		Panel p = new Panel();
		// Get�{�^��
		Button b = new Button("Get");
		b.addActionListener(buttonAction);
		p.add(b);

		// Send�{�^��
		b = new Button("Send");
		b.addActionListener(buttonAction);
		p.add(b);

		// Quit�{�^��
		b = new Button("Quit");
		b.addActionListener(buttonAction);
		p.add(b);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;

		layout.setConstraints(p, c);
		f.add(p);

		// ���[�����X�g�G���A�̕\��
		list = new List(5);
		list.addActionListener(listAction);
		layout.setConstraints(list, c);
		f.add(list);

		// ���b�Z�[�W�G���A�̕\��
		message = new TextArea(18, 70);
		message.setEditable(false);
		layout.setConstraints(message, c);
		f.add(message);

		f.show();

		mm = new MailManager();
		list();
	}

	// list���\�b�h
	// ���[���̃��X�g��\�����܂�
	void list(){
		String[] mailList = mm.list();
		list.removeAll();
		for (int i = 0; i < mailList.length; i++){
			list.add(mailList[i]);
		}
	}

	// readMail���\�b�h
	// ���b�Z�[�W��\�����܂�
	void readMail(int i){
		message.setText(mm.readMail(i));
	}

	// sendMail���\�b�h
	// Editor�N���X���g����,���[���𑗐M���܂�
	void sendMail(){
		new Editor();
	}

	// main���\�b�h
	public static void main(String[] arg){
		new MMGui();
	}

	// Editor�N���X
	// ���b�Z�[�W�ҏW��ʂ�񎦂�,���b�Z�[�W�����[���Ƃ��đ��M���܂�
	class Editor {
		Frame f;
		TextField subject;
		TextField to;
		TextArea message;

		// �{�^���̏���
		ActionListener editorAction = new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				String cmd = ae.getActionCommand();
				if (debug){
					System.out.println("MMGui.Editor.editorAction" + cmd);
				}
				if ("OK".equalsIgnoreCase(cmd)){
					mm.sendmail(to.getText(),
								subject.getText(),
								message.getText());
				}
				f.dispose();
			}
		};

		// �R���X�g���N�^
		// ���b�Z�[�W�ҏW�E�B���h�E���������܂�
		Editor(){
			f = new Frame();
			f.setSize(600, 400);
			subject = new TextField(50);
			to = new TextField(50);
			message = new TextArea(15, 70);
			f.setLayout(new FlowLayout());
			Panel p = new Panel();
			p.add(new Label("Subject:"));
			p.add(subject);
			f.add(p);
			p = new Panel();
			p.add(new Label("To:"));
			p.add(to);
			f.add(p);
			f.add(message);
			Button b = new Button("OK");
			b.addActionListener(editorAction);
			f.add(b);
			b = new Button("CANCEL");
			b.addActionListener(editorAction);
			f.add(b);
			f.show();
		}
	}
}