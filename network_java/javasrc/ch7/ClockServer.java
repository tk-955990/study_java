// RMIによるNetClockプログラムの実装例
// (4)サーバプロセス実装のクラスファイル

// ClockServer.java
// このクラスは、サーバプロセスのクラスです
// NetClockのRMI版システムにおけるサーバの機能を記述します
// RMIレジストリにおける名前の登録や、サーバの起動を行います
// 使用方法
// java ClockServer
// なお、サーバ起動の前に、レジストリを起動してください
// RMIレジストリの起動は以下のようにします
// rmiregistry

// ライブラリの利用
import java.rmi.Naming;
// ClockServerクラス
public class ClockServer{
	// コンストラクタ
	public ClockServer(){
		try{
			Clock c = new ClockImpl() ;
			Naming.rebind("//localhost/ClockService",c) ;
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	// mainメソッド
	public static void main(String args[]){
		new ClockServer() ;
	}
}