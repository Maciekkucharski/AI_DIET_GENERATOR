from src.rec_system.data.recipes import get_all_recipes, get_filtered_recipes, get_taste, update_taste, add_taste_profiles, \
    get_dish_id
import pytest
import pandas as pd

API_KEY = '947992fa499844bb9e8e4a026e65a037'

class TestRecipesNoApi:
    @pytest.mark.parametrize("expected", [False])
    def test_get_filtered_recipes_no_parameters(self, expected):
        df = get_filtered_recipes()
        assert df.empty == expected


    @pytest.mark.parametrize("test_input,expected", [(pd.read_csv('./tests/unit/data/all_recipes.csv'), 8)])
    def test_get_filtered_recipes_no_ids_parameter(self, test_input, expected):
        df = get_filtered_recipes(df=test_input)
        assert len(df) == expected


    @pytest.mark.parametrize("test_input,expected", [((pd.DataFrame(
        data={'id': [1, 2, 3, 4, 5, 6, 7, 8, 9, 10],
              'titles': ['foo_1', 'foo_2', 'foo_3', 'foo_4', 'foo_5', 'foo_6', 'foo_7', 'foo_8', 'foo_9', 'foo_10', ]}),
                                                       [1, 2, 3, 4, 5]), 5)])
    def test_get_filtered_recipes_from_df(self, test_input, expected):
        df = get_filtered_recipes(df=test_input[0], ids=test_input[1])
        assert len(df) == expected


    @pytest.mark.parametrize("test_input,expected", [((pd.read_csv('./tests/unit/data/all_recipes.csv'),
                                                       [715769, 715495, 4, 5]), 2)])
    def test_get_filtered_recipes_from_csv(self, test_input, expected):
        df = get_filtered_recipes(df=test_input[0], ids=test_input[1])
        assert len(df) == expected


    @pytest.mark.parametrize("test_input,expected", [((pd.read_csv('./tests/unit/data/all_recipes.csv'), []), 0)])
    def test_get_filtered_recipes_empty_list(self, test_input, expected):
        df = get_filtered_recipes(df=test_input[0], ids=test_input[1])
        assert len(df) == expected


    @pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(), None)])
    def test_get_filtered_recipes_empty_df(self, test_input, expected):
        df = get_filtered_recipes(df=test_input)
        assert df == expected


    @pytest.mark.parametrize("test_input,expected",
                             [((pd.read_csv('tests/unit/data/recipes.csv'), 'Simple Skillet Lasagna'), [715573])])
    def test_get_dish_df(self, test_input, expected):
        df = get_dish_id(df=test_input[0], dish_name=test_input[1])
        assert df == expected


    @pytest.mark.parametrize("test_input,expected", [((pd.DataFrame(), 'foo_dish_name'), None)])
    def test_get_dish_empty_df(self, test_input, expected):
        df = get_dish_id(df=test_input[0], dish_name=test_input[1])
        assert df == expected


    @pytest.mark.parametrize("test_input,expected", [((pd.read_csv('tests/unit/data/recipes.csv'), 'foo_dish_name'), [])])
    def test_get_dish_wrong_name(self, test_input, expected):
        df = get_dish_id(df=test_input[0], dish_name=test_input[1])
        assert df == expected


@pytest.mark.skip(reason="tests requires api requests")
class TestRecipesApi:
    """
    Tests that require api connection
    """

    @pytest.mark.parametrize("expected", [False])
    def test_get_all_recipes_if_empty(self, expected):
        df = get_all_recipes(api_key=API_KEY)
        assert df.empty == expected

    @pytest.mark.parametrize("expected", [['id', 'title']])
    def test_get_all_recipes_check_columns(self, expected):
        df = get_all_recipes(api_key=API_KEY)
        assert df.columns.to_list() == expected

    @pytest.mark.parametrize("test_input, expected", [('foo_api_key', KeyError)])
    def test_get_all_recipes_wrong_api_key(self, test_input, expected):
        with pytest.raises(expected) as exc_info:
            get_all_recipes(api_key=test_input)

    @pytest.mark.parametrize("test_input, expected", [(('foo_api_key', 1), 'failure')])
    def test_get_taste_wrong_api_key(self, test_input, expected):
        json = get_taste(recipe_id=1, api_key=test_input[0])
        assert json['status'] == expected

    @pytest.mark.parametrize("test_input, expected", [(-1, 'failure')])
    def test_get_taste_wrong_id(self, test_input, expected):
        json = get_taste(recipe_id=test_input, api_key=API_KEY)
        assert json['status'] == expected

    @pytest.mark.parametrize("test_input, expected", [(1, (7, False))])
    def test_get_taste_correct_input(self, test_input, expected):
        json = get_taste(recipe_id=test_input, api_key=API_KEY)
        assert (len(json), None in json.values()) == expected

    @pytest.mark.parametrize("test_input, expected", [((pd.DataFrame(
        data={'id': [715769, 715495],
              'titles': ['Broccolini Quinoa Pilaf', 'Turkey Tomato Cheese Pizza']}), 'foo_api_key'), True)])
    def test_add_taste_profiles_wrong_api_key(self, test_input, expected):
        df = add_taste_profiles(api_key=test_input[1], df=test_input[0])
        assert df.equals(test_input[0]) == expected

    @pytest.mark.skip(reason="test requires too many api requests")
    @pytest.mark.parametrize("expected", [(255, 9)])
    def test_add_taste_profiles_no_parameters(self, expected):
        df = add_taste_profiles(api_key=API_KEY)
        assert df.shape == expected

    @pytest.mark.parametrize("test_input, expected", [(pd.DataFrame(
        data={'id': [715769, 715573],
              'titles': ['Broccolini Quinoa Pilaf', 'Simple Skillet Lasagna']}),
                                                       (pd.read_csv('tests/unit/data/recipes.csv'), True))])
    def test_add_taste_profiles_df(self, test_input, expected):
        df = add_taste_profiles(api_key=API_KEY, df=test_input)
        assert df.equals(expected[0]) == expected[1]

    @pytest.mark.parametrize("expected", [None])
    def test_add_taste_profiles_empty_df(self, expected):
        df = add_taste_profiles(api_key=API_KEY, df=pd.DataFrame())
        assert df == expected

    @pytest.mark.parametrize("test_input, expected", [((pd.DataFrame(), 1), KeyError)])
    def test_update_taste_empty_df(self, test_input, expected):
        with pytest.raises(expected) as exc_info:
            update_taste(api_key=API_KEY, df=test_input[0], recipe_id=test_input[1])

    @pytest.mark.parametrize("test_input, expected", [(-1, 1)])
    def test_update_taste_wrong_id(self, test_input, expected):
        df = update_taste(api_key=API_KEY, df=pd.DataFrame(), recipe_id=test_input)
        assert df is None
