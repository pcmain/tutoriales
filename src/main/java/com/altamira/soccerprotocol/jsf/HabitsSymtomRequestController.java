package com.altamira.soccerprotocol.jsf;

import com.altamira.soccerprotocol.HabitsSymtomRequest;
import com.altamira.soccerprotocol.jsf.util.JsfUtil;
import com.altamira.soccerprotocol.jsf.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;

@Named("habitsSymtomRequestController")
@SessionScoped
public class HabitsSymtomRequestController implements Serializable {

       private WebTarget webTarget;
    private Client client;
    private static final String URL_BASE ="http://ec2-18-215-224-35.compute-1.amazonaws.com:3723/SymtomsPatient-0.0.1-SNAPSHOT" ;
    private List<HabitsSymtomRequest> items = null;
    private HabitsSymtomRequest selected;

    public HabitsSymtomRequestController() {
        client = ClientBuilder.newClient();
        webTarget = client.target(URL_BASE).path("habitssymtom");
    }

    public HabitsSymtomRequest getSelected() {
        return selected;
    }

    public void setSelected(HabitsSymtomRequest selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

  

    public HabitsSymtomRequest prepareCreate() {
        selected = new HabitsSymtomRequest();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("HabitsSymtomRequestCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("HabitsSymtomRequestUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("HabitsSymtomRequestDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<HabitsSymtomRequest> getItems() {
        if (items == null) {
            items = findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    create(selected);
                } else {
                    remove(selected);
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

    public HabitsSymtomRequest getHabitsSymtomRequest(java.lang.Long id) {
        return findById(id);
    }

    public List<HabitsSymtomRequest> getItemsAvailableSelectMany() {
        return findAll();
    }

    public List<HabitsSymtomRequest> getItemsAvailableSelectOne() {
        return findAll();
    }

    @FacesConverter(forClass = HabitsSymtomRequest.class)
    public static class HabitsSymtomRequestControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            HabitsSymtomRequestController controller = (HabitsSymtomRequestController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "habitsSymtomRequestController");
            return controller.getHabitsSymtomRequest(getKey(value));
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
            if (object instanceof HabitsSymtomRequest) {
                HabitsSymtomRequest o = (HabitsSymtomRequest) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), HabitsSymtomRequest.class.getName()});
                return null;
            }
        }

    }
    
       public List<HabitsSymtomRequest> findAll() {  
        WebTarget resource = webTarget;     
        String get = resource.request(MediaType.APPLICATION_JSON).get(String.class);
        return castToList(get);       
    }

 

    public HabitsSymtomRequest findById(Long id) {  
        WebTarget resource = webTarget;
        resource.path(MessageFormat.format("{0}", new Object[]{id}));
        String get = resource.request(MediaType.APPLICATION_JSON).get(String.class);
        return new HabitsSymtomRequest(get);
        
    }
    
    public List<HabitsSymtomRequest> castToList(String jsonArrayString) {
       JSONArray  JsonArrayList = new JSONArray(jsonArrayString);
       List<HabitsSymtomRequest> list = new ArrayList<>();
       for(int i=0;i<JsonArrayList.length();i++){
           HabitsSymtomRequest btr =  new HabitsSymtomRequest(JsonArrayList.get(i).toString());
           list.add(btr);
       }
       return list;
    }
    
          private void remove (HabitsSymtomRequest entity){
        String id = String.valueOf(entity.getId())+"";
         WebTarget resource = webTarget;
        resource.path(MessageFormat.format("{0}", new Object[]{id})).request().delete();
    }
	
	
	
	    private void create (HabitsSymtomRequest entity) throws Exception{
        Response response = webTarget.request(MediaType.APPLICATION_JSON).
                post(Entity.entity(entity, MediaType.APPLICATION_JSON),Response.class);
        
        if(response.getStatus() != Response.Status.OK.getStatusCode()){
            throw new Exception("unabled to created entity " + response.getStatus());
        }      
    }

}