// RMIによるNetClockプログラムの実装例
// (1)リモートサービスのインタフェース定義ファイル

// Clock.java Clockインタフェース
// このインタフェースは、ClockImplクラスのためのインタフェースです
// ClockImplをはじめとして、NetClockのRMI版に必須のインタフェースです

// ライブラリの利用
import java.rmi.Remote;
import java.rmi.RemoteException ;

// Clockインタフェース
public interface Clock extends Remote{
	String putTime() throws RemoteException;
}