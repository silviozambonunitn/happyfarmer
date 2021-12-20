package servlet;

/**
 *
 * @author Silvio
 */
public class Recensione {
    private String autore;
    private int qualità;
    private int freschezza;
    private int cordialità;
    private int qualitàPrezzo;

    public Recensione(String autore, int qualità, int freschezza, int cordialità, int qualitàPrezzo) {
        this.autore = autore;
        this.qualità = qualità;
        this.freschezza = freschezza;
        this.cordialità = cordialità;
        this.qualitàPrezzo = qualitàPrezzo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public int getQualità() {
        return qualità;
    }

    public void setQualità(int qualità) {
        this.qualità = qualità;
    }

    public int getFreschezza() {
        return freschezza;
    }

    public void setFreschezza(int freschezza) {
        this.freschezza = freschezza;
    }

    public int getCordialità() {
        return cordialità;
    }

    public void setCordialità(int cordialità) {
        this.cordialità = cordialità;
    }

    public int getQualitàPrezzo() {
        return qualitàPrezzo;
    }

    public void setQualitàPrezzo(int qualitàPrezzo) {
        this.qualitàPrezzo = qualitàPrezzo;
    }
        
}
