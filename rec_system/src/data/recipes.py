import requests
import pandas as pd
import traceback


def get_all_recipes(api_key: str, to_csv: bool = False, destination: str = "./data/all_recipes.csv"):
    URL_GET_ALL = "https://api.spoonacular.com/recipes/complexSearch/"
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
    if to_csv:
        european_recipes.to_csv(destination, index=False)
    return european_recipes


def get_filtered_recipes(df: pd.DataFrame = None, unfiltered_recipes_path: str = './data/all_recipes.csv',
                         ids: pd.DataFrame = None,
                         ids_path: str = './ids.csv',
                         to_csv: bool = False, destination: str = "./data/filtered_recipes.csv"):
    if not df:
        df = pd.read_csv(unfiltered_recipes_path)
    if not ids:
        ids = list(map(int, pd.read_csv(ids_path)))
    df = df[df['id'].isin(ids)]
    if to_csv:
        df.to_csv(destination, index=False)
    return df


def get_taste(recipe_id: id, api_key: str):
    taste_url = f"https://api.spoonacular.com/recipes/{recipe_id}/tasteWidget.json/"
    PARAMS = {
        'apiKey': api_key,
    }
    return requests.get(
        url=taste_url,
        params=PARAMS
    ).json()


def update_taste(df: pd.DataFrame, recipe_id: int, api_key: str):
    taste_dict = get_taste(recipe_id, api_key)
    df.loc[df['id'] == recipe_id, ['sweetness', 'saltiness', 'sourness', 'bitterness', 'savoriness', 'fattiness',
                                   'spiciness']] = (
        taste_dict['sweetness'], taste_dict['saltiness'], taste_dict['sourness'], taste_dict['bitterness'],
        taste_dict['savoriness'], taste_dict['fattiness'], taste_dict['spiciness']
    )


def add_taste_profiles(api_key: str, df: pd.DataFrame = None,
                       filtered_recipes_path: str = './data/filtered_recipes.csv', ids: pd.DataFrame = None,
                       ids_path: str = './ids.csv', to_csv: bool = False, destination: str = "./data/recipes.csv"):
    if not ids:
        ids = list(map(int, pd.read_csv(ids_path)))
    if not df:
        df = pd.read_csv(filtered_recipes_path)
    for id in ids:
        try:
            update_taste(df, id, api_key)
        except:
            traceback.print_exc()
    if to_csv:
        df.to_csv(destination, index=False)
    return df
