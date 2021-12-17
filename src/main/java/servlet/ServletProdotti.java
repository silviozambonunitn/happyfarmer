package servlet;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Silvio
 */
@WebServlet(name = "ServletProdotti", urlPatterns = "/prodotti/*")
public class ServletProdotti extends HttpServlet {

    private HashMap<Long, Prodotto> prodotti;
    private long id;
    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection collProd;

    @Override
    public void init() throws ServletException {
        mongoClient = new MongoClient("mongodb+srv://sz:sz@happyfarmerdb.v8oyl.mongodb.net/test?authSource=admin&replicaSet=atlas-shcncz-shard-0&readPreference=primary&appname=MongoDB%20Compass&ssl=true");
        db = mongoClient.getDatabase("happyfarmerdb");
        collProd = db.getCollection("prodotti");
        if (collProd.countDocuments() > 0) {
            //Prendi prodotti e inseriscili nella hashmap
            Document buff;
        } else {
            prodotti = new HashMap<>();
            id = 0;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requested = req.getPathInfo();
        String neededCategory = req.getParameter("category");
        String searchBy = req.getParameter("name");
        if (neededCategory != null && searchBy == null) {
            //Ritorno i prodotti filtrati per la categoria richiesta, json vuoto nel caso non esistano matches
            Prodotto prodotto;
            HashMap<Long, Prodotto> exportBuf = new HashMap<>();
            for (long i = 0; i < id; i++) { //Non esiste foreach per Map
                if ((prodotto = prodotti.get(i)) != null && prodotto.getCategoria().equals(neededCategory)) {
                    exportBuf.put(i, prodotto);
                }
            }
            out.print(new JSONObject(exportBuf).toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
            resp.setHeader("Access-Control-Allow-Origin", "*");
        } else if (neededCategory == null && searchBy != null) {
            //Ritorno i prodotti il cui nome contiene la stringa richiesta, json vuoto nel caso non esistano matches
            Prodotto prodotto;
            HashMap<Long, Prodotto> exportBuf = new HashMap<>();
            for (long i = 0; i < id; i++) {
                if ((prodotto = prodotti.get(i)) != null && prodotto.getNome().toLowerCase().contains(searchBy.toLowerCase())) {
                    exportBuf.put(i, prodotto);
                }
            }
            out.print(new JSONObject(exportBuf).toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (neededCategory != null && searchBy != null) {
            //Ritorno i prodotti filtrati attraverso entrambi i filtri precendenti
            Prodotto prodotto;
            HashMap<Long, Prodotto> exportBuf = new HashMap<>();
            for (long i = 0; i < id; i++) {
                if ((prodotto = prodotti.get(i)) != null) {
                    if (prodotto.getNome().toLowerCase().contains(searchBy.toLowerCase()) && prodotto.getCategoria().equals(neededCategory)) {
                        exportBuf.put(i, prodotto);
                    }
                }
            }
            out.print(new JSONObject(exportBuf).toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (requested == null || requested.equals("/")) {
            //Ritorno tutti i prodotti
            JSONObject export = new JSONObject(prodotti);
            out.print(export.toString());
            resp.setHeader("Content-Type", "application/json;charset=utf-8");
        } else if (requested.matches("/[0-9]+$")) { //Java REGEX ('/' seguito da qualsiasi numero positivo intero lungo quanto si vuole)
            //Fornisco il prodotto richiesto
            long key = Long.parseLong(requested.split("/")[1]);
            try {
                JSONObject export = new JSONObject(prodotti.get(key));
                out.print(export.toString());
                resp.setHeader("Content-Type", "application/json;charset=utf-8");
            } catch (NullPointerException e) {
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
                        id, //Prende l'id dell'hashmap
                        newJsonProduct.getString("nome"),
                        newJsonProduct.getFloat("prezzo"),
                        newJsonProduct.getString("categoria"),
                        newJsonProduct.getBoolean("disponibile"),
                        newJsonProduct.getInt("minQuantity"));
                synchronized (this) { //Sincronizzato, visto che legge gli attributi
                    prodotti.put(id, newProduct);
                }
                resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201
                resp.setHeader("Location", req.getRequestURL().toString() + '/' + id++); //mostra dove è disponibile il prodotto
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST metod not allowed on single resources"); //Code 405
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requested = req.getPathInfo();
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405 https://restfulapi.net/http-methods/#put
        } else if (requested.matches("/[0-9]+$")) {
            long key = Long.parseLong(requested.split("/")[1]);
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            try {
                JSONObject newJsonProduct = new JSONObject(received.toString());
                Prodotto newProduct = new Prodotto(
                        id, //Prende l'id dell'hashmap
                        newJsonProduct.getString("nome"),
                        newJsonProduct.getFloat("prezzo"),
                        newJsonProduct.getString("categoria"),
                        newJsonProduct.getBoolean("disponibile"),
                        newJsonProduct.getInt("minQuantity"));
                synchronized (this) { //Da migliorare (tanto codice nel blocco sync), ma non sono riuscito a pensare di meglio
                    if (prodotti.replace(key, newProduct) == null) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che ha richiesto di modificare non esiste"); //Code 404
                    } else {
                        resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/#put
                    }
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            String errMessage = "Usa /prodotti/productId per effettuare modifiche ad un prodotto, dove productId è un intero positivo identificante un prodotto esistente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requested = req.getPathInfo();
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405 https://restfulapi.net/http-methods/
        } else if (requested.matches("/[0-9]+$")) {
            long key = Long.parseLong(requested.split("/")[1]);
            synchronized (this) {
                if (prodotti.remove(key) == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il prodotto che hai richiesto di eliminare non esiste"); //Code 404
                } else {
                    resp.setStatus(HttpServletResponse.SC_OK); //Code 200 https://restfulapi.net/http-methods/
                }
            }
        } else {
            String errMessage = "Usa /prodotti/productId per eliminare un prodotto, dove productId è un intero positivo identificante un prodotto esistente";
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errMessage); //Code 400
        }
    }

    @Override
    public void destroy() {
        if(!prodotti.isEmpty()){
            //Aggiorna prodotti nel db
        }
        mongoClient.close();
    }

}
