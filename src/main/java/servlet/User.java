/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

/**
 *
 * @author Silvio
 */
public abstract class User {
    
    private String name;
    private String surname;
    protected String email;         //Inserire campo di conferma nel modulo, caso doppia registrazione
    private String birthDate;
    private String phoneNumber;   //Opzionale
    private String userId;
    protected String password;      //Inserire campo di conferma nel modulo. Nel caso doppia registrazione le password devono essere diverse

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserId() {
        return userId;
    }
    
}
