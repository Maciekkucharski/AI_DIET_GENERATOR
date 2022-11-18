import pandas as pd

pd.set_option('display.max_columns', 500)


def update_survey_answers_file():
    sheet_ids = ['1yUzQLGwdEn0zUZj7c_y5dnJi4hxk4wGQltBSxk3ZUOE', '1NxPcU-3V44ym9Slsh-HvRDDIgHzV59ySODvAzaDf9Nw',
                 '147TGE1t-TuIcVVcdlhJnweC1gYb1LpM7_q-ui_c9iHg', '1yBgvpb6DgHUhnDyRFDfqDU5S26CPHFws7Ice7A6V1jE',
                 '1m1Ts2wLX38jup3wQOp2k0HW63ebewiUXEjFus23vPuo', '1mIsMsOw1jGCuFTzPZyw0wLZyRqqD2m32SGiN1DyNH90',
                 '1RVm-aa35TxOE8f7uxlqAhTk-Lr2Kw88ehgiIeDcuUsk', '1I3JDwCXwZaWBmLUjfp_257epaUHqrk1o5vhoPPdYKdM',
                 '19EoiUu8yB4637pyEBKQdC-RYCNWcvjktxYy975zc9iU', '1ghiqVoaMkO5F8Qh7A90N8sMmouh1l5sxpkDUlTgW75E',
                 '1LVWX9ODt5nScHKuWpACEkwAbV1OtKI-XNIG5-CQ4dJc', '18Hx-gjtKAJh0Mm-M2wuNR0N2tp1Tlh_r1iBEnAcfHbE',
                 '14Jjv6nYrrbtke4zG5rBMavOyh8mhlrDSzjjvFGmunXE', '1V64x4Rvhfq0gRAEt5DEh9SylmAyaSDPSVuk6DFGNUWs',
                 '171mWbBJroQRjPYJRHB5CimWULBZcywDTSocv2YRtRWI', '1XZXBL76r8VY3IHnJuXOQ_eTYDVjvTONyEtdeISVUdok',
                 '1pT8nYF272bnO28H_Ttm1ziGcf2bLQd02Gy-x_dKa5d8', '1N2w2MAv9Oun5zLCgdB4QmDX6FlPpQXLK_cTKVCJfgAw',
                 '1ChaPNihN0HsVQRLmiwuQ9GA_8DL7FmGumq0MYgRX7AA', '1FtIGUXp1H52Nc9tYPPAMu98zcW0za6s5hsGAyKC-RJc']
    df = pd.DataFrame({})
    for sheet_id in sheet_ids:
        tmp_df = pd.read_csv(f'https://docs.google.com/spreadsheets/d/{sheet_id}/export?format=csv')
        df = pd.concat([df, tmp_df])
    df = df.drop(['Sygnatura czasowa'], axis=1)
    df.to_csv('./data/survey.csv', index=False)


def parse_survey_answers_files():
    df = pd.read_csv('./data/survey.csv')
    df.dropna()
    df = df.drop(
        ['W skali od 1 do 10 jak bardzo lubisz słone jedzenie', 'W skali od 1 do 10 jak bardzo lubisz słodkie jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz gorzkie jedzenie', 'W skali od 1 do 10 jak bardzo lubisz mięso',
         'W skali od 1 do 10 jak bardzo lubisz pikantne jedzenie',
         'W skali od 1 do 10 jak bardzo lubisz kuchnie śródziemnomorską',
         'W skali od 1 do 10 jak bardzo lubisz kuchnie azjatycką',
         'W skali od 1 do 10 jak bardzo lubisz kuchnie amerykańską',
         'W skali od 1 do 10 jak bardzo lubisz kuchnie arabską',
         'W skali od 1 do 10 jak bardzo lubisz owoce morza(również ryby)',
         'W skali od 1 do 10 jak bardzo lubisz tłuste jedzenie', 'W skali od 1 do 10 jak bardzo lubisz kuchnie Polską'
         ],
        axis=1)
    columns = df.columns
    df.T.insert(0, "questions", columns)
    df = df.pivot_table(index='Adres e-mail')
    return df




import pandas as pd
import numpy as np
ratings_matrix = pd.read_csv('./data/survey.csv')
ratings_matrix.dropna()
ratings_matrix = ratings_matrix.drop(
    ['W skali od 1 do 10 jak bardzo lubisz słone jedzenie', 'W skali od 1 do 10 jak bardzo lubisz słodkie jedzenie',
     'W skali od 1 do 10 jak bardzo lubisz gorzkie jedzenie', 'W skali od 1 do 10 jak bardzo lubisz mięso',
     'W skali od 1 do 10 jak bardzo lubisz pikantne jedzenie',
     'W skali od 1 do 10 jak bardzo lubisz kuchnie śródziemnomorską',
     'W skali od 1 do 10 jak bardzo lubisz kuchnie azjatycką',
     'W skali od 1 do 10 jak bardzo lubisz kuchnie amerykańską',
     'W skali od 1 do 10 jak bardzo lubisz kuchnie arabską',
     'W skali od 1 do 10 jak bardzo lubisz owoce morza(również ryby)',
     'W skali od 1 do 10 jak bardzo lubisz tłuste jedzenie', 'W skali od 1 do 10 jak bardzo lubisz kuchnie Polską'
     ],
    axis=1)
ratings_matrix = ratings_matrix.pivot_table(index='Adres e-mail', fill_value=0)
ratings_matrix.shape
X = ratings_matrix.T
X.head()
X.shape
X1 = X
from sklearn.decomposition import TruncatedSVD
SVD = TruncatedSVD(n_components=10)
decomposed_matrix = SVD.fit_transform(X)
decomposed_matrix.shape
correlation_matrix = np.corrcoef(decomposed_matrix)
correlation_matrix.shape
X.index[75]
i = 'Frikadellen German Meat Patties'
product_names = list(X.index)
product_ID = product_names.index(i)
product_ID
correlation_product_ID = correlation_matrix[product_ID]
correlation_product_ID.shape
Recommend = list(X.index[correlation_product_ID > 0.65])

# Removes the item already bought by the customer
Recommend.remove(i)

Recommend[0:24]