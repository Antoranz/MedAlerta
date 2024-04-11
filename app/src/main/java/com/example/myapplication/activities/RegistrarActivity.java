package com.example.myapplication.activities;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.example.myapplication.utils.async.PostDataAsync.postDataAsync;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.myapplication.R;
import com.example.myapplication.utils.async.PostDataAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegistrarActivity extends AppCompatActivity  {

    private EditText emailText,passwordText,repitPasswordText,nameText,surnameText,editTextDate,domicilioText,postalAddressText,phoneText,dniText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registrar);

        initIgui();
    }

    private void initIgui() {
        emailText = findViewById(R.id.idEmail);
        passwordText = findViewById(R.id.idPassword);
        repitPasswordText = findViewById(R.id.idRepitPassword);
        nameText = findViewById(R.id.idName);
        surnameText = findViewById(R.id.idSurname);
        editTextDate = findViewById(R.id.editTextDate);
        editTextDate.setOnClickListener(v -> openDatePicker());

        domicilioText = findViewById(R.id.idDomicilio);
        postalAddressText = findViewById(R.id.editTextTextPostalAddress);
        phoneText = findViewById(R.id.editTextPhone);
        dniText = findViewById(R.id.idDNI);

        registerButton = findViewById(R.id.idRegisterButton);

        registerButton.setOnClickListener(v -> {
           if(!passwordText.getText().toString().equals(repitPasswordText.getText().toString())){
               makeTextToast("Las contraseñas no coinciden");
           }else if(passwordText.getText().toString().isEmpty() || emailText.getText().toString().isEmpty()){
               makeTextToast("Ningún campo debe estar vacío");
           }else if(!emailText.getText().toString().contains("@")){
               makeTextToast("Email no válido");

           } else if(!esDniValido(dniText.getText().toString())){
               makeTextToast("DNI inválido");
           }
           else{

               Executor executor = Executors.newSingleThreadExecutor();
               String urlServidor = "http://10.0.2.2:3000/pacientes/registrarPaciente";

               JSONObject postData = new JSONObject();
               try {

                   String fechaString = editTextDate.getText().toString();
                   DateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
                   DateFormat formatoBD = new SimpleDateFormat("yyyy-MM-dd");


                   Date fecha = formatoSalida.parse(fechaString);

                   String fechaFormateadaBD = formatoBD.format(fecha);



                   postData.put("Nombre", nameText.getText().toString());
                   postData.put("Apellidos",surnameText.getText().toString());
                   postData.put("FechaDeNacimiento", fechaFormateadaBD);
                   postData.put("Direccion", domicilioText.getText().toString());
                   postData.put("CodigoPostal", postalAddressText.getText().toString());
                   postData.put("telefono", phoneText.getText().toString());
                   postData.put("email", emailText.getText().toString());
                   postData.put("DNI", dniText.getText().toString());
                   postData.put("password", passwordText.getText().toString());

               } catch (JSONException e) {
                   e.printStackTrace();
               } catch (ParseException e) {
                   throw new RuntimeException(e);
               }

               postDataAsync(urlServidor, executor, (PostDataAsync.OnTaskCompleted) result -> {
                   runOnUiThread(() -> {
                       if (result != null) {

                           Log.d(TAG, "Registro finalizado correctamente");
                           makeTextToast("Registro finalizado correctamente");
                           Intent intent = new Intent(this, IniciarSesionActivity.class);
                           startActivity(intent);

                       }else{
                           Log.d(TAG, "Error al registrar");
                           makeTextToast("Error al registrar");
                       }
                   });
               }, "POST", postData.toString());

           }
        });
    }

    public static boolean esDniValido(String dni) {

        if (dni.length() != 9) {
            return false;
        }

        String numero = dni.substring(0, 8);
        char letra = dni.charAt(8);

        try {

            int numeroInt = Integer.parseInt(numero);

            char letraEsperada = calcularLetraDni(numeroInt);

            return letra == letraEsperada;
        } catch (NumberFormatException e) {

            return false;
        }
    }

    private void openDatePicker() {
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();

        // Crear un DatePickerDialog y mostrarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Crear un objeto Calendar con la fecha seleccionada
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);

                    // Formatear la fecha seleccionada en el formato deseado
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());

                    // Actualizar el campo de texto con la fecha seleccionada en el formato deseado
                    editTextDate.setText(selectedDateFormatted);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        // Mostrar el DatePickerDialog
        datePickerDialog.show();
    }

    private static char calcularLetraDni(int numero) {

        char[] letras = "TRWAGMYFPDXBNJZSQVHLCKE".toCharArray();
        int indiceLetra = numero % 23;
        return letras[indiceLetra];
    }
    private void makeTextToast(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
    }
}
