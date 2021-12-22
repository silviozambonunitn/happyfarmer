package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
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
 * Servlet per gestire retrieve, inserimento, modifica eliminazione di Prodotti
 *
 * @author Silvio
 */
@WebServlet(name = "ServletProdotti", urlPatterns = "/prodotti/*")
public class ServletProdotti extends HttpServlet {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Prodotto> prodotti;

    @Override
    public void init() throws ServletException {
        ConnectionString connectionString = new ConnectionString("mongodb+srv://sz:sz@happyfarmerdb.v8oyl.mongodb.net/test?authSource=admin&replicaSet=atlas-shcncz-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        mongoClient = MongoClients.create(clientSettings);
        db = mongoClient.getDatabase("happyfarmerdb");
        prodotti = db.getCollection("prodotti", Prodotto.class);
        System.out.println("Connessione con MongoDB eseguita con successo!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setHeader("Access-Control-Allow-Origin", "*"); //CORS required
        String requested = req.getPathInfo();
        String neededCategory = req.getParameter("category");
        String searchBy = req.getParameter("name");
        if (neededCategory != null && searchBy == null) {
            //Ritorno i prodotti filtrati per la categoria richiesta, json vuoto nel caso non esistano matches
            JSONArray exportBuf = new JSONArray();
            MongoCursor<Prodotto> cursore = prodotti.find(eq("categoria", neededCategory)).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (neededCategory == null && searchBy != null) {
            //Ritorno i prodotti il cui nome contiene la stringa richiesta, json vuoto nel caso non esistano matches
            JSONArray exportBuf = new JSONArray();
            MongoCursor<Prodotto> cursore = prodotti.find(eq("nome", searchBy)).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (neededCategory != null && searchBy != null) {
            //Ritorno i prodotti filtrati attraverso entrambi i filtri precendenti
            JSONArray exportBuf = new JSONArray();
            MongoCursor<Prodotto> cursore = prodotti.find(and(eq("nome", searchBy), eq("categoria", neededCategory))).cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (requested == null || requested.equals("/")) {
            //Ritorno tutti i prodotti
            JSONArray exportBuf = new JSONArray();
            MongoCursor<Prodotto> cursore = prodotti.find().cursor();
            while (cursore.hasNext()) {
                exportBuf.put(new JSONObject(cursore.next()));
            }
            out.print(exportBuf.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (ObjectId.isValid(requested.split("/")[1])) {
            //Fornisco il prodotto richiesto
            String key = requested.split("/")[1];
            Prodotto p = prodotti.find(eq("_id", key)).first();
            if (p == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto richiesto non esiste"); //Code 404
            } else {
                out.print(new JSONObject(p).toString());
                resp.setHeader("Content-Type", "application/json;charset=utf-8");
            }
        } else {
            String errMessage = "Si prega di usare /prodotti o /prodotti/ per richiedere tutti i prodotti, /prodotti/idProdotto per richiedere un prodotto,"
                    + "dove idProdotto è una stringa identificante un prodotto già presente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requested = req.getPathInfo();
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Location");
        if (requested == null || requested.equals("/")) {
            //Lettura input
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            //Lettura Json
            try {
                JSONObject j = new JSONObject(received.toString());
                Prodotto newProduct = new Prodotto(
                        j.getString("nome"),
                        j.getFloat("prezzo"),
                        j.getString("categoria"),
                        j.getBoolean("disponibile"),
                        j.getInt("minQuantity"),
                        j.getInt("maxQuantity"),
                        j.getString("produttore"));
                //Inserimento in DB
                try {
                    prodotti.insertOne(newProduct);
                    resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201
                    resp.setHeader("Location", req.getRequestURL().toString() + '/' + newProduct.getId()); //mostra dove è disponibile il prodotto
                } catch (MongoException e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database insertion isn't working\n" + e.getMessage());
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST method not allowed on single resources"); //Code 405
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setHeader("Access-Control-Allow-Origin", "*");
        String requested = req.getPathInfo();
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405 https://restfulapi.net/http-methods/#put
        } else if (ObjectId.isValid(requested.split("/")[1])) {
            String key = requested.split("/")[1];
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            try {
                JSONObject j = new JSONObject(received.toString());
                Prodotto newProduct = new Prodotto(
                        j.getString("nome"),
                        j.getFloat("prezzo"),
                        j.getString("categoria"),
                        j.getBoolean("disponibilità"),
                        j.getInt("minQuantity"),
                        j.getInt("maxQuantity"),
                        j.getString("produttore"));
                newProduct.setId(key);
                if (prodotti.findOneAndReplace(eq("_id", key), newProduct) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che ha richiesto di modificare non esiste"); //Code 404
                } else {
                    resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/#put
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
            String key = requested.split("/")[1];
            if (prodotti.findOneAndDelete(eq("_id", key)) == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che hai richiesto di eliminare non esiste"); //Code 404
            } else {
                resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/
            }
        } else {
            String errMessage = "Usa /prodotti/productId per eliminare un prodotto esistente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "content-type");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    }

}
