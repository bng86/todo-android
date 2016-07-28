package com.oursky.todo_android.content.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yuyauchun on 13/3/15.
 */

public class Task implements Parcelable{
    private int id;
    private String name;
    private String dueAt;
    private boolean finished = false;

    public Task() {}

    public Task(String name) {
        this.name = name;
    }

    public Task(String name, String dueAt) {
        this.name = name;
        this.dueAt = dueAt;
    }

    protected Task(Parcel in) {
        id = in.readInt();
        name = in.readString();
        dueAt = in.readString();
        finished = in.readByte() != 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDueAt() {
        return dueAt;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setIsFinished(boolean finished) {
        this.finished = finished;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(dueAt);
        parcel.writeByte((byte) (finished ? 1 : 0));
    }
}
