package pjwstk.aidietgenerator.entity;

public class Question {
    private Integer id;
    private Integer value;

    public Question(Integer id, Integer value) {
        this.id = id;
        this.value = value;
    }

    public Question() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }
}
