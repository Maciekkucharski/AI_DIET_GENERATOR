from src.utils import load_and_preprocess_data
import pytest
import pandas as pd
import os
import pudb

ROOT_PATH = os.path.dirname(os.path.abspath(__file__))


@pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(), ValueError)])
def test_load_and_preprocess_data_with_empty_df(test_input, expected):
    with pytest.raises(expected,
                       match='The truth value of a DataFrame is ambiguous..*') as exc_info:
        load_and_preprocess_data(df=test_input)


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/empty_csv.csv", ValueError)])
def test_load_and_preprocess_data_with_empty_csv(test_input, expected):
    with pytest.raises(expected,
                       match='No columns to parse from file') as exc_info:
        load_and_preprocess_data(ratings_path=test_input)


@pytest.mark.parametrize("test_input,expected", [
    (pd.DataFrame(data={'Adres e-mail': ['foo'], 'pytanie': ['foo'], 'ocena': [2]}),
     ['Adres e-mail', 'pytanie', 'ocena'])])
def test_load_and_preprocess_data_with_df_check_columns(test_input, expected):
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        df=test_input)
    assert data.columns.tolist() == expected


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/ratings.csv", False)])
def test_load_and_preprocess_data_with_df_check_if_df_is_empty(test_input, expected):
    pudb.set_trace()
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        ratings_path=test_input)
    assert data.empty is expected


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/ratings.csv", False)])
def test_load_and_preprocess_data_with_df_check_if_user_idx_is_empty(test_input, expected):
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        ratings_path=test_input)
    assert email_order.empty is expected


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/ratings.csv", False)])
def test_load_and_preprocess_data_with_df_check_if_product_idx_is_empty(test_input, expected):
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        ratings_path=test_input)
    assert dishes_order.empty is expected


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/ratings.csv", False)])
def test_load_and_preprocess_data_with_df_check_if_user_cat_is_empty(test_input, expected):
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        ratings_path=test_input)
    assert sorted_users.empty is expected


@pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/ratings.csv", False)])
def test_load_and_preprocess_data_with_df_check_if_product_cat_is_empty(test_input, expected):
    data, email_order, dishes_order, sorted_users, sorted_dishes = load_and_preprocess_data(
        ratings_path=test_input)
    assert sorted_dishes.empty is expected
