package com.pstickney.wol.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.InvalidParameterException;
import java.util.Enumeration;

public class WakeOnLanUtils
{
    public static String btos(byte[] bytes)
    {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for( byte b : bytes ) {
            sb.append(String.format("%02X ", b));
            if( ++i % 16 == 0 ) {
                i = 0;
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }

    public static byte stob(String str) throws NumberFormatException
    {
        return (byte) Integer.parseInt(str, 16);
    }

    public static byte[] parseMAC(String str) throws InvalidParameterException
    {
        byte[] bytes = new byte[6];
        String[] split = str.split("(\\:|\\-)");

        if( split.length != 6 )
            throw new InvalidParameterException("Cannot parse MAC address " + str);

        for (int i = 0; i < 6; i++)
            bytes[i] = stob(split[i]);

        return bytes;
    }

    public static InetAddress getLANAddress() throws Exception
    {
        Enumeration nics = NetworkInterface.getNetworkInterfaces();
        while( nics.hasMoreElements() )
        {
            NetworkInterface nic = (NetworkInterface) nics.nextElement();
            Enumeration addresses = nic.getInetAddresses();
            while( addresses.hasMoreElements() )
            {
                InetAddress address = (InetAddress) addresses.nextElement();
                if( !address.isLinkLocalAddress() && !address.isLoopbackAddress() && address.isSiteLocalAddress() )
                    return address;
            }
        }
        return null;
    }
}
