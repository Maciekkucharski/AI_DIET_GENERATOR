package pjwstk.aidietgenerator.entity;

public enum DietGoal {
    LOOSE("loose"), MAINTAIN("maintain"), GAIN("gain"), MUSCLE("muscle");

    public final String dietGoal;

    DietGoal(String dietGoal) {this.dietGoal = dietGoal;}
}
