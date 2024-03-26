import csgostats_scraper
import logging
import json
from utils import Utils

# Credits https://github.com/ShayneEvans/csstats_scraper

name = 'JoseBambora'
id = '76561198315415935'
site = 'https://csstats.gg/player/'
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
util = Utils()

def get_stats(id):
    url = f'{site}{id}'
    return csgostats_scraper.scrape_profile(url, util)


if __name__ == '__main__':
    data = get_stats(id)
    with open('file.json','w') as f:
        json.dump(data,f,indent=4,ensure_ascii=False)
    # util.close()
