package io.loremipsum.grpc.demo.util;

import java.net.*;
import java.util.Enumeration;

public class AddressUtil {
    public static String getLocalIp() throws SocketException, UnknownHostException {
        final DatagramSocket socket = new DatagramSocket();
        socket.connect(InetAddress.getByName("8.8.8.8"), 12345);
        return socket.getLocalAddress().getHostAddress();
    }
}
