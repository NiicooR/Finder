/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package logica;



/**
 *
 * @author Ariza, Gonzalo (62069)
 * Muñoz Campos, Agustín (62846)
 * Ramírez, Nicolás (63318)
 */
public class Documento {
    
    private String link;
    
    
    public Documento()
    {        
    }
    
    public Documento (String l)
    {
      if(!l.isEmpty())link= l;
    }
    
  
    
    public String getLink()
    {
        return link;
    }
    
    @Override
       public String toString() {
        return link;
    }
    
    public boolean equals (Documento d) 
   {
       return (this.getLink().equals(d.getLink()));
   }
    
    
}
