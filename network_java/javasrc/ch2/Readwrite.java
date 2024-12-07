// Readwrite.java
// キーボードからの入力を受け取り,そのまま画面に出力します
// このプログラムを終了するには,コントロールＣを入力してください
//ライブラリの利用
import java.io.*;
// Readwriteクラス
public class Readwrite {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024];//配列の定義
		// 以下のループを無限に繰り返します
		//
		while (true) {
			try {
				// System.inからの読み込み
				int n = System.in.read(buff);
				// System.outへの書き出し
				System.out.write(buff, 0, n);
			}
			// 以下は例外処理です
			catch(Exception e){
				// 例外時はプログラムを終了します
				System.exit(1);
			}
		}
	}
}