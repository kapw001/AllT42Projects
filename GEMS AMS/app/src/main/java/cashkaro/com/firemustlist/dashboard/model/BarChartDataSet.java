package cashkaro.com.firemustlist.dashboard.model;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by yasar on 12/9/17.
 */

public class BarChartDataSet {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BarDataSet getBarDataSet() {
        return barDataSet;
    }

    public void setBarDataSet(BarDataSet barDataSet) {
        this.barDataSet = barDataSet;
    }

    private BarDataSet barDataSet;

    public LineDataSet getLineDataSet() {
        return lineDataSet;
    }

    public void setLineDataSet(LineDataSet lineDataSet) {
        this.lineDataSet = lineDataSet;
    }

    private LineDataSet lineDataSet;
}
