package ru.ushell.app.ui.TableListStudent.Table;

import static ru.ushell.app.ui.TableListStudent.Table.Table.TableStudentList;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;

public class TableListReadDb {


    public static void NamesStudentsGroup(String Date){



    }
    private static void CreateData(String NameStudent, String Date){
        String newData = Date.replace("_"," ");

        LinkedTreeMap<String, Boolean> outStudent = new LinkedTreeMap<>();
        outStudent.put("00:00", true);

        Table newStudent = new Table(NameStudent, newData, outStudent);
        TableStudentList.add(newStudent);

    }

    public void TableLayoutRead(TableLayout table){
        // чтение таблицы с посешаемостью
//        TableLayout table = findViewById(R.id.table_layout);
        int numRows = table.getChildCount();
        for (int i = 0; i < numRows; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            int numCols = row.getChildCount();
            for (int j = 0; j < numCols; j++) {
                View cell = row.getChildAt(j);
                if (cell instanceof TextView) {
                    String data = ((TextView) cell).getText().toString();
                    // Use the data as needed
                    System.out.println(data+"data");
                }
            }
        }
    }
}
