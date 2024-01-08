package com.caversia.plugins.votes.bungee.business;

import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyPair;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.BadPaddingException;

import com.caversia.plugins.votes.bungee.Main;
import com.caversia.plugins.votes.bungee.listeners.VotesHandler;
import com.caversia.plugins.votes.bungee.model.Vote;
import com.caversia.plugins.votes.bungee.utils.Logger;
import com.caversia.plugins.votes.bungee.utils.RSA;

/**
 * Implements the Votifier protocol and listen for votes announcements from registered websites.
 * 
 * @author amartins
 */
public class VotesReceiver {

    private ServerSocket server;

    private AtomicBoolean running;

    private KeyPair keyPair;

    /**
     * Starts the receiver.
     * 
     * @param host the interface host to bind
     * @param port the port to bind
     * @throws Exception when initializing the RSA keys or starting the server fails
     */
    public void start(String host, int port) throws Exception {
        Logger.info("Starting server on {}:{}", host, port);
        try {
            initializeRSA();

            server = new ServerSocket();
            server.bind(new InetSocketAddress(host, port));

            running = new AtomicBoolean(true);

            Main.self.getProxy().getScheduler().runAsync(Main.self, () -> run());
        } catch (Exception e) {
            Logger.error("Error initializing vote receiver: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Shuts down the receiver.
     */
    public void shutdown() {
        running.set(false);

        if (server == null) {
            return;
        }

        try {
            server.close();
            Logger.info("Server stopped...");
        } catch (Exception ex) {
            Logger.warn("Unable to shut down vote receiver cleanly.");
        }
    }

    /**
     * Runs the receive loop.
     */
    private void run() {
        while (running.get()) {
            try {
                Socket socket = server.accept();
                Main.self.runAsyncTask(() -> acceptVote(socket));
            } catch (SocketException se) {
                Logger.warn("Protocol error. Ignoring packet - {}", se.getMessage());
            } catch (Exception e) {
                Logger.warn("Exception caught while receiving a vote notification: {}", e.getMessage());
            }
        }
    }

    /**
     * Deals with accepting a vote connection.
     * 
     * @param socket the socket where the vote is being received
     */
    private void acceptVote(Socket socket) {
        try {
            socket.setSoTimeout(3000);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            InputStream in = socket.getInputStream();
            writer.write("VOTIFIER 1.9");
            writer.newLine();
            writer.flush();

            byte[] block = new byte[256];
            in.read(block, 0, block.length);
            block = RSA.decrypt(block, keyPair.getPrivate());
            int position = 0;
            String opcode = this.readString(block, position);
            position += opcode.length() + 1;
            if (!opcode.equals("VOTE")) {
                throw new Exception("Unable to decode RSA");
            }
            String serviceName = this.readString(block, position);
            position += serviceName.length() + 1;
            String username = this.readString(block, position);
            position += username.length() + 1;
            String address = this.readString(block, position);
            position += address.length() + 1;
            String timeStamp = this.readString(block, position);
            position += timeStamp.length() + 1;

            // Parse and broadcast the vote
            Vote vote = new Vote(serviceName, username, address, timeStamp);
            Main.self.runAsyncTask(() -> VotesHandler.handle(vote));

            writer.close();
            in.close();
            socket.close();
        } catch (BadPaddingException bpe) {
            Logger.warn("Unable to decrypt vote record. Make sure that that your public keymatches the one"
                    + " you gave to the server list: {}", bpe.getMessage());
        } catch (Exception e) {
            Logger.warn("Exception caught while receiving a vote notification: {}", e.getMessage());
        }
    }

    /**
     * Initializes the RSA system.
     * 
     * @throws Exception when initializing the crypto system fails
     */
    private void initializeRSA() throws Exception {
        try {
            File dataFolder = Main.self.getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File rsaFolder = new File(dataFolder, "rsa");
            if (!rsaFolder.exists()) {
                keyPair = RSA.generate(2048);
                rsaFolder.mkdir();
                RSA.save(rsaFolder, keyPair);
            } else {
                this.keyPair = RSA.load(rsaFolder);
                Logger.info("Loaded RSA files.");
            }
        } catch (Exception e) {
            Logger.error("Error initializing the RSA keypair: ", e.getMessage());
            throw e;
        }
    }

    /**
     * Reads a {@link String} from a byte array, starting at a given offset.
     * 
     * @param data the byte array
     * @param offset the offset where to start reading
     * @return the read {@link String}.
     */
    private String readString(byte[] data, int offset) {
        StringBuilder builder = new StringBuilder();
        for (int i = offset; i < data.length && data[i] != 10; ++i) {
            builder.append((char) data[i]);
        }
        return builder.toString();
    }
}