import numpy
import pandas as pd

from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile
from src.rec_system.utils import load_and_preprocess_data
import pytest

MODEL_PARAMETERS = dict(
    factors=20,
    alpha=0.6,
    regularization=0.06,
)


class TestRecommender:
    """
    Tests related to Recommender
    """

    @pytest.mark.parametrize("test_input,expected", [((20, 40), (2, 40))])
    def test_product_recommendation_shape(self, test_input, expected):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        suggestions_and_score = recommender.recommend_products(expected[0], items_to_recommend=expected[1])
        assert len(suggestions_and_score) == expected[0]
        for i in suggestions_and_score:
            assert len(i) == expected[1]

    @pytest.mark.parametrize("test_input,expected", [(20, (2, 10))])
    def test_similar_user_shape(self, test_input, expected):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        similar_users = recommender.similar_users(test_input)
        assert len(similar_users) == expected[0]
        for i in similar_users:
            assert len(i) == expected[1]

    @pytest.mark.parametrize("test_input,expected", [((2, 10, 20), (numpy.float64, str))])
    def test_compare_taste_with_taste_profile_no_parameters_shape(self, test_input, expected):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        suggestions_and_score = recommender.recommend_products(test_input[0], items_to_recommend=test_input[1])

        result = compare_taste_with_taste_profile([
            sorted_dishes[i] for i in suggestions_and_score[0].tolist()],
            sorted_users[test_input[2]])
        for item in result:
            assert type(item[0]) == expected[0]
            assert type(item[1]) == expected[1]

    @pytest.mark.parametrize("test_input", [(2, 10, 20)])
    def test_compare_taste_with_taste_profile_is_sorted(self, test_input):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        suggestions_and_score = recommender.recommend_products(test_input[0], items_to_recommend=test_input[1])

        result = compare_taste_with_taste_profile([
            sorted_dishes[i] for i in suggestions_and_score[0].tolist()],
            sorted_users[test_input[2]])
        result_scores = list(map(lambda x: x[0], result))
        assert result_scores == sorted(result_scores, reverse=True)

    @pytest.mark.parametrize("test_input,expected", [((2, 10, pd.DataFrame()), None)])
    def test_compare_taste_with_taste_profile_no_user_profiles(self, test_input, expected):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        suggestions_and_score = recommender.recommend_products(test_input[0], items_to_recommend=test_input[1])

        result = compare_taste_with_taste_profile([
            sorted_dishes[i] for i in suggestions_and_score[0].tolist()],
            sorted_users[test_input[0]], user_profiles_df=test_input[2])
        assert result == expected

    @pytest.mark.parametrize("test_input,expected", [((2, 10, pd.DataFrame()), None)])
    def test_compare_taste_with_taste_profile_no_recipes(self, test_input, expected):
        data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data()
        recommender = Recommender(
            data['ocena'],
            email_order,
            dishes_order,
        )
        recommender.create_and_fit(
            model_params=MODEL_PARAMETERS,
        )
        suggestions_and_score = recommender.recommend_products(test_input[0], items_to_recommend=test_input[1])

        result = compare_taste_with_taste_profile([
            sorted_dishes[i] for i in suggestions_and_score[0].tolist()],
            sorted_users[test_input[0]], recipes_df=test_input[2])
        assert result == expected
