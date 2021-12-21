package servlet;

/**
 *
 * @author Silvio
 */
public class ProductEntry {

    private Prodotto prodotto;
    private int quantità;

    public ProductEntry(Prodotto prodotto, int quantità) {
        this.prodotto = prodotto;
        this.quantità = quantità;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public int getQuantità() {
        return quantità;
    }

    public void setQuantità(int quantità) {
        this.quantità = quantità;
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
        final ProductEntry other = (ProductEntry) obj;
        return this.prodotto.equals( other.prodotto);
    }

    @Override
    public int hashCode() {
        return this.prodotto.hashCode();
    }

}
