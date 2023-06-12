package pt.ua.estga.project4;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ClientManager {

    static private ArrayList<ClientData> lista;
    static String path;

    public ClientManager() throws IOException {
        path = new File(".").getCanonicalPath() + "clients.dat";
        lista = new ArrayList<>();

        if (new File(path).exists()) {
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(path));
            try {
                lista = (ArrayList<ClientData>) obj.readObject();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void AdicionarCliente(String email, String nome, String sobrenome) {
        lista.add(new ClientData(email, nome, sobrenome));
    }

    public static ClientData obterCliente(String email) {
        for (ClientData cliente : lista) {
            if (cliente.getEmail().equals(email)) {
                return cliente;
            }
        }
        return null;
    }

    public static int obterClienteID(String nome) {
        for (ClientData cliente : lista) {
            if (cliente.getNome().equals(nome)) {
                return cliente.getID();
            }
        }
        return -1;
    }

    public static boolean existeCliente(String email) {
        for (ClientData cliente : lista) {
            if (cliente.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveData() {
        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(ClientManager.path));
            obj.writeObject(lista);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    static class ClientData implements Serializable{

        protected int ID;
        protected String email;
        protected String nome;
        protected String sobrenome;

        public ClientData(String email, String nome, String sobrenome) {
            this.email = email;
            this.nome = nome;
            this.sobrenome = sobrenome;
            this.ID = lista.size() + 1;
        }

        public String getEmail() {
            return email;
        }

        public int getID() {
            return ID;
        }

        public String getNome() {
            return nome;
        }

        public String getSobrenome() {
            return sobrenome;
        }

    }

}
