from src.rec_system.data.users import get_user_profiles
import pytest
import pandas as pd

foo_df = pd.DataFrame(data={'Adres e-mail': ['foo'], 'W skali od 1 do 10 jak bardzo lubisz słone jedzenie': [1],
                            'W skali od 1 do 10 jak bardzo lubisz kwaśne jedzenie': [1],
                            'W skali od 1 do 10 jak bardzo lubisz słodkie jedzenie': [1],
                            'W skali od 1 do 10 jak bardzo lubisz gorzkie jedzenie': [1],
                            'W skali od 1 do 10 jak bardzo lubisz pikantne jedzenie': [1],
                            'W skali od 1 do 10 jak bardzo lubisz tłuste jedzenie': [1]}
                      )

class TestUsers:
    @pytest.mark.parametrize("test_input,expected", [(pd.DataFrame(), None)])
    def test_get_user_data_with_empty_df(self, test_input, expected):
        df = get_user_profiles(df=test_input)
        assert df == expected


    @pytest.mark.parametrize("test_input,expected", [('./tests/unit/data/empty_csv.csv', ValueError)])
    def test_get_user_data_with_empty_csv(self, test_input, expected):
        with pytest.raises(expected, match='No columns to parse from file') as exc_info:
            get_user_profiles(survey_path=test_input)


    @pytest.mark.parametrize("expected", [False])
    def test_get_user_data_with_no_parameters(self, expected):
        df = get_user_profiles()
        assert df.empty == expected


    @pytest.mark.parametrize("test_input,expected", [(foo_df, False)])
    def test_get_user_data_with_df(self, test_input, expected):
        df = get_user_profiles(df=test_input)
        assert df.empty == expected


    @pytest.mark.parametrize("test_input,expected", [
        (foo_df, ['email', 'saltiness', 'sourness', 'sweetness', 'bitterness', 'spiciness', 'fattiness'])])
    def test_get_user_data_check_columns(self, test_input, expected):
        df = get_user_profiles(df=test_input)
        assert df.columns.tolist() == expected


