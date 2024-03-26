
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

class Utils:
    def create_driver(self):
        op = webdriver.ChromeOptions()
        op.add_argument("--headless=new")
        op.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")
        op.add_argument("--no-sandbox")
        self.driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=op)
        

    def __init__(self):
        self.create_driver()
        self.index1 = {
            'played':0,
            'won':1,
            'lost':2,
            'tied':3,
            'kills':4,
            'deaths':5,
            'assists':6,
            'headshots':7,
            'damage':8,
            'rounds':9
        }
        self.names1 = list(self.index1.keys())
        self.index2 = {
            'date' : 0,
            'map' : 2,
            'score':3,
            'kills':5,
            'deaths':6,
            'assists':7,
            'HS':9,
            'ADR':10,
            'rating':19
        }
        self.names2 = list(self.index2.keys())

    def close(self):
        self.driver.quit()