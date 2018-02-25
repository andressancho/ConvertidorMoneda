package convertidor.andressancho.com.convertidormoneda;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SoapCall sp;
    Convertidor convertidor = new Convertidor();



    public void convertir(View view){

        RadioButton rbdac = findViewById(R.id.rbdac);
        TextView txt = findViewById(R.id.txt);
        EditText et = findViewById(R.id.etCantidad);

        if (rbdac.isChecked()){

            txt.setText(Double.toString(convertidor.convDtoC(Double.parseDouble(et.getText().toString()))));
        }
        else{

            txt.setText(Double.toString(convertidor.convCtoD(Double.parseDouble(et.getText().toString()))));
        }



    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=  new SoapCall();
        sp.execute();
        TextView txt = findViewById(R.id.txtVenta);
        txt.setText("Venta: "+ Double.toString( Convertidor.venta));

    }

    public  class SoapCall extends AsyncTask<String,Object,String> {

        public static final String NAMESPACE = "http://ws.sdde.bccr.fi.cr";
        public static final String METHOD_NAME  = "ObtenerIndicadoresEconomicos";
        public static final String SOAP_ACTION  = "http://ws.sdde.bccr.fi.cr/ObtenerIndicadoresEconomicos";
        public static final String URL = "http://indicadoreseconomicos.bccr.fi.cr/indicadoreseconomicos/WebServices/wsIndicadoresEconomicos.asmx";
        public int timeout=3000;
        public String response;
        double venta=0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
            request.addProperty("tcIndicador","318");
            request.addProperty("tcFechaInicio","24/02/2018");
            request.addProperty("tcFechaFinal","24/02/2018");
            request.addProperty("tcNombre","A");
            request.addProperty("tnSubNiveles","N");


            SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet=true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(URL);

            try{
                transportSE.call(SOAP_ACTION,envelope);
                SoapObject ress=(SoapObject) envelope.getResponse();
                response= ress.toString();
                response=response.split("NUM_VALOR=")[1];
                response= response.split(";")[0];




            }catch (Exception e){
                e.printStackTrace();
                Log.e("Error",e.toString());
            }
            Log.d("LOG_TAG",envelope.bodyIn.toString());


            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null){
                Convertidor.venta=Double.parseDouble(s);
            }
        }
    }


}
