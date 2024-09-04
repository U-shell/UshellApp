package ru.ushell.app.models.e_class;

import java.util.HashSet;
import java.util.Set;

import ru.ushell.app.models.User;

public class ERoleClass{

    public static Boolean AccessControl(){
        Set<String> intersection = new HashSet<>(User.getRoles());
        intersection.retainAll(getRootRole());
        return !intersection.isEmpty();
    }

    public static boolean containsValueGroup() {
        return User.getRoles().contains(ERole.ROLE_STUDENT.name());
    }

    public static boolean containsValueHead() {
        return User.getRoles().contains(ERole.ROLE_HEAD.name());
    }
    public static boolean containsValueTeacher() {
        return User.getRoles().contains(ERole.ROLE_TEACHER.name());
    }

    private static Set<String> getRootRole(){
        Set<String> role = new HashSet<>();
        role.add(ERole.ROLE_TEACHER.name());
        role.add(ERole.ROLE_HEAD.name());
        return role;
    }

}
