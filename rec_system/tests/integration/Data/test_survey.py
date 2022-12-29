from src.rec_system.data.survey import update_survey_answers_file, convert_to_implicit_dataset
import pytest


class TestSurvey:
    @pytest.mark.parametrize("expected", [['Adres e-mail', 'pytanie', 'ocena']])
    def test_convert_to_implicit_dataset_from_update_survey_answers_file_shape(self, expected):
        df = update_survey_answers_file(to_csv=False)
        df = convert_to_implicit_dataset(df=df)
        assert df.columns.to_list() == expected

    @pytest.mark.parametrize("expected", [False])
    def test_convert_to_implicit_dataset_from_update_survey_answers_file_empty(self, expected):
        df = update_survey_answers_file(to_csv=False)
        df = convert_to_implicit_dataset(df=df)
        assert df.empty == expected




