package co.com.controladores;

import co.com.entidades.Venta;
import co.com.controladores.util.JsfUtil;
import co.com.controladores.util.JsfUtil.PersistAction;
import co.com.entidades.LineaFactura;
import co.com.entidades.Producto;
import co.com.entidades.Usuario;
import co.com.facades.ProductoFacade;
import co.com.facades.VentaFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("ventaController")
@SessionScoped
public class VentaController implements Serializable {

    @EJB
    private co.com.facades.VentaFacade ejbFacade;
    
    @EJB
    private co.com.facades.ProductoFacade productoFacade;
    
    @EJB
    private co.com.facades.UsuarioFacade usuarioFacade;
    
    private List<Venta> items = null;
    private List<Producto> productos = null;
    private List<Usuario> usuarios = null;

    private Venta selected;
    private Integer cantidadSeleccionada;
    private Long productoSeleccionado;
    private Long usuarioSeleccionado;

    @PostConstruct
    public void PostConstruct (){
        productos = productoFacade.findAll();
        usuarios = usuarioFacade.findAll();
    }
    
    
    public VentaController() {
    }

    public Venta getSelected() {
        return selected;
    }

    public void setSelected(Venta selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private VentaFacade getFacade() {
        return ejbFacade;
    }

    public Venta prepareCreate() {
        selected = new Venta();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VentaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VentaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VentaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Venta> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            selected.setUsuario(buscarUsuario(usuarioSeleccionado));
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Venta getVenta(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Venta> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Venta> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Venta.class)
    public static class VentaControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VentaController controller = (VentaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ventaController");
            return controller.getVenta(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Venta) {
                Venta o = (Venta) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Venta.class.getName()});
                return null;
            }
        }

    }

    public VentaFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(VentaFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public ProductoFacade getProductoFacade() {
        return productoFacade;
    }

    public void setProductoFacade(ProductoFacade productoFacade) {
        this.productoFacade = productoFacade;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Integer getCantidadSeleccionada() {
        return cantidadSeleccionada;
    }

    public void setCantidadSeleccionada(Integer cantidadSeleccionada) {
        this.cantidadSeleccionada = cantidadSeleccionada;
    }

    public Long getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Long productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public Long getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Long usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }
    
    public Producto buscarProducto(Long id) {
        for (Producto p : productos) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    public Usuario buscarUsuario(Long id) {
        for (Usuario p : usuarios) {
            if(p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }
    
    public void agregarProducto() {
        selected.setLineaFactura(selected.getLineaFactura() != null ? selected.getLineaFactura() : new ArrayList<>());
        LineaFactura lf =  new LineaFactura();
        lf.setProducto(buscarProducto(productoSeleccionado));
        lf.setCantidad(cantidadSeleccionada);
        selected.getLineaFactura().add(lf);
    }
    
    public void eliminarProducto(LineaFactura lf) {
        selected.getLineaFactura().remove(lf);
    }

}
