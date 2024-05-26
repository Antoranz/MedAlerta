package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.example.myapplication.data.Paciente;
import com.example.myapplication.utils.Controller;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

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
           }else if(!Objects.equals(Controller.getInstance().obtenerEmailRepetido(emailText.getText().toString()), "")){
               makeTextToast("Email ya registrado");
           }else if(!esDniValido(dniText.getText().toString())){
               makeTextToast("DNI inválido");
           } else if(!Objects.equals(Controller.getInstance().obtenerDniRepetido(dniText.getText().toString()), "")){
               makeTextToast("Ese DNI ya está registrado");
           }
           else if(phoneText.getText().toString().length() != 9){
               makeTextToast("El teléfono debe tener 9 dígitos");
           }
           else if(nameText.getText().toString().isEmpty() || surnameText.getText().toString().isEmpty() ||
                   domicilioText.getText().toString().isEmpty() || postalAddressText.getText().toString().isEmpty()){
               makeTextToast("Todos los campos son obligatorios");
           }
           else{
               String fechaString = editTextDate.getText().toString();
               DateFormat formatoSalida = new SimpleDateFormat("yyyy-MM-dd");
               Date fecha = null;
               Date fechaActual = new Date();
               try {
                   fecha = formatoSalida.parse(fechaString);
                   if(fecha.after(fechaActual)){
                       makeTextToast("La fecha de nacimiento no puede ser posterior a la fecha actual");
                       return;
                   }
               } catch (ParseException e) {
                   throw new RuntimeException(e);
               }
               Paciente paciente = new Paciente(dniText.getText().toString(),
                       emailText.getText().toString(),nameText.getText().toString(), surnameText.getText().toString(),
                       postalAddressText.getText().toString(), fecha,
                       domicilioText.getText().toString(), phoneText.getText().toString(), passwordText.getText().toString()
               );
               Controller.getInstance().registrarPaciente(paciente, this);
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

        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {

                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay);


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String selectedDateFormatted = sdf.format(selectedDateCalendar.getTime());


                    editTextDate.setText(selectedDateFormatted);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

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
