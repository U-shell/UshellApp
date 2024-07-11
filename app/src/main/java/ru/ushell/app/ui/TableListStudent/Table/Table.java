package ru.ushell.app.ui.TableListStudent.Table;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class Table extends ArrayList<Table> {
    public static ArrayList<Table> TableStudentList = new ArrayList<>();

    //сортировочный список
    public static ArrayList<Table> TablePeople(String DataItem){
        ArrayList<Table> tables = new ArrayList<>();
        for(Table table: TableStudentList){
            if(table.DataDay.equals(DataItem)){
                tables.add(table);
            }
        }
        return tables;
    }


    private String NameStudent;
    public String DataDay;
    private LinkedTreeMap<String, Boolean> OutStudent;

    public Table(String NameStudent,String DataDay, LinkedTreeMap<String, Boolean> OutStudent){
        this.NameStudent = NameStudent;
        this.DataDay = DataDay;
        this.OutStudent = OutStudent;
    }


    public String getNameStudent() {
        return NameStudent;
    }

    public void setNameStudent(String NameStudent) {
        this.NameStudent = NameStudent;
    }

    public String getData() {
        return DataDay;
    }

    public void setData(String DataDay) {
        this.DataDay = DataDay;
    }

    public LinkedTreeMap<String, Boolean> getOutStudent() {
        return OutStudent;
    }

    public void setOutStudent(LinkedTreeMap<String, Boolean> OutStudent) {
        this.OutStudent = OutStudent;
    }

}
