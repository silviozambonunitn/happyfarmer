package servlet;

import org.bson.codecs.pojo.annotations.BsonCreator;

/**
 *
 * @author Silvio
 */
public class Produttore extends User {

    private String ragioneSociale;
    private ContoVirtuale conto;

    @BsonCreator
    public Produttore() {
        super();
        ragioneSociale = "Initialized!";
        conto = new ContoVirtuale();
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }

    public ContoVirtuale getConto() {
        return conto;
    }

    public void setConto(ContoVirtuale conto) {
        this.conto = conto;
    }
}
