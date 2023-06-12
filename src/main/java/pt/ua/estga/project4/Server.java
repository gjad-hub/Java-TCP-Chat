package pt.ua.estga.project4;

import java.net.*;
import java.io.*;

public class Server {

    private final ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() throws IOException {
        
        ClientManager cliM = new ClientManager();
        
        try {
            while (!serverSocket.isClosed()) {

                Socket cliSock = serverSocket.accept();
                System.out.println("Novo cliente !!");
                ClientHandler clientHandler = new ClientHandler(cliSock);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }

            ClientManager.saveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6000);
        Server server = new Server(serverSocket);
        server.startServer();
    }

}
