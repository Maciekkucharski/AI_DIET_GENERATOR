package pjwstk.aidietgenerator.view;

import java.sql.Timestamp;

public class WeightView {

    private Long id;

    private double weight;

    private Timestamp timestamp;

    public WeightView(Long id, double weight, Timestamp timestamp){
        this.id = id;
        this.weight = weight;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
