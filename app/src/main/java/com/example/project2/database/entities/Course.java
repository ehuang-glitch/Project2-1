
package com.example.project2.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="courses")
public class Course {
    @PrimaryKey(autoGenerate=true)
    private int id;
    private String code;
    private String name;
    private int percentage;
    private String assignments;
    private int userId;

    public Course(String code,String name,int percentage,String assignments,int userId){
        this.code=code; this.name=name; this.percentage=percentage;
        this.assignments=assignments; this.userId=userId;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getCode(){return code;}
    public void setCode(String c){this.code=c;}
    public String getName(){return name;}
    public void setName(String n){this.name=n;}
    public int getPercentage(){return percentage;}
    public void setPercentage(int p){this.percentage=p;}
    public String getAssignments(){return assignments;}
    public void setAssignments(String a){this.assignments=a;}
    public int getUserId(){return userId;}
    public void setUserId(int u){this.userId=u;}
}
