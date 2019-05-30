package com.example.cameraapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("task")
    @Expose
    public String task;

    public String getTask() { return task;}

    public  void setTask(String task) { this.task = task; }

}
