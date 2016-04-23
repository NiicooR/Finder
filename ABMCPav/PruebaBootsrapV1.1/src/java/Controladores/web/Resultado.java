package controladores.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author agust
 */
public class Resultado {
    
    String titulo = "";
    String detalle = "";
    String nombreArchivo;   

    public String getTitulo() {
        return titulo;
    }

    public String getDetalle() {
        return detalle + "...";
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }    
    
    public void obtenerDatos(String ruta) throws FileNotFoundException, IOException
    {
            File f = new File(ruta);
            BufferedReader br = new BufferedReader(new FileReader(f));
            nombreArchivo = f.getName();
            
            String line = "";
            
            while(detalle.length() <= 600)
            {  
                line = br.readLine();

                if(!line.isEmpty())
                {                    
                    if(titulo == ""){
                        titulo = line;
                    }
                    else if(detalle == "")
                    {
                        detalle = line;
                    }
                    else if(detalle != "")
                    {
                        detalle = detalle + line;
                    }                   
                }
                else if(detalle != "")
                {
                    detalle = detalle + ". ";
                }
            }
//            System.out.println(nombreArchivo);
//            System.out.println(titulo);
//            System.out.println(detalle+"...");            
    }
    
}
