package pjwstk.aidietgenerator.entity;

public enum PhysicalActivity {
    LOW(1.2), AVERAGE(1.3), HEAVY(1.4);

    public final double factor;

    PhysicalActivity(double factor){
        this.factor = factor;
    }
}
