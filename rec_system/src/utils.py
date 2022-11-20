import pandas as pd


def load_and_preprocess_data():
    df = pd.read_csv(
        "C:/Users/mkuchars/PycharmProjects/AI_DIET_GENERATOR/rec_system/src/data/ratings.csv",
        encoding="utf-8",
    )
    # Remove nans values
    df = df.dropna()

    # Get unique entries in the dataset of users and products
    users = df["Adres e-mail"].unique()
    products = df["pytanie"].unique()

    df['ocena'] = df['ocena'].astype(int)

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

    return df, user_idx, product_idx
