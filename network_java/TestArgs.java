// 引数で渡された文字を表示

class TestArgs {
    public static void main(String[] args) {
        // コマンドライン引数を処理するための変数numberを0で初期化
        int number = 0;

        // コマンドライン引数の数だけループを実行
        while (number < args.length) {
            // コマンドライン引数の番号とその値を表示
            // args[number]はコマンドラインから渡された引数を1つずつ取得
            System.out.println("args[" + number + "]:" + args[number]);

            // 変数numberをインクリメントして次の引数へ
            number++;
        }
    }
}


