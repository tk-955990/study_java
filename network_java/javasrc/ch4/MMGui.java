// GUI 版メールクライアントプログラムMMGui.java
// このプログラムは,グラフィカルなユーザインタフェースを備えた
// メールクライアントプログラムです
// 使い方java MMGui
// MMGui.javaのコンパイルには,MailManager.javaファイルが必要です

// ライブラリの利用
import java.awt.*;
import java.awt.event.*;
import java.util.StringTokenizer;

// MMGuiクラス
public class MMGui {
	static boolean debug = MailManager.debug;
	MailManager mm;

	// 無名クラスによるメール表示処理の設定
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

	// 無名クラスによるボタン処理の設定
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

	// コンストラクタ
	// ウィンドウなどを設定します
	public MMGui ()
	{
		f = new Frame();
		f.setSize(600, 480);
		GridBagLayout layout = new GridBagLayout();
		f.setLayout(layout);

		Panel p = new Panel();
		// Getボタン
		Button b = new Button("Get");
		b.addActionListener(buttonAction);
		p.add(b);

		// Sendボタン
		b = new Button("Send");
		b.addActionListener(buttonAction);
		p.add(b);

		// Quitボタン
		b = new Button("Quit");
		b.addActionListener(buttonAction);
		p.add(b);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;

		layout.setConstraints(p, c);
		f.add(p);

		// メールリストエリアの表示
		list = new List(5);
		list.addActionListener(listAction);
		layout.setConstraints(list, c);
		f.add(list);

		// メッセージエリアの表示
		message = new TextArea(18, 70);
		message.setEditable(false);
		layout.setConstraints(message, c);
		f.add(message);

		f.show();

		mm = new MailManager();
		list();
	}

	// listメソッド
	// メールのリストを表示します
	void list(){
		String[] mailList = mm.list();
		list.removeAll();
		for (int i = 0; i < mailList.length; i++){
			list.add(mailList[i]);
		}
	}

	// readMailメソッド
	// メッセージを表示します
	void readMail(int i){
		message.setText(mm.readMail(i));
	}

	// sendMailメソッド
	// Editorクラスを使って,メールを送信します
	void sendMail(){
		new Editor();
	}

	// mainメソッド
	public static void main(String[] arg){
		new MMGui();
	}

	// Editorクラス
	// メッセージ編集画面を提示し,メッセージをメールとして送信します
	class Editor {
		Frame f;
		TextField subject;
		TextField to;
		TextArea message;

		// ボタンの処理
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

		// コンストラクタ
		// メッセージ編集ウィンドウを準備します
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