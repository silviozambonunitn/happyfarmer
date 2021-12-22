package servlet;

import java.util.ArrayList;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author Silvio
 */
public class Ordine {

    @BsonId
    private String id;
    private float totale;
    //private PuntoVendita puntoRitiro;
    //QR code da implementare
    private boolean ritirato;
    private String consumatore;
    private String produttore;
    private Pagamento pagamento;
    private ArrayList<ProductEntry> prodotti;

    public Ordine() {
        id = new ObjectId().toHexString();
        prodotti = new ArrayList<>();
    }

    public Ordine(String consumatore, String produttore, ArrayList<ProductEntry> prodotti, float totale) {
        id = new ObjectId().toHexString();
        this.consumatore = consumatore;
        this.produttore = produttore;
        this.prodotti = prodotti;
        this.totale = totale;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*boolean aggiungiProdotto(Prodotto p, int quantità) {
        //come gestire la quantità?
        return false;
    }

    boolean modificaQuantità(Prodotto p, int nuovaQuantità) {
        return false;
    }

    boolean rimuoviProdotto(Prodotto p) {
        return false;
    }

    boolean tentaPagamento() {
        return false;
    }*/
    public void setProdotti(ArrayList<ProductEntry> prodotti) {
        this.prodotti = prodotti;
    }

    public ArrayList<ProductEntry> getProdotti() {
        return prodotti;
    }

    public void setTotale(float totale) {
        this.totale = totale;
    }

    public float getTotale() {
        return totale;
    }

    /*public PuntoVendita getPuntoRitiro() {
        return puntoRitiro;
    }

    public void setPuntoRitiro(PuntoVendita puntoRitiro) {
        this.puntoRitiro = puntoRitiro;
    }*/
    public boolean isRitirato() {
        return ritirato;
    }

    public void setRitirato(boolean ritirato) {
        this.ritirato = ritirato;
    }

    public String getConsumatore() {
        return consumatore;
    }

    public void setConsumatore(String consumatore) {
        this.consumatore = consumatore;
    }

    public String getProduttore() {
        return produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
