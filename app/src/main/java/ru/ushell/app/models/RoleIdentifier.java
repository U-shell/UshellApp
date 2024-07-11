package ru.ushell.app.models;

import static ru.ushell.app.models.ERole.*;

import java.util.Set;

public class RoleIdentifier {
    private Set<ERole> eRoles;
    public boolean identification(Set<String> userRoles){
        System.out.println(userRoles.toArray());
        for (String role: userRoles){
            ERole eRole = ERole.valueOf(role);
            if(eRoles.contains(eRole)){
                eRoles.clear();
                return true;
            }
        }
        return false;
    }
    public RoleIdentifier(Set<ERole> eRoles) {
        this.eRoles = eRoles;
    }
    public void setRoles(Set<ERole> eRole) {
        eRoles = eRole;
    }

    public void setStudent(){
        eRoles.add(ROLE_STUDENT);
    }
    public void setTeacher(){
        eRoles.add(ROLE_TEACHER);
    }
    public void setHead(){
        eRoles.add(ROLE_HEAD);
        return;
    }
    public void setDeputy(){
        eRoles.add(ROLE_DEPUTY);
    }
    public void setAdmin(){
        eRoles.add(ROLE_ADMIN);
    }



}
