import requests
import pandas as pd
import traceback


def get_all_recipes(api_key: str, to_csv: bool = False, destination: str = "./src/rec_system/data/all_recipes.csv"):
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


def get_filtered_recipes(df: pd.DataFrame = None, unfiltered_recipes_path: str = './src/rec_system/data/all_recipes.csv',
                         ids: list = None,
                         ids_path: str = './src/rec_system/data/ids.csv',
                         to_csv: bool = False, destination: str = "./src/rec_system/data/filtered_recipes.csv"):
    if df is None:
        df = pd.read_csv(unfiltered_recipes_path)
    if df.empty:
        print("no data found in dataframe")
        return None
    if ids is None:
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
    if len(taste_dict) != 7:
        print("no such recipe id")
        return None
    df.loc[df['id'] == recipe_id, ['sweetness', 'saltiness', 'sourness', 'bitterness', 'savoriness', 'fattiness',
                                   'spiciness']] = (
        taste_dict['sweetness'], taste_dict['saltiness'], taste_dict['sourness'], taste_dict['bitterness'],
        taste_dict['savoriness'], taste_dict['fattiness'], taste_dict['spiciness']
    )


def add_taste_profiles(api_key: str, df: pd.DataFrame = None,
                       filtered_recipes_path: str = './src/rec_system/data/filtered_recipes.csv', to_csv: bool = False,
                       destination: str = "./src/rec_system/data/recipes.csv"):
    if df is None:
        df = pd.read_csv(filtered_recipes_path)
    if df.empty:
        print("no data found")
        return None
    for id in df['id'].to_list():
        try:
            update_taste(df, id, api_key)
        except:
            traceback.print_exc()
    if to_csv:
        df.to_csv(destination, index=False)
    return df


def get_dish_id(dish_name: str, df: pd.DataFrame = None, recipes_path: str = './src/rec_system/data/recipes.csv'):
    if df is None:
        df = pd.read_csv(recipes_path)
    if df.empty:
        print("no data found")
        return None
    df = df.loc[df['titles'] == dish_name]['id'].to_list()
    return df
