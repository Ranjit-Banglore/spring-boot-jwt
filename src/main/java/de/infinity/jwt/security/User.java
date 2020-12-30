package de.infinity.jwt.security;

import javax.persistence.*;


@Entity
@Table(name = "user", schema = "public")
@NamedQuery(name = User.NQ_GET_BY_NAME,
        query = " SELECT u FROM User u WHERE u.userName = : userName")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name",unique = true)
    private String userName;
    private String password;
    private Boolean active;
    private String roles;

    public static final String NQ_GET_BY_NAME = "User.findByName";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
