// RMIによる分散処理プログラムの実装例
// (5)クライアントプロセス実装のクラスファイル

// PiClient.java
// このクラスは、クライアントプロセスのクラスです
// 分散処理システムにおけるクライアントの機能を記述します
// RMIレジストリにおける名前の取得や、サーバへの仕事の依頼を行います
// 使用方法
// java PiClient サーバ名
// なお、クライアント起動の前に、サーバとレジストリを起動してください

// ライブラリの利用
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.* ;

//PiClientクラス
public class PiClient{

	// mainメソッド
	public static void main(String args[]){
		long result ;//サーバの返す値
		long millis ;//経過時間
		long maxloopcount=10000000 ;//発生する点の個数

		try{
			//計算開始
			System.out.println("Start") ;
			//現在の時刻（ミリ秒）
			millis = System.currentTimeMillis() ;
			//rmiregistryによるサーバの検索
			Pi p = (Pi)Naming.lookup("//"+args[0]+"/PiService") ;
			//サーバのputPIメソッドによるπの計算
			result=p.putPi(maxloopcount);
			//計算終了、経過時間の測定
			//経過時間にはネットワーク処理も含まれます
			millis = System.currentTimeMillis() - millis ;

			//πの値と経過時間の出力
			System.out.println((double)result/maxloopcount*4) ;
			System.out.println((double)millis/1000 + "sec") ;
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}