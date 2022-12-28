package pjwstk.aidietgenerator.service;

import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Diet;
import pjwstk.aidietgenerator.entity.Gender;
import pjwstk.aidietgenerator.entity.PhysicalActivity;

import static pjwstk.aidietgenerator.entity.Gender.FEMALE;
import static pjwstk.aidietgenerator.entity.Gender.MALE;

@Service
public class DietService {

//    Harris-Benedict Formula
    public double dailyBMR(double bodyWeight, int bodyHeight, int age, Gender gender, PhysicalActivity physicalActivity){

        double male = (88.362 + 13.397 * bodyWeight + 4.799 * bodyHeight - 5.677 * age) * physicalActivity.factor;
        double female = (447.593 + 9.247 * bodyWeight + 3.098 * bodyHeight - 4.330 * age) * physicalActivity.factor;

        return gender == MALE ? male : female;

    }

    public double goalCalories(double bodyWeight, int bodyHeight, int age, Gender gender, PhysicalActivity physicalActivity, Diet diet){
        double bmr = dailyBMR(bodyWeight, bodyHeight, age, gender, physicalActivity);
        double kcalIntake = bmr;

        switch(diet){
            case LOOSE:
                kcalIntake = bmr - 500;
                if(kcalIntake <= 1200 && gender == FEMALE){
                    kcalIntake = 1200;
                }
                else if(kcalIntake <= 1500 && gender == MALE){
                    kcalIntake = 1500;
                }
                break;

            case MAINTAIN:
                kcalIntake = bmr;
                break;

            case GAIN:
                kcalIntake = bmr + 500;
                break;

            case MUSCLE:
                kcalIntake = 44*bodyWeight;
                break;
        }

        return kcalIntake;
    }

}
