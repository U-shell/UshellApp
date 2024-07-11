package ru.ushell.app.models;

import static ru.ushell.app.models.ERole.ROLE_STUDENT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.Set;

import ru.ushell.app.api.body.BodyRequestTokenIdentifier;
import ru.ushell.app.api.request.info.RequestInfoGroup;
import ru.ushell.app.interfaces.OnDataSavedListener;


public class User {

    private static final String PREF_NAME = "UserPreferences";
    private static final String KEY_ID = "idStudent";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ROLES = "roles";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_MIDDLE_NAME = "middleName";


    private static final String KEY_GROUP_NAME = "groupName";
    private static final String KEY_ID_GROUP = "idGroup";
    private static final String KEY_ID_SUBGROUP = "idSubgroup";


    private static final String KEY_STATUS = "status";
    private static final String KEY_ACCESS_TOKEN = "accessToken";
    private static final String KEY_TYPE_TOKEN = "tokenType";

    private static SharedPreferences sharedPreferences;

    private static User instance;

    public User(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static User getInstance(Context context) {
        if (instance == null) {
            instance = new User(context.getApplicationContext());
        }
        return instance;
    }

    public static void clear() {
        sharedPreferences.edit().clear().apply();
    }


    public void saveUserData(Integer idStudent,
                             String firstname, String lastname, String middlename,
                             String email, Set<String> roles,
                             String accessToken, String tokenType,
                             OnDataSavedListener onDataSavedListener) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID, idStudent);

        editor.putString(KEY_FULL_NAME, String.format("%s %s %s",lastname, firstname, middlename));

        editor.putString(KEY_FIRST_NAME,firstname);
        editor.putString(KEY_LAST_NAME,lastname);
        editor.putString(KEY_MIDDLE_NAME,middlename);

        editor.putString(KEY_ACCESS_TOKEN,accessToken);
        editor.putString(KEY_TYPE_TOKEN,tokenType);
        BodyRequestTokenIdentifier bodyRequestTokenIdentifier = new BodyRequestTokenIdentifier();

        editor.putStringSet(KEY_ROLES, roles);
        getInfoUser(roles, editor,accessToken,tokenType, onDataSavedListener);

        editor.putString(KEY_EMAIL, email);



        editor.apply();
    }

    @SuppressLint("CommitPrefEdits")
    public static void saveSubgroup(Integer Subgroup){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID_SUBGROUP,Subgroup);
    }

    private void getInfoUser(Set<String> roles, SharedPreferences.Editor editor,
                             String accessToken, String tokenType,
                             OnDataSavedListener onDataSavedListener) {

        boolean hasStudentRole = false;

        for (String role: roles){
            ERole eRole = ERole.valueOf(role);

            if(eRole.equals(ROLE_STUDENT)) {
                hasStudentRole = true;

                RequestInfoGroup.getInfoGroup(accessToken, tokenType, infoGroupData -> {

                    if (infoGroupData != null) {
                        int idGroup = (int) infoGroupData.get(0);
                        String nameGroup = (String) infoGroupData.get(1);

                        editor.putString(KEY_GROUP_NAME, nameGroup);
                        editor.putInt(KEY_ID_GROUP, idGroup);
                        editor.apply();

                        onDataSavedListener.onDataSaved(); // Уведомить об окончании сохранения данных

                    } else {
                        System.out.println("error");
                    }
                });
            }
//            else if (eRole.equals(ROLE_TEACHER)){
//                editor.putString(KEY_INFO_GROUP, Teacher.getInfoTeacher());
//            }
//            else{
//                System.out.println(1);
//                editor.putString(KEY_INFO_GROUP, "");
//
//            }
        }
        if (!hasStudentRole) {

            onDataSavedListener.onDataSaved();  // Уведомить об окончании сохранения данных, если роль студента не найдена

        }

    }

    public static Integer getIdStudent(){
        return sharedPreferences.getInt(KEY_ID,-1);
    }

    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME," ");
    }

    public static String getGroupName() {
        return sharedPreferences.getString(KEY_GROUP_NAME," ");
    }

    public Integer getIDGroup() {
        return sharedPreferences.getInt(KEY_ID_GROUP,-1);
    }

    public static String getTypeToken() {
        return sharedPreferences.getString(KEY_TYPE_TOKEN,"error");
    }

    public static String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,"error");
    }

    public static Set<String> getRoles(){
        return sharedPreferences.getStringSet(KEY_ROLES, Collections.singleton("error"));
    }
}
