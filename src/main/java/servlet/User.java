package servlet;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author silviozambonunitn, francescounitn
 */
public abstract class User {

    @BsonId
    private String id;
    private String name;
    private String surname;
    protected String email;         //Inserire campo di conferma nel modulo, caso doppia registrazione
    private String birthDate;
    private String phoneNumber;   //Opzionale
    private String userId;
    protected String password;      //Inserire campo di conferma nel modulo. Nel caso doppia registrazione le password devono essere diverse

    public User() {
        id = new ObjectId().toHexString();
        phoneNumber = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
