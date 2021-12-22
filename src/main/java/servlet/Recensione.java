package servlet;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author Silvio
 */
public class Recensione {

    @BsonId
    private String id;
    private String autore;
    private int qualità;
    private int freschezza;
    private int cordialità;
    private int qualitàPrezzo;

    @BsonCreator
    public Recensione() {
        id = new ObjectId().toHexString();
    }

    public Recensione(String id, String autore, int qualità, int freschezza, int cordialità, int qualitàPrezzo) {
        this.autore = autore;
        this.qualità = qualità;
        this.freschezza = freschezza;
        this.cordialità = cordialità;
        this.qualitàPrezzo = qualitàPrezzo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public int getQualità() {
        return qualità;
    }

    public void setQualità(int qualità) {
        this.qualità = qualità;
    }

    public int getFreschezza() {
        return freschezza;
    }

    public void setFreschezza(int freschezza) {
        this.freschezza = freschezza;
    }

    public int getCordialità() {
        return cordialità;
    }

    public void setCordialità(int cordialità) {
        this.cordialità = cordialità;
    }

    public int getQualitàPrezzo() {
        return qualitàPrezzo;
    }

    public void setQualitàPrezzo(int qualitàPrezzo) {
        this.qualitàPrezzo = qualitàPrezzo;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + this.id.hashCode();
        hash = 17 * hash + this.autore.hashCode();
        hash = 17 * hash + this.qualità;
        hash = 17 * hash + this.freschezza;
        hash = 17 * hash + this.cordialità;
        hash = 17 * hash + this.qualitàPrezzo;
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
        final Recensione other = (Recensione) obj;
        return this.id.equals(other.id);
    }

}
