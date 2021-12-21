package servlet;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.types.ObjectId;

/**
 *
 * @author Silvio
 */
public class Produttore extends Consumatore {

    private String ragioneSociale;
    private String conto; //ObjectId

    @BsonCreator
    public Produttore() {
        super();
        ragioneSociale = "Initialized!";
        conto = new ObjectId().toHexString();
    }

    public Produttore(String ragioneSociale,
            String name, String surname, String email, String birthDate, String phoneNumber, String password) {
        super(name, surname, email, birthDate, phoneNumber, password);
        this.ragioneSociale = ragioneSociale;
        conto = new ObjectId().toHexString();
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public String getConto() {
        return conto;
    }

    public void setConto(String conto) {
        this.conto = conto;
    }
}
