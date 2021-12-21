package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(name = "ServletUtenti", urlPatterns = "/users/*")
public class ServletUtenti extends HttpServlet {

    private MongoClient mongoClient;
    private MongoDatabase db;
    private MongoCollection<Produttore> produttori;
    private MongoCollection<Consumatore> consumatori;

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
        produttori = db.getCollection("produttori", Produttore.class);
        consumatori = db.getCollection("consumatori", Consumatore.class);
        System.out.println("Connessione con MongoDB eseguita con successo!");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String type = req.getPathInfo().split("/")[1];
        String id = req.getPathInfo().split("/")[2];
        resp.setHeader("Access-Control-Allow-Origin", "*"); //CORS required
        if (null == type) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Richiesta malformata: Usa .../[produttori OR consumatori]/..");
        } else {
            switch (type) {
                case "produttori":
                    if (id == null) {
                        //Ritorno tutti i produttori
                        JSONArray array = new JSONArray();
                        MongoCursor<Produttore> cursore = produttori.find().cursor();
                        while (cursore.hasNext()) {
                            array.put(cursore.next());
                        }
                        out.print(array.toString());
                        resp.setHeader("Content-Type", "application/json;charset=utf-8");
                    } else if (ObjectId.isValid(id)) {
                        //Ritorno il produttore richiesto
                        try {
                            out.print(new JSONObject(produttori.find(eq("_id", id)).first()).toString());
                        } catch (JSONException e) {
                            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving requested resource!");
                        }
                    } else {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id non valido");
                    }
                    break;
                case "consumatori":
                    if (id == null) {
                        //Ritorno tutti i consumatori
                        JSONArray array = new JSONArray();
                        MongoCursor<Consumatore> cursore = consumatori.find().cursor();
                        while (cursore.hasNext()) {
                            array.put(cursore.next());
                        }
                        out.print(array.toString());
                        resp.setHeader("Content-Type", "application/json;charset=utf-8");
                    } else if (ObjectId.isValid(id)) {
                        //Ritorno il consumatore richiesto
                        try {
                            out.print(new JSONObject(produttori.find(eq("_id", id)).first()).toString());
                        } catch (JSONException e) {
                            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error retrieving requested resource!");
                        }
                    } else {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id non valido");
                    }
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Categoria di utenti richiesta non valida");
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "content-type");
    }
}
