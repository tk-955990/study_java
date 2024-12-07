// 実行後、入力した文字を表示

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class ReadWrite {
    public static void main(String[] args) {
        // InputStreamReaderを使用してバイトストリームを文字ストリームに変換し、
        // BufferedReaderでバッファリングを行うことで効率的に入力を処理
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                // 標準入力から1行読み込む
                String input = reader.readLine();

                // 読み込んだ内容をそのまま標準出力に書き出す
                // Nullチェックを行うことで、入力ストリームが終了したときにループを終了することも可能
                if (input != null) {
                    System.out.println(input);
                } else {
                    // 入力がnull（EOFの場合など）ならば、ループを終了
                    break;
                }
            } catch (IOException e) {
                // 入力/出力エラーが発生した場合のエラーハンドリング
                // 0は「正常終了」、0以外は「異常終了」
                System.err.println("An error occurred: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}

