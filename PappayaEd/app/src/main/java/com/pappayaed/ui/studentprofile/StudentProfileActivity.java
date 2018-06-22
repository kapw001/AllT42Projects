package com.pappayaed.ui.studentprofile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pappayaed.R;
import com.pappayaed.RecyclerViewCommon.RecyclerViewAdapterMultiView;
import com.pappayaed.common.Utils;
import com.pappayaed.ui.showprofile.StudentList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.profile_name)
    TextView profileName;
    @BindView(R.id.profile_id)
    TextView profileId;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.back)
    ImageView back;

    private RecyclerViewAdapterMultiView adapter;
    private StudentList studentList;

    private List list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        ButterKnife.bind(this);


        init();


    }


    private void init() {

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);

//        recyclerview.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));


        list = new ArrayList();
        studentList = (StudentList) getIntent().getSerializableExtra("studentlist");

        profileName.setText(studentList.getName());

        profileId.setText("Student ID " + studentList.getId());
        list.addAll(getList(studentList));

        adapter = new RecyclerViewAdapterMultiView(list);

        recyclerview.setAdapter(adapter);

        Utils.setBorderColor(profileImage);

    }


    @OnClick(R.id.back)
    public void onBack() {

        super.onBackPressed();

    }


    private List getList(StudentList st) {

        List list = new ArrayList();

        list.add(new Header("Profile"));

        StudentDetails name = new StudentDetails("Name", st.getName(), R.drawable.ic_account_circle_black_24dp, true);
        list.add(name);
        StudentDetails dob = new StudentDetails("DOB", st.getBirthDate(), R.drawable.ic_cake_black_24dp);
        list.add(dob);
        StudentDetails gender = new StudentDetails("Gender", st.getGender(), R.drawable.ic_gender);
        list.add(gender);

        StudentDetails bloodgroup = new StudentDetails("Bood Group", st.getBloodGroup(), R.drawable.ic_blood_type);
        list.add(bloodgroup);

        StudentDetails email = new StudentDetails("Email", st.getEmail(), R.drawable.ic_email_black_24dp);
        list.add(email);

        StringBuilder builder = new StringBuilder();
        builder.append(st.getStreet1());
        String s2 = st.getStreet2() != null && st.getStreet2().length() > 0 ? "," + st.getStreet2() : "";
        builder.append(s2);
        String city = st.getCity() != null && st.getCity().length() > 0 ? "," + st.getCity() : "";
        builder.append(city);
        String state = st.getState() != null && st.getState().length() > 0 ? "," + st.getState() : "";
        builder.append(state);
        String country = st.getCountry() != null && st.getCountry().length() > 0 ? "," + st.getCountry() : "";
        builder.append(country);
        String zipcode = st.getZip() != null && st.getZip().length() > 0 ? "," + st.getZip() : "";
        builder.append(zipcode);

        StudentDetails address = new StudentDetails("Address", builder.toString(), R.drawable.ic_home_black_24dp);
        list.add(address);

        StudentDetails contact = new StudentDetails("Emergency Contact", st.getMobile(), R.drawable.ic_phone_black_24dp);
        list.add(contact);


        list.add(new Header("Class Credendtials"));

        StudentDetails grade = new StudentDetails("Grade", st.getGrade(), R.drawable.ic_group_black_24dp, true);
        list.add(grade);

        StudentDetails section = new StudentDetails("Batch", st.getSection(), R.drawable.ic_group_black_24dp);
        list.add(section);

        StudentDetails rollnumber = new StudentDetails("Roll Number", st.getCurrentRollNumber(), R.drawable.ic_person_black_24dp);
        list.add(rollnumber);


//        StudentDetails section = new StudentDetails("Class", st.getSection(), R.drawable.ic_person_outline_black_24dp);


        return list;


    }

}
