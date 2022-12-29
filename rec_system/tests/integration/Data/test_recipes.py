import pandas as pd
import pytest

from src.rec_system.data.recipes import get_all_recipes, get_filtered_recipes, add_taste_profiles

API_KEY = '947992fa499844bb9e8e4a026e65a037'


class TestRecipes:
    @pytest.mark.parametrize("expected", [sorted(list(map(int, pd.read_csv('./src/rec_system/data/ids.csv'))))])
    def test_get_all_recipes_to_get_filtered_recipes(self, expected):
        all_recipes = get_all_recipes(api_key=API_KEY)
        filtered_recipes = get_filtered_recipes(df=all_recipes)
        assert sorted(filtered_recipes['id'].to_list()) == expected

    @pytest.mark.skip(reason="test requires too many api requests")
    @pytest.mark.parametrize("expected", [pd.read_csv('./src/rec_system/data/recipes.csv')])
    def test_get_all_recipes_to_get_filtered_recipes_to_add_taste_profiles(self, expected):
        all_recipes = get_all_recipes(api_key=API_KEY)
        filtered_recipes = get_filtered_recipes(df=all_recipes)
        recipes = add_taste_profiles(api_key=API_KEY, df=filtered_recipes)
        assert recipes == expected
