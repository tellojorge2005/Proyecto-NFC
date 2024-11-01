package com.nfcproject;

public class Alumno {
    private int id;
    private String matricula;
    private String nombre;
    private String apellidos;
    private String gradoAcademico;
    private String semestre;
    private String correo;
    private String password;
    private String numero;
    private String fotografia;
    private String clase;
    private String UID;

    public  Alumno(int id, String matricula, String nombre, String apellidos,
                   String gradoAcademico, String semestre, String correo, String password,
                   String numero, String fotografia, String clase, String UID){
        this.id = id;
        this.matricula = matricula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.gradoAcademico = gradoAcademico;
        this.semestre = semestre;
        this.correo = correo;
        this.password = password;
        this.numero = numero;
        this.fotografia = fotografia;
        this.clase = clase;
        this.UID = UID;
    }
    public String toString(){
        return "ID: "+id+" Matricula: "+matricula+" Nombre: "+nombre+" Apellidos: "
                +apellidos+ " Grado: "+gradoAcademico+" Semestre: "+semestre+" Correo: "+correo
                +" Contraseña: "+password+" Numero: "+numero+" Fotografía: "+fotografia
                +" Clase: "+clase+" UID: "+UID;
    }
    public String getUID(){
        return  UID;
    }
}
