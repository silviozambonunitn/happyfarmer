package servlet;

import org.bson.codecs.pojo.annotations.BsonCreator;

/**
 *
 * @author Silvio
 */
public class Consumatore extends User {

    @BsonCreator
    public Consumatore() {
        super();
    }
}
