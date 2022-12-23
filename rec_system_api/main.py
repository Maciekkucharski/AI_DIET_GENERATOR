from fastapi import FastAPI
from src.rec_system.engine.recommender import Recommender, compare_taste_with_taste_profile
from src.rec_system.utils import load_and_preprocess_data
app = FastAPI()


@app.get("/generate_diet")
async def root():
    return {"message": "Hello World"}