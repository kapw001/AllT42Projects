package cashkaro.com.firemustlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cashkaro.com.firemustlist.adapter.ExpandableListAdapter;
import cashkaro.com.firemustlist.model.PersonData;
import cashkaro.com.firemustlist.model.VisitorInfo;

import static android.content.ContentValues.TAG;

/**
 * Created by yasar on 18/8/17.
 */

public class StudentFragment extends BaseFragment implements ExpandableListAdapter.OnCheckBoxClick {

    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<PersonData>> listDataChild;
    private int lastExpandedPosition = -1;

    private EditText search;

    private LinearLayout ascending, descending, selectall;
    private ImageView ascendingimg, descendingimg, selectallimg;
    private boolean isDesending = true;
    private boolean isAscending = true;
    private boolean isSelectAll = true;

    private List<PersonData> personDataList;
    private List<PersonData> studentsList;
    private List<String> headerList;

    private TextView msg;

    public static StudentFragment getInstance(List<PersonData> personDataList) {
        StudentFragment fragment = new StudentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("personDataList", (Serializable) personDataList);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.studentfraglayout, container, false);

        msg = (TextView) view.findViewById(R.id.msg);

        studentsList = new ArrayList<>();
        personDataList = new ArrayList<>();
        headerList = new ArrayList<>();
//        selectallimg = (ImageView) view.findViewById(R.id.selectallimg);
//        ascendingimg = (ImageView) view.findViewById(R.id.ascendingimg);
//        descendingimg = (ImageView) view.findViewById(R.id.descendingimg);

//        descending = (LinearLayout) view.findViewById(R.id.descending);
//        ascending = (LinearLayout) view.findViewById(R.id.ascending);
//        selectall = (LinearLayout) view.findViewById(R.id.selectall);
//
//
//        descending.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isDesending) {
//                    descendingimg.setImageResource(R.drawable.ic_descending_disabled);
//                    isDesending = false;
//                } else {
//                    descendingimg.setImageResource(R.drawable.ic_descending_enabled);
//                    isDesending = true;
//                }
//
//            }
//        });
//
//        ascending.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isAscending) {
//                    ascendingimg.setImageResource(R.drawable.ic_ascending_disabled);
//                    isAscending = false;
//                } else {
//                    ascendingimg.setImageResource(R.drawable.ic_ascending_enabled);
//                    isAscending = true;
//                }
//
//            }
//        });
//
//        selectall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isSelectAll) {
//                    selectallimg.setImageResource(R.drawable.ic_check);
//                    isSelectAll = false;
//                } else {
//                    selectallimg.setImageResource(R.drawable.ic_uncheck);
//                    isSelectAll = true;
//                }
//                selectUnselectAll(!isSelectAll);
//            }
//        });
//
//        search = (EditText) view.findViewById(R.id.search);
//
//        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    Toast.makeText(getActivity(), "Search", Toast.LENGTH_SHORT).show();
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        });

        // get the listview
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
//        prepareListData();
//
//        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
//
//        // setting list adapter
//        expListView.setAdapter(listAdapter);

//        expListView.setChildDivider(getResources().getDrawable(R.color.background));
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
//        DisplayMetrics metrics = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int width = metrics.widthPixels;
//        expListView.setIndicatorBounds(width - GetPixelFromDips(90), width - GetPixelFromDips(10));
////
//
        expListView.setIndicatorBounds(345, 375);

        personDataList = (List<PersonData>) getArguments().getSerializable("personDataList");

        loadData(personDataList);

        return view;
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<PersonData>>();

        // Adding child data
//        listDataHeader.add("Class A");
//        listDataHeader.add("Class B");
//        listDataHeader.add("Class C");

//        listDataChild.put(listDataHeader.get(0), Utils.getStudent()); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), Utils.getStudent());
//        listDataChild.put(listDataHeader.get(2), Utils.getStudent());
//        listDataChild.put(listDataHeader.get(3), new ArrayList<VisitorInfo>());
    }

    public void loadData(List<PersonData> list) {

//        list.addAll(Utils.getPersonList());

        if (list.size() > 0) {


            studentsList.clear();
            headerList.clear();

            for (int i = 0; i < list.size(); i++) {
                PersonData personData = list.get(i);

                if (personData.getType().toLowerCase().toString().equalsIgnoreCase("Student".toLowerCase().toString())) {
                    studentsList.add(personData);
                    Log.e(TAG, "loadData: " + personData.getType().toLowerCase().toString());
                }
            }


            Collections.sort(studentsList, new Comparator<PersonData>() {

                @Override
                public int compare(PersonData o1, PersonData o2) {
                    return o1.getStudClass().compareTo(o2.getStudClass());
                }
            });

            String stud_class = "";

            for (int i = 0; i < studentsList.size(); i++) {
                PersonData personData = studentsList.get(i);
                if (!personData.getStudClass().toLowerCase().equalsIgnoreCase(stud_class.toLowerCase())) {
                    stud_class = personData.getStudClass();
                    headerList.add(personData.getStudClass());
                }

            }


            for (int i = 0; i < headerList.size(); i++) {
                Log.e(TAG, "loadData: Header Name   " + headerList.get(i));
            }


            List<List<PersonData>> li = new ArrayList<>();

            for (int i = 0; i < headerList.size(); i++) {
                String headerName = headerList.get(i);

                List<PersonData> l = new ArrayList<>();
                for (int j = 0; j < studentsList.size(); j++) {
                    PersonData personData = studentsList.get(j);

                    if (headerName.toLowerCase().equalsIgnoreCase(personData.getStudClass().toLowerCase())) {
                        l.add(personData);
                    }


                }

                li.add(l);
            }


            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<PersonData>>();

            // Adding child data
            listDataHeader.addAll(headerList);

            for (int i = 0; i < headerList.size(); i++) {
                listDataChild.put(headerList.get(i), li.get(i));
            }


            listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
            expListView.setAdapter(listAdapter);
            msg.setVisibility(View.GONE);
        } else {

            msg.setVisibility(View.VISIBLE);

//            Toast.makeText(getActivity(), "There is no records", Toast.LENGTH_SHORT).show();
        }

    }

    private void selectUnselectAll(boolean isSelect) {


        for (int i = 0; i < studentsList.size(); i++) {
            PersonData personData = studentsList.get(i);

            studentsList.get(i).setIsSafe(isSelect);


        }
//        listDataHeader = new ArrayList<String>();
//        listDataChild = new HashMap<String, List<PersonData>>();
//
//        // Adding child data
//        listDataHeader.add("Class A");
//        listDataChild.put(listDataHeader.get(0), studentsList);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

    }

    @Override
    public void OnItemClick(int pos,PersonData personData) {

        passListInterface.addUpdateList(personData);
    }
}
