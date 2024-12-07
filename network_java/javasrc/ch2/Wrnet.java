// Wrnet.java
// ネットワーク上のサーバに接続し,データを送ります
// その後サーバからデータを受け取り,そのまま画面に出力します
// 使い方java Wrnet DNS 名ポート番号
// 例java Wrnet kiku.fuis.fukui-u.ac.jp 80

//ライブラリの利用
import java.io.*;
import java.net.* ;

// Wrnetクラス
public class Wrnet {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//配列の定義
		Socket wrsocket = null ;// サーバ接続用ソケット
		InputStream instr = null;// データ読み取り用オブジェクト
		OutputStream outstr = null;// データ出力用オブジェクト
		boolean cont = true ;
		// 指定のポートに対して,ソケットを作成します
		// 入出力のストリームを作り,データ読み出しを準備します
		try{
			wrsocket
				= new Socket(args[0],Integer.parseInt(args[1])) ;
			instr = wrsocket.getInputStream() ;
			outstr = wrsocket.getOutputStream() ;
		}
		catch(Exception e){
			System.err.println("ネットワークエラーです") ;
			System.exit(1) ;
		}
		//
		while (cont) {
			try {
				// System.inからの読み込み
				int n = System.in.read(buff);
				// System.outへの書き出し
				// System.out.write(buff, 0, n) ;
				// 行頭ピリオドの検出
				if(buff[0] == '.') cont = false ;
				else outstr.write(buff,0,n) ;
			}
			// 以下は例外処理です
			catch(Exception e){
				// 例外時はプログラムを終了します
				System.exit(1);
			}
		}
		// データの終了まで,以下のループを繰り返します
		cont = true ;
		while (cont) {
			try {
				// 読み込み
				int n = instr.read(buff);
				// System.outへの書き出し
				System.out.write(buff, 0, n) ;
			}
			// 以下は例外処理です
			catch(Exception e){
				// 読み出し終了時にループも終了します
				cont = false ;
			}
		}
		// コネクションを閉じます
		try{
			instr.close() ;
		}
		catch(Exception e){
			// ネットワーククローズ失敗です
			System.err.println("ネットワークのエラーです") ;
			System.exit(1) ;
		}
	}
}
