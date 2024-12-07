// RMIによるNetClockプログラムの実装例
// (2)リモートサービスの実装のクラスファイル

// ClockImpl.java
// このクラスは、リモートサービスの実装クラスです
// NetClockのRMI版システムにおける機能を記述します
// 具体的には、日時を返すメソッドputTime()を提供します

// ライブラリの利用
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.Date ;

// ClockImplクラス
// このクラスはClockインタフェースの実装を与えます
public class ClockImpl extends UnicastRemoteObject
	implements Clock{

	// コンストラクタClockImpl()
	public ClockImpl() throws RemoteException{
		super();
	}

	// putTimeメソッド
	// 現在の時刻を返します
	public String putTime(){
		Date d = new Date() ;
		return d.toString() ;
	}
}