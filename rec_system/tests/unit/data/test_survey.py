from src.data.survey import update_survey_answers_file, convert_to_implicit_dataset
import pytest
import pandas as pd


@pytest.mark.skip(reason="long test")
@pytest.mark.parametrize("expected", [pd.read_csv("./tests/unit/data/survey.csv").columns.to_list()])
def test_update_survey_answers_file_check_columns(expected):
    df = update_survey_answers_file(to_csv=True, destination="./test.csv")
    assert df.columns.to_list() == expected


@pytest.mark.parametrize("expected", [False])
def test_update_survey_answers_file_check_if_not_empty(expected):
    df = update_survey_answers_file(to_csv=False)
    assert df.empty == expected


@pytest.mark.parametrize("test_input,expected",
                         [(pd.read_csv("./tests/unit/data/survey.csv"),
                           pd.read_csv("./tests/unit/data/ratings.csv").columns.to_list())])
def test_convert_to_implicit_dataset_check_columns(test_input, expected):
    df = convert_to_implicit_dataset(df=test_input)
    assert df.columns.to_list() == expected


@pytest.mark.parametrize("test_input,expected",
                         [(pd.read_csv("./tests/unit/data/survey.csv"),
                           False)])
def test_convert_to_implicit_dataset_no_parameters(test_input, expected):
    df = convert_to_implicit_dataset(df=test_input)
    assert df.empty == expected


@pytest.mark.parametrize("test_input,expected",
                         [(pd.DataFrame(),
                           None)])
def test_update_survey_answers_file_check_empty_df(test_input, expected):
    df = convert_to_implicit_dataset(df=test_input)
    assert df == expected


@pytest.mark.parametrize("test_input,expected", [('./tests/unit/data/empty_csv.csv', ValueError)])
def test_convert_to_implicit_dataset_with_empty_csv(test_input, expected):
    with pytest.raises(expected, match='No columns to parse from file') as exc_info:
        convert_to_implicit_dataset(survey_path=test_input)


@pytest.mark.parametrize("test_input,expected", [(pd.read_csv("./tests/unit/data/survey.csv"), False)])
def test_convert_to_implicit_dataset_with_df(test_input, expected):
    df = convert_to_implicit_dataset(df=test_input)
    assert df.empty == expected
