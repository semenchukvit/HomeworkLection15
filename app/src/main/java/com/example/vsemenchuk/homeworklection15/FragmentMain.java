package com.example.vsemenchuk.homeworklection15;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FragmentMain extends Fragment {

    private static final String EXTRA_STUDENTS = "com.example.vsemenchuk.homeworklection14.extra.STUDENTS";

    private ListView listView;
    private ArrayList<Student> students;

    public static FragmentMain newInstance(ArrayList<Student> students) {
        FragmentMain fragment = new FragmentMain();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_STUDENTS, students);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        students = getArguments().getParcelableArrayList(EXTRA_STUDENTS);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_main, container, false);

        listView = view.findViewById(R.id.lvMain);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = students.get(position);
                if (listener != null) {
                    listener.onClick(student);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Student student = students.get(position);
                if (listener != null) {
                    listener.onLongClick(student);
                }

                return true;
            }
        });

        init();

        return view;
    }

    private void init() {
        ArrayAdapter<Student> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                students
        );
        listView.setAdapter(adapter);
    }

    public interface ActionListener {
        void onClick(Student student);
        void onLongClick(Student student);
    }

    private ActionListener listener;

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

}
