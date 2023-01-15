from src.rec_system.utils import load_and_preprocess_data
import pytest
import pandas as pd


class TestUtils:
    @pytest.mark.parametrize("expected", [False])
    def test_load_and_preprocess_data_no_arguments(self, expected):
        data = load_and_preprocess_data()
        assert data.empty == expected

    @pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(), None)])
    def test_load_and_preprocess_data_with_empty_df(self, test_input, expected):
        data = load_and_preprocess_data(df=test_input)
        assert data == expected

    @pytest.mark.parametrize("test_input,expected", [("./tests/unit/data/empty_csv.csv", ValueError)])
    def test_load_and_preprocess_data_with_empty_csv(self, test_input, expected):
        with pytest.raises(expected,
                           match='No columns to parse from file') as exc_info:
            load_and_preprocess_data(ratings_path=test_input)

    @pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(
        data={'Adres e-mail': ['foo_mail'], 'pytanie': ['foo_ocena'], 'ocena': [2]}), ['foo_mail'])])
    def test_load_and_preprocess_data_with_df_check_columns(self, test_input, expected):
        data = load_and_preprocess_data(
            df=test_input)
        assert data.columns.tolist() == expected

    @pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(
        data={'Adres e-mail': ['foo'], 'pytanie': ['foo'], 'ocena': [2]}), False)])
    def test_load_and_preprocess_data_with_df_check_if_df_is_not_empty(self, test_input, expected):
        data = load_and_preprocess_data(
            df=test_input)
        assert data.empty == expected

    @pytest.mark.parametrize("test_input,expected",
                             [(pd.read_csv('./tests/unit/data/ratings.csv').set_index('Adres e-mail'), [3, 9, 1, 6])])
    def test_load_and_preprocess_data_with_df_values(self, test_input, expected):
        data = load_and_preprocess_data(
            df=test_input)
        assert list(data.values[0]) == expected
