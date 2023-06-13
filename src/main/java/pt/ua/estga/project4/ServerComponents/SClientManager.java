package pt.ua.estga.project4.ServerComponents;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SClientManager {

    /**
     *
     */
    static private ArrayList<SClientData> list;

    /**
     * Clients Data
     */
    static String path;

    /**
     * Creates the clients.dat file
     */
    public SClientManager() {
        try {
            path = new File(".").getCanonicalPath() + "/clients.dat";
            list = new ArrayList<>();

            loadData();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SClientManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     *
     * @param email
     * @param nome
     * @param sobrenome
     */
    public static void AdicionarCliente(String email, String nome, String sobrenome) {
        int newId = !list.isEmpty() ? list.get(list.size() - 1).getID() + 1 : 1;
        list.add(new SClientData(newId, email, nome, sobrenome));
    }

    /**
     *
     * @param email
     * @return
     */
    public static SClientData obterCliente(String email) {
        for (SClientData cliente : list) {
            if (cliente.getEmail().equals(email)) {
                return cliente;
            }
        }
        return null;
    }

    /**
     *
     * @param nome
     * @return
     */
    public static int obterClienteID(String nome) {
        for (SClientData cliente : list) {
            if (cliente.getFirstName().equals(nome)) {
                return cliente.getID();
            }
        }
        return -1;
    }

    /**
     *
     * @param email
     * @return true if a client exists with the provided email
     */
    public static boolean existeCliente(String email) {
        for (SClientData cliente : list) {
            if (cliente.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public static boolean saveData() {

        try {
            ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(SClientManager.path));
            obj.writeObject((ArrayList<SClientData>) list);
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     *
     * @return @throws IOException
     * @throws ClassNotFoundException
     */
    public static boolean loadData() throws IOException, ClassNotFoundException {
        if (new File(path).exists()) {
            ObjectInputStream obj = new ObjectInputStream(new FileInputStream(path));
            list = (ArrayList<SClientData>) obj.readObject();

            for (SClientData c : list) {
                System.out.println("Email: " + c.email);
            }
            return true;
        }
        return false;
    }
}
