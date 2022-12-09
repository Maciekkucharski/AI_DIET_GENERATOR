from implicit.als import AlternatingLeastSquares
from implicit.nearest_neighbours import bm25_weight
from scipy.sparse import csr_matrix
from typing import Dict, Any
import pandas as pd
from scipy import spatial


def _get_sparse_matrix(values, user_idx, product_idx):
    return csr_matrix(
        (values, (user_idx, product_idx)),
        shape=(len(user_idx.unique()), len(product_idx.unique())),
    )


def _get_model(**params):
    return AlternatingLeastSquares(**params)


class InternalStatusError(Exception):
    pass


class Recommender:
    def __init__(
            self,
            values,
            user_idx,
            product_idx,
    ):
        self.user_product_matrix = _get_sparse_matrix(values, user_idx, product_idx)
        self.user_idx = user_idx
        self.product_idx = product_idx

        self.model = None
        self.fitted = False

    def create_and_fit(
            self,
            model_params: Dict[str, Any] = dict(),
    ):
        data = bm25_weight(
            self.user_product_matrix,
            K1=1.2,
            B=0.75,
        )
        self.model = _get_model(**model_params)
        self.fitted = True
        self.model.fit(data)
        return self

    def recommend_products(
            self,
            user_id,
            items_to_recommend=5,
    ):
        """Finds the recommended items for the user.
        Returns:
            (items, scores) pair, of suggested item and score.
        """

        if not self.fitted:
            raise InternalStatusError(
                "Fit the model before trying to recommend"
            )

        return self.model.recommend(
            user_id,
            self.user_product_matrix[user_id],
            filter_already_liked_items=False,
            N=items_to_recommend,
        )

    def similar_users(self, user_id):
        return self.model.similar_users(user_id)


def compare_taste_with_taste_profile(dish_name_list, user_email, user_profiles_df: pd.DataFrame = None,
                                     user_profiles_path: str = './src/rec_system/data/user_profiles.csv',
                                     recipes_df: pd.DataFrame = None, recipes_path: str = './src/rec_system/data/recipes.csv'):
    if user_profiles_df is None:
        user_profiles_df = pd.read_csv(user_profiles_path)
    if user_profiles_df.empty:
        print("no user profiles found")
        return None
    user_profile = (user_profiles_df.loc[user_profiles_df['email'] == user_email][
                        ["saltiness", "bitterness", 'spiciness', 'fattiness']
                    ].values * 10)[0]
    if recipes_df is None:
        recipes_df = pd.read_csv(recipes_path)
    if recipes_df.empty:
        print("no recipes data found")
        return None
    cosine_similarity_list = list()
    for dish_name in dish_name_list:
        dish = recipes_df.loc[recipes_df['title'] == dish_name][
            ["saltiness", "bitterness", 'spiciness', 'fattiness']
        ].values[0]
        cosine_similarity_list.append((1 - spatial.distance.cosine(user_profile, dish), dish_name))
    cosine_similarity_list.sort(key=lambda x: x[0], reverse=True)
    return cosine_similarity_list
