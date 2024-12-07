// スレッドテストプログラムThreadtest.java
// このプログラムは,０から10 までの数値を出力するスレッドを並行動作させます
// 使い方java Threadtest

// ライブラリの利用
import java.net.*;
import java.io.*;

// Threadtestクラス
// Threadtestクラスは,スレッドの作成と実行を管理します
public class Threadtest {
	// mainメソッド
	public static void main(String[] arg){
		try {
			// スレッド用クラスCountTenのオブジェクトを生成します
			CountTen no1 =
			new CountTen("No1");
			CountTen no2 =
			new CountTen("No2");
			// スレッドを生成します
			Thread no1_thread = new Thread(no1);
			Thread no2_thread = new Thread(no2);
			// スレッドを起動します
			no1_thread.start();
			no2_thread.start();
		}
		catch(Exception e){
			System.err.print(e);
			System.exit(1);
		}
	}
}

// CountTenクラス
// ０から10 まで数え上げます
class CountTen implements Runnable {
	String myname ;
	// コンストラクタスレッドの名前を受け取ります
	public CountTen(String name){
		myname = name;
	}
	// 処理の本体
	public void run(){
		int i ;
		for(i=0;i<=10;++i){
			System.out.println(myname + ":" + i) ;
		}
	}
}