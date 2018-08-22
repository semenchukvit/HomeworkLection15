package com.example.vsemenchuk.homeworklection15;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentEdit extends DialogFragment {

    private static final String EXTRA_STUDENT = "com.example.vsemenchuk.homeworklection14.extra.STUDENT";

    EditText etFirstName, etLastName, etAge;
    Student student;

    public static FragmentEdit newInstance(Student student) {
        FragmentEdit fragment = new FragmentEdit();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_STUDENT, student);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        student = getArguments().getParcelable(EXTRA_STUDENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_edit, container, false);

        getDialog().setTitle("Edit Student");

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etAge = view.findViewById(R.id.etAge);

        view.findViewById(R.id.btnSave).setOnClickListener(saveListener);
        view.findViewById(R.id.btnCancel).setOnClickListener(cancelListener);

        init();

        return view;
    }

    private void init() {
        etFirstName.setText(student.getFirstName());
        etLastName.setText(student.getLastName());
        etAge.setText(String.valueOf(student.getAge()));
    }

    private View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            student.setFirstName(etFirstName.getText().toString());
            student.setLastName(etLastName.getText().toString());
            student.setAge(Integer.valueOf(etAge.getText().toString()));

            if (listener != null) {
                listener.saveListener(student);
            }
        }
    };

    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.cancelListener();
            }
        }
    };

    public interface ActionListener {
        void saveListener(Student student);
        void cancelListener();
    }

    private ActionListener listener;

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

}
