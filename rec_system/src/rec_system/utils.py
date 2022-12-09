import pandas as pd


def load_and_preprocess_data(df: pd.DataFrame = None,
                             ratings_path: str = './src/rec_system/data/ratings.csv', ):
    if df is None:
        df = pd.read_csv(
            ratings_path,
            encoding="utf-8",
        )
    if df.empty:
        print("no data found")
        return (None, None, None, None, None)
    # Remove nans values

    df = df.dropna()
    # Get unique entries in the dataset of users and products
    users = df["Adres e-mail"].unique()
    products = df["pytanie"].unique()

    # convert ratings to int type
    df['ocena'] = df['ocena'].astype(int)

    # Create a categorical type for users and product. User ordered to ensure
    # reproducibility
    user_cat = pd.CategoricalDtype(categories=sorted(users), ordered=True)
    product_cat = pd.CategoricalDtype(categories=sorted(products), ordered=True)

    # Transform and get the indexes of the columns
    user_idx = df["Adres e-mail"].astype(user_cat).cat.codes
    product_idx = df["pytanie"].astype(product_cat).cat.codes

    # Add the categorical index to the starting dataframe
    df["Adres e-mail"] = user_idx
    df["pytanie"] = product_idx

    return df, user_idx, product_idx, user_cat.categories, product_cat.categories
