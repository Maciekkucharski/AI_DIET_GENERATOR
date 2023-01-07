package pjwstk.aidietgenerator.service;

import org.springframework.stereotype.Service;
import pjwstk.aidietgenerator.entity.Diet;
import pjwstk.aidietgenerator.entity.Gender;
import pjwstk.aidietgenerator.entity.PhysicalActivity;
import pjwstk.aidietgenerator.service.Utils.ApiConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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

    public void getRecommendedIds(Long UserId) throws IOException {
        URL url = new URL (ApiConstants.GENERATOR);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"user_id\":" + " 43," +
                "\"items_to_recommend\": 50}";


        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }

    public void replaceRecommendedMeal(Long RecipeId) throws IOException {
        URL url = new URL (ApiConstants.REPLACER);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"dish_id\": " + RecipeId + ", " +
                "\"items_to_recommend\": 10}";


        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }
    }
}