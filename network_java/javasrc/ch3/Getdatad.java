// Getdatad.java
// データ受信サーバ
// クライアントからデータを受け取り、そのまま画面に出力します
// 引数でポート番号を指定します
// 使い方java Getdatad port256 port
// 実際のポート番号は256*(port256)+portになります

//ライブラリの利用
import java.io.*;
import java.net.* ;

// Getdatadクラス
public class Getdatad {
	// プログラムの本体main
	public static void main(String[] args){
		byte[] buff = new byte[1024]; //配列の定義
		ServerSocket servsock = null ;//サーバソケット
		Socket sock = null ;// 接続用ソケット
		InputStream instr = null;// データ読み取り用オブジェクト
		boolean cont = true ;

		final int dataport = Integer.parseInt(args[0])*256
		+ Integer.parseInt(args[1]) ; // データ受信用ポート

		// 受信用サーバソケットを作成します
		// 入出力のストリームを作り,データ読み出しを準備します
		try{
			// サーバソケットの作成
			servsock = new ServerSocket(dataport,1) ;
			// サーバ処理の繰り返し
			while(true){
				sock = servsock.accept() ;//接続受付
				cont = true ;
				while(cont){
					try{
						// データの読み込みと出力
						instr = sock.getInputStream() ;
						int n = instr.read(buff);
						// System.outへの書き出し
						System.out.write(buff, 0, n) ;
					}
					// 以下は例外処理です
					catch(Exception e){
						// 読み出し終了時にループも終了します
						cont = false ;
					}
				}
				// 接続終了
				sock.close() ;
				System.out.println("接続終了") ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}