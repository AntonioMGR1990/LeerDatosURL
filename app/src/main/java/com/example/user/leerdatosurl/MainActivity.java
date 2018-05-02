package com.example.user.leerdatosurl;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComunicacionTask com = new ComunicacionTask();
                com.execute("http://www.omie.es/datosPub/marginalpdbc/marginalpdbc_20180502.1");
                Toast.makeText(MainActivity.this, "conexion ok", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class ComunicacionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cadenaJson = "";
            try {
                URL url = new URL(params[0]);
                URLConnection con = url.openConnection();
                //recuperacion de la respuesta JSON
                String s;
                InputStream is = con.getInputStream();
                //utilizamos UTF-8 para que interprete
                //correctamente las ñ y acentos
                BufferedReader bf = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((s = bf.readLine()) != null) {
                    cadenaJson += s;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return cadenaJson;
        }

        @Override
        protected void onPostExecute(String result) {
            String[] datosCiudad = null;
            try {
                //creamos un array JSON a partir de la cadena recibida
                JSONArray jarray = new JSONArray(result);
                //creamos el array de String con el tamaño
                //del array JSON
                datosCiudad = new String[jarray.length()];
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject job = jarray.getJSONObject(i);
                    //por cada objeto JSON, creamos una cadena
                    //con la propiedad año y la suma de
                    //habitantes de cada zona,el resultado lo
                    //añadimos al array
                    int habitantes = job.getInt("Centro") + job.getInt("Norte") + job.getInt("Urbanizaciones");
                    datosCiudad[i] = job.getString("Año") + " - " + habitantes;
                }

            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
    }
}
