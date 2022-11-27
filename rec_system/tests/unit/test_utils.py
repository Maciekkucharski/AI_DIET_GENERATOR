from src.utils import load_and_preprocess_data
import pytest
import pandas as pd


@pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(), None)])
def test_load_and_preprocess_data_with_empty_df(test_input, expected):
    with pytest.raises(ValueError,
                       match='The truth value of a DataFrame is ambiguous..*') as exc_info:
        load_and_preprocess_data(df=test_input)

# def test_load_and_preprocess_data_with_csv_path():
#     dataset = load_and_preprocess_data()
