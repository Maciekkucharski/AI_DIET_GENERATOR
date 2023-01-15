# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.4] - 2023-01-15
- replaced recommendation engine from implicit methods(wrond way) to using truncated svd 

## [0.0.2] - 2022-12-16

## Fixed
- fixed labels related to recipe name

## [0.0.1] - 2022-12-14
## Added
####Utils
- load_and_preprocess_data
####Recommender
- _get_sparse_matrix
- _get_model
- InternalStatusError Class
- Recommender Class
- create_and_fit
- recommend_products
- similar_users
- compare_taste_with_taste_profile
####Recipes
- get_all_recipes
- get_filtered_recipes
- get_taste
- update_taste
- add_taste_profiles
- get_dish_id
####Survey
- update_survey_answers_file
- convert_to_implicit_dataset
####Users
- get_user_profiles