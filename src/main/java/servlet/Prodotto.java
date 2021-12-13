package servlet;

import java.util.ArrayList;
import java.util.Objects;
//import javafx.scene.image.Image;

/**
 *
 * @author Silvio
 */
public class Prodotto {
    private int id;
    private String nome;
    //private Image image;
    private float prezzo;
    private String categoria; //Implementare enum Categoria adattando controlli in doGet
    private boolean disponibilità;
    private ArrayList<String> certificazioni;
    private ArrayList<Recensione> recensioni;
    private int minQuantity;

    public Prodotto(int id) {
    }

    public Prodotto(int id, String nome, float prezzo, String categoria, boolean disponibilità, ArrayList<String> certificazioni, ArrayList<Recensione> recensioni, int minQuantity) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.categoria = categoria;
        this.disponibilità = disponibilità;
        this.certificazioni = certificazioni;
        this.recensioni = recensioni;
        this.minQuantity = minQuantity;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isDisponibilità() {
        return disponibilità;
    }

    public void setDisponibilità(boolean disponibilità) {
        this.disponibilità = disponibilità;
    }

    public ArrayList<String> getCertificazioni() {
        return certificazioni;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.nome);
        hash = 23 * hash + Objects.hashCode(this.categoria);
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
