package miar.com.miar_project_client.model;

import java.io.Serializable;

public class Spent implements Serializable {
    public String spentId;
    public Double amount;
    public String label;
    public String creatorPseudo;

    public Boolean isChecked = false;

    public Spent() {}
    public Spent(Double amount, String label, String creatorPseudo) {
        this.amount = amount;
        this.label = label;
        this.creatorPseudo = creatorPseudo;
    }
}
