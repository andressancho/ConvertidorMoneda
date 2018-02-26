package convertidor.andressancho.com.convertidormoneda;

/**
 * Created by Usuario on 24/02/2018.
 */

public class Convertidor {

    static double  venta;
    Convertidor() {

    }

    public double convCtoD(double c){
        double d = c/venta;
        return d;
    }
    public double convDtoC(double d){
        double c= d*venta;
        return c;
    }
}
