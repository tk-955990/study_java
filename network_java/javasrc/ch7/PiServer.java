// RMIによる分散処理プログラムの実装例
// (4)サーバプロセス実装のクラスファイル

// PiServer.java
// このクラスは、サーバプロセスのクラスです
// 分散処理システムにおけるサーバの機能を記述します
// RMIレジストリにおける名前の登録や、サーバの起動を行います
// 使用方法
// java PiServer
// なお、サーバ起動の前に、レジストリを起動してください
// RMIレジストリの起動は以下のようにします
// rmiregistry

// ライブラリの利用
import java.rmi.Naming;

// PiServerクラス
public class PiServer{

	// コンストラクタ
	public PiServer(){
		try{
			Pi p = new PiImpl() ;
			Naming.rebind("//localhost/PiService",p) ;
		} catch(Exception e) {
			e.printStackTrace() ;
		}
	}

	// mainメソッド
	public static void main(String args[]){
		new PiServer() ;
	}
}
