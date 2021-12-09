package servlet;

import java.util.ArrayList;

/**
 *
 * @author Silvio
 */
public class Ordine extends User{
    private int id; //O String? Se serve per mongodb
    private ArrayList<Prodotto> prodotti;
    private float totale;
    private PuntoVendita puntoRitiro;
    //private Image qr;
    private boolean ritirato;
    //Valutare correttezza!
    private Consumatore consumatore;
    private Produttore produttore;
    
    public float calcolaPrezzo(){
        return -1;
    }
    
    boolean inserisciProdotto(Prodotto p, int quantità){
        return false;
    }
    
    boolean modificaQuantità(Prodotto p, int nuovaQuantità){
        return false;
    }
    
    boolean rimuoviProdotto(Prodotto p){
        return false;
    }
}
