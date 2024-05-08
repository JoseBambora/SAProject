import csgostats_scraper
import logging
from utils import Utils
from flask import Flask, jsonify

# Credits https://github.com/ShayneEvans/csstats_scraper

app = Flask(__name__)

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
util = Utils()

cache = {}

def get_stats(id):
    if not id in cache.keys():
        site = 'https://csstats.gg/player/'
        url = f'{site}{id}'
        cache[id] = csgostats_scraper.scrape_profile(url, util)
    return cache[id]


@app.route('/stats/<string:idPlayer>/', methods=['GET'])
def getCsStats(idPlayer):
    data = get_stats(idPlayer)
    return jsonify(data)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')