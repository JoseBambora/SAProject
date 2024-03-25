import re
import csgostats_scraper
import pandas as pd

# Credits https://github.com/ShayneEvans/csstats_scraper

name = 'JoseBambora'
url = 'https://csstats.gg/player/76561198315415935'

# Function used to gather all player info
def get_player_info(player,player_url, find_win_percentage_regex, find_kill_death_ratio_regex, find_hltv_rating_regex, find_headshot_percentage_regex, find_adr_regex):
    # List of tuples that will be used to store all player statistics

    profile_info = csgostats_scraper.scrape_profile(player_url, player)
    player_info_scraped = csgostats_scraper.get_stats(profile_info[0],
                                                          profile_info[1],
                                                          profile_info[2],
                                                          find_win_percentage_regex,
                                                          find_kill_death_ratio_regex,
                                                          find_hltv_rating_regex,
                                                          find_headshot_percentage_regex,
                                                          find_adr_regex)
    return player_info_scraped,pd.DataFrame(profile_info[3])

def get_values():
    # Pre compiling regular expressions
    find_win_percentage_regex = re.compile(r"Win:(.*?) KPD:")
    find_kill_death_ratio_regex = re.compile(r'KPD:(.*?) Rating')
    find_hltv_rating_regex = re.compile(r'Rating:(.*?) HS:')
    find_headshot_percentage_regex = re.compile(r'HS:(.*?) ADR:')
    find_adr_regex = re.compile(r'ADR:(.*?)\\')

    # If user selected option 0, use sequential method
    player_info = get_player_info(name,url,
                                           find_win_percentage_regex,
                                           find_kill_death_ratio_regex,
                                           find_hltv_rating_regex,
                                           find_headshot_percentage_regex,
                                           find_adr_regex)
    return player_info


if __name__ == '__main__':
    x,y = get_values()
    print(x)
    print(y)
    print(y.info())
