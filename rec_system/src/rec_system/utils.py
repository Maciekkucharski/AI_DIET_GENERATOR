import pandas as pd


def load_and_preprocess_data(df: pd.DataFrame = None,
                             ratings_path: str = './src/rec_system/data/ratings.csv') -> pd.DataFrame:
    """Converts results to be compatible with recommendation model.
                Parameters:
                    df (pd.DataFrame): dataframe with results from the poll
                    ratings_path (str): if dataframe was not provided or is None this is a path with csv with all ratings from the system
                Returns:
                    (pd.DataFrame) Dataframe with results modified results form poll.
    """
    if df is None:
        df = pd.read_csv(ratings_path)
    if df.empty:
        print("no data found")
        return None
    ratings_matrix = df.pivot_table(values='ocena', index='Adres e-mail', columns='pytanie', fill_value=0)
    X = ratings_matrix.T
    return X
