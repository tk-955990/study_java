import java.io.*;
import java.net.*;

public class UDPEchoServer {

    private static final int ECHOMAX = 255;

    public static void main(String[] args) throws IOException {

        if (args.length != 1)
            throw new IllegalArgumentException("Parameter(s): <Port>");

        int servPort = Integer.parseInt(args[0]);

        DatagramSocket socket = new DatagramSocket(servPort);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);

        for (;;) {                  // 繰り返し実行され、データグラムを受信してエコーする
            socket.receive(packet); // クライアントからパケットを受信
            System.out.println(
                "Hndling client at " + packet.getAddress().getHostAddress() + " on port " + packet.getPort());
            socket.send(packet);       // 同じパケットをクライアントに返す
            packet.setLength(ECHOMAX); // バッファが縮小しないように長さをリセットする
        }
        /* この部分には到達しない */
    }
}
