package servlet;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import java.io.BufferedReader;
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
@WebServlet(name = "ServletUtenti", urlPatterns = "/utenti/*")
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
        String requested = req.getPathInfo();
        resp.setHeader("Access-Control-Allow-Origin", "*"); //CORS required
        if (requested == null || requested.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Richiesta malformata: Usa ../utenti/[produttori OR consumatori]/");
        } else {
            String requests[] = requested.split("/");
            switch (requests[1]) {
                case "produttori":
                    if (requests.length == 2) {
                        //Ritorno tutti i produttori
                        JSONArray array = new JSONArray();
                        MongoCursor<Produttore> cursore = produttori.find().cursor();
                        while (cursore.hasNext()) {
                            array.put(new JSONObject(cursore.next()));
                        }
                        out.print(array.toString());
                        resp.setHeader("Content-Type", "application/json;charset=utf-8");
                    } else if (ObjectId.isValid(requests[2])) {
                        //Ritorno il produttore richiesto
                        Produttore p = produttori.find(eq("_id", requests[2])).first();
                        if (p == null) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il produttore richiesto non esiste"); //Code 404
                        } else {
                            out.print(new JSONObject(p).toString());
                            resp.setHeader("Content-Type", "application/json;charset=utf-8");
                        }
                    } else {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Id non valido");
                    }
                    break;
                case "consumatori":
                    if (requests.length == 2) {
                        //Ritorno tutti i consumatori
                        JSONArray array = new JSONArray();
                        MongoCursor<Consumatore> cursore = consumatori.find().cursor();
                        while (cursore.hasNext()) {
                            array.put(new JSONObject(cursore.next()));
                        }
                        out.print(array.toString());
                        resp.setHeader("Content-Type", "application/json;charset=utf-8");
                    } else if (ObjectId.isValid(requests[2])) {
                        //Ritorno il consumatore richiesto
                        Consumatore p = consumatori.find(eq("_id", requests[2])).first();
                        if (p == null) {
                            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Il consumatore richiesto non esiste"); //Code 404
                        } else {
                            out.print(new JSONObject(p).toString());
                            resp.setHeader("Content-Type", "application/json;charset=utf-8");
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
        String requested = req.getPathInfo();
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Headers", "Location");
        if (requested.equals("/consumatori") || requested.equals("/consumatori/")) {
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
                Consumatore cons = new Consumatore(
                        j.getString("nome"),
                        j.getString("cognome"),
                        j.getString("email"),
                        j.getString("dataNascita"),
                        j.getString("numTelefono"),
                        j.getString("password"));
                //Inserimento in DB
                try {
                    consumatori.insertOne(cons);
                    resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201
                    resp.setHeader("Location", req.getRequestURL().toString() + '/' + cons.getId()); //mostra dove è disponibile il consumatore
                } catch (MongoException e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database insertion isn't working\n" + e.getMessage());
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else if (requested.equals("/produttori") || requested.equals("/produttori/")) {
            StringBuilder received = new StringBuilder();
            String line;
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                received.append(line);
            }
            try {
                JSONObject j = new JSONObject(received.toString());
                Produttore prod = new Produttore(
                        j.getString("ragSociale"),
                        j.getString("nome"),
                        j.getString("cognome"),
                        j.getString("email"),
                        j.getString("dataNascita"),
                        j.getString("numTelefono"),
                        j.getString("password"));
                try {
                    produttori.insertOne(prod);
                    resp.setStatus(HttpServletResponse.SC_CREATED); //Code 201
                    resp.setHeader("Location", req.getRequestURL().toString() + '/' + prod.getId()); //mostra dove è disponibile il consumatore
                } catch (MongoException e) {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "The database insertion isn't working\n" + e.getMessage());
                }
            } catch (JSONException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "The server was unable to parse the Json object you uploaded");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); //Code 405
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
