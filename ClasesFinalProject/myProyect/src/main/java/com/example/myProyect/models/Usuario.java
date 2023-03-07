package com.example.myProyect.models;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "usuario")
public class Usuario {
    private final static String TAG = "Usuario";
    private static final String tabla = "usuario";
    /**
     * Campos de la tabla
     */
    @Id
    private int id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String gender;
    private String tipoUsuario;
    private String session;
    private String ip;
    private Timestamp ultimaConexion;

    private Date created_at;
    @Transient
    //Para que nos evite un error ya que esto estara ha cero
    private Date updated_at;


    public Usuario() {
        init();
    }

    public void init() {
        id = 0;
        nombre = null;
        apellido = null;
        email = null;
        password = null;
        gender = null;
        tipoUsuario = null;
        session = null;
    }

    public int getId() {
        return id;
    }

    /**
     * Hacemos el método privado ya que no permitimos modificar el id desde fuera
     * al ser de tipo autoincrement
     */
    private void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipo) {
        if (tipo.equals(TipoUsuario.USER) || tipo.equals(TipoUsuario.ADMIN)) {
            this.tipoUsuario = tipo;
        } else {
            this.tipoUsuario = TipoUsuario.USER;
        }
    }

    public boolean isAdmin() {
        return this.tipoUsuario.equals(TipoUsuario.ADMIN);
    }

    public String getSession() {
        return session;
    }

    /**
     * La session se asigna mediante login
     */
    private void setSession(String session) {
        this.session = session;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getUltimaConexion() {
        return this.ultimaConexion;
    }

    private void setUltimaConexion(Timestamp t) {
        this.ultimaConexion = t;
    }

    public boolean validate() {
        return nombre != null && email != null && password != null;
    }

    public String generateAuthToken() {
        return UUID.randomUUID().toString().toLowerCase() + id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Timestamp getCurrentTimestamp() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    public static class Gender {
        public final static String MASCULINO = "M";
        public final static String FEMENINO = "F";
    }

    public static class TipoUsuario {
        public final static String USER = "U";
        public final static String ADMIN = "A";
    }
}