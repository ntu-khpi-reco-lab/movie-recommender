import logging
import requests
import os
from dotenv import load_dotenv

# Настройка логирования
logging.basicConfig(level=logging.INFO,  # Уровень логирования
    format='%(asctime)s - %(levelname)s - %(message)s',  # Формат логов
    handlers=[logging.FileHandler("films.log",encoding="utf-8")])

load_dotenv()
TOKEN = os.getenv('TOKEN')

class Films:
    def __init__(self, numb_page):
        self.header = {
            'accept': 'application/json',
            'Authorization': TOKEN
        }
        self.page = numb_page
        self.films = []
        self.genres = []
        self.fetch_data()
        self.fetch_genre()

    def fetch_data(self):
        logging.info(f"Загрузка фильмов, страниц: {self.page}")
        for i in range(1, self.page + 1):
            response = requests.get(
                f'https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page={i}',
                headers=self.header
            )
            if response.status_code == 200:
                self.films.extend(response.json()['results'])
            else:
                logging.error(f"Ошибка при загрузке данных для страницы {i}")

    def fetch_genre(self):
        logging.info("Загрузка жанров")
        response = requests.get('https://api.themoviedb.org/3/genre/movie/list', headers=self.header)
        if response.status_code == 200:
            self.genres = response.json()
        else:
            logging.error("Ошибка при загрузке жанров")

    def give_all_data(self):
        logging.info(f"Данные {self.films} ")
        return self.films

