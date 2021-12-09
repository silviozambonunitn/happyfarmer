package servlet;

/**
 *
 * @author Silvio
 */
public class Pagamento {
    private float somma;
    private String metodo;//Enum?

    public Pagamento(float somma, String metodo) {
        this.somma = somma;
        this.metodo = metodo;
    }
    
    public boolean effettuaTransazione(){
        return false;
    }
}
