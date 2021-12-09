package servlet;

import java.util.ArrayList;

/**
 *
 * @author Silvio
 */
public class Produttore {
    private String ragioneSociale;
    private ContoVirtuale conto;
    
    //Da appoggio per query db, credo
    private ArrayList<Prodotto> ordini; //Distinguere tra da ritirare e vecchi?
}
