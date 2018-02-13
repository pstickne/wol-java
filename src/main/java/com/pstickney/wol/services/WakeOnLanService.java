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
        wake(broadcastAddress, port, macAddresses, 0);
    }

    public static void wake(String broadcastAddress, String port, List<String> macAddresses, Integer verbosity) throws IOException
    {
        int intPort = Integer.parseInt(port);

        for (String macAddress : macAddresses) 
        {
            if( verbosity > 0 )
                System.out.println("Attempting to wake " + macAddress);
            if( verbosity > 1 )
                System.out.println(broadcastAddress + ":" + port);
            
            byte[] parsed = WakeOnLanUtils.parseMAC(macAddress);
            byte[] data = createDatagram(parsed, verbosity);
            
            sendPacket(broadcastAddress, intPort, parsed, data, verbosity);
        }
    }

    private static byte[] createDatagram(byte[] macAddress)
    {
        return createDatagram(macAddress, 0);
    }
    
    private static byte[] createDatagram(byte[] macAddress, Integer verbosity)
    {
        int i;
        byte[] bytes = new byte[6 + 16 * macAddress.length];
        
        if( verbosity > 0 )
            System.out.println("Creating datagram with length " + bytes.length);
        
        for (i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
        }
        for (i = 6; i < bytes.length; i += macAddress.length) {
            System.arraycopy(macAddress, 0, bytes, i, macAddress.length);
        }
        
        if( verbosity > 1 )
            System.out.println(WakeOnLanUtils.btos(bytes));

        return bytes;
    }

    private static void sendPacket(String address, int port, byte[] mac, byte[] data) throws IOException 
    {
        sendPacket(address, port, mac, data, 0);
    }
    
    private static void sendPacket(String address, int port, byte[] mac, byte[] data, Integer verbosity) throws IOException
    {
        InetSocketAddress inet = new InetSocketAddress(address, port);
        DatagramPacket packet = new DatagramPacket(data, data.length, inet);
        DatagramSocket socket = new DatagramSocket();
        
        if( verbosity > 0 )
            System.out.println("Broadcasting datagram packet");
        
        socket.setBroadcast(true);
        socket.send(packet);
        
        if( verbosity > 2 )
            System.out.println("Closing datagram socket");
        
        socket.close();
    }
}
