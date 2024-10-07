import pymongo
from films import Films
from consts import PAGES, MONGO_CLIENT_PATH, CLIENT
import logging


logging.basicConfig(level=logging.INFO,  # Уровень логирования
    format='%(asctime)s - %(levelname)s - %(message)s',  # Формат логов
    handlers=[logging.FileHandler("films.log",encoding="utf-8")])


def db_connect(db_name):
    logging.info("Подключение к базе данных MongoDB")
    myclient = pymongo.MongoClient(MONGO_CLIENT_PATH)
    mydb = myclient[CLIENT]
    mycol = mydb[db_name]
    return mycol


def fill_db():
    films = Films(PAGES)
    logging.info(f"Возвращение всех данных: {len(films.give_all_data())} фильмов")
    mycol = db_connect("films")
    mylist = films.give_all_data()
    logging.info(f"Вставка {len(mylist)} фильмов в базу данных")

    try:
        x = mycol.insert_many(mylist)
        logging.info(f"Идентификаторы вставленных документов: {x.inserted_ids}")
    except Exception as e:
        logging.error(f"Ошибка при вставке данных в MongoDB: {e}")
