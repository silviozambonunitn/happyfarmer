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
    private PuntoVendita puntoRitiro;
    //QR code
    private boolean ritirato;
    private Consumatore consumatore;
    private Produttore produttore;
    private Pagamento pagamento;
    private ArrayList<ProductEntry> prodotti;

    public Ordine() {
        id = new ObjectId().toHexString();
        prodotti = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float calcolaPrezzo() {
        int tot = 0;
        for (int i = 0; i < prodotti.size(); i++) {
            tot += prodotti.get(i).getProdotto().getPrezzo();
        }
        return tot;
    }

    boolean aggiungiProdotto(Prodotto p, int quantità) {
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
    }

    public ArrayList<Prodotto> getProdotti() {
        return null;
    }

    public float getTotale() {
        return totale;
    }

    public PuntoVendita getPuntoRitiro() {
        return puntoRitiro;
    }

    public void setPuntoRitiro(PuntoVendita puntoRitiro) {
        this.puntoRitiro = puntoRitiro;
    }

    public boolean isRitirato() {
        return ritirato;
    }

    public void setRitirato(boolean ritirato) {
        this.ritirato = ritirato;
    }

    public Consumatore getConsumatore() {
        return consumatore;
    }

    public void setConsumatore(Consumatore consumatore) {
        this.consumatore = consumatore;
    }

    public Produttore getProduttore() {
        return produttore;
    }

    public void setProduttore(Produttore produttore) {
        this.produttore = produttore;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
}
