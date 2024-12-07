// Testargs.java
// コマンドへの引数を画面に表示します
// 使い方java Testarg (引数１) (引数２) ...

import java.io.* ;
public class Testargs{
	// メインプログラム
	public static void main(String[] args){
		// メッセージの出力
		int number = 0 ;
		while(number < args.length){
			System.out.println(args[number]);
			++ number ;
		}
	}
	//メインプログラムの終了
}