package controladores.web;

import java.io.File;

/**
 *
 * @author agust
 */
public class LecturaDirectorio {

    File[] documentos;

    public LecturaDirectorio(File[] documentos) {
        this.documentos = documentos;
    }

    public File[] getDocumentos() {
        return documentos;
    }    
    
    //Tiene como parametro la ruta del directorio que contiene los documentos y retorna
    //un arrego tipo File con todos los documentos del directorio. (".\\DocumentosTP1\\")
    public File[] leerDirectorio(String ruta) {
        File f = new File(ruta);

        if (f.exists()) {
            documentos = f.listFiles();

            for (File documento : documentos) {
                
            }

        } else {
            System.out.println("Error");            
        }
        
        return documentos;
    }

}
