package io.loremipsum.grpc.demo.util;

import javax.annotation.Nullable;
import java.net.*;
import java.util.Enumeration;

public class NetworkUtils {
    public static String getLocalIp() throws SocketException, UnknownHostException {
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 12345);
        return socket.getLocalAddress().getHostAddress();
    }

    public static String getPeerIp(@Nullable final SocketAddress socketAddress){
        if (socketAddress == null) {
            return "unknown-ip";
        }

        if (!(socketAddress instanceof InetSocketAddress)) {
            return socketAddress.toString();
        }

        final InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        final String hostString = inetSocketAddress.getHostString();
        return hostString == null ? "unknown-ip" : hostString;
    }
}
