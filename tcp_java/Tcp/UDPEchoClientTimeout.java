import java.io.*;
import java.net.*;

public class UDPEchoClientTimeout {

    private static final int TIMEOUT = 3000; // 再送のタイムアウト(ミリ秒)
    private static final int MAXTRIES = 5;   // 再送回数の上限

    public static void main(String[] args) throws IOException {
        if ((args.length < 2) || (args.length > 3)) // 引数の数が正しいかどうかの確認
            throw new IllegalArgumentException("Paramaetaer(s): <Server> <Word> [<Port>]");

        // 第一引数 : サーバー名またはIPアドレス
        InetAddress serverAddress = InetAddress.getByName(args[0]);

        // 第二引数 : デフォルトの文字エンコード方式を使って入力文字列をバイトに変換する
        byte[] bytesToSend = args[1].getBytes();

        // 第三引数 : 使用するポート番号 ※未入力の場合は７番ポート
        int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7;

        DatagramSocket socket = new DatagramSocket();

        socket.setSoTimeout(TIMEOUT); // 受信待機時間の最大値(ミリ秒)
        // 送信パケット生成
        DatagramPacket sendPacket = new DatagramPacket(bytesToSend, bytesToSend.length, serverAddress, servPort);
        // 受信パケット生成
        DatagramPacket receivePacket = new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

        int tries = 0;                    // パケット再送回数(パケットが紛失した場合は再送が必要)
        boolean receivedResponse = false; // 応答が受信されたかどうかを示すフラグ
        do {
            socket.send(sendPacket); // エコー文字列を送信する
            try {
                socket.receive(receivePacket); // エコー応答の受信を試みる

                if (!receivePacket.getAddress().equals(serverAddress)) // 送信元をチェック
                    throw new IOException("Received packet from an unknown source");

                receivedResponse = true; // 正しい応答が受信された場合、フラグを更新
            } catch (IOException e) {    // 何も受け取っていない
                tries += 1;              // 再送回数をインクリメント
                System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
            }
        } while ((!receivedResponse) && (tries < MAXTRIES));

        if (receivedResponse)
            System.out.println("Received: " + new String(receivePacket.getData(), 0, receivePacket.getLength()));
        else
            System.out.println("No response -- giving up.");

        socket.close(); // ソケットを閉じてリソースを解放する
    }
}