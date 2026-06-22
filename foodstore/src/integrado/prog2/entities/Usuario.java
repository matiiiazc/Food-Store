package integrado.prog2.entities;

import integrado.prog2.enums.Rol;

public class Usuario extends Base {
    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasenia;
    private Rol rol;

    public Usuario(Long id, String nombre, String apellido, String mail,
                   String celular, String contrasenia, Rol rol) {
        super(id);
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasenia = contrasenia;
        this.rol = rol;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getMail() { return mail; }
    public void setMail(String mail) { this.mail = mail; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    @Override
    public String toString() {
        return String.format("[ID: %d] %s %s | Mail: %s | Cel: %s | Rol: %s",
                getId(), nombre, apellido, mail, celular, rol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        Usuario u = (Usuario) o;
        return getId().equals(u.getId());
    }
}
