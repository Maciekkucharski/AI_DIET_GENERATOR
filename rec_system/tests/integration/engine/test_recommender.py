import numpy
import pandas as pd

from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile
from src.rec_system.utils import load_and_preprocess_data
import pytest


class TestRecommender:
    """
    Tests related to Recommender
    """

    @pytest.mark.parametrize("test_input,expected", [
        (('tast@gmail.com', 0.7, pd.read_csv('./tests/integration/Data/user_profiles.csv'),
          pd.read_csv('./src/rec_system/data/recipes.csv')), (list, int))])
    def test_product_recommendation_type(self, test_input, expected):
        data = load_and_preprocess_data()
        recommender = Recommender(data)
        recommender.create_and_fit()
        recommendations = recommender.recommend_products(test_input[0], test_input[1], test_input[2],
                                                         test_input[3])
        assert expected[0] == type(recommendations)
        for recommendation in recommendations:
            assert expected[1] == type(recommendation)

    @pytest.mark.parametrize("test_input,expected", [
        (('Corned Beef and Cabbage', 0.7,
          pd.read_csv('./src/rec_system/data/recipes.csv')), (list, int))])
    def test_similar_product_recommendation_type(self, test_input, expected):
        data = load_and_preprocess_data()
        recommender = Recommender(data)
        recommender.create_and_fit()
        recommendations = recommender.similar_dishes(test_input[0], test_input[1], test_input[2], )
        assert expected[0] == type(recommendations)
        for recommendation in recommendations:
            assert expected[1] == type(recommendation)

    @pytest.mark.parametrize("test_input,expected", [
        (('foo', 0.7,
          pd.read_csv('./src/rec_system/data/recipes.csv')), ValueError)])
    def test_similar_product_recommendation_wrong_value(self, test_input, expected):
        data = load_and_preprocess_data()
        recommender = Recommender(data)
        recommender.create_and_fit()

    @pytest.mark.parametrize("test_input,expected", [
        (('foo_email', 0.7, pd.read_csv('./tests/integration/Data/user_profiles.csv'),
          pd.read_csv('./src/rec_system/data/recipes.csv')), KeyError)])
    def test_product_recommendation_wrong_value(self, test_input, expected):
        data = load_and_preprocess_data()
        recommender = Recommender(data)
        recommender.create_and_fit()
        with pytest.raises(expected) as exc_info:
            recommender.recommend_products(test_input[0], test_input[1], test_input[2],
                                           test_input[3])
