package pt.ua.estga.project4.ServerComponents;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SClientHandler implements Runnable {

    //Lists
    /**
     *
     */
    public static ArrayList<SClientHandler> ClientHandlerList = new ArrayList<>();

    //Stream Elements
    /**
     *
     */
    private Socket socket;

    /**
     *
     */
    private BufferedReader streamReader;

    /**
     *
     */
    private BufferedWriter streamWriter;

    //user properties
    /**
     *
     */
    private String email;

    /**
     *
     */
    private String fnome;

    /**
     *
     */
    private String lnome;

    /**
     *
     */
    private int ID;

    /**
     *
     * @param Socket
     * @throws IOException
     */
    public SClientHandler(Socket Socket) throws IOException {

        try {
            this.socket = Socket;
            this.streamWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.streamReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.email = streamReader.readLine();

            checkAndStoreIncomingClient();
        } catch (IOException e) {
            closeAll();
        }

        for (SClientHandler client : ClientHandlerList) {
            broadCastMessage("user/" + client.email);
        }
    }

    public void checkAndStoreIncomingClient() throws IOException {

        if (!SClientManager.existeCliente(email)) {

            sendDataToSender("new@user");

            do {
                fnome = streamReader.readLine();
                lnome = streamReader.readLine();
            } while (fnome == null || fnome.isEmpty() && lnome == null || lnome.isEmpty());

            SClientManager.AdicionarCliente(email, fnome, lnome);
            SClientManager.saveData();
        } else {
            sendDataToSender("---");
        }

        ClientHandlerList.add(this);

    }

    /**
     *
     */
    @Override
    public void run() {

        try {

            while (socket.isConnected()) {

                String incomingMessage;
                if ((incomingMessage = streamReader.readLine()) == null) {
                    throw new IOException();
                }

                String[] messageStructure = incomingMessage.split("/");
                String userEmail = incomingMessage.split(":")[1].split("/")[0];
                String message = incomingMessage.split("/")[1];

                if (messageStructure.length - 1 != 0) {

                    if (messageStructure[0].equals("message:lp")) {
                        broadCastMessage("lp: " + message);
                    }

                    if (incomingMessage.split(":")[0].equals("message")) {
                        forwardIncomingMessage(userEmail, message);
                    }

                }
            }
        } catch (IOException e) {
            closeAll();
        }
    }

    private void forwardIncomingMessage(String userEmail, String message) {
        for (SClientHandler client : ClientHandlerList) {
            if (client.email.equals(userEmail)) {
                //Successful Message
                sendDataToSender("To: " + client.email + ": " + message);
                //Forwarded message
                sendDataToDest(client, email + ": " + message);
            }
        }
    }

    /**
     *
     * @param usersEmailList
     * @param message
     */
    private void forwardIncomingMessageToGroup(String[] usersEmailList, String message) {
        for (String user : usersEmailList) {
            for (SClientHandler client : ClientHandlerList) {
                if (client.email.equals(user)) {
                    //Successful Message
                    sendDataToSender("To: " + client.email + ": " + message.split("/")[1]);
                    //Forwarded message
                    sendDataToDest(client, email + ": " + message.split("/")[1]);
                }
            }
        }
    }

    /**
     *
     * @param m
     */
    private void broadCastMessage(String m) {
        for (SClientHandler client : ClientHandlerList) {
            try {
                client.streamWriter.write(m);
                client.streamWriter.newLine();
                client.streamWriter.flush();
            } catch (IOException e) {
                closeAll();
            }
        }
    }

    /**
     *
     * @param c
     * @param m
     */
    private void sendDataToSender(String m) {
        try {
            this.streamWriter.write(m);
            this.streamWriter.newLine();
            this.streamWriter.flush();
        } catch (IOException e) {
            closeAll();
        }
    }

    private void sendDataToDest(SClientHandler Client, String m) {
        try {
            Client.streamWriter.write(m);
            Client.streamWriter.newLine();
            Client.streamWriter.flush();
        } catch (IOException e) {
            closeAll();
        }
    }

    /**
     *
     */
    private void remove() {
        ClientHandlerList.remove(this);
        broadCastMessage("userR/" + email);
    }

    /**
     *
     */
    private void closeAll() {
        remove();
        try {
            if (this.streamReader != null) {
                streamReader.close();
            }
            if (this.streamWriter != null) {
                streamWriter.close();
            }
            if (this.socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
