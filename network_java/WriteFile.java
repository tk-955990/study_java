// 実行後、入力した文字をファイルに書き込み 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

class WriteFile {
  public static void main(String[] args) {

    // try-with-resourcesを使用して、リソースを自動的にクローズ
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
         // ファイルに書き込むためのFileWriterを作成。指定したパスにファイルを作成
         FileWriter file = new FileWriter("/home/tk955990/study/network_java/test.txt");
         // BufferedWriterでFileWriterをラップし、PrintWriterでBufferedWriterをラップ
         // PrintWriterでファイルに書き込むための効率的な方法を提供
         PrintWriter writer = new PrintWriter(new BufferedWriter(file))) {

      // 無限ループでユーザーからの入力を処理
      while (true) {
        try {
          // 標準入力から1行読み込む
          String input = reader.readLine();

          // 入力が","の場合、ループを終了する
          if (input.equals(",")) {
            break;
          } else {
            // 入力内容を標準出力に表示
            System.out.println(input);

            // 入力内容をファイルに書き込む
            writer.println(input);
          }
        } catch (FileNotFoundException e) {
          // ファイルが見つからないエラーが発生した場合の処理
          System.err.println("error!");
        }
      }
    } catch (IOException e) {
      // 入出力エラーが発生した場合の処理
      System.err.println("Error!");
    }
  }
}

