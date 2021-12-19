package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

/**
 *
 * @author Silvio
 */
@WebServlet(name = "ServletProdotti", urlPatterns = "/prodotti/*")
public class ServletProdotti extends HttpServlet {

    //private HashMap<ObjectId, Prodotto> prodotti;
    //private long numProd;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Prodotto> collProd;

    @Override
    public void init() throws ServletException {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://sz:sz@happyfarmerdb.v8oyl.mongodb.net/test?authSource=admin&replicaSet=atlas-shcncz-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        //try with res
        mongoClient = MongoClients.create(clientSettings);
        db = mongoClient.getDatabase("happyfarmerdb");
        collProd = db.getCollection("prodotti", Prodotto.class);
        System.out.println("Init eseguito con successo!");
        //prodotti = new HashMap<>();
        //numProd = 0;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setHeader("Access-Control-Allow-Origin", "*"); //CORS
        String requested = req.getPathInfo();
        String neededCategory = req.getParameter("category");
        String searchBy = req.getParameter("name");
        if (neededCategory != null && searchBy == null) {
            //Ritorno i prodotti filtrati per la categoria richiesta, json vuoto nel caso non esistano matches
            JSONArray exportBuf = new JSONArray();
            /*Prodotto prodotto;
            for (long i = 0; i < numProd; i++) { //Non esiste foreach per Map
                if ((prodotto = prodotti.get(i)) != null && prodotto.getCategoria().equals(neededCategory)) {
                    exportBuf.put(prodotto);
                }
            }
             */
            MongoCursor<Prodotto> cursore = collProd.find(eq("categoria", neededCategory)).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (neededCategory == null && searchBy != null) {
            //Ritorno i prodotti il cui nome contiene la stringa richiesta, json vuoto nel caso non esistano matches
            //Prodotto prodotto;
            JSONArray exportBuf = new JSONArray();
            /*for (long i = 0; i < numProd; i++) {
                if ((prodotto = prodotti.get(i)) != null && prodotto.getNome().toLowerCase().contains(searchBy.toLowerCase())) {
                    exportBuf.put(prodotto);
                }
            }*/
            MongoCursor<Prodotto> cursore = collProd.find(eq("nome", searchBy)).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (neededCategory != null && searchBy != null) {
            //Ritorno i prodotti filtrati attraverso entrambi i filtri precendenti
            Prodotto prodotto;
            JSONArray exportBuf = new JSONArray();
            /*for (long i = 0; i < numProd; i++) {
                if ((prodotto = prodotti.get(i)) != null) {
                    if (prodotto.getNome().toLowerCase().contains(searchBy.toLowerCase()) && prodotto.getCategoria().equals(neededCategory)) {
                        exportBuf.put(prodotto);
                    }
                }
            }
            out.print(new JSONArray(exportBuf).toString());*/
            MongoCursor<Prodotto> cursore = collProd.find(and(eq("nome", searchBy), eq("categoria", neededCategory))).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (requested == null || requested.equals("/")) {
            //Ritorno tutti i prodotti
            JSONArray exportBuf = new JSONArray();
            /*for (long i = 0; i < numProd; i++) {
                export.put(new JSONObject(prodotti.get(i)));
            }
            out.print(export.toString());*/
            MongoCursor<Prodotto> cursore = collProd.find().cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (ObjectId.isValid(requested.split("/")[1])) { //Java REGEX ('/' seguito da qualsiasi numero positivo intero lungo quanto si vuole)
            //Fornisco il prodotto richiesto
            ObjectId key = new ObjectId(requested.split("/")[1]);
            try {
                JSONObject export = new JSONObject(/*prodotti.get(key)*/collProd.find(eq("_id", key)).first());
                out.print(export.toString());
                resp.setHeader("Content-Type", "application/json;charset=utf-8");
            } catch (NullPointerException e) { //Modificare exception
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto richiesto non esiste"); //Code 404
            }
        } else {
            String errMessage = "Si prega di usare /prodotti o /prodotti/ per richiedere tutti i prodotti, /prodotti/idProdotto per richiedere un prodotto,"
                    + "dove idProdotto è un intero positivo identificante un prodotto già presente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requested = req.getPathInfo();
        resp.setHeader("Access-Control-Allow-Origin", "*");
        if (requested == null || requested.equals("/")) {
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            try {
                JSONObject newJsonProduct = new JSONObject(received.toString());
                Prodotto newProduct = new Prodotto(
                        newJsonProduct.getString("nome"),
                        newJsonProduct.getFloat("prezzo"),
                        newJsonProduct.getString("categoria"),
                        newJsonProduct.getBoolean("disponibile"),
                        newJsonProduct.getInt("minQuantity"));
                synchronized (this) { //Sincronizzato, visto che legge gli attributi
                    //prodotti.put(newProduct.getId(), newProduct);
                    collProd.insertOne(newProduct);
                }
                resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201 //O String normale??
                resp.setHeader("Location", req.getRequestURL().toString() + '/' + newProduct.getId()); //mostra dove è disponibile il prodotto
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST metod not allowed on single resources"); //Code 405
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String requested = req.getPathInfo();
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405 https://restfulapi.net/http-methods/#put
        } else if (ObjectId.isValid(requested.split("/")[1])) {
            ObjectId key = new ObjectId(requested.split("/")[1]);
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            try {
                JSONObject newJsonProduct = new JSONObject(received.toString());
                Prodotto newProduct = new Prodotto(
                        newJsonProduct.getString("nome"),
                        newJsonProduct.getFloat("prezzo"),
                        newJsonProduct.getString("categoria"),
                        newJsonProduct.getBoolean("disponibile"),
                        newJsonProduct.getInt("minQuantity"));
                newProduct.setId(key);
                synchronized (this) { //Da migliorare (tanto codice nel blocco sync), ma non sono riuscito a pensare di meglio
                    /*if (prodotti.replace(key, newProduct) == null) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che ha richiesto di modificare non esiste"); //Code 404
                    } else {*/
                    collProd.findOneAndReplace(eq("_id", key), newProduct);
                    resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/#put
                    //}
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            String errMessage = "Usa /prodotti/productId per effettuare modifiche ad un prodotto esistente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String requested = req.getPathInfo();
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405 https://restfulapi.net/http-methods/
        } else if (ObjectId.isValid(requested.split("/")[1])) {
            ObjectId key = new ObjectId(requested.split("/")[1]);
            synchronized (this) {
                /*if (prodotti.remove(key) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che hai richiesto di eliminare non esiste"); //Code 404
                } else {*/
                collProd.findOneAndDelete(eq("_id", key));
                resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/
                //}
            }
        } else {
            String errMessage = "Usa /prodotti/productId per eliminare un prodotto esistente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
    }

}
