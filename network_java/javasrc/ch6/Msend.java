// マルチキャストパケット送信プログラムMsend.java
// このプログラムは,マルチキャストパケットを送信します
// 使い方java Msend [マルチキャストアドレス] [ポート番号]

// ライブラリの利用
import java.net.*;
import java.io.*;

// Msendクラス
public class Msend {

	// 処理の本体
	public static void main(String[] arg){
		String multicastAddress = "224.0.0.1" ;
				// マルチキャストアドレス224.0.0.1は,
				// ルータを越えない場合のアドレスです
		int port = 6000 ;
				// チャット用のポート番号,指定がなければ6000 番とします
		MulticastSocket s = null;
		byte[] buff = new byte[1024] ;

		if(arg.length >= 1) multicastAddress = arg[0] ;
		if(arg.length >= 2) port = Integer.parseInt(arg[1]) ;
		try{
			InetAddress chatgroup
				= InetAddress.getByName(multicastAddress) ;
			MulticastSocket soc = new MulticastSocket(port) ;
			soc.joinGroup(chatgroup) ;
			while(true){
				int n = System.in.read(buff,0,buff.length) ;
				if(n > 0){
					DatagramPacket dp
						= new DatagramPacket(buff,n,chatgroup,port) ;
					soc.send(dp) ;
				}
				else break ;// ループ終了
			}
		}catch(Exception e){
			e.printStackTrace() ;
			System.exit(1) ;
		}
	}
}