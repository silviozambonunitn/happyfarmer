package servlet;

import java.util.ArrayList;
import java.util.Objects;
//import javafx.scene.image.Image;

/**
 *
 * @author Silvio
 */
public class Prodotto {

    private long id;
    private String nome;
    //private Image image;
    private float prezzo;
    private String categoria; //Implementare enum Categoria adattando controlli in doGet ServletProdotti
    private boolean disponibilità;
    private ArrayList<String> certificazioni;
    private ArrayList<Recensione> recensioni;
    private int minQuantity;
    private float mediaQP;

    public Prodotto(long id, String nome, float prezzo, String categoria, boolean disponibilità, int minQuantity) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.categoria = categoria;
        this.disponibilità = disponibilità;
        this.minQuantity = minQuantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    /*public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }*/
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

    public boolean isDisponibile() {
        return disponibilità;
    }

    public void setDisponibilità(boolean disponibilità) {
        this.disponibilità = disponibilità;
    }

    public ArrayList<String> getCertificazioni() {
        return certificazioni;
    }

    public boolean aggiungiCert(String c) {
        return false; //da implementare
    }

    public boolean aggiungiRec(Recensione r) {
        return false; //da implementare
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

    public float getMediaQP() {
        float sum = 0;
        for (int i = 0; i < recensioni.size(); i++) {
            sum += recensioni.get(i).getQualitàPrezzo();
        }
        this.mediaQP = sum / recensioni.size();
        return mediaQP;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 43 * hash + Objects.hashCode(this.nome);
        hash = 43 * hash + Float.floatToIntBits(this.prezzo);
        hash = 43 * hash + Objects.hashCode(this.categoria);
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
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
