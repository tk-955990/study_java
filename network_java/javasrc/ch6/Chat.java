// マルチキャスト版チャットプログラムChat.java
// このプログラムは,マルチキャストを使ってチャット機能を提供します
// 使い方java Chat [ポート番号]

// ライブラリの利用
import java.net.*;
import java.io.*;

// Chatクラス
// Chatクラスは,受信スレッドを作成・実行し,送信を担当します
public class Chat {
	final byte TTL = 1 ;//同一セグメント内部のみ到達可能とします
	final String MULTICASTADDRESS = ("224.0.0.1") ;
				// マルチキャストアドレス224.0.0.1は,
				// ルータを超えない場合のアドレスです
	int port = 6000 ;
				// チャット用のポート番号,指定がなければ6000 番とします
	byte[] buff = new byte[1024] ;//送信用バッファ
	String myname ="" ;// 利用者名
	int nameLength = 0 ;//利用者名の長さ
	MulticastSocket soc = null ; // マルチキャストソケット
	InetAddress chatgroup = null ; //チャット用アドレス

	// コンストラクタ利用者名などを設定します
	public Chat(int portno){
	port = portno ; //ポート番号の設定
	BufferedReader lineread
	  = new BufferedReader(new InputStreamReader(System.in)) ;
	System.out.println("お名前をどうぞ") ;
	try{
		myname = lineread.readLine() ;
	}catch(Exception e){
		e.printStackTrace() ;
		System.exit(1) ;
	}
	System.out.println("ようこそ" + myname + "さん.") ;
	myname = myname + ">" ;
	nameLength = (myname.getBytes()).length ;
	for(int i = 0;i < nameLength;++i)
		buff[i] = (myname.getBytes())[i] ;
	}

	// makeMulticastSocketメソッド
	//MULTICASTADDRESSに対してマルチキャストソケットを作成します
	public void makeMulticastSocket()
	{
		try{
			chatgroup
				= InetAddress.getByName(MULTICASTADDRESS) ;
			soc = new MulticastSocket(port) ;
			soc.joinGroup(chatgroup) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// startLintenerメソッド
	// スレッド用クラスListenPacketのオブジェクトを生成し,起動します
	public void startListener()
	{
		try{
			ListenPacket lisner =
				new ListenPacket(soc);
			Thread lisner_thread = new Thread(lisner);
			lisner_thread.start();//受信スレッドの開始
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// sendMsgsメソッド
	// マルチキャストパケットの送信を担当します
	public void sendMsgs()
	{
		try{
			// 送信ループ
			while(true){
				int n = System.in.read(
						buff,nameLength ,1024 - nameLength ) ;
				if(n > 0){
					DatagramPacket dp
						= new DatagramPacket(
						buff,n + nameLength,chatgroup,port) ;
					soc.send(dp) ;
				}
				else break ;// ループ終了
			}
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// quitGroupメソッド
	// 接続を終了します
	public void quitGroup()
	{
		try{
			// 接続の終了
			System.out.println("接続終了") ;
			soc.leaveGroup(chatgroup) ;
			System.exit(0) ;
		}
		catch(Exception e){
			e.printStackTrace() ;
			System.exit(1);
		}
	}

	// mainメソッド
	public static void main(String[] arg){
		Chat c = null ;
		int portno = 6000 ;
		if(arg.length >= 1) portno = Integer.parseInt(arg[0]) ;
		c = new Chat(portno) ;
		c.makeMulticastSocket() ;
		c.startListener() ;
		c.sendMsgs() ;
		c.quitGroup() ;
	}
}

// ListenPacketクラス
// マルチキャストパケットを受信します
class ListenPacket implements Runnable {
	MulticastSocket s = null;
	// コンストラクタマルチキャストスレッドを受け取ります
	public ListenPacket(MulticastSocket soc){
		s = soc;
	}
	// 処理の本体
	public void run(){
		byte[] buff = new byte[1024] ;
		try{
			while(true){
				DatagramPacket recv
					= new DatagramPacket(buff,buff.length) ;
				s.receive(recv) ;
				if(recv.getLength() > 0){
					System.out.write(buff,0,recv.getLength()) ;
				}
			}
		}catch(Exception e){
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}
}