package pjwstk.aidietgenerator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Question {

    private Integer id;
    private Integer value;

    public Question(Integer value) {
        this.value = value;
    }
}
