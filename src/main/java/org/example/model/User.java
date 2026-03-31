package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Integer id;
    private String password;
    private String name;
    private String email;
    private boolean isAdmin;

    public User() {
    }

    public User(Integer id, String password, String name, String email, boolean isAdmin) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public boolean getAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

}
