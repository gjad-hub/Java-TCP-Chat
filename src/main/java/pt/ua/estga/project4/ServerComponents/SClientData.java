/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pt.ua.estga.project4.ServerComponents;

import java.io.Serializable;

/**
 *
 * @author gonc, ricar
 */
class SClientData implements Serializable {

    /**
     * Client personal ID. not used for now
     */
    protected static int ID;

    /**
     * Client personal email.
     */
    protected String email;

    /**
     * Client First Name.
     */
    protected String firstName;

    /**
     * Client Last Name.
     */
    protected String lastName;

    /**
     *
     * @param ID -> ID to be saved
     * @param email -> Client's Personal Email
     * @param firstName -> Client's First Name
     * @param lastName -> Client's Last Name
     */
    public SClientData(int ID, String email, String nome, String sobrenome) {
        this.email = email;
        this.firstName = nome;
        this.lastName = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public int getID() {
        return ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
