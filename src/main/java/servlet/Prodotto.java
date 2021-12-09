package servlet;

import java.util.ArrayList;
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
    
}
