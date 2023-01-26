package pjwstk.aidietgenerator.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pjwstk.aidietgenerator.entity.Gender;

import javax.persistence.Column;

@NoArgsConstructor
@Setter
@Getter
public class UserStatsRequest {
    private double weight;
    private int height;
    private int age;
    private String gender;
}
