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
    private String id;
    private String produttore;
    private String nome;
    private float prezzo;
    private String categoria; //Implementare enum Categoria adattando controlli in doGet ServletProdotti
    private boolean disponibilità;
    private int minQuantity;
    private int maxQuantity;
    private ArrayList<String> certificazioni;
    private ArrayList<Recensione> recensioni;

    public Prodotto() {
        id = new ObjectId().toHexString();
        certificazioni = new ArrayList<>();
        recensioni = new ArrayList<>();
    }

    public Prodotto(String nome, float prezzo, String categoria, boolean disponibilità, int minQuantity, int maxQuantity, String produttore) {
        this.id = new ObjectId().toHexString();
        this.nome = nome;
        this.prezzo = prezzo;
        this.categoria = categoria;
        this.disponibilità = disponibilità;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.produttore = produttore;
        certificazioni = new ArrayList<>();
        recensioni = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getProduttore() {
        return produttore;
    }

    public void setProduttore(String produttore) {
        this.produttore = produttore;
    }

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
        try {
            return this.id.equals(((Prodotto) obj).getId());
        } catch (ClassCastException e) {
            return false;
        }
    }
}
