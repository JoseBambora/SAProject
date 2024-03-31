import csgostats_scraper
import logging
from utils import Utils
from flask import Flask, jsonify

# Credits https://github.com/ShayneEvans/csstats_scraper

app = Flask(__name__)


logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
util = Utils()


def get_stats(id):
    site = 'https://csstats.gg/player/'
    url = f'{site}{id}'
    return csgostats_scraper.scrape_profile(url, util)


@app.route('/stats/<string:idPlayer>/', methods=['GET'])
def getCsStats(idPlayer):
    data = get_stats(idPlayer)
    return jsonify({'data': data })

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')