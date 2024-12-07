// Readnet.java
// ネットワーク上のサーバからデータを受け取り,そのまま画面に出力します
// 使い方java Readnet DNS 名ポート番号
// 例java Readnet kiku.fuis.fukui-u.ac.jp 6000

//ライブラリの利用
import java.io.*;
import java.net.* ;

// Readnetクラス
public class Readnet {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//配列の定義
		Socket readsocket = null ;// サーバ接続用ソケット
		InputStream instr = null;// データ読み取り用オブジェクト
		boolean cont = true ;
		// 指定のポートに対して,ソケットを作成します
		// オブジェクトinstrを作り,データ読み出しを準備します
		try{
			readsocket
				= new Socket(args[0],Integer.parseInt(args[1])) ;
			instr = readsocket.getInputStream() ;
		}
		catch(Exception e){
			System.err.println("ネットワークエラーです") ;
			System.exit(1) ;
		}

		// データの終了まで,以下のループを繰り返します
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