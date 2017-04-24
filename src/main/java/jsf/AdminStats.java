package jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import java.util.Date;

@ManagedBean
@SessionScoped
public class AdminStats {
    private Date date;
    private String page = "admin/admin-stats";

    public Date getDate() {
        return (date != null) ? date : (date = new Date());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String theDayBefore(){
        this.date.setDate(date.getDate() - 1);
        return page;
    }

    public String theDayAfter(){
        this.date.setDate(date.getDate() + 1);
        return page;
    }
}