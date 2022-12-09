import pandas as pd


def get_user_profiles(df: pd.DataFrame = None, survey_path: str = './src/rec_system/data/survey.csv', to_csv: bool = False,
                      destination: str = "./src/rec_system/data/user_profiles.csv"):
    if df is None:
        df = pd.read_csv(survey_path)
    if df.empty:
        print("no data found")
        return None
    df = df[
        ['Adres e-mail',
         'W skali od 1 do 10 jak bardzo lubisz słone jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz kwaśne jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz słodkie jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz gorzkie jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz pikantne jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz tłuste jedzenie']
    ]
    df.columns = ['email', 'saltiness', 'sourness', 'sweetness', 'bitterness', 'spiciness', 'fattiness']
    df = df.sort_values(by='email')
    if to_csv:
        df.to_csv(destination, index=False)
    return df
