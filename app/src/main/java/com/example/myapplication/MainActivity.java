package com.example.myapplication;

import static com.example.myapplication.ObtenerDoctoresAsync.obtenerDoctoresAsync;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ObtenerDoctoresAsync;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Executor executor = Executors.newSingleThreadExecutor();
        String urlServidor = "http://10.0.2.2:3000/users/obtener-doctores";

        obtenerDoctoresAsync(urlServidor, executor, (ObtenerDoctoresAsync.OnTaskCompleted) result -> {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.i("DOCTORES",jsonArray.toString());
                } catch (JSONException e) {
                    Log.e("ObtenerDoctoresTask", "Error al procesar JSON", e);
                }
            }
        });
    }

}