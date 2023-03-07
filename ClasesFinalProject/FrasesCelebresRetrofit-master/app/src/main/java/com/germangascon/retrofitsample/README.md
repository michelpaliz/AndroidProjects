# PROYECTO ANDROID

## Introduccion
En primer lugar antes que todo el proyecto tiene una base de datos que ya estaba creada, utilizamos
springboot como servidor donde recibiremos peticiones de android para que el servidor las responda.
<br>
### Pasos que he tomado para realizar mi proyecto
He creado varias actividades, concretamente tengo 3 actividades para lo que es registro y el login del usuario
He creado varios fragmentos, para poder utilizarlos a la hora de poder anyadir o modificar cada lista.
He creado un fragmento para la pantalla de preferencias para poder cambiar el puerto y el ip, no muestro los datos del usuario
por temas de seguridad, de todas maneras esta comentado en el codigo.
Respecto al adaptador he creado solamente uno porque he pensado en reutilizarlo para las demas listas

-He hecho uso de Volley para el registro y el login en mis usuarios.
En la clase de SingInActivity.class se encuentra la url de mi usuario servicios.

```java

String url = "http://192.168.9.127:9080/api/v1/user/login";


```

### Recomendaciones para utilizar mi proyecto
Registrar un nuevo usuario (administrador/usuario)
Logearse con el nuevo usuario
Comprobar que el usuario se ha guardado en nuestras preferencias automaticamente.
Hacer consultas como usuario o adminitrador
Anyadir o modifcar si el usuario es administrador
En el caso de modificar he hecho el mismo uso con el de anyadir, en este caso lo que cambia seria la respuesta del servidor y la peticion del cliente.

### Problemas encontrados
<p>A la hora de intentar anyadir la notificacion no he podido implementarlo, 
A la hora de implementar la clase PendingIntent tengo problemas con los flags, lo podria hacer un boton con la notificacion diaria.</p>

### Configuracion

Spring configuration
-spring.jpa.database=MYSQL
Nombre base de datos: proyecto
Nombre del usuario: root
Password: 539012
puerto tomcat: 9080
puerto: 3306


-Por defecto no tengo creado ningun usuario necesitamos primero regristrar un usuario

```java

PendingIntent pendingIntent = PendingIntent.getBroadcast(this,  234324243, intent, PendingIntent.FLAG_IMMUTABLE);


```

