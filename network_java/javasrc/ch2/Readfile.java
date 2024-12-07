// Readfile.java
// ファイルの内容を読み取り,そのまま画面に出力します
// 使い方java Readfile ファイル名

//ライブラリの利用
import java.io.*;

// Readfileクラス
public class Readfile {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//配列の定義
		boolean cont = true ;// ループ制御用変数
		FileInputStream infile = null;// ファイル読取用
		// オブジェクトinfileを作り,ファイル読み出しを準備します
		try{
			infile = new FileInputStream(args[0]) ;
		}
		catch(FileNotFoundException e){
			// ファイル準備の失敗
			System.err.println("ファイルがありません") ;
			System.exit(1) ;
		}

		// ファイルの終了まで,以下のループを繰り返します
		while (cont) {
			try {
				// ファイルからの読み込み
				int n = infile.read(buff);
				// System.outへの書き出し
				System.out.write(buff, 0, n) ;
			}
			// 以下は例外処理です
			catch(Exception e){
				// 読み出し終了時にループも終了します
				cont = false ;
			}
		}
		// ファイルを閉じます
		try{
			infile.close() ;
		}
		catch(IOException e){
			// ファイルクローズの失敗です
			System.err.println("ファイルのエラーです") ;
			System.exit(1) ;
		}
	}
}