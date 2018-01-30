package com.pstickney.wol.services;

import com.pstickney.wol.utils.WakeOnLanUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.List;

public class WakeOnLanService 
{
    public WakeOnLanService()
    {
        
    }
    
    public void wake(String broadcastAddress, String port, List<String> macAddresses) throws Exception 
    {
        int intPort = Integer.parseInt(port);

        for (String macAddress : macAddresses) {
            byte[] parsed = WakeOnLanUtils.parseMAC(macAddress);
            byte[] data = createDatagram(parsed);

            sendPacket(broadcastAddress, intPort, parsed, data);
        }
    }

    private byte[] createDatagram(byte[] macAddress)
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

    private void sendPacket(String address, int port, byte[] mac, byte[] data)
    {
        try {
            InetSocketAddress inet = new InetSocketAddress(address, port);
            DatagramPacket packet = new DatagramPacket(data, data.length, inet);
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            socket.send(packet);
            socket.close();

            System.out.println("WakeOnLan packet sent to " + WakeOnLanUtils.btos(mac));
        } catch (Exception e) {
            System.out.println("Failed to send WakeOnLan packet: + e");
            System.exit(1);
        }
    }
}
