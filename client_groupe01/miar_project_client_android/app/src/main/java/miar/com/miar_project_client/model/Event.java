package miar.com.miar_project_client.model;

import java.util.ArrayList;
import java.util.List;

public class Event
{
	public String id;
	public String name;
	public String description;
	//public Date creationDate;

	public String place;
	public String creatorPseudo;

	public float moyenne = 0;
	public double resultat = 0;
	public String supposedDate;

	public List< String > contributors = new ArrayList<>();
	public List< String > spents = new ArrayList<>();
}
