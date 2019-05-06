package com.example.wilson.login;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    EditText txtCor, txtPas;
    Button btnIngresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txtCor = (EditText) findViewById(R.id.txtCor);
        txtPas = (EditText) findViewById(R.id.txtPas);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                Thread tr= new Thread(){
                    @Override
                    public void run() {
                         final String res= enviarPost(txtCor.getText().toString(),txtPas.getText().toString());
                        //método para trabajar con la interfaz gráfica dentro del hilo
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int r= objJSON(res);
                                if (r>0){
                                    Intent i = new Intent(getApplicationContext(),Principal.class);
                                    startActivity(i);
                                } else {
                                Toast.makeText(getApplicationContext(),"Usuario o Pas incorrecto",Toast.LENGTH_SHORT).show();
                                }
                                }

                        });
                    }
                };

                tr.start();
                }
            });

    }

    public String enviarPost(String cor, String pas) {
        String parametros = "cor=" + cor + "&pas=" + pas;
        HttpURLConnection connection = null;
        String respuesta = "";
        try {

                //StrictMode.ThreadPolicy policy = new
                        //StrictMode.ThreadPolicy.Builder().permitAll().build();
                //StrictMode.setThreadPolicy(policy);
            URL url = new URL("https://inventario.electrosistemas.com.co/inventario/valida.php");
            //local
            //URL url = new URL("http://192.168.0.20/formando_codigo_web_service_php_mysql/valida.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length", "" + Integer.toString(parametros.getBytes().length));
            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();
            Scanner inStream = new Scanner(connection.getInputStream());

            while (inStream.hasNextLine()) {
                respuesta += (inStream.nextLine());


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }

    public int objJSON(String resp) {
        int res = 0;
        try {
            JSONArray json = new JSONArray(resp);
            if (json.length() > 0) {
                res = 1;
            }
        } catch (Exception e) {
        }
        return res;
    }
}

