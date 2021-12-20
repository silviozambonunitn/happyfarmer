package servlet;

import java.util.ArrayList;

/**
 *
 * @author Silvio
 */
public class Produttore extends User {

    private String ragioneSociale;
    private ContoVirtuale conto;

    //Da appoggio per query db, credo
    private ArrayList<Ordine> ordini; //Distinguere tra da ritirare e vecchi?

    public Produttore() {
        super();
        ordini = new ArrayList<>();
    }
}
