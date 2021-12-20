package servlet;

import java.util.ArrayList;

/**
 *
 * @author Silvio
 */
public class Consumatore extends User {

    //Da appoggio per query db, credo
    private ArrayList<Ordine> ordini; //Distinguere tra da ritirare e vecchi?

    public Consumatore() {
        super();
        ordini = new ArrayList<>();
    }
}
