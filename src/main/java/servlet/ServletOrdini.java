package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Silvio
 */
@WebServlet(name = "ServletOrdini", urlPatterns = "/ordini/*")
public class ServletOrdini extends HttpServlet {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Ordine> ordini;

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
        ordini = db.getCollection("ordini", Ordine.class);
        System.out.println("Connessione con MongoDB eseguita con successo!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setHeader("Access-Control-Allow-Origin", "*"); //CORS required
        String requested = req.getPathInfo().split("/")[1];
        if (ObjectId.isValid(requested)) {
            Ordine o = ordini.find(eq("_id", requested)).first();
            if (o == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "L'ordine richiesto non esiste"); //Code 404
            } else {
                out.print(new JSONObject(o).toString());
                resp.setHeader("Content-Type", "application/json;charset=utf-8");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Usa .../ordini/IdOrdine per accedere ad un ordine");
        }
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
            //Lettura json
            try {
                JSONObject j = new JSONObject(received.toString());
                ProductEntry p;
                ArrayList<ProductEntry> array = new ArrayList<>();
                JSONArray jarray = j.getJSONArray("prodotti");
                JSONObject buff;
                for (int i = 0; i < jarray.length(); i++) {
                    buff = jarray.getJSONObject(i);
                    p = new ProductEntry(buff.getString("prodotto"), buff.getInt("quantità"));
                    array.add(p);
                }
                Ordine ord = new Ordine(
                        j.getString("consumatore"),
                        j.getString("produttore"),
                        array,
                        j.getFloat("totale"));
                //inserimento in db
                try {
                    ordini.insertOne(ord);
                    resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201
                    resp.setHeader("Location", req.getRequestURL().toString() + '/' + ord.getId()); //mostra dove è disponibile il consumatore
                } catch (MongoException e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database insertion isn't working\n" + e.getMessage());
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Usa .../ordini(/) per caricare un ordine");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Method not implemented yet");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "Method not implemented yet");
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "content-type");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    }
}
