package ru.ushell.app.ui.TableListStudent.Table;

import static ru.ushell.app.ui.TableListStudent.Table.Table.TableStudentList;

import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class help {
    public static ArrayList<String> arrayName = new ArrayList<>();
    public static ArrayList<String> arrayData = new ArrayList<>();

    public static void HelpPeople(String NameStudent, String Date){
        if(!arrayName.contains(NameStudent)){
            arrayName.add(NameStudent);
            String newData = Date.replace("_"," ");

            LinkedTreeMap<String, Boolean> outStudent = new LinkedTreeMap<>();
            outStudent.put("00:00", true);

            Table newStudent = new Table(NameStudent, newData, outStudent);
            TableStudentList.add(newStudent);
        }
    }
    public static boolean HelpTrue(String NameStudent, String Date) {
        if(!arrayName.contains(NameStudent)){
            arrayName.add(NameStudent);
            arrayData.add(Date);
            System.out.println(true);
            return true;
        }
        else{
            return false;
        }
    }
}
