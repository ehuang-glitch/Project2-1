
package com.example.project2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project2.database.*;
import com.example.project2.database.dao.CourseDao;
import com.example.project2.database.entities.Course;
import java.util.List;

public class LandingPageActivity extends AppCompatActivity {
    CourseAdapter adapter;
    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_landing_page);

        RecyclerView rv=findViewById(R.id.rvCourses);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CourseAdapter();
        rv.setAdapter(adapter);

        AppDatabase db=AppDatabase.get(getApplicationContext());
        CourseDao dao=db.courseDao();

        AppDatabase.exec.execute(()->{
            if(dao.count()==0){
                dao.insert(
                    new Course("CS101","Intro to CS",92,"• Lab 1\n• Quiz 2",1),
                    new Course("MATH201","Calculus II",88,"• Assignment 1",1)
                );
            }
            List<Course> data=dao.getByUser(1);
            runOnUiThread(()->adapter.set(data));
        });
    }
}
