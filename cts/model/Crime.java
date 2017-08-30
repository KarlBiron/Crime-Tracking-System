package ae.ac.ud.ceit.cts.model;

import java.io.Serializable;

/**
 * Created by atalla on 09/04/17.
 */

public class Crime implements Serializable{

    private String reference;
    private String date_time;
    private String created;
    private String evidences;
    private String location;
    private String suspects;
    private String victims;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getEvidences() {
        return evidences;
    }

    public void setEvidences(String evidences) {
        this.evidences = evidences;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSuspects() {
        return suspects;
    }

    public void setSuspects(String suspects) {
        this.suspects = suspects;
    }

    public String getVictims() {
        return victims;
    }

    public void setVictims(String victims) {
        this.victims = victims;
    }

    @Override
    public String toString() {
        return "Crime{" +
                "reference='" + reference + '\'' +
                ", date_time='" + date_time + '\'' +
                ", created='" + created + '\'' +
                ", evidences='" + evidences + '\'' +
                ", location='" + location + '\'' +
                ", suspects='" + suspects + '\'' +
                ", victims='" + victims + '\'' +
                '}';
    }
}

/**
 * The builder abstraction.
 */


