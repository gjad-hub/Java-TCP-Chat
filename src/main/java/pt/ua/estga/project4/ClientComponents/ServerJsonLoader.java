package pt.ua.estga.project4.ClientComponents;

import com.nimbusds.jose.shaded.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 *
 * @author gonc,ricar
 */
public class ServerJsonLoader {

    /**
     *
     */
    static private ArrayList<Message> lista;

    /**
     *
     */
    static String path;

    /**
     *
     * @throws IOException
     */
    public ServerJsonLoader() throws IOException {
        LoadFile();
    }

    /**
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void LoadFile() throws FileNotFoundException, IOException {
        path = new File(".").getCanonicalPath() + "/messages.json";

        if (new File(path).exists()) {

            Type messageListType = new ArrayList<Message>() {
            }.getClass().getGenericSuperclass();

            BufferedReader br = new BufferedReader(new FileReader(path));

            lista = new Gson().fromJson(br, messageListType);
        } else {
            System.out.println(path);
        }
    }

    /**
     *
     * @param from
     * @param to
     * @param message
     */
    public static void addMessage(String from, String to, String message) {
        Message newMessage = new Message(from, to, message);
        lista.add(newMessage);

        try (Writer writer = new FileWriter(path)) {
            Gson gson = new Gson();
            gson.toJson(lista, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param From
     * @param to
     * @return
     */
    public static ArrayList<String> getMessagesChat(String From, String to) {
        ArrayList<String> str = new ArrayList<>();

        for (Message message : ServerJsonLoader.lista) {
            if (message.getFrom().equals(From) && message.getTo().equals(to)) {
                str.add(message.getMessage());
            }
        }
        return str;
    }

    /**
     *
     */
    static class Message {

        /**
         *
         */
        private String From;

        /**
         *
         */
        private String To;

        /**
         *
         */
        private String Message;

        /**
         *
         * @param From
         * @param To
         * @param Message
         */
        public Message(String From, String To, String Message) {
            this.From = From;
            this.To = To;
            this.Message = Message;
        }

        /**
         *
         * @return
         */
        public String getFrom() {
            return From;
        }

        /**
         *
         * @return
         */
        public String getTo() {
            return To;
        }

        /**
         *
         * @return
         */
        public String getMessage() {
            return Message;
        }

        /**
         *
         * @return
         */
        @Override
        public String toString() {
            return "Message{" + "From=" + From + ", To=" + To + ", Message=" + Message + '}';
        }
    }
}
