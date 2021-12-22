package servlet;

import java.util.ArrayList;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

/**
 *
 * @author Silvio
 */
public class ContoVirtuale {
    @BsonId
    private String id;
    private double value;
    private ArrayList<Pagamento> pagamenti;

    @BsonCreator
    public ContoVirtuale() {
        id=new ObjectId().toHexString();
        value=0;
        pagamenti=new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ArrayList<Pagamento> getPagamenti() {
        return pagamenti;
    }

    public void setPagamenti(ArrayList<Pagamento> pagamenti) {
        this.pagamenti = pagamenti;
    }
}
