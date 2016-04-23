package entidades;


public class Consulta {
    String consulta;

    public Consulta(String consulta) {
        this.consulta = consulta;
    }

    public String getConsulta() {
        return consulta;
    }

    public void setConsulta(String consulta) {
        this.consulta = consulta;
    }

    @Override
    public String toString() {
        return "Consulta{" + "consulta=" + consulta + '}';
    }    
        
}
