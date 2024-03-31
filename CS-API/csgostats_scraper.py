from selenium.webdriver.common.by import By
import logging
from utils import Utils

def is_int(value):
    try:
        return int(value)
    except ValueError:
        return None

def is_float(value):
    try:
        return float(value)
    except ValueError:
        return None

# Uses selenium to obtain stats from csgostats.gg
def scrape_profile(url, utils : Utils):
    matches = get_matches(utils,url)
    metadata = get_metadata(utils,url)
    return {
        'metadata' : metadata,
        'matches' : matches
    }


def get_type_value(value):
    i = is_int(value)
    if i != None:
        return i
    else:
        f = is_float(value)
        if f != None:
            return f
        else:
            return value

def build_res_metadata(util : Utils,name,rating,kd,overall_stats):
    stats = {n : get_type_value(overall_stats[util.index1[n]]) for n in util.names1 } 
    stats.update({'name': name,'rating' : float(rating),'kpd' : float(kd)})
    return stats

def get_metadata(util : Utils,url):
    logging.info("Task Started: Get metadata")
    driver = util.driver
    driver.get(url)
    name = driver.find_element(By.ID,'player-name').text
    rating = driver.find_element(By.ID,'rating').text
    kd = driver.find_element(By.ID,'kpd').text
    overall_stats = driver.find_elements(By.CLASS_NAME, "total-stat")
    overall_stats = [elem.find_element(By.CLASS_NAME, "total-value").get_attribute('innerHTML') for elem in overall_stats]
    logging.info("Task Completed: Get metadata")
    return build_res_metadata(util,name,rating,kd,overall_stats)

def get_element(html, index):
    return html[index].get_attribute('innerText').strip()

def scrap_row(util : Utils ,row):
    tds = row.find_elements(By.TAG_NAME,'td')
    return {name : get_type_value(get_element(tds,util.index2[name])) for name in util.names2}

def get_matches(util : Utils,player_profile):
    logging.info("Task Started: Get matches stats")
    driver = util.driver
    driver.get(player_profile+'#/matches')
    rows = driver.find_elements(By.CLASS_NAME, "p-row.js-link")
    res = [scrap_row(util, row) for row in rows]
    logging.info("Task Completed: Get matches stats")
    return res