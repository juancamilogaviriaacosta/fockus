/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.facades;

import co.com.entidades.LineaFactura;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author juan
 */
@Stateless
public class LineaFacturaFacade extends AbstractFacade<LineaFactura> {

    @PersistenceContext(unitName = "fockusPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LineaFacturaFacade() {
        super(LineaFactura.class);
    }
    
}
