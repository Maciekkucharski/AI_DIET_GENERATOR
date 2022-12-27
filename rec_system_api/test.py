import mysql.connector as connection
import pandas as pd
from src.rec_system.utils import load_and_preprocess_data
from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile



MODEL_PARAMETERS = dict(
    factors=60,
    alpha=0.6,
    regularization=0.06,
)

try:
    mydb = connection.connect(host="35.198.85.35", user="root", password="jajco123", database="DietGenerator",
                              use_pure=True)
    query = """
SELECT
    u.email AS 'Adres e-mail',
    re.recipeName AS pytanie,
    ra.score AS ocena
FROM ratings ra
INNER JOIN users u
    ON u.id = ra.UserID
INNER JOIN recipes re
    ON re.id = ra.RecipeID;
"""
    result_dataFrame = pd.read_sql(query, mydb)
    print(result_dataFrame)

    query = """
    SELECT
        re.id,
        re.recipeName as title,
        re.saltiness,
        re.sourness,
        re.sweetness,
        re.bitterness ,
        re.spiciness,
        re.fattiness
    FROM recipes as re
    """
    result2_dataFrame = pd.read_sql(query, mydb)
    print(result_dataFrame)
    mydb.close()  # close the connection
except Exception as e:
    mydb.close()
    print(str(e))

data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(result_dataFrame)
print(data)
print(email_order)
print(dishes_order)
print(sorted_users)

print(sorted_dishes)


recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
recommender.create_and_fit(
    model_params=MODEL_PARAMETERS,
)
suggestions_and_score = recommender.recommend_products(2, items_to_recommend=20)
compare_taste_with_taste_profile([sorted_dishes[i] for i in suggestions_and_score[0].tolist()], sorted_users[20], )






