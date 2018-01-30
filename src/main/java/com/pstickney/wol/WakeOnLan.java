package com.pstickney.wol;

import org.apache.commons.cli.*;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

public class WakeOnLan
{
	private int port = 0;
	private String broadcastAddress = null;
	private Options options = null;
	private CommandLineParser parser = null;
	private CommandLine cmd = null;

	public static void main(String ...args)
	{
		new WakeOnLan(true, args);
	}

	private WakeOnLan()
	{

	}

	public WakeOnLan(String ...args)
	{
		this(false, args);
	}

	public WakeOnLan(boolean sendNow, String ...args)
	{
		init(args);
		if( sendNow )
			send();
	}

	public void init(String ...args)
	{
		options = new Options();
		options.addOption("h", "help", false, "Display help information");
		options.addOption("v", "version", false, "Display version information");
		options.addOption("ip", "broadcast", true, "Broadcast IP address");
		options.addOption("p", "port", true, "Port");

		parser = new DefaultParser();
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void send()
	{
		if( cmd.hasOption("h") ) {
			help();
			System.exit(0);
		}

		if( cmd.hasOption("v") ) {
			printVersion();
			System.exit(0);
		}

		if( cmd.getArgList().size() < 1 )
		{
			help();
			System.exit(1);
		}

		broadcastAddress = cmd.getOptionValue("ip", "255.255.255.255");
		port = Integer.parseInt(cmd.getOptionValue("p", "9"));

		for( String macAddress : cmd.getArgList() )
		{
			byte[] parsedBytes = parseMAC(macAddress);
			byte[] data = createDatagram(parsedBytes);

			sendPacket(broadcastAddress, port, parsedBytes, data);
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

			System.out.println("WakeOnLan packet sent to " + btos(mac));
		}
		catch (Exception e) {
			System.out.println("Failed to send WakeOnLan packet: + e");
			System.exit(1);
		}
	}

	private void printVersion()
	{
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(new FileReader("pom.xml"));
			System.out.println(model.getVersion());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	private void help()
	{
		HelpFormatter formatter = new HelpFormatter();
		String header =
				"\nWake-on-LAN (WoL) is an ethernet standard that allows a computer to be turned on by a network message. " +
				"WoL is implemented using a specially designed frame called a magic packet, which is sent to all computer " +
				"in a network, among them the computer to be awakened.\n" +
				"The magic packet is a broadcast frame containing anywhere within its payload, 6 bytes of 0xFF followed by " +
				"sixteen repetitions of the target computer's 48-bit MAC address, for a total of 102 bytes.\n\n" +
				"Options:";
		formatter.printHelp(80,"WakeOnLan [[MAC ...]", header, options, "",true);
	}

	private String btos(byte[] bytes)
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

	private byte stob(String str)
	{
		return (byte) Integer.parseInt(str, 16);
	}

	private byte[] parseMAC(String str)
	{
		byte[] bytes = new byte[6];
		String[] split = str.split("(\\:|\\-)");

		if( split.length != 6 ) {
			help();
			System.exit(1);
		}

		for (int i = 0; i < 6; i++)
			bytes[i] = stob(split[i]);

		return bytes;
	}
}