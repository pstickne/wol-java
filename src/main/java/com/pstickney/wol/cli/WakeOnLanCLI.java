package com.pstickney.wol.cli;

import com.pstickney.wol.services.WakeOnLanService;
import org.apache.commons.cli.*;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

public class WakeOnLanCLI
{
    private static Options options = null;
    private static CommandLine cmd = null;
    private static CommandLineParser parser = null;

    public static void main(String ...args)
    {
        options = new Options();
        options.addOption("h", "help", false, "Display help information");
        options.addOption("v", "version", false, "Display version information");
        options.addOption("ip", "broadcast", true, "Broadcast IP address");
        options.addOption("p", "port", true, "Port");

        try {
            parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            e.printStackTrace();
        }

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

        String broadcastAddress = cmd.getOptionValue("ip", "255.255.255.255");
        String port = cmd.getOptionValue("p", "9");

        try {
            WakeOnLanService.wake(broadcastAddress, port, cmd.getArgList());
        } catch (InvalidParameterException e) {
            e.printStackTrace(System.err);
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }

    private WakeOnLanCLI()
    {

    }

    private static void printVersion()
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

    private static void help()
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
}