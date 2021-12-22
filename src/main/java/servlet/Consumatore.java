package servlet;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author silviozambonunitn, francescounitn
 */
public class Consumatore {

    @BsonId
    private String id; //Non uso la mail per facilità di gestione nei parametri url
    private String name;
    private String surname;
    protected String email; //Inserire campo di conferma nel modulo, caso doppia registrazione
    private String birthDate;
    private String phoneNumber;   //Opzionale
    protected String password; //Inserire campo di conferma nel modulo. Nel caso doppia registrazione le password devono essere diverse

    @BsonCreator
    public Consumatore() {
        id = new ObjectId().toHexString();
        name = null;
        surname = null;
        email = null;
        birthDate = null;
        phoneNumber = null;
        password = null;
    }

    public Consumatore(String name, String surname, String email, String birthDate, String phoneNumber, String password) {
        id = new ObjectId().toHexString();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.name.hashCode();
        hash = 67 * hash + this.surname.hashCode();
        hash = 67 * hash + this.email.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Consumatore other = (Consumatore) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
