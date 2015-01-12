package com.ebridge.smpp.server.simulator.shell;

import com.ebridge.smpp.server.simulator.SmppServerSimulator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activator implements BundleActivator {

    private volatile Binding simulatorCommandListener;
    private volatile History history;

    private SmppServerSimulator smppServerSimulator;

    public void start(BundleContext context) throws Exception {

        smppServerSimulator = new SmppServerSimulator();

        int port = getPort(context);
        int max = getMaxConnections(context);
        simulatorCommandListener = getTelnetBinding(context, port, max);
        simulatorCommandListener.start();

        System.out.println(
                "eBridge SMPP Server Simulator - listening on port " + port);
    }

    public void stop(BundleContext context) throws Exception {
        simulatorCommandListener.stop();
        writeHistory( history, context );
    }

    private Binding getTelnetBinding(BundleContext context, int port, int max) throws IOException {
        return new SimulatorCommandListener(getExecuteCommand(context), new ServerSocket(port), max);
    }

    private int getMaxConnections(BundleContext context) {
        String maxConnectionsProperty = context.getProperty("com.ebridge.smpp.server.shell.connection.max");
        int maxConnections = 4;
        if (maxConnectionsProperty != null) {
            maxConnections = Integer.parseInt(maxConnectionsProperty);
        }
        return maxConnections;
    }

    private int getPort(BundleContext context) {
        String portProperty = context.getProperty("com.ebridge.smpp.server.shell.port");
        int port = 6061;
        if (portProperty != null) {
            port = Integer.parseInt(portProperty);
        }
        return port;
    }

    private void writeHistory(History history, BundleContext context) throws IOException {
        List<String> list = history.get();
        File log = context.getDataFile("smpp.log.txt");

        if (log.exists() && !log.delete()) {
            throw new IOException("Unable to delete previous log file!");
        }
        write(list, log);
    }

    private void write(List<String> list, File log) throws IOException {
        PrintWriter output = null;
        IOException original = null;
        try {
            output = new PrintWriter(new FileWriter(log));

            for (String entry : list) {
                output.println(entry);
            }
        } catch (IOException ex) {
            original = ex;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } finally {
                if (original != null) {
                    throw original;
                }
            }
        }
    }

    private Command getExecuteCommand(BundleContext context) throws IOException {

        Map<String, Command> commands = new HashMap<String, Command>();

        commands.put("help",
                new HelpCommand(commands).setHelp("help - display commands."));

        commands.put("start",
                new StartCommand( smppServerSimulator )
                        .setHelp("start <port> - Start the SMPP Server on the given port."));

        commands.put("stop",
                new StopCommand()
                        .setHelp("stop <port> - Stop SMPP Server on the given port."));

        commands.put("send",
                new SendMessageCommand( smppServerSimulator )
                        .setHelp("send <source> <recipient> <message>"));

        commands.put("messages",
                new ListMessagesCommand()
                        .setHelp("messages - List messages received."));

        commands.put("clients",
                new ListClientsCommand(smppServerSimulator)
                        .setHelp("clients - List connected clients."));

        commands.put("users",
                new ReloadUsersCommand()
                        .setHelp("users - Reload system users from config file"));

        HistoryDecorator command = new HistoryDecorator(new ExecuteCommand(commands), readHistory(context));
        context.addFrameworkListener(command);
        context.addBundleListener(command);
        this.history = command;

        commands.put("history",
                new HistoryCommand(command)
                        .setHelp("history {<n>} - Show the last commands (up to <n> if present)."));

        return command;
    }

    private List<String> readHistory(BundleContext context) throws IOException {

        File log = context.getDataFile("smpp.log.txt");
        List<String> result = new ArrayList<String>();

        if (log.isFile()) {
            read(log, result);
        }

        return result;
    }

    private void read(File log, List<String> result) throws IOException {

        BufferedReader input = null;
        IOException original = null;
        try {
            input = new BufferedReader(new FileReader(log));
            for (String line = input.readLine(); line != null; line = input.readLine()) {
                result.add(line);
            }
        } catch (IOException ex) {
            original = ex;
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } finally {
                if (original != null) {
                    throw original;
                }
            }
        }
    }
}
