
package cashkaro.com.firemustlist.dashboard.model;

import java.io.Serializable;
import java.util.Date;

public class DataSetUpdate implements Serializable {

    private Date date;

    public Date getDate() {
        return date;
    }

    public DataSetUpdate(Date date) {
        this.date = date;
    }

    public void setDate(Date date) {

        this.date = date;
    }
}
