package servlet;

import java.util.ArrayList;

/**
 *
 * @author Silvio
 */
public class Ordine extends User {

    private int id; //O String? Se serve per mongodb
    private ArrayList<Prodotto> prodotti;
    private float totale;
    private PuntoVendita puntoRitiro;
    //private Image qr;
    private boolean ritirato;
    //Valutare correttezza!
    private Consumatore consumatore;
    private Produttore produttore;
    private Pagamento pagamento;

    public float calcolaPrezzo() {
        int tot = 0;
        for (int i = 0; i < prodotti.size(); i++) {
            tot += prodotti.get(i).getPrezzo();
        }
        return tot;
    }

    boolean inserisciProdotto(Prodotto p, int quantità) {
        //come gestire la quantità?
        return false;
    }

    boolean modificaQuantità(Prodotto p, int nuovaQuantità) {
        return false;
    }

    boolean rimuoviProdotto(Prodotto p) {
        return false;
    }
    
    boolean tentaPagamento(){
        return false;
    }
}
