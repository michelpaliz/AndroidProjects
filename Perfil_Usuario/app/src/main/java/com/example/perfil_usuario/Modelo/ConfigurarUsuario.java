package com.example.perfil_usuario.Modelo;

public class ConfigurarUsuario {

    private final Usuario usuario;
    private String mensaje;

    public ConfigurarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public boolean cambioContrasenya(String nuevaContrasenya){
        if (usuario.getContrasenya().equalsIgnoreCase(nuevaContrasenya)){
            mensaje = "La contrasenya se ha actualizado exitosamente";
            return true;
        }else{
            mensaje = "Las contrasenyas no coinciden";
        }
        return false;

    }

    public void setNuevaContrasenya(String contrasenya){
        usuario.setContrasenya(contrasenya);
    }


}
