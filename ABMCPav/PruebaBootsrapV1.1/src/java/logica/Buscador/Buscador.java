/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica.Buscador;

import java.util.LinkedList;

/**
 *
 * @author nicor_000
 * 
 */
public class Buscador {
    /**cantidadDocumentos cantidad de documentos a buscar en la consulta (busqueda)**/
    int cantidadDocumentosamostrar;
    /** cantidad de documentos N para la formula coseno**/
    int cantidaddocumentostotal;
    /**consulta **/
    String consultacadena;
    String[] cadenas;
    LinkedList listapalabras;
    LinkedList listaordenadapalabras;
    
    
    
    
    
    public Buscador (String cadena)
    {
        String consultacadena= cadena;        
        cadenas = consultacadena.split(" ");
        //Collection.sort(lista);
}

    
    
   
    
    
    
    /*
      Nulo: Identificamos cuantos documentos vamos a mostrar.
     ¨Primero: Debemos buscar por cada palabra de la Consulta su idf, en practica nr ( buscar terminos con menor nr)
      Segundo: Traer los Primeros R documentos. SI no se llegan a lor R, se busca el proximo con menor nr.
      Tercer: 
      
    */
}
