package entidades;

import controladores.web.Resultado;
import java.util.ArrayList;

public class ListaResultados {
    ArrayList<Resultado> listaResultados;

    public ListaResultados() {
        listaResultados = new ArrayList<>();
    }
    
    public ArrayList<Resultado> getLista() {        
        return listaResultados;
    }
    
    public void addResultado(Resultado r)
    {
        listaResultados.add(r);        
    }
    
    private static ListaResultados instancia;
    
    public static ListaResultados getInstance() {
        if (instancia == null) instancia = new ListaResultados();        
        return instancia;
    }
    
    public void nuevaLista()
    {
        instancia.getLista().clear();
    }

}
