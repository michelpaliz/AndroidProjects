package com.example.messenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateMessageActivity extends Activity {

    private Button btnEnviarMensaje;
    private EditText etMessage;

    //PRIMERA PARTE
    //Lo que tenemos que hacer es que cuando el usuario clickee en el boton envie la peticion a la
    //segunda actividad.
    //SEGUNDA PARTE
        //1- Crearemos un intent que especificara una accion.
        //2- Permiteremos que el usuario escoga la app para hacerlo.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnEnviarMensaje = findViewById(R.id.btnSend);
        btnEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CREAMOS EL INTENT
                Intent intent = new Intent(getApplicationContext(),ReceiveMessageActivity.class);
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                //Android creara un createChoose por el intent que soporte text/plain data.
                Intent chosenIntent = Intent.createChooser(intent1,"send message");

                etMessage = findViewById(R.id.etMessage);
                String message = etMessage.getText().toString();
                //SETEAMOS EL INTENT
                //El key de putExtra tienen que ser unicos
                intent1.putExtra(Intent.EXTRA_TEXT, message);
                intent1.setType("text/plain");// si no pongo esta linea no funcionara el intent1
                //Este intent es explicito es decir indicamos a Android cual clase queremos empezar.
                intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE,message);
                startActivity(chosenIntent);



            }
        });

    }
}
