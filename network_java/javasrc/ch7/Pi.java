// RMIによる分散処理プログラムの実装例
// (1)リモートサービスのインタフェース定義ファイル

// Pi.java Piインタフェース
// このインタフェースは、PiImplクラスのためのインタフェースです

// ライブラリの利用
import java.rmi.Remote;
import java.rmi.RemoteException ;

// Piインタフェース
public interface Pi extends Remote{
	long putPi(long maxloopcount) throws RemoteException;
}