import pandas as pd

COLUMNS_TO_DROP = ['W skali od 1 do 10 jak bardzo lubisz słone jedzenie',
                   'W skali od 1 do 10 jak bardzo lubisz słodkie jedzenie',
                   'W skali od 1 do 10 jak bardzo lubisz gorzkie jedzenie',
                   'W skali od 1 do 10 jak bardzo lubisz mięso',
                   'W skali od 1 do 10 jak bardzo lubisz pikantne jedzenie',
                   'W skali od 1 do 10 jak bardzo lubisz kuchnie śródziemnomorską',
                   'W skali od 1 do 10 jak bardzo lubisz kuchnie azjatycką',
                   'W skali od 1 do 10 jak bardzo lubisz kuchnie amerykańską',
                   'W skali od 1 do 10 jak bardzo lubisz kuchnie arabską',
                   'W skali od 1 do 10 jak bardzo lubisz owoce morza(również ryby)',
                   'W skali od 1 do 10 jak bardzo lubisz tłuste jedzenie',
                   'W skali od 1 do 10 jak bardzo lubisz kuchnie Polską',
                   'W skali od 1 do 10 jak bardzo lubisz kwaśne jedzenie'
                   ]
SHEET_IDS = ['1yUzQLGwdEn0zUZj7c_y5dnJi4hxk4wGQltBSxk3ZUOE', '1NxPcU-3V44ym9Slsh-HvRDDIgHzV59ySODvAzaDf9Nw',
             '147TGE1t-TuIcVVcdlhJnweC1gYb1LpM7_q-ui_c9iHg', '1yBgvpb6DgHUhnDyRFDfqDU5S26CPHFws7Ice7A6V1jE',
             '1m1Ts2wLX38jup3wQOp2k0HW63ebewiUXEjFus23vPuo', '1mIsMsOw1jGCuFTzPZyw0wLZyRqqD2m32SGiN1DyNH90',
             '1RVm-aa35TxOE8f7uxlqAhTk-Lr2Kw88ehgiIeDcuUsk', '1I3JDwCXwZaWBmLUjfp_257epaUHqrk1o5vhoPPdYKdM',
             '19EoiUu8yB4637pyEBKQdC-RYCNWcvjktxYy975zc9iU', '1ghiqVoaMkO5F8Qh7A90N8sMmouh1l5sxpkDUlTgW75E',
             '1LVWX9ODt5nScHKuWpACEkwAbV1OtKI-XNIG5-CQ4dJc', '18Hx-gjtKAJh0Mm-M2wuNR0N2tp1Tlh_r1iBEnAcfHbE',
             '14Jjv6nYrrbtke4zG5rBMavOyh8mhlrDSzjjvFGmunXE', '1V64x4Rvhfq0gRAEt5DEh9SylmAyaSDPSVuk6DFGNUWs',
             '171mWbBJroQRjPYJRHB5CimWULBZcywDTSocv2YRtRWI', '1XZXBL76r8VY3IHnJuXOQ_eTYDVjvTONyEtdeISVUdok',
             '1pT8nYF272bnO28H_Ttm1ziGcf2bLQd02Gy-x_dKa5d8', '1N2w2MAv9Oun5zLCgdB4QmDX6FlPpQXLK_cTKVCJfgAw',
             '1ChaPNihN0HsVQRLmiwuQ9GA_8DL7FmGumq0MYgRX7AA', '1FtIGUXp1H52Nc9tYPPAMu98zcW0za6s5hsGAyKC-RJc']


def update_survey_answers_file(to_csv: bool = True, destination: str = "./src/rec_system/data/survey.csv"):
    sheet_ids = SHEET_IDS
    df = pd.DataFrame({})
    for sheet_id in sheet_ids:
        tmp_df = pd.read_csv(f'https://docs.google.com/spreadsheets/d/{sheet_id}/export?format=csv')
        df = pd.concat([df, tmp_df])
    df = df.drop(['Sygnatura czasowa'], axis=1)
    if to_csv:
        df.to_csv(destination, index=False)
    df.reset_index(drop=True, inplace=True)
    return df


def convert_to_implicit_dataset(df: pd.DataFrame = None, survey_path: str = './src/rec_system/data/survey.csv',
                                to_csv: bool = False,
                                destination: str = "./src/rec_system/data/ratings.csv"):
    if df is None:
        df = pd.read_csv(survey_path)
    if df.empty:
        print("no data found")
        return None
    df.dropna()
    df = df.drop(COLUMNS_TO_DROP, axis=1)
    df_dict = df.to_dict()
    modified_dict = dict()

    i = 0
    for question, ratings_dict in df_dict.items():
        if question == 'Adres e-mail':
            continue
        for index, rating in ratings_dict.items():
            modified_dict[i] = {
                'Adres e-mail': df['Adres e-mail'][index],
                'pytanie': question,
                'ocena': rating
            }
            i += 1
    df = pd.DataFrame.from_dict(modified_dict).T
    df = df[df['ocena'].notna()]
    if to_csv:
        df.to_csv(destination, index=False)
    return df.reset_index(drop=True)
