package servlet;

import java.util.ArrayList;
import java.util.Objects;
import org.bson.codecs.pojo.annotations.*;
import org.bson.types.ObjectId;
//import javafx.scene.image.Image;

/**
 *
 * @author Silvio
 */
public class Prodotto {

    @BsonId
    private ObjectId id;
    private String nome;
    //private Image image;
    private float prezzo;
    private String categoria; //Implementare enum Categoria adattando controlli in doGet ServletProdotti
    private boolean disponibilità;
    private int minQuantity;
    private ArrayList<String> certificazioni;
    private ArrayList<Recensione> recensioni;
    //private float mediaQP;

    public Prodotto(@BsonProperty("nome") String nome,
            @BsonProperty("prezzo") float prezzo,
            @BsonProperty("categoria") String categoria,
            @BsonProperty("disponibilità") boolean disponibilità,
            @BsonProperty("minQuantity") int minQuantity) {
        this.id = new ObjectId();
        this.nome = nome;
        this.prezzo = prezzo;
        this.categoria = categoria;
        this.disponibilità = disponibilità;
        this.minQuantity = minQuantity;
        certificazioni=new ArrayList<>();
        recensioni=new ArrayList<>();
        //mediaQP = 0;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(float prezzo) {
        this.prezzo = prezzo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilità(boolean disponibilità) {
        this.disponibilità = disponibilità;
    }

    public ArrayList<String> getCertificazioni() {
        return certificazioni;
    }

    public boolean aggiungiCert(String c) {
        return certificazioni.add(c);
    }

    public boolean aggiungiRec(Recensione r) {
        return recensioni.add(r);
    }

    public ArrayList<Recensione> getRecensioni() {
        return recensioni;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(int minQuantity) {
        this.minQuantity = minQuantity;
    }

    /*public float getMediaQP() {
        if (recensioni.isEmpty()) {
            this.mediaQP = -1;
        } else {
            float sum = 0;
            for (int i = 0; i < recensioni.size(); i++) {
                sum += recensioni.get(i).getQualitàPrezzo();
            }
            this.mediaQP = sum / recensioni.size();
        }
        return mediaQP;
    }*/

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.nome);
        hash = 29 * hash + Float.floatToIntBits(this.prezzo);
        hash = 29 * hash + Objects.hashCode(this.categoria);
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
        final Prodotto other = (Prodotto) obj;
        if (Float.floatToIntBits(this.prezzo) != Float.floatToIntBits(other.prezzo)) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.categoria, other.categoria)) {
            return false;
        }
        return true;
    }
}
