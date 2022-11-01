package com.example.stopwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //PAG 162

    //CASO 1. Vamos a crear un simple cronometro
        //usaremos un int para hacer seguimientos de los segundos que han pasado
        //usaremos un boolean to verficar la accion.
        //CICLO DE VIDA
        //ACTIVITY LAUNCHED
            //1- onCreate();
                //ACTIVITY RUNNING
                    //2- onDestroy();
                        //ACTIVITY DESTROYED
    //CASO 2. Problema: Cuando el usario gira la pantalla android lo detecta y por defecto destruye todas
        //las variables que estaban creadas y por consecuencia afectara a nuestros metodos;
        //1.Opcion - Decir a Android que no haga un restart en la actividad cuando haya un cambio en
            //configuracion. (No recomendable) xq Android cuando recrea la actividad se asegura que
            //escoge los recursos correctos para la nueva configuracion.
            //Esto simplemente es anyadir en el Manifest.xml android:configChanges="orientation|screenSize"
            //Se puede iplementar este metodo si se desea efectuar cambios.
            //public void onConfigurationChanged(Configuration config) {}
        //2.Opcion - Decir a Android que guarde el estado de la actividad.
    //CASO 3. Que pasaria si cuando estamos utilizando nuestra app nos interrupen con un llamada por
            //ejemplo, es entonces donde tendriamos que hacer algo para que nuestra app se quede
            //pausada, mejor dicho visible para que despues siga su anterior estado.
            //TENEMOS QUE UTILIZAR 3 CICLOS PARA LA VISIBILIDAD
                //onStart() -- se llama cuando la actividad es visible.
                //onStop() --  se llama cuando la actividad no es visible por otra app que esta en
                            //en frente de nuestra app. Simpre es llamada antes que la actividad va a ser
                            //destruida, ojo el metodo (onSaveInstanceState()) es llamado antes del onStop();
                //onRestart() -- se llama despues de que nuestra actividad ha sido escondida por otra
                            //actividad, siempre antes de ser visible para el usuario.
            //RESUMEN DEL CICLO DE VIDA (VISIBILIDAD)
    //El onPause() SUELE ESTAR LIGADO CON onStart().
              //*ACTIVITY LAUCHED
                //1- OnCreate()
                    //2- onStart()  -- primera vez
                         //*ACTIVITY RUNNING
                            //3- onStop()  -- no visible cuando se llama
                            //4- onRestart() -- cuando sea visible otra vez sera llamado seguido de onStart()
                               //5-onDestroy
                                //*ACTIVITY DESTROYED
    //CASO 4. Que pasaria si queremos dividir nuestra pantalla donde pondremos nuestra app 1 arriba
            //y nuestra app 2 debajo.
            //Esto quiere decir cuando nuestra actividad no esta siendo el principal focus la otra actividad
            //cedera y sera autamicamente pausada pero estara visible.
            //TENEMOS 2 CICLOS DE VIDA
                //1- onPause() -- sera llamado cuando otra actividad es visible pero otra actividad tiene el focus.
                //2- onResume() -- sera llamado inmediatamente antes de que nuestra actividad este apunto de interactuar con el usuario.

            //RESUMEN DEL CLICO DE VIDA
    //El onPause() SUELE ESTAR LIGADO CON onResume().
             //*ACTIVITY LAUCHED
              // 1. OnCreate()
                //onStart()
                  //2. TODO onResume() -- aparece cuando la actividad esta en el foreground y es el focus.
                    //4.*ACTIVITY RUNNING
                         //3. TODO onPause() -- aparece cuando la actividad esta en el foreground y deja de ser el focus.
                            //5. onStop()
                                //6. OnRestart() -- cuando sea visible sera llamado seguido de onStart() y onResume()
                                    //onDestroy()
                                      //7.*ACTIVITY DESTROYED


    //En mi caso escogeremos la segunda opcion que lo haremos.

    //**************************************************************//

    private int numSegundos = 0;
    private Boolean trabajando = false;
    private Boolean estabaTrabajando = false;

    //PARA FOREGROUND

    @Override
    protected void onPause() {
        super.onPause();
        estabaTrabajando = trabajando;
        trabajando = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (estabaTrabajando){
            trabajando = true;
        }
    }

    //PARA VISIBILIDAD

    @Override
    protected void onStop() {
        super.onStop();
        //Cuando la app este en segundo plano (no visible)
        // Si no ponemos esta linea el estado anterior (antes de la interrupcion)
        // de la app no se guardara y no se reinicira.
        estabaTrabajando = trabajando;
        trabajando = false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        //Si estaba trabajando pues comienza el funcionamiento de nuevo.
        if (estabaTrabajando){
            trabajando = true;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    //PARA EL ALAMACENAMIENTO DE NUESTROS ATRIBUTOS
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Almacenamos nuestras variables cuando cambiamos alguna configuracion asi Android
        // no destruye nuestros atributos.
        outState.putInt("segundos", numSegundos);
        outState.putBoolean("running", trabajando);
        outState.putBoolean("wasRunning",estabaTrabajando);
    }

    //PARA EL ESTADO DE NUESTRA APP
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Comenzamos la actividad
        if (savedInstanceState != null){
            //Cuando la actividad ya esta comenzada quiero que me guardes las instancias que ya estaban
            //creadas con anterioridad.
            numSegundos = savedInstanceState.getInt("segundos");
            trabajando = savedInstanceState.getBoolean("running");
            estabaTrabajando = savedInstanceState.getBoolean("wasRunning");
        }
        runTimer();
        final Button btnStart = findViewById(R.id.btnStart);
        start(btnStart);
        final Button btnStop = findViewById(R.id.btnStop);
        stop(btnStop);
        final Button btnReset = findViewById(R.id.btnReset);
        reset(btnReset);
    }

    public void start( Button btnStart){
        btnStart.setOnClickListener(view -> {
            trabajando = true;
        });
    }

    public void stop(Button btnStop){
        btnStop.setOnClickListener(view -> {
            estabaTrabajando = trabajando; //Almacena el estado actual
            trabajando = false;
        });
    }

    public void reset(Button btnReset){
        btnReset.setOnClickListener(view -> {
            numSegundos = 0;
            estabaTrabajando = false;
            trabajando = false;
        });
    }

    //Metodo dinamico para crear segundos, minutos y horas y tambien utilizando el handler
    //para controlar los estados de la funcion.
    public void runTimer(){
        final TextView timeView =findViewById(R.id.tvTiempoMarcado);
        final Handler handler = new Handler();
        //El Handler es una clase donde puede manipular el estado nuestra actividad.
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Las variables van estar en globado con los numeros de segundos
                int horas = numSegundos/3600;
                int minutos = (numSegundos%3600)/60; // te da 0 cuando llege 60 minutos
                int segundos = numSegundos%60; // te da 0 cuando llege a 60 segundos
                String tiempo = String.format(Locale.FRENCH,"%d:%02d:%02d", horas, minutos, segundos);
                timeView.setText(tiempo);
                if (trabajando){
                    numSegundos++;
                }
                handler.postDelayed( this,1000);
            }
        });

    }




}