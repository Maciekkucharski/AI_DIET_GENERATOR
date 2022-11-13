import requests
import pandas as pd

URL_GET_ALL = "https://api.spoonacular.com/recipes/complexSearch/"


def get_all_recipes(api_key: str):
    european_recipes = pd.DataFrame()
    for i in range(5):
        PARAMS_GET_ALL = {
            'apiKey': api_key,
            'cuisine': 'European',
            'number': '100',
            'offset': 100 * i
        }
        tmp = pd.DataFrame(requests.get(
            url=URL_GET_ALL,
            params=PARAMS_GET_ALL).json()['results'],
                           columns=('id', 'title')
                           )
        european_recipes = pd.concat([european_recipes, tmp])
    return european_recipes


def get_filtered_recipes():
    df = pd.read_csv('./data/unfiltered_recipes.csv')
    ids = list(map(int, pd.read_csv('./data/ids.csv')))
    df = df[df['id'].isin(ids)]
    return df


def _get_taste(recipe_id: id, api_key: str):
    taste_url = f"https://api.spoonacular.com/recipes/{recipe_id}/tasteWidget.json/"
    PARAMS = {
        'apiKey': api_key,
    }
    return requests.get(
        url=taste_url,
        params=PARAMS
    ).json()


def update_taste(df: pd.DataFrame, recipe_id: int):
    taste_dict = _get_taste(recipe_id)
    df.loc[df['id'] == recipe_id, ['sweetness', 'saltiness', 'sourness', 'bitterness', 'savoriness', 'fattiness',
                                   'spiciness']] = (
        taste_dict['sweetness'], taste_dict['saltiness'], taste_dict['sourness'], taste_dict['bitterness'],
        taste_dict['savoriness'], taste_dict['fattiness'], taste_dict['spiciness']
    )


def get_processed_data():
    ids = list(map(int, pd.read_csv('./data/ids_.csv')))
    df = pd.read_csv('./data/filtered_recipes.csv')
    for id in ids:
        try:
            update_taste(df, id)
        except:
            print("out of requests")
    return df





