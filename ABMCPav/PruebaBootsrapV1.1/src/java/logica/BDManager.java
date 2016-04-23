/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Ariza, Gonzalo (62069)
 * Muñoz Campos, Agustín (62846)
 * Ramírez, Nicolás (63318)
 */
public class BDManager {

    private static BDManager singleIntance;
    private Connection connection = null;
    private Statement statement = null;
    private String query = "";
    private ResultSet rs = null;
    private PreparedStatement ps = null;
    private PreparedStatement pi = null;
    private PreparedStatement pu = null;
    private PreparedStatement psaux = null;
    private PreparedStatement piaux = null;
    private PreparedStatement psaux2 = null;
    private PreparedStatement psaux3 = null;
    private PreparedStatement pioi = null;
    private PreparedStatement pior = null;

    private BDManager() {
        String error = connect();
        if (!error.equals("")) {
            System.out.println("" + error);

        }

    }

    private String connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\nicor_000\\Documents\\GitHub\\ABMCPav\\PruebaBootsrapV1.1\\DB\\BuscadorVectorial");

        } catch (SQLException ex) {
            return "Error " + ex.getMessage();
        } catch (ClassNotFoundException ex) {
            return "Class Error " + ex.getMessage();
        }
        return "";
    }

    public static BDManager getBDManager() {
        if (singleIntance != null) {
            return singleIntance;
        } else {
            singleIntance = new BDManager();
            return singleIntance;
        }
    }

     /**
     * Almacena en la BD un documento.
     *
     * @param d documento
     */
    public void persistDoc(Documento d) {

        if (d == null) {
            return;
        }

        try {
            connection.setAutoCommit(false);
            pioi = connection.prepareStatement("Insert or ignore into DOCUMENTO (ruta) values (?)");
            
            String link = d.getLink();
            pioi.setString(1,link);
            pioi.executeUpdate();
            
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {

                System.out.println("Error " + e.getMessage());

            }

        }

    }
    
     public void persistpalabras(Iterator<Palabra> i, HashMap<String, Palabra> map) {
        int pordondeva = 0;
        Palabra p = null;
        Documento d = null;
        boolean aparecio = false;
        if (i == null) {
            return;
        }
int cdp = map.size();
        try {
            connection.setAutoCommit(false);
            
            
            
            String ior="Insert or replace into VOCABULARIO (palabra, documentos, maxRepeticion)";
            ior += " values ( ?, ?, ?)";

//            String ior = "Insert or replace into VOCABULARIO  (cadena, repeticiones)";
//            ior += " values ( ?,coalesce";
//            ior += "( (SELECT repeticiones FROM palabra WHERE cadena LIKE ?) + ?,?))";
            pior = connection.prepareStatement(ior);
            
            String insert = "Insert into POSTEO (palabra, idDocumento, repeticion) values (?,(select id from DOCUMENTO where ruta like ?),?);";
//            String ioi = "Insert or ignore into PalabraXDocumento (idDoc, cadena) ";
//            ioi += "values ((select idDoc from Documento where link like ?), ?)";
//             pioi = connection.prepareStatement(ioi);
                pi = connection.prepareStatement(insert);
              

            while (i.hasNext()) {
                // assert(p.getCadena() == la key del objeto)
                if(pordondeva % 500 == 0){System.out.println(pordondeva);}
              
//               Palabra p = i.next();
                p = i.next();
               // System.out.println(p.getCadena());
               
               if (p.getaparicion()== false){ continue;}
               
                         

                pior.setString(1, p.getCadena());
                pior.setInt(2,p.getDocumentosdiferentes());
                pior.setInt(3, p.getRepeticionmaxima());
  
                pior.executeUpdate();

                String ruta = p.getDoc().getLink();
//                Documento d = p.getDoc();
                d = p.getDoc();
                pi.setString(1, p.getCadena());
                pi.setString(2, ruta);
                pi.setInt(3, p.getRepeticionpordocumento());
                pi.executeUpdate();
               pordondeva++;
              
                
            }

            connection.commit();

   //         mapapalabras.registrarFinPersist();
    //        mapapalabras.registrarFinPersitinFiles("Listo");

            connection.setAutoCommit(true);

        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
            try {
                connection.rollback();
       //         mapapalabras.registrarFinPersitinFiles("Error al guardar.");
            } catch (SQLException e) {

                System.out.println("Error " + e.getMessage());
            }
        }

    }
    
    
    
    
    
    /**
     * Retorna una lista de todas las palabras que recupera de la BD
     *
     * @return retorna la lista
     */
    public LinkedList materializeWords() {
        statement = null;
        rs = null;
        Palabra p;
        LinkedList<Palabra> list = new LinkedList<>();

        query = "select p.cadena as 'Palabra', p.repeticiones as 'Repeticiones', count(pxd.idDoc) as 'Aparicion' ";
        query += "from Palabra as p join PalabraXDocumento as pxd on (p.cadena= pxd.cadena) ";
        query += "group by p.cadena, p.repeticiones";

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()) {
                p = Palabra.generarPalabra(rs.getString("Palabra"));
                p.setContador(rs.getInt("Repeticiones"));

             //   p.setCantidadDocumentos(rs.getInt("Aparicion"));

                list.add(p);

            }

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Error " + ex.getMessage());
                }
            }

        }

        return list;

    }

    /**
     * Busca una cadena en la BD, crea una Palabra junto con cada aparición en
     * documentos distintos y la retorna
     *
     * @param cadena a buscar en BD
     * @return la palabra que recupera de la BD
     */
    public Palabra materializeAparicion(String cadena) {
        statement = null;
        Palabra p = null;
        Documento d;
        query = "select d.link as 'link', pxd.cadena as 'cadena', p.repeticiones as 'repeticiones' ";
        query += "from PalabraXDocumento as pxd join Documento as d on (pxd.idDoc = d.idDoc) ";
        query += "join Palabra as p on (pxd.cadena = p.cadena) ";
        query += "where pxd.cadena like '" + cadena + "'";
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);

            while (rs.next()) {
                if (null == p) {
                    p = Palabra.generarPalabra(rs.getString("cadena"));
                    p.setContador(rs.getInt("repeticiones"));
                }
                d = new Documento(rs.getString("link"));

                p.addDocument(d);
            }
        } catch (SQLException ex) {
            System.out.println("Error " + ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {

                    System.out.println("Error " + ex.getMessage());
                }
            }

        }

        return p;

    }

    /**
     * Borra el contenido de la BD.
     */
    public void deleteall() {

        statement = null;

        try {
            query = "Delete from Palabra";
            statement = connection.createStatement();
            statement.executeUpdate(query);

            query = "Delete from Documento";
            statement = connection.createStatement();
            statement.executeUpdate(query);

            query = "Delete from PalabraXDocumento";
            statement = connection.createStatement();
            statement.executeUpdate(query);

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {

                    System.out.println("Error " + ex.getMessage());
                }
            }
        }

    }

    /**
     * Almacena en la BD todos los documentos recorriendo el iterador.
     *
     * @param i iterador de una lista o coleccion de documentos
     */
    public void persistDoc(Iterator<Documento> i) {

        if (i == null) {
            return;
        }

        try {
            connection.setAutoCommit(false);
            pioi = connection.prepareStatement("Insert or ignore into Documento (link) values (?)");
            while (i.hasNext()) {
                String link = i.next().getLink();
                pioi.setString(1, link);
                pioi.executeUpdate();

            }
            connection.commit();
            connection.setAutoCommit(true);

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e) {

                System.out.println("Error " + e.getMessage());

            }

        }

    }

    /**
     * Almacena en la BD todas las palabras
     *
     * @param i iterador de la lista o coleccion de palabras.
     * @param mapapalabras puntero para el envio de mensajes.
     * @param contador cantidad de palabras que debe almacenar.
     *
     */
    
   

    /**
     *
     * @return retorna una lista con los documentos que se recuperan de la BD.
     */
    public LinkedList materializeDocs() {

        statement = null;
        Documento d;
        LinkedList<Documento> list = new LinkedList<>();

        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("select path from DOCUMENTO");
            while (rs.next()) {
                d = new Documento(rs.getString("path"));
                list.add(d);
            }

        } catch (SQLException ex) {

            System.out.println("Error " + ex.getMessage());
        }

        return list;

    }

    
}
