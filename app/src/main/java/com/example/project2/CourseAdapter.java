
package com.example.project2;

import android.view.*;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import com.example.project2.database.entities.Course;
import com.example.project2.R;
import java.util.*;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH>{
    List<Course> list = new ArrayList<>();

    public void set(List<Course> l){
        list.clear();
        if(l!=null) list.addAll(l);
        notifyDataSetChanged();
    }

    @NonNull
    public VH onCreateViewHolder(@NonNull ViewGroup p,int v){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_course_card,p,false));
    }

    public void onBindViewHolder(@NonNull VH h,int i){
        Course c=list.get(i);
        h.code.setText(c.getCode());
        h.name.setText(c.getName());
        h.grade.setText(c.getPercentage()+"%");
        h.pb.setProgress(c.getPercentage());
        h.assign.setText(c.getAssignments());
    }

    public int getItemCount(){return list.size();}

    static class VH extends RecyclerView.ViewHolder{
        TextView code,name,grade,assign;
        ProgressBar pb;
        VH(View v){
            super(v);
            code=v.findViewById(R.id.tvCourseCode);
            name=v.findViewById(R.id.tvCourseName);
            grade=v.findViewById(R.id.tvCourseGrade);
            pb=v.findViewById(R.id.progressBarGrade);
            assign=v.findViewById(R.id.tvAssignmentsList);
        }
    }
}
