from src.data.survey import update_survey_answers_file, convert_to_implicit_dataset
import pytest


class TestSurvey:
    @pytest.mark.parametrize("expected", [False])
    def test_convert_to_implicit_dataset_from_update_survey_answers_file(self, expected):
        df = update_survey_answers_file(to_csv=False)
        df = convert_to_implicit_dataset(df=df)
        print(df)
