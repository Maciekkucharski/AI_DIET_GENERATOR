from src.rec_system.utils import load_and_preprocess_data
import pandas as pd
import mysql.connector as connection
from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile


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

X = load_and_preprocess_data(ratings_result)
print(X.columns)
X.to_csv('data.csv')
rec = Recommender(X)

rec.create_and_fit()
print(X)
print(rec.similar_dishes('5 Minute Tiramisu', 0.7, recipes_df=recipes_results))


print(rec.recommend_products('agakucha7@gmail.com', 0.95, user_profiles_df=survey_results, recipes_df=recipes_results))

