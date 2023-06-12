package pt.ua.estga.project4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    //Lists
    public static ArrayList<ClientHandler> ClientHandlers = new ArrayList<>();

    //Stream Elements
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    //user properties
    private String email;
    private String fnome;
    private String lnome;
    private int ID;

    public ClientHandler(Socket Socket) throws IOException {

        try {
            this.socket = Socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.email = reader.readLine();
        } catch (IOException e) {
            closeAll();
        }

        if (!ClientManager.existeCliente(email)) {

            writer.write("new@user");
            writer.newLine();
            writer.flush();

            do {
                fnome = reader.readLine();
                lnome = reader.readLine();
            } while (fnome == null || fnome.isEmpty() && lnome == null || lnome.isEmpty());

            ClientManager.AdicionarCliente(email, fnome, lnome);
            ClientManager.saveData();
        } else {
            writer.write("---");
            writer.newLine();
            writer.flush();
        }

        ClientHandlers.add(this);

        for (ClientHandler client : ClientHandlers) {
            broadCastMessage("user/" + client.email);
        }
    }

    @Override
    public void run() {
        String message;
        try {

            while (socket.isConnected()) {
                message = reader.readLine();

                if (message == null) {
                    throw new IOException();
                }

                if (message.split("/").length - 1 != 0) {

                    if (message.split(":")[0].equals("message")) {

                        String user = message.split(":")[1].split("/")[0];
                        for (ClientHandler c : ClientHandlers) {
                            if (c.email.equals(user)) {
                                sendMessage(this, "To: " + c.email + ": " + message.split("/")[1]);
                                sendMessage(c, email + ": " + message.split("/")[1]);
                            }
                        }
                    }

                    if (message.split("/")[0].equals("message:lp")) {
                        broadCastMessage("lp: " + message.split("/")[1]);
                    }
                }
            }
        } catch (IOException e) {
            closeAll();
        }
    }

    private void broadCastMessage(String m) {
        for (ClientHandler client : ClientHandlers) {
            try {
                client.writer.write(m);
                client.writer.newLine();
                client.writer.flush();
            } catch (IOException e) {
                closeAll();
            }
        }
    }

    private void sendMessage(ClientHandler c, String m) {
        try {
            c.writer.write(m);
            c.writer.newLine();
            c.writer.flush();
        } catch (IOException e) {
            closeAll();
        }
    }

    private void remove() {
        ClientHandlers.remove(this);
        broadCastMessage("userR/" + email);
    }

    private void closeAll() {
        remove();
        try {
            if (this.reader != null) {
                reader.close();
            }
            if (this.writer != null) {
                writer.close();
            }
            if (this.socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
