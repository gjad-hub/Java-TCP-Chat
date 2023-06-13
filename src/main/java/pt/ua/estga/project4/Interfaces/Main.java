/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pt.ua.estga.project4.Interfaces;

import pt.ua.estga.project4.ServerComponents.Server;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricar
 */
public class Main {

    public static void main(String[] args) {

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            System.out.println("Click enter to Quit!");
            sc.nextLine();
            System.exit(0);
        }).start();

        try {
            Server server = new Server(new ServerSocket(6000));
            server.startServer();
        } catch (BindException e) {
            System.out.println("Theres a server opened already, Quiting!");
        } catch (IOException ex) {

        }

    }

}
