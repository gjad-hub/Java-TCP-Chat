package pt.ua.estga.project4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String username;

    public String getUsername() {
        return username;
    }

    public Client(Socket Socket, String username) {
        try {
            this.socket = Socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

            writer.write(username);
            writer.newLine();
            writer.flush();

            String check = reader.readLine();

            if (check.equals("new@user")) {
                writer.write(JOptionPane.showInputDialog("Primeiro Nome:"));
                writer.newLine();
                writer.flush();

                writer.write(JOptionPane.showInputDialog("Ultimo Nome:"));
                writer.newLine();
                writer.flush();
            }

        } catch (IOException e) {
            closeAll();
        }
    }

    public void send(String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            closeAll();
        }
    }

    public void listen(DefaultListModel modelMessages, DefaultListModel modelUsers) {
        new Thread(() -> {
            String recivedMessage;

            ArrayList<String> unique = new ArrayList<>();
            while (socket.isConnected()) {
                try {
                    recivedMessage = (String) reader.readLine();

                    if (recivedMessage.split("/")[0].equals("user")) {

                        String data = recivedMessage.split("/")[1];
                        if (!unique.contains(data)) {
                            modelUsers.addElement(data);
                            unique.add(data);
                        }

                    } else if (recivedMessage.split("/")[0].equals("userR")) {
                        String data = recivedMessage.split("/")[1];

                        if (unique.contains(data)) {
                            unique.remove(data);
                            modelUsers.removeElement(data);
                        }
                    } else if (!recivedMessage.equals("##$$")) {

                        String tmp = recivedMessage.split(":")[0];

                        if (!tmp.equals("To")) {
                            JsonLoader.LoadFile();
                            if (tmp.equals("lp")) {
                                JsonLoader.addMessage(tmp, tmp, recivedMessage);
                            } else {
                                JsonLoader.addMessage(tmp, this.username, recivedMessage);
                            }
                        }

                        modelMessages.addElement(recivedMessage);
                    }

                } catch (IOException e) {
                    closeAll();
                }
            }
        }).start();
    }

    private void closeAll() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws IOException {

    }
}
