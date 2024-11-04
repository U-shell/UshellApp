package ru.ushell.app.models;

import static ru.ushell.app.models.e_class.ERole.ROLE_STUDENT;
import static ru.ushell.app.models.e_class.ERole.ROLE_TEACHER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ru.ushell.app.api.request.chat.RequestUserChat;
import ru.ushell.app.api.request.info.RequestInfoGroup;
import ru.ushell.app.api.request.info.RequestInfoTeacher;
import ru.ushell.app.api.response.ResponseSingIn;
import ru.ushell.app.models.e_class.ERole;
import ru.ushell.app.utils.interfaces.OnDataSavedListener;


public class User {

    private static final String PREF_NAME = "UserPreferences";
    private static final String ID_USER = "idUser";

    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ROLES = "roles";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_MIDDLE_NAME = "middleName";

    private static final String KEY_DES_USER = "desUser";

    private static final String KEY_ID_GROUP = "idGroup";
    private static final String KEY_ID_SUBGROUP = "idSubgroup";

    private static final String KEY_ID_USER_CHAT = "idUserChat";
    private static final String KEY_USERNAME = "username";


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
    public void SaveUserDara(
            ResponseSingIn userData,
            OnDataSavedListener onDataSavedListener
    ){
        Set<String> roles = new HashSet<>(userData.getRoles());
       
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(ID_USER, userData.getId());

        editor.putString(KEY_FULL_NAME, userData.getFullName());

        editor.putString(KEY_FIRST_NAME, userData.getFirstname());
        editor.putString(KEY_LAST_NAME, userData.getLastname());
        editor.putString(KEY_MIDDLE_NAME, userData.getMiddlename());

        editor.putString(KEY_ACCESS_TOKEN, userData.getAccessToken());
        editor.putString(KEY_TYPE_TOKEN, userData.getTypeToken());
                                                                                 
        editor.putStringSet(KEY_ROLES, roles);
        editor.putString(KEY_EMAIL, userData.getEmail());

        getInfoUser(roles, editor, userData.getEmail(), userData.getAccessToken(), userData.getTypeToken(), onDataSavedListener);

        editor.apply();
   }

    @SuppressLint("CommitPrefEdits")
    public static void saveSubgroup(Integer Subgroup){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_ID_SUBGROUP, Subgroup);
        editor.apply();
    }

    private void getInfoUser(Set<String> roles, SharedPreferences.Editor editor,
                             String email,
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

                        editor.putString(KEY_DES_USER, nameGroup);
                        editor.putInt(KEY_ID_GROUP, idGroup);
                        editor.apply();

                        onDataSavedListener.onDataSaved(); // Уведомить об окончании сохранения данных

                    } else {
                        System.out.println("error User getInfoUser");
                    }
                });
            }
            else if (eRole.equals(ROLE_TEACHER)){
                hasStudentRole = true;

                RequestInfoTeacher.getInfoTeacher(accessToken, tokenType, infoTeacherData->{

                   if(infoTeacherData != null){
                       String nameGroup = String.format("%s %s",infoTeacherData.get(0), infoTeacherData.get(1));
                       editor.putString(KEY_DES_USER, nameGroup);
                       editor.apply();

                       onDataSavedListener.onDataSaved();

                   }else{
                       System.out.println("error getTeacherUser");
                   }
                });
            }
            else{
                editor.putString(KEY_ID_GROUP, "");
                editor.apply();
            }
        }

        RequestUserChat.getCurrentUser(
                String.format("@%s",email.split("@")[0]),
                infoUserData -> {
                    editor.putString(KEY_ID_USER_CHAT, infoUserData);
                    editor.putString(KEY_USERNAME, String.format("@%s",email.split("@")[0]));
                    editor.apply();
                }
        );

        if (!hasStudentRole) {
            onDataSavedListener.onDataSaved();  // Уведомить об окончании сохранения данных, если роль студента не найдена
        }
    }

    public static Integer getIdUser() {
        return sharedPreferences.getInt(ID_USER, -1);
    }

    public static Integer getIDGroup() {
        return sharedPreferences.getInt(KEY_ID_GROUP,-1);
    }


    public static String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME," ");
    }

    public static String getNameBrief() {
        String f = sharedPreferences.getString(KEY_FIRST_NAME," ");
        String l = sharedPreferences.getString(KEY_LAST_NAME," ");
        String ret = String.format("%s %s",f,l);

        if(ret.equals("   ")){
            return " ";
        }else{
            return ret;
        }
    }

    public static String getGroupName() {
        return sharedPreferences.getString(KEY_DES_USER," ");
    }

    public static String getTypeToken() {
        return sharedPreferences.getString(KEY_TYPE_TOKEN,"error getTypeToken");
    }

    public static String getAccessToken() {
        return sharedPreferences.getString(KEY_ACCESS_TOKEN,"error getAccessToken");
    }

    public static Set<String> getRoles(){
        return sharedPreferences.getStringSet(KEY_ROLES, Collections.singleton("error getRoles"));
    }

    public static String getUsername(){
        return sharedPreferences.getString(KEY_USERNAME, "err");
    }
    public static String getKeyIdUserChat(){
        return sharedPreferences.getString(KEY_ID_USER_CHAT, "err");
    }
}
