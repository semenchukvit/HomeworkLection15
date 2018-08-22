package com.example.vsemenchuk.homeworklection15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Student>> {

    public static final String PREFERENCES_NAME = "com.example.vsemenchuk.homeworklection15_preferences";
    public static final String PREF_SELECTED_LANGUAGE_VALUE = "select_language";

    SaveStudentTask saveStudentTask;
    RemoveStudentTask removeStudentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);
        String languageNumber = preferences.getString(PREF_SELECTED_LANGUAGE_VALUE, "1");*/


        Locale localeDefault = new Locale("ru");
        Locale.setDefault(localeDefault);

        Configuration configurationDefault = new Configuration();
        configurationDefault.locale = localeDefault;

        getResources().updateConfiguration(configurationDefault, getResources().getDisplayMetrics());


        setContentView(R.layout.activity_main);
        getSupportLoaderManager().initLoader(0, null, this);
    }

    public void onClickAdd(View view) {
        editStudent(new Student());
    }

    private void editStudent(Student student) {
        final FragmentEdit fragmentEdit = FragmentEdit.newInstance(student);
        fragmentEdit.setActionListener(new FragmentEdit.ActionListener() {
            @Override
            public void saveListener(Student student) {
                saveStudentTask = new SaveStudentTask();
                saveStudentTask.execute(student);

                fragmentEdit.dismiss();
            }

            @Override
            public void cancelListener() {
                fragmentEdit.dismiss();
            }
        });

        fragmentEdit.show(getSupportFragmentManager(), "dialog");
    }

    private void removeStudent(Student student) {
        removeStudentTask = new RemoveStudentTask();
        removeStudentTask.execute(student);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemSettings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Student>> onCreateLoader(int id, @Nullable Bundle args) {
        return new StudentsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Student>> loader, ArrayList<Student> data) {
        FragmentMain fragmentMain = FragmentMain.newInstance(data);
        fragmentMain.setActionListener(new FragmentMain.ActionListener() {
            @Override
            public void onClick(Student student) {
                editStudent(student);
            }

            @Override
            public void onLongClick(Student student) {
                removeStudent(student);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.relLayoutMainActivity, fragmentMain).commit();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Student>> loader) {

    }

    class SaveStudentTask extends AsyncTask<Student, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Student... students) {
            Boolean isSaved = true;
            DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);

            return dbHelper.saveStudent(students[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }

    class RemoveStudentTask extends AsyncTask<Student, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Student... students) {
            Boolean isRemoved = true;
            DataBaseHelper dbHelper = new DataBaseHelper(MainActivity.this);

            return dbHelper.deleteStudent(students[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            getSupportLoaderManager().restartLoader(0, null, MainActivity.this);
        }
    }
}
