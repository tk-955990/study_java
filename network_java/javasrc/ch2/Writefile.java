// Writefile.java
// キーボードからの入力を受け取り,そのまま画面に出力します
// また,ファイルに入力を順に格納します
// 使い方java Writefile ファイル名
// このプログラムを終了するには,行の先頭にピリオド.を入力してください
//ライブラリの利用
import java.io.*;
// Writefileクラス
public class Writefile {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//配列の定義
		boolean cont = true ;// ループ制御用変数
		FileOutputStream outfile = null;
		// ファイル出力用オブジェクト
		// オブジェクトoutfileを作り,ファイルの出力を準備します
		try{
			outfile = new FileOutputStream(args[0]) ;
		}
		catch(FileNotFoundException e){
			// ファイル準備の失敗
			System.err.println("ファイルがありません") ;
			System.exit(1) ;
		}
		// 行頭へのピリオドの入力まで,以下のループを繰り返します
		while (cont) {
			try {
				// System.inからの読み込み
				int n = System.in.read(buff);
				// System.outへの書き出し
				System.out.write(buff, 0, n) ;
				// 行頭ピリオドの検出
				if(buff[0] == '.') cont = false ;
				else outfile.write(buff,0,n) ;
			}
			// 以下は例外処理です
			catch(Exception e){
				// 例外時はプログラムを終了します
				System.exit(1);
			}
		}
		// ファイルを閉じます
		try{
			outfile.close() ;
		}
		catch(IOException e){
			// ファイルクローズの失敗です
			System.err.println("ファイルのエラーです") ;
			System.exit(1) ;
		}
	}
}