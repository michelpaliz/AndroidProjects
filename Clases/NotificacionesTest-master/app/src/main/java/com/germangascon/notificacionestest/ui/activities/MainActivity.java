package com.germangascon.notificacionestest.ui.activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.germangascon.notificacionestest.ui.dialogs.AlertDialogFragment;
import com.germangascon.notificacionestest.ui.dialogs.DialogoConfirmacion;
import com.germangascon.notificacionestest.ui.dialogs.DialogoFecha;
import com.germangascon.notificacionestest.ui.dialogs.DialogoHora;
import com.germangascon.notificacionestest.ui.dialogs.DialogoPersonalizado;
import com.germangascon.notificacionestest.ui.dialogs.DialogoSeleccion;
import com.germangascon.notificacionestest.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String CHANNEL_ID = "CH_01";
    public static final int ID_ALERTA_NOTIFICACION = 1;
    public static final int REQUEST_RECEIVE_SMS = 1;
    public static final int REQUEST_POST_NOTIFICATIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bAlert = findViewById(R.id.bAlert);
        Button bConfirm = findViewById(R.id.bConfirm);
        Button bSelect = findViewById(R.id.bSelect);
        Button bCustom = findViewById(R.id.bCustom);
        Button bSnackbar = findViewById(R.id.bSnackbar);
        Button bDate = findViewById(R.id.bDate);
        Button bTime = findViewById(R.id.bTime);
        Button bNotification = findViewById(R.id.bNotification);

        bAlert.setOnClickListener(this);
        bConfirm.setOnClickListener(this);
        bSelect.setOnClickListener(this);
        bCustom.setOnClickListener(this);
        bSnackbar.setOnClickListener(this);
        bDate.setOnClickListener(this);
        bTime.setOnClickListener(this);
        bNotification.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission( this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.RECEIVE_SMS},
                    REQUEST_RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission( this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( this, new String[] {Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_POST_NOTIFICATIONS);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bAlert:
                //Mediante una clase propia
                /*
                DialogoAlerta dialogoAlerta = new DialogoAlerta();
                dialogoAlerta.show(getSupportFragmentManager(), "error_dialog_alert");
                */

                //Mediante una clase propia que recibe el texto a mostrar mediante
                //argumentos a través de un Bundle.
                Bundle bundle = new Bundle();
                bundle.putString(AlertDialogFragment.TEXTO_TITULO, "Buenos días!!");
                bundle.putString(AlertDialogFragment.TEXTO_MENSAJE, "¿Cómo te encuentras hoy?");
                bundle.putString(AlertDialogFragment.TEXTO_BOTON, "Nice");
                AlertDialogFragment dialogFragment = new AlertDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getSupportFragmentManager(), "error_dialog_alert");


                //Sin DialogFrament. Esta forma no está recomendada
                //Es mucho mejor utilizar DialogFragment ya que garantiza que se aborden
                //correctamente eventos de ciclo de vida como cuando
                //el usuario presiona el botón Atrás o gira la pantalla
                //También permite reutilizar la IU del diálogo como componente integrable a una IU más grande
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hola");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                }).create().show();
                */
                break;
            case R.id.bConfirm:
                //Mediante una clase propia
                DialogoConfirmacion dialogoConfirmacion = new DialogoConfirmacion();
                dialogoConfirmacion.show(getSupportFragmentManager(), "error_dialog_confirm");
                break;
            case R.id.bSelect:
                //Mediante una clase propia
                DialogoSeleccion dialogoSeleccion = new DialogoSeleccion();
                dialogoSeleccion.show(getSupportFragmentManager(), "error_dialog_select");
                break;
            case R.id.bSnackbar:
                Snackbar.make(view, "Esto es una prueba", Snackbar.LENGTH_LONG)
                        .setAction("Acción", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.i("Snackbar", "Pulsada acción snackbar");
                            }
                        }).show();
                break;
            case R.id.bDate:
                DialogoFecha dialogoFecha = new DialogoFecha();
                dialogoFecha.show(getSupportFragmentManager(), "error_dialog_date");
                break;
            case R.id.bTime:
                DialogoHora dialogoHora = new DialogoHora();
                dialogoHora.show(getSupportFragmentManager(), "error_dialog_time");
                break;
            case R.id.bCustom:
                //Bundle bundle = new Bundle();
                bundle = new Bundle();
                bundle.putString(DialogoPersonalizado.TEXTO_NOMBRE, "John");
                bundle.putString(DialogoPersonalizado.TEXTO_APELLIDOS, "Doe");
                bundle.putString(DialogoPersonalizado.TEXTO_BOTON, "Aceptar");
                DialogoPersonalizado dialogoPersonalizado = new DialogoPersonalizado();
                dialogoPersonalizado.setArguments(bundle);
                dialogoPersonalizado.show(getSupportFragmentManager(), "error_dialog_custom");
                break;
            case R.id.bNotification:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mostrarNotificacion();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void mostrarNotificacion() {
        crearCanalNotificacion();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_warning)
                .setContentTitle("Mensaje de Alerta")
                .setContentText("Ejemplo de notificación")
                .setContentInfo("4")
                .setTicker("Alerta !!");
        //Creamos el PendingIntent
        Intent intent = new Intent (MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //Asignamos el PendingIntent que será ejecutado al pulsar sobre la notificación
        mBuilder.setContentIntent(pendingIntent);
        //Finalmente mostrarmos la notificación
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID_ALERTA_NOTIFICACION, mBuilder.build());
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = "Mi canal";
            String descripcion = "Mi canal de notificación ";
            int importancia = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, nombre, importancia);
            channel.setDescription(descripcion);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_RECEIVE_SMS:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlertDialogFragment.TEXTO_TITULO, "Atención");
                    bundle.putString(AlertDialogFragment.TEXTO_MENSAJE, "La aplicación requiere permiso para leer SMS, de lo contrario puede que no funcione de la forma esperada");
                    bundle.putString(AlertDialogFragment.TEXTO_BOTON, "Entendido");
                    AlertDialogFragment dialogFragment = new AlertDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(), "error_dialog_alert");
                }
                break;
            case REQUEST_POST_NOTIFICATIONS:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Bundle bundle = new Bundle();
                    bundle.putString(AlertDialogFragment.TEXTO_TITULO, "Atención");
                    bundle.putString(AlertDialogFragment.TEXTO_MENSAJE, "La aplicación requiere permiso para enviar notificaciones, de lo contrario puede que no funcione de la forma esperada");
                    bundle.putString(AlertDialogFragment.TEXTO_BOTON, "Entendido");
                    AlertDialogFragment dialogFragment = new AlertDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(), "error_dialog_alert");
                }
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
