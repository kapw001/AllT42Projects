package rs.com.dashboardgems;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import rs.com.dashboardgems.models.MyItem;

/**
 * Created by yasar on 17/11/17.
 */

public class MyCustomC extends DefaultClusterRenderer<MyItem> {

    public MyCustomC(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
    }
}
