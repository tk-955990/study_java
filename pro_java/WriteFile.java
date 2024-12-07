import java.io.IOException; // IOExceptionクラスは、ファイル操作で発生する例外を処理するために使用
import java.nio.file.Files; // Filesクラスは、ファイルやディレクトリの操作に関連するメソッドを提供
import java.nio.file.Path; // Pathクラスは、ファイルやディレクトリのパス（場所）を表現するために使用

// WriteFileクラスの定義
class WriteFile {
  // mainメソッド: Javaプログラムのエントリーポイント
  public static void main(String[] args) throws IOException {
    // 変数'message'に複数行の文字列を代入（Java 13以降で利用可能なテキストブロックを使用）
    var message = """
            test
            message
            """;

    // ファイルのパスを'Path'クラスを使って定義（ここでは"test.txt"をカレントディレクトリに作成）
    var p = Path.of("test.txt");

    // Files.writeStringメソッドを使って、'message'の内容を指定したパス'p'に書き込む
    // 指定したファイルが存在しない場合は自動的に作成される
    Files.writeString(p, message);
  }
}
