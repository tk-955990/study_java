// RMIによる分散処理プログラムの実装例
// (5)クライアントプロセス実装のクラスファイル

// DPiClient.java
// このクラスは、クライアントプロセスのクラスです
// 分散処理システムにおけるクライアントの機能を記述します
// RMIレジストリにおける名前の取得や、サーバへの仕事の依頼を行います
// 使用方法
// java DPiClient サーバ名1 サーバ名2 ・・・
// なお、クライアント起動の前に、サーバとレジストリを起動してください

// ライブラリの利用
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.* ;

//DPiClientクラス
public class DPiClient{

	// mainメソッド
	public static void main(String args[]){
		long result=0 ;//サーバの計算結果を格納する
		long millis ;//経過時間
		long maxloopcount=10000000 ;//点の個数
		int i ;//サーバの数

		//スレッドを構成するための配列を宣言する
		// DPiClientでは、サーバの数だけスレッドを作成します
		// 各スレッドはサーバの計算処理終了を待ち、
		// 終了後Resultクラスのオブジェクトに結果を報告します
		launchPiServer l[] = new launchPiServer[args.length];
		Thread t[] = new Thread [args.length];

		//処理の本体
		try{
			//処理時間計測開始
			millis = System.currentTimeMillis() ;
			//引数で指定されたサーバに処理を依頼します
			for(i=0;i<args.length;++i){
				//引数の個数だけ処理を繰り返します
				//各サーバごとにスレッドを割り当てる
				l[i] = new launchPiServer(args[i],
				maxloopcount/args.length,millis) ;
				t[i] = new Thread(l[i]) ;
				t[i].start() ;//処理の開始
			}
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}

// launchPiServerクラス
// サーバに対して処理を依頼し、結果を受け取ります
// サーバから受け取った処理結果は、
//Resultクラスのcollect()メソッドで集計します
class launchPiServer implements Runnable{
	String address ;//サーバのアドレスを格納する
	long maxl ;//繰り返しの回数(点の数)
	long millis ;//処理開始時刻を格納する

	//コンストラクタ
	public launchPiServer(String name, long maxloopcount,
			long m){
		//呼び出し側から受け取った値をクラス内部で保持します
		address = name ;
		maxl = maxloopcount ;
		millis = m ;
	}

	//スレッドの本体
	public void run(){
		Pi p ;//サーバ用オブジェクト
		try{
			//PiServiceの利用
			p = (Pi)Naming.lookup("//"+address+"/PiService") ;
			//サーバが見つかったので、処理を依頼します
			System.out.println("Start "+ address) ;
			Result.collect(address,maxl,millis,p.putPi(maxl));
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}

// Resultクラス
// 各サーバから返された値を集計します
class Result{
	static int i=0 ;
	static long all=0 ;

	//collectメソッド
	// 複数のスレッドから呼び出されるため、排他制御が必要になります
	// このため、synchronizedというキーワードを付けて
	// メソッドを宣言します
	// 引数の一覧
	// address サーバのアドレス
	// maxloopcount サーバが生成した点の個数
	// misllis 処理開始時刻
	// res 円の内側に入った点の個数

	// collect()メソッド
	static public synchronized void
	collect(String address,long maxloopcount,long millis,
			long res){
		System.out.println("Finish "+address) ;
		// 円周率πの近似値の計算
		all+=res ;//これまでの結果に、新たに得られた結果を加算
		++i ;//返事を返したサーバの個数を数えます
		//πの近似値を出力します
		System.out.println(" " +
			(double)all/(maxloopcount*i)*4) ;
		//経過時間を出力します
		millis = System.currentTimeMillis() - millis ;
		System.out.println(" " + (double)millis/1000 + "sec") ;
	}
}