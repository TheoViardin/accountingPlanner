package miar.com.miar_project_client.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventForDetail {
    public String id;
    public String name;
    public String description;
    //public Date creationDate;

    public String place;
    public String creatorPseudo;
    public User creator;
    public float moyenne = 0;
    public double resultat = 0;
    public Date supposedDate;

    public List<User> contributors = new ArrayList<>();
    public List<Spent> spents = new ArrayList<>();
}
