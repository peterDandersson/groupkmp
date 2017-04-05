package testpack;

import javax.faces.bean.ManagedBean;

/**
 * Created by Elev1 on 2017-04-05.
 */
@ManagedBean
public class TestEJB {
    private String testString;

    public String getTestString() {
        return (testString != null) ? testString : "Hello test!";
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
