from implicit.als import AlternatingLeastSquares
from implicit.nearest_neighbours import bm25_weight
from scipy.sparse import csr_matrix
from typing import Dict, Any


MODEL = {
    "als": AlternatingLeastSquares,
}


def _get_sparse_matrix(values, user_idx, product_idx):
    return csr_matrix(
        (values, (user_idx, product_idx)),
        shape=(len(user_idx.unique()), len(product_idx.unique())),
    )


def _get_model(name: str, **params):
    model = MODEL.get(name)
    if model is None:
        raise ValueError("No model with name {}".format(name))
    return model(**params)


class InternalStatusError(Exception):
    pass


class Recommender:
    def __init__(
            self,
            values,
            user_idx,
            product_idx,
    ):
        self.user_product_matrix = _get_sparse_matrix(values, user_idx, product_idx)
        self.user_idx = user_idx
        self.product_idx = product_idx

        self.model = None
        self.fitted = False

    def create_and_fit(
            self,
            model_name: str,
            model_params: Dict[str, Any] = {},
    ):
        data = bm25_weight(
            self.user_product_matrix,
            K1=1.2,
            B=0.75,
        )
        self.model = _get_model(model_name, **model_params)
        self.fitted = True
        self.model.fit(data)
        return self

    def recommend_products(
            self,
            user_id,
            items_to_recommend=5,
    ):
        """Finds the recommended items for the user.
        Returns:
            (items, scores) pair, of suggested item and score.
        """

        if not self.fitted:
            raise InternalStatusError(
                "Fit the model before trying to recommend"
            )

        return self.model.recommend(
            user_id,
            self.user_product_matrix[user_id],
            filter_already_liked_items=False,
            N=items_to_recommend,
        )
