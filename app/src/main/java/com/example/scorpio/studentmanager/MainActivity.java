package com.example.scorpio.studentmanager;

import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scorpio.studentmanager.entities.Student;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName;
    private EditText etSec;
    private EditText etAge;
    private LinearLayout llStudentList;
    private List<Student> studentList;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }

    private void init() {
        etName = (EditText) findViewById(R.id.et_name);
        etSec = (EditText) findViewById(R.id.et_sex);
        etAge = (EditText) findViewById(R.id.et_age);

        llStudentList = (LinearLayout) findViewById(R.id.ll_student_list);

        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_restore).setOnClickListener(this);
        findViewById(R.id.btn_add_student).setOnClickListener(this);

        studentList = new ArrayList<Student>();
        filePath = Environment.getExternalStorageDirectory().getPath() + "/student.xml";

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_save:
                if (studentList.size() > 0) {
                    if (saveStudet2Local()) {
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "当前没有数据", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_restore:
                if (restoreStudentFromLocal()) {
                    Toast.makeText(this, "恢复成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "恢复失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_add_student:
                addStudent();
                break;
            default:
                break;
        }

    }

    private boolean restoreStudentFromLocal() {
        try {
            XmlPullParser parser = Xml.newPullParser();//获得xml解析类的引用
            parser.setInput(new FileInputStream(filePath), "utf-8");

            int eventType = parser.getEventType();

            studentList.clear();

            Student student = null;
            String nodeName = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                nodeName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if ("student".equals(nodeName)) {
                            student = new Student();
                        } else if ("name".equals(nodeName)) {
                            student.setName(parser.nextText());
                        } else if ("sex".equals(nodeName)) {
                            student.setSex(parser.nextText());
                        } else if ("age".equals(nodeName)) {
                            student.setAge(Integer.valueOf(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("student".equals(nodeName)) {
                            studentList.add(student);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
            refreshStudentList();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void refreshStudentList() {
        llStudentList.removeAllViews();
        TextView childView;
        for (Student student : studentList) {
            childView = new TextView(this);
            childView.setTextSize(23);
            childView.setTextColor(Color.BLACK);
            childView.setText("    " + student.getName() + "    " + student.getSex() + "    " + student.getAge());
        }
    }

    private boolean saveStudet2Local() {
        try {
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(new FileOutputStream(filePath), "utf-8");

            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "infos");
            for (Student stu : studentList) {
                serializer.startTag(null, "student");

                serializer.startTag(null, "name");
                serializer.text(stu.getName());
                serializer.endTag(null, "name");

                serializer.startTag(null, "sex");
                serializer.text(stu.getSex());
                serializer.endTag(null, "sex");

                serializer.startTag(null, "age");
                serializer.text(String.valueOf(stu.getAge()));
                serializer.endTag(null, "age");

                serializer.endTag(null, "student");
            }
            serializer.endTag(null, "infos");
            serializer.endDocument();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addStudent() {
        String name = etName.getText().toString();
        String sex = etSec.getText().toString();
        String age = etAge.getText().toString();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(sex) && !TextUtils.isEmpty(age)) {
            studentList.add(new Student(name, sex, Integer.valueOf(age)));
            TextView childView = new TextView(this);
            childView.setTextSize(23);
            childView.setTextColor(Color.BLACK);
            childView.setText("    " + name + "    " + sex + "    " + age);
            llStudentList.addView(childView);
        } else {
            Toast.makeText(this, "请正确输入", Toast.LENGTH_SHORT);
        }
    }
}
