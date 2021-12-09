package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Silvio
 */
@WebServlet(name = "ServletProdotti", urlPatterns = "/prodotti/*")
public class ServletProdotti extends HttpServlet{
    
    private HashMap<Long,Prodotto> prodotti;
    private long id;
    
    @Override
    public void init() throws ServletException {
        prodotti=new HashMap<>();
        id=0; //PRENDERE DA DB?
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String requested = req.getPathInfo();
    }
    
    /**
     * Inserimento nuovo prodotto
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }
    
    /**
     * Modifica prodotto
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }
    
    /**
     * Eliminazione prodotto
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
    }
}
