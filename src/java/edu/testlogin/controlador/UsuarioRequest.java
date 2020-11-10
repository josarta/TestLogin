/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.testlogin.controlador;

import edu.testlogin.entity.Usuario;
import edu.testlogin.facade.UsuarioFacadeLocal;
import edu.testlogin.utilidad.Email;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Usuario
 */
@Named(value = "usuarioRequest")
@RequestScoped
public class UsuarioRequest implements Serializable {

    @EJB
    UsuarioFacadeLocal usuarioFacadeLocal;

    private Usuario objUsuario = new Usuario();
    private ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    private boolean nuevoActualizar;

    private String correo = "";
    private String clave = "";

    /**
     * Creates a new instance of UsuarioRequest
     */
    public UsuarioRequest() {
    }

    @PostConstruct
    public void postUsuario() {
        listaUsuarios.addAll(usuarioFacadeLocal.findAll());
    }

    public void registrarUsuario() {
        String mensaje = "Usuario con nombre : " + objUsuario.getNombres() + " " + objUsuario.getApellidos();
        try {
            usuarioFacadeLocal.create(objUsuario);
            listaUsuarios.add(objUsuario);
            mensaje += " Registrado ok";
        } catch (Exception e) {
            mensaje += " No se pudo registrar";
        }
        FacesMessage ms = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage("mensajeOk", ms);
    }

    public void recuperarClave() {

        String mensaje = "Usuario con el correo : " + correo;
        Usuario usuarioResultado = new Usuario();
        usuarioResultado = usuarioFacadeLocal.recuperarClave(correo);

        if (usuarioResultado.getNombres() == null) {
            mensaje += " NO ESTA REGISTRADO ";
        } else {
            try {

                int nuevaClave = (int) (Math.random() * 100000);
                usuarioResultado.setClave("RE-" + nuevaClave);
                usuarioFacadeLocal.edit(usuarioResultado);

                Email.sendClaves(usuarioResultado.getCorreo(),
                        usuarioResultado.getNombres() + " " + usuarioResultado.getApellidos(),
                        usuarioResultado.getCorreo(),
                        "RE-" + nuevaClave);

            } catch (Exception e) {
                System.out.println("error enviando mensaje de recuperacion --> " + e.getMessage());
            }

            mensaje += " su clave se envio al correo registrado.";

        }
        FacesMessage ms = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage(null, ms);
    }

    public void removerUsuario(Usuario ObjRemover) {
        usuarioFacadeLocal.remove(ObjRemover);
        listaUsuarios.remove(ObjRemover);
    }

    public void cargarFormulario(Usuario OjbCargar) {
        this.objUsuario = OjbCargar;
        this.nuevoActualizar = true;
    }

    public void actualizarUsuario() {
        usuarioFacadeLocal.edit(objUsuario);
        this.nuevoActualizar = false;
        listaUsuarios.clear();
        listaUsuarios.addAll(usuarioFacadeLocal.findAll());

    }

    public void validarUsuario() {

        String mensaje = "";

        Usuario usulog = usuarioFacadeLocal.validarUsuario(correo, clave);

        if (usulog.getId() == null) {
            mensaje = "Usuario no existe !!!!!";
        } else {
            mensaje = "Hola " + usulog.getNombres() + " " + usulog.getApellidos();
        }
        FacesMessage ms = new FacesMessage(mensaje);
        FacesContext.getCurrentInstance().addMessage("mensajeOk", ms);

    }

    public Usuario getObjUsuario() {
        return objUsuario;
    }

    public void setObjUsuario(Usuario objUsuario) {
        this.objUsuario = objUsuario;
    }

    public ArrayList<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ArrayList<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void setNuevoActualizar(boolean nuevoActualizar) {
        this.nuevoActualizar = nuevoActualizar;
    }

    public boolean getNuevoActualizar() {
        return this.nuevoActualizar;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

}
