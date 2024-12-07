// ファイル入出力時に発生する例外を処理するクラス
import java.io.IOException; // 入出力例外を処理するためのクラス
import java.nio.file.Files; // ファイルやディレクトリ操作に関連するメソッドを提供するクラス
import java.nio.file.NoSuchFileException; // ファイルが見つからない場合にスローされる例外クラス
import java.nio.file.Path; // ファイルやディレクトリのパスを表現するためのクラス

// ReadFileクラスの定義
class ReadFile {
  // mainメソッド: Javaプログラムのエントリーポイント
  public static void main(String[] args) {
    // "test.txt" ファイルのパスを表すPathオブジェクトを作成
    // Path.of() はJava 11以降で導入された静的メソッドで、指定したファイルのパスを生成する
    var p = Path.of("test.txt");

    // Files.readString() メソッドを使って、指定されたパスのファイル内容を文字列として読み込む
    // ファイルの全内容が一度に読み込まれる
    try {
      String s = Files.readString(p);

      // 読み込んだファイルの内容を標準出力に表示する（コンソールに出力）
      System.out.println(s);
    } catch (NoSuchFileException e) {
      // ファイルが見つからない場合のエラーメッセージを表示
      System.out.println("ファイルが見つかりません: " + e.getFile());
    } catch (IOException e) {
      // その他の入出力エラーが発生した場合のエラーメッセージを表示
      System.out.println("ファイルの読み込み中にエラーが発生しました: " + e.getMessage());
    }
  }
}

