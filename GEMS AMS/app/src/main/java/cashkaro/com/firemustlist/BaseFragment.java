package cashkaro.com.firemustlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.List;

import cashkaro.com.firemustlist.model.PersonData;

/**
 * Created by yasar on 18/8/17.
 */

public abstract class BaseFragment extends Fragment {

    public PassListInterface passListInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        passListInterface = (PassListInterface) activity;
    }

    public abstract void loadData(List<PersonData> list);
}
