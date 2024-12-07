// いんちきHTTPサーバPhttpd.java
// このプログラムはポート番号80番で動作するサーバです
// 使い方java Phttp データファイル名
// WWWクライアントからの接続に対し,引数で指定したファイルを返します

// ライブラリの利用
import java .io.* ;
import java.net.* ;
import java.util.* ;

// Phttpdクラス
class Phttpd{
	public static void main(String args[]){
		// サーバソケットの準備
		ServerSocket servsock = null ;
		Socket sock ;
		// 入出力の準備
		OutputStream out ;
		BufferedReader in ;
		FileInputStream infile = null;
		byte[] buff = new byte[1024];
		// その他の変数
		boolean cont = true ;
		int i ;

		try{
			// サーバ用ソケットの作成（ポート番号80番）
			servsock = new ServerSocket(80,300) ;
			while(true){
				sock = servsock.accept() ;// 接続要求の受付
				// 接続先の表示
				System.out.println("接続要求"
					+ (sock.getInetAddress()).getHostName()) ;
				// オブジェクトinfileを作り,ファイルを準備します
				try{
					infile = new FileInputStream(args[0]) ;
				}
				catch(Exception e){
					// ファイル準備の失敗
					System.err.println("ファイルがありません") ;
					System.exit(1) ;
				}
				// 読み書き用オブジェクトの生成
				in = new BufferedReader(new
				InputStreamReader(sock.getInputStream()));
				out = sock.getOutputStream() ;
				// とりあえず改行を２つ読み飛ばす
				for(i = 0; i < 2;++i)
					in.readLine() ;
				// ファイルの出力
				cont = true ;
				while(cont){
					// ファイルからの読み込みとネットワーク出力
					try{
						int n = infile.read(buff);
						out.write(buff,0,n) ;
					}
					catch(Exception e){
						cont = false ;
					}
				}
				// 接続終了
				sock.close() ;
				infile.close() ;
			}
		}catch(IOException e){
			System.exit(1) ;
		}
	}
}