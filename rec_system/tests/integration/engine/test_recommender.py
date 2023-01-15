import numpy
import pandas as pd

from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile
from src.rec_system.utils import load_and_preprocess_data
import pytest


class TestRecommender:
    """
    Tests related to Recommender
    """

    @pytest.mark.parametrize("test_input,expected", [((20, 40), (2, 40))])
    def test_product_recommendation_shape(self, test_input, expected):
        data = load_and_preprocess_data()
        recommender = Recommender(data)
        recommender.create_and_fit()
        suggestions_and_score = recommender.recommend_products()

