/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.testlogin.facade;

import edu.testlogin.entity.Usuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Usuario
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> implements UsuarioFacadeLocal {

    @PersistenceContext(unitName = "TestLoginPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }

    public Usuario validarUsuario(String correo, String clave) {
        try {
            Query q = em.createQuery("SELECT u FROM Usuario u WHERE u.clave = :clave AND u.correo = :correo");
            q.setParameter("clave", clave);
            q.setParameter("correo", correo);
            return   (Usuario) q.getSingleResult();
        } catch (Exception e) {
            return new Usuario();
        }
    }

}
