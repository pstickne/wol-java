package com.pstickney.wol.services;

import com.pstickney.wol.utils.WakeOnLanUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

public class WakeOnLanService
{
    public static void wake(String macAddress) throws IOException
    {
        wake(Arrays.asList(macAddress));
    }

    public static void wake(List<String> macAddresses) throws IOException
    {
        wake("255.255.255.255", "9", macAddresses);
    }

    public static void wake(String broadcastAddress, String port, List<String> macAddresses) throws IOException
    {
        int intPort = Integer.parseInt(port);

        for (String macAddress : macAddresses) {
            byte[] parsed = WakeOnLanUtils.parseMAC(macAddress);
            byte[] data = createDatagram(parsed);

            sendPacket(broadcastAddress, intPort, parsed, data);
        }
    }

    private static byte[] createDatagram(byte[] macAddress)
    {
        int i;
        byte[] bytes = new byte[6 + 16 * macAddress.length];
        for (i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
        }
        for (i = 6; i < bytes.length; i += macAddress.length) {
            System.arraycopy(macAddress, 0, bytes, i, macAddress.length);
        }

        return bytes;
    }

    private static void sendPacket(String address, int port, byte[] mac, byte[] data) throws IOException
    {
        InetSocketAddress inet = new InetSocketAddress(address, port);
        DatagramPacket packet = new DatagramPacket(data, data.length, inet);
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
        socket.send(packet);
        socket.close();
    }
}
