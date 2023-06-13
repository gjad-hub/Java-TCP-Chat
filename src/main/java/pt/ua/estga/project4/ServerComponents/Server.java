package pt.ua.estga.project4.ServerComponents;

import pt.ua.estga.project4.ServerComponents.*;
import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author gonc, ricar
 */
public class Server {

    /**
     *
     */
    private final ServerSocket serverSocket;

    /**
     *
     * @param serverSocket
     */
    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     *
     */
    public void startServer() {

        SClientManager cliM = new SClientManager();

        try {
            while (!serverSocket.isClosed()) {

                Socket cliSock = serverSocket.accept();
                System.out.println("New Client Joined! ");

                new Thread(new SClientHandler(cliSock)).start();
            }
        } catch (IOException e) {
            closeServerSocket();
        }
    }

    /**
     *
     */
    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

            SClientManager.saveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
