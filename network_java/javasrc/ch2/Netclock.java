// 時刻を答えるサーバプログラムNetclock.java
// このプログラムはポート番号6000番で動作するサーバです
// クライアントからの接続に対し,時刻を返します
// このプログラムを停止させるにはコントロールCを入力してください
// 使い方java Netclock

// ライブラリの利用
import java.io.* ;
import java.net.* ;
import java.util.* ;

// Netclockクラス
class Netclock{
	public static void main(String args[]){
		ServerSocket servsock = null ;// サーバ用ソケット
		Socket sock ;// ソケットの読み書き用オブジェクト
		OutputStream out ;// 出力ストリーム
		String outstr ;// 出力データを格納する文字列
		int i ;//出力の繰り返し制御用変数
		Date d ;// 日付時刻処理用オブジェクト

		try{
			// サーバソケットの作成
			servsock = new ServerSocket(6000,300) ;
			// サーバ処理の繰り返し
			while(true){
				sock = servsock.accept() ;//接続受付
				// 出力用データの作成
				d = new Date() ;
				outstr = "\n"
					+ "Hello, this is Netclock server."
					+ "\n" + d.toString() + "\n"
					+ "Thank you." + "\n";
				// データの出力
				out = sock.getOutputStream() ;
				for(i = 0; i < outstr.length();++i)
					out.write((int)outstr.charAt(i)) ;
				out.write('\n') ;
				// 接続終了
				sock.close() ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}