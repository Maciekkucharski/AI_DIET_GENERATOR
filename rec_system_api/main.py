from fastapi import FastAPI, Body
from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile
from src.rec_system.utils import load_and_preprocess_data
import mysql.connector as connection
import pandas as pd

app = FastAPI()

MODEL_PARAMETERS = dict(
    factors=100,
    alpha=0.6,
    regularization=0.06,
)


@app.get("/")
async def root():
    return {
        "empty_mes"
    }


@app.post("/generate")
async def generate(body_dict: dict = Body(..., example={
    "user_id": 30,
    "items_to_recommend": 51,
})):
    ratings_result = None
    survey_results = None
    recipes_results = None
    try:
        mydb = connection.connect(host="35.198.85.35", user="root", password="jajco123", database="DietGenerator",
                                  use_pure=True)
        query = """
        SELECT
            u.email AS 'Adres e-mail',
            re.title AS pytanie,
            ra.score AS ocena
        FROM ratings ra
        INNER JOIN users u
            ON u.id = ra.user_id
        INNER JOIN recipes re
            ON re.id = ra.recipe_id;
        """
        ratings_result = pd.read_sql(query, mydb)
        query = """
            SELECT
                u.email AS 'email',
                su.answer1 AS 'saltiness',
                su.answer2 AS 'sourness',
                su.answer3 AS 'sweetness',
                su.answer4 AS 'bitterness',
                su.answer6 AS 'spiciness',
                su.answer12 AS 'fattiness'
            FROM surveys as su
            INNER JOIN users u
                ON u.id = su.user_id;
            """
        survey_results = pd.read_sql(query, mydb)
        query = """
        SELECT
            re.id,
            re.title as title,
            re.saltiness,
            re.sourness,
            re.sweetness,
            re.bitterness ,
            re.spiciness,
            re.fattiness
        FROM recipes as re   
                    """
        recipes_results = pd.read_sql(query, mydb)
        query = """
        select id, email from users;
        """
        users = pd.read_sql(query, mydb)
        mydb.close()  # close the connection
    except Exception as e:
        mydb.close()
        print(str(e))
    if ratings_result is not None and survey_results is not None and recipes_results is not None and users is not None:
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(ratings_result)
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        # convert user number to user id
        user_email = users.loc[users['id'] == body_dict['user_id']]['email'].values[0]
        user_number = list(sorted_users).index(user_email)
        suggestions_and_score = recommender.recommend_products(user_number,
                                                               items_to_recommend=body_dict['items_to_recommend'])

        results = compare_taste_with_taste_profile([sorted_dishes[i] for i in suggestions_and_score[0].tolist()],
                                                   user_email,
                                                   user_profiles_df=survey_results,
                                                   recipes_df=recipes_results)
        # convert number of dish to dish id
        return [int(recipes_results.loc[recipes_results['title'] == i[1]]['id'].values[0]) for i in results]
    else:
        return "missing data from database"


@app.post("/replace")
async def replace(body_dict: dict = Body(..., example={
    "dish_id": 640134,
    "items_to_recommend": 10,
})):
    ratings_result = None
    try:
        mydb = connection.connect(host="35.198.85.35", user="root", password="jajco123", database="DietGenerator",
                                  use_pure=True)
        query = """
        SELECT
            u.email AS 'Adres e-mail',
            re.title AS pytanie,
            ra.score AS ocena
        FROM ratings ra
        INNER JOIN users u
            ON u.id = ra.user_id
        INNER JOIN recipes re
            ON re.id = ra.recipe_id;
        """
        ratings_result = pd.read_sql(query, mydb)
        query = """
                SELECT
                    re.id,
                    re.title,
                    re.saltiness,
                    re.sourness,
                    re.sweetness,
                    re.bitterness ,
                    re.spiciness,
                    re.fattiness
                FROM recipes as re   
                            """
        recipes_results = pd.read_sql(query, mydb)
        mydb.close()  # close the connection
    except Exception as e:
        mydb.close()
        print(str(e))
    mydb.close()
    if ratings_result is not None and recipes_results is not None:
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(ratings_result)
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        dish_name = recipes_results.loc[recipes_results['id'] == body_dict['dish_id']]['title'].values[0]
        dish_number = list(sorted_dishes).index(dish_name)
        results = recommender.similar_dishes(dish_number,
                                             items_to_recommend=body_dict['items_to_recommend'])[0]
        results = [int(result) for result in results]
        # convert list of dish numbers to dish id
        return [int(recipes_results.loc[recipes_results['title'] == sorted_dishes[i]]['id'].values[0]) for i in results]
    else:
        return "missing data from database"
