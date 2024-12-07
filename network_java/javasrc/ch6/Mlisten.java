// マルチキャストパケット受信プログラムMlisten.java
// このプログラムは,マルチキャストパケットを受信します
// 使い方java Mlisten [マルチキャストアドレス] [ポート番号]

// ライブラリの利用
import java.net.*;
import java.io.*;

// Mlistenクラス
public class Mlisten {
	// 処理の本体
	public static void main(String[] arg){
		String multicastAddress = "224.0.0.1" ;
				// マルチキャストアドレス224.0.0.1は,
				// ルータを越えない場合のアドレスです
		int port = 6000 ;
				// チャット用のポート番号,指定がなければ6000 番とします
		byte[] buff = new byte[1024] ;
		if(arg.length >= 1) multicastAddress = arg[0] ;
		if(arg.length >= 2) port = Integer.parseInt(arg[1]) ;
		try{
			InetAddress chatgroup
				= InetAddress.getByName(multicastAddress) ;
			MulticastSocket soc = new MulticastSocket(port) ;
			soc.joinGroup(chatgroup) ;
			while(true){
				DatagramPacket recv
					= new DatagramPacket(buff,buff.length) ;
				soc.receive(recv) ;
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