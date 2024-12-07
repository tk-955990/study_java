// 引数で渡されたファイル名の内容を表示

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class ReadFile {
  public static void main(String[] args) {

    try {
      // args[0]で指定されたファイルパスからFileオブジェクトを作成
      File file = new File(args[0]);

      // ファイルが存在するか確認
      if (!file.exists()) {
        // ファイルが存在しなければメッセージを表示して終了
        System.out.println("ファイルが存在しません");
        return;
      }

      // FileReaderを使ってファイルを読み込む準備をする
      FileReader fileReader = new FileReader(file);
      
      // BufferedReaderでFileReaderをラップして、効率的にファイルを読み込む
      BufferedReader buffReader = new BufferedReader(fileReader);

      String data;  // 1行ずつ読み込んだデータを保持するための変数

      // ファイルの最後まで1行ずつ読み込む。nullの場合はファイルの終端を意味する
      while ((data = buffReader.readLine()) != null) {
        // 読み込んだ行を標準出力に表示
        System.out.println(data);
      }

      // ファイルの読み込みが終わったらBufferedReaderを閉じてリソースを解放
      buffReader.close();
    } catch (IOException e) {
      // 入出力エラーが発生した場合の例外処理
      System.err.println("error!!");
    }
  }
}

