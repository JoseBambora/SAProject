from selenium.webdriver.common.by import By
import logging
from utils import Utils

# Uses selenium to obtain stats from csgostats.gg
def scrape_profile(url, utils : Utils):
    matches = get_matches(utils,url)
    metadata = get_metadata(utils,url)
    return {
        'metadata' : metadata,
        'matches' : matches
    }

def build_res_metadata(util : Utils,name,rating,kd,overall_stats):
    stats = {n : overall_stats[util.index1[n]] for n in util.names1 } 
    return {
        'name': name,
        'rating' : rating,
        'kpd' : kd,
        'stats' : stats
    }

def get_metadata(util : Utils,url):
    logging.info("Task started: Get metadata")
    driver = util.driver
    driver.get(url)
    name = driver.find_element(By.ID,'player-name').text
    rating = driver.find_element(By.ID,'rating').text
    kd = driver.find_element(By.ID,'kpd').text
    overall_stats = driver.find_elements(By.CLASS_NAME, "total-stat")
    overall_stats = [elem.find_element(By.CLASS_NAME, "total-value").get_attribute('innerHTML') for elem in overall_stats]
    logging.info("Task completed: Get metadata")
    return build_res_metadata(util,name,rating,kd,overall_stats)

def get_element(html, index):
    return html[index].get_attribute('innerText')

def scrap_row(util : Utils ,row):
    tds = row.find_elements(By.TAG_NAME,'td')
    return {name : get_element(tds,util.index2[name]) for name in util.names2}

def get_matches(util : Utils,player_profile):
    logging.info("Task Start: Get matches stats")
    driver = util.driver
    driver.get(player_profile+'#/matches')
    rows = driver.find_elements(By.CLASS_NAME, "p-row.js-link")
    res = [scrap_row(util, row) for row in rows]
    logging.info("Task Completed: Get matches stats")
    return res