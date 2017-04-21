package components;

import javax.el.ELContext;
import javax.el.MethodExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.util.Map;


@FacesComponent("groupkmp.components.QuestionView")
public class deregisterComponent extends UINamingContainer {
/*    private int value = 2;

    public int getValue() {
        FacesContext context = getFacesContext();
        ELContext elContext = context.getELContext();
        UIComponent uiComp = this.getNamingContainer();
        Map<String, Object> ptAttrs = uiComp.getPassThroughAttributes();
        Map<String, Object> attrs = uiComp.getAttributes();
        return value;
    }

    public void setValue(int value) {

        this.value = value;

        //select();
        Object[] params = {getValue()};
        execEL("select", params);
    }

    public Object select() {
        MethodExpression expression = (MethodExpression) getAttributes().get("select");

        ELContext elContext = getFacesContext().getELContext();
        Object[] params = {getValue()};

        return expression.invoke(elContext, params);


    }

    public Object execEL(String method, Object[] params) {
        MethodExpression expression = (MethodExpression) getAttributes().get(method);
        ELContext elContext = getFacesContext().getELContext();
        return expression.invoke(elContext, params);
    }*/

}
