from sklearn.decomposition import TruncatedSVD
import pandas as pd
from scipy import spatial
import numpy as np
from src.rec_system.data.recipes import get_dish_id


class InternalStatusError(Exception):
    pass


class Recommender:
    def __init__(
            self,
            data: pd.DataFrame
    ):
        self.model = None
        self.data = data
        self.decomposed_matrix = None

    def create_and_fit(
            self,
            n_components: int = 50,
    ):
        """Creates and SVD model
                    Parameters:
                        n_components(int): value of hidden features
                    Returns:
                        (Recommender) returns decomposition matrix
                """
        self.model = TruncatedSVD(n_components=n_components)
        self.decomposed_matrix = self.model.fit_transform(self.data)

    def recommend_products(
            self,
            user_email: str,
            correlation_threshold: float = 0.7,
            user_profiles_df: pd.DataFrame = pd.DataFrame,
            recipes_df: pd.DataFrame = pd.DataFrame,

    ):
        """Finds the recommended items for the user.
            Parameters:
                user_email: email of user that you want to recommend to
                correlation_threshold: threshold that satisfies the level of correlation
                user_profiles_df: dataframe with taste profiles of users
                recipes_df: dataframe with recipes and taste profiles
            Returns:
                (items) list of items recommended for the user.
        """
        if self.decomposed_matrix is None:
            raise InternalStatusError(
                "Fit the model before trying to recommend"
            )
        user_ratings = self.data[[user_email]]
        ratings_list = list()
        for index, row in user_ratings.iterrows():
            if row[0] == 0:
                continue
            similar_dishes = self.similar_dishes(index, correlation_threshold, recipes_df=recipes_df)
            similar_dishes.append(get_dish_id(index, df=recipes_df))
            compared_similar_dishes = compare_taste_with_taste_profile(dish_name_list=similar_dishes,
                                                                       user_email=user_email,
                                                                       user_profiles_df=user_profiles_df,
                                                                       recipes_df=recipes_df)
            for item in compared_similar_dishes:
                ratings_list.append((item[1], item[0] * int(row[0])))
        sorted_ratings_list = sorted(ratings_list, key=lambda x: x[1], reverse=True)
        return list(dict.fromkeys([i[0] for i in sorted_ratings_list]))

    def similar_dishes(self, dish_name: str, correlation_threshold: float = 0.85,
                       recipes_df: pd.DataFrame = None) -> list:
        """Finds the recommended dishes similar to the one provided.
                    Parameters:
                        dish_name: id of dish that you want to recommend to
                        correlation_threshold: threshold that satisfies the level of correlation
                        recipes_df: dataframe with recipes and taste profiles
                    Returns:
                        (items) list of similar items recommended.
                """
        if self.decomposed_matrix is None:
            raise InternalStatusError(
                "Fit the model before trying to recommend"
            )
        # creating correlation matrix
        correlation_matrix = np.corrcoef(self.decomposed_matrix)
        product_names = list(self.data.index)
        product_ID = product_names.index(dish_name)
        # get correlation array for specific product
        correlation_product_ID = correlation_matrix[product_ID]
        # get only items which correlate on a satisfying level
        recommended_items = list(self.data.index[correlation_product_ID > correlation_threshold])
        recommended_items.remove(dish_name)
        return [get_dish_id(i, df=recipes_df) for i in recommended_items]


def compare_taste_with_taste_profile(dish_name_list, user_email, user_profiles_df: pd.DataFrame = None,
                                     user_profiles_path: str = './src/rec_system/data/user_profiles.csv',
                                     recipes_df: pd.DataFrame = None,
                                     recipes_path: str = './src/rec_system/data/recipes.csv'):
    """returns the value of cosine distance between taste vectors.
                Parameters:
                    dish_name_list: list of dishes to be compared
                    user_email: email of the user to compare to
                    recipes_df: dataframe with recipes and taste profiles
                    recipes_path: path to file with the recipes dataframe
                    user_profiles_path: dataframe with recipes and taste profiles
                    user_profiles_df: path to file with the user profiles dataframe
                Returns:
                    (cosine_distance,dish_name) rating and dish name of provided dishes.
            """
    # print(dish_name_list)
    if user_profiles_df is None:
        user_profiles_df = pd.read_csv(user_profiles_path)
    if user_profiles_df.empty:
        print("no user profiles found")
        return None
    user_profile = (user_profiles_df.loc[user_profiles_df['email'] == user_email][
                        ["saltiness", "bitterness", 'spiciness', 'fattiness', 'sweetness']
                    ].values * 10)[0]
    if recipes_df is None:
        recipes_df = pd.read_csv(recipes_path)
    if recipes_df.empty:
        print("no recipes data found")
        return None
    cosine_similarity_list = list()
    for dish_name in dish_name_list:
        dish = recipes_df.loc[recipes_df['id'] == dish_name][
            ["saltiness", "bitterness", 'spiciness', 'fattiness', 'sweetness']
        ].values[0]
        cosine_similarity_list.append((1 - spatial.distance.cosine(user_profile, dish), dish_name))
    cosine_similarity_list.sort(key=lambda x: x[0], reverse=True)
    return cosine_similarity_list
