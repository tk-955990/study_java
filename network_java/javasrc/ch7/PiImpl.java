// RMIによる分散処理プログラムの実装例
// (2)リモートサービスの実装のクラスファイル

// PiImpl.java
// このクラスは、リモートサービスの実装クラスです
// 分散処理システムにおける機能を記述します
// 具体的には、モンテカルロ法によりπ/4を計算します

// ライブラリの利用
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.lang.Math ;

// PiImplクラス
public class PiImpl extends UnicastRemoteObject
	implements Pi{

	// コンストラクタPiImpl
	public PiImpl() throws RemoteException{
		super();
	}

	// putPiメソッド
	// モンテカルロ法によりπ/4を計算します
	public long putPi(long maxloopcount){
		long i ; // 点生成の繰り返し回数
		long in=0 ; // 半径１の円内に点が存在する場合の数
		double x,y ; // ランダムに生成する点のx,y座標
		for(i=0;i<maxloopcount;++i){
			x=Math.random() ;// x座標
			y=Math.random() ;// y座標
			if((x*x+y*y)<=1.0) // もし半径１の円内ならば・・・
			++in ; // inを1増やす
		}
		return in ;
	}
}