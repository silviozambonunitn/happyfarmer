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
    
    protected String name;
    protected String surname;
    protected String email;         //Inserire campo di conferma nel modulo, caso doppia registrazione
    protected String birthDate;
    protected String phoneNumber;   //Opzionale
    protected String userId;
    protected String password;      //Inserire campo di conferma nel modulo. Nel caso doppia registrazione le password devono essere diverse
    
}
