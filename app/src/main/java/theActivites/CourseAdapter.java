package theActivites;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.entites.CanvasClone;
import com.example.canvasclone_3.R;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.VH>{
    List<CanvasClone> list = new ArrayList<>();

    public void set(List<CanvasClone> l){
        list.clear();
        if(l!=null) list.addAll(l);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        return new VH(LayoutInflater.from(p.getContext()).inflate(R.layout.item_course_card,p,false));
    }

    public void onBindViewHolder(@NonNull VH h,int i){
        CanvasClone c=list.get(i);
        h.code.setText(String.valueOf(c.getId()));
        h.name.setText(c.getAssignment());
        h.grade.setText(c.getGrade()+"%");
        h.pb.setProgress(c.getMaxGrade());
        h.assign.setText(c.getAssignment());
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
