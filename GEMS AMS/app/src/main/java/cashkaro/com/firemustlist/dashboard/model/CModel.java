package cashkaro.com.firemustlist.dashboard.model;

import java.util.List;

/**
 * Created by yasar on 11/9/17.
 */

public class CModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private List<DataSet> list;
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DataSet> getList() {
        return list;
    }

    public void setList(List<DataSet> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
