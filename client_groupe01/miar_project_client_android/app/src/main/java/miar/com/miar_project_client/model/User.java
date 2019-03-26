package miar.com.miar_project_client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    public String id;
    public String pseudo;
    public String email;
    public String mdp;

    public Double total;
    public Double average;

    public boolean ischecked = false;

    /*public List<Event> created;
    public List<Event> invited;*/
    public Double balance;

    public User() {}

    public User(String pseudo, String email, String mdp, Double balance) {
        this.pseudo = pseudo;
        this.email = email;
        this.mdp = mdp;
        this.balance = balance;
    }

}
