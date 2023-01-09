import requests
import pandas as pd
import traceback


def get_all_recipes(api_key: str, to_csv: bool = False,
                    destination: str = "./src/rec_system/data/all_recipes.csv") -> pd.DataFrame:
    """Converts results to be compatible with recommendation model.
                Parameters:
                    api_key (str): api key that will allow you to use spoonacular API
                    to_csv (bool): this parameter specifies if the dataframe should be saved to a csv
                    destination (str): specifies path for csv to be saved in
                Returns:
                    (pd.DataFrame) returns dataframe with all recipes that are from european cuisine
    """
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


def get_filtered_recipes(df: pd.DataFrame = None,
                         unfiltered_recipes_path: str = './src/rec_system/data/all_recipes.csv',
                         ids: list = None,
                         ids_path: str = './src/rec_system/data/ids.csv',
                         to_csv: bool = False,
                         destination: str = "./src/rec_system/data/filtered_recipes.csv") -> pd.DataFrame:
    """Converts results to be compatible with recommendation model.
                Parameters:
                    df (pd.DataFrame): Dataframe with recipes
                    unfiltered_recipes_path (str): if dataframe was not provided or is None this is a path with csv with recipes
                    ids (list): list of desired ids that are to be filtered
                    ids_path (str): if list of IDS was not provided or is None this is a path with csv with ids of recipes to filter
                    to_csv (bool): this parameter specifies if the dataframe should be saved to a csv
                    destination (str): specifies path for csv to be saved in
                Returns:
                    (pd.DataFrame) returns dataframe with specified filtered recipes
    """
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


def get_taste(recipe_id: int, api_key: str) -> dict:
    """Gets taste profile of provided recipe form spoonacular API
                Parameters:
                    recipe_id (int): id of recipe
                    api_key (str): api key that will allow you to use spoonacular API
                Returns:
                    (pd.DataFrame) returns Dictionary with taste profile from the recipe
    """
    taste_url = f"https://api.spoonacular.com/recipes/{recipe_id}/tasteWidget.json/"
    PARAMS = {
        'apiKey': api_key,
    }
    return requests.get(
        url=taste_url,
        params=PARAMS
    ).json()


def update_taste(df: pd.DataFrame, recipe_id: int, api_key: str) -> None:
    """Updates the taste profile of a single recipe based on spoonacular API
                Parameters:
                    df (pd.DataFrame): Dataframe with recipes
                    recipe_id (int): id of recipe
                    api_key (str): api key that will allow you to use spoonacular API
                Returns:
    """
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
                       destination: str = "./src/rec_system/data/recipes.csv") -> pd.DataFrame:
    """Updates the taste profile of provided recipes based on spoonacular API
                Parameters:
                    df (pd.DataFrame): Dataframe with recipes
                    filtered_recipes_path (str): if dataframe was not provided or is None this is a path with csv with recipes
                    destination (str): specifies path for csv to be saved in
                    api_key (str): api key that will allow you to use spoonacular API
                Returns:
                    (pd.DataFrame) returns dataframe with specified filtered recipes with taste profiles
    """
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


def get_dish_id(dish_name: str, df: pd.DataFrame = None,
                recipes_path: str = './src/rec_system/data/recipes.csv') -> pd.DataFrame:
    """Gets dish id based on the dish name
                Parameters:
                    dish_name (str): name if a dish to get id from
                    df (pd.DataFrame): Dataframe with recipes
                    recipes_path (str): if dataframe was not provided or is None this is a path with csv with recipes
                Returns:
                    (pd.DataFrame) returns dataframe with specified filtered recipes with taste profiles
    """
    if df is None:
        df = pd.read_csv(recipes_path)
    if df.empty:
        print("no data found")
        return None
    df = df.loc[df['title'] == dish_name]['id'].to_list()
    return df
