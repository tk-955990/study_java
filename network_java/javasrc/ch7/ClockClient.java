// RMIによるNetClockプログラムの実装例
// (5)クライアントプロセス実装のクラスファイル

// ClockClient.java
// このクラスは、クライアントプロセスのクラスです
// NetClockのRMI版システムにおけるクライアントの機能を記述します
// RMIレジストリにおける名前の取得や、サーバへの仕事の依頼を行います
// 使用方法
// java ClockClient
// なお、クライアント起動の前に、サーバとレジストリを起動してください

// ライブラリの利用
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;

// ClockClientクラス
public class ClockClient{
	// mainメソッド
	public static void main(String args[]){
		try{
			Clock c = (Clock)Naming.lookup("//localhost/ClockService") ;
			System.out.println(c.putTime()) ;
		}catch(Exception e){
			System.out.println(e) ;
		}
	}
}