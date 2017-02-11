package com.dpenaskovic.todo;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = ToDoDatabase.NAME, version = ToDoDatabase.VERSION)
public class ToDoDatabase
{
    public static final String NAME = "MyDataBase";
    public static final int VERSION = 1;
}
