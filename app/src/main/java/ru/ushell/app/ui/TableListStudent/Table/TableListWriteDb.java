package ru.ushell.app.ui.TableListStudent.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class TableListWriteDb {

    static Set<String> KeyUsers = new HashSet<String>();
    static ArrayList<String> NameStudentList = new ArrayList<>();

//    public static void WriteOutDb(){
//        reference = FirebaseDatabase.getInstance().getReference("list_group/" + NameGroup);
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Map<String, Object> Snapshot = (Map<String, Object>) snapshot.getValue();
//                assert Snapshot != null;
//                KeyUsers = Snapshot.keySet();
//                if (KeyUsers != null && NameStudentList.size() != KeyUsers.size()) {
//                    for (String i : KeyUsers) {
//                        Map<String, String> NameStudent = (Map<String, String>) snapshot.child(i).getValue();
//                        String Name = NameStudent.get("name");
//
//                        NameStudentList.add(String.valueOf(Name));
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        Map<String, Object> childUpdates = new HashMap<>();
//
//        ArrayList<Table> StudentList = Table.TableStudentList;
//
//        for(Table Student : StudentList){
//            int IndexName = NameStudentList.indexOf(Student.getNameStudent());
//            int i = 0;
//            for(String Key : KeyUsers){
//                if (i == IndexName){
//                    for (String OutStudent : Student.getOutStudent()) {
//                        String Data = (Student.getData()).replace(" ", "_");
//                        childUpdates.put(Key + "/tasks/" + Data + "/"+OutStudent, true);
//                        System.out.println(childUpdates);
//                        reference.updateChildren(childUpdates);
//                    }
//                }
//                i++;
//            }
//        }
//    }


    public static void WriteOneStudent(String Name, String Time,String DataLesson , boolean StatusStudent){


    }
}
