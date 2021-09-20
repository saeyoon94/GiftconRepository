import json
from elasticsearch import Elasticsearch, helpers

root_path = "C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/search_data/json/elastic_nlu.json"

es = Elasticsearch('localhost:9200')

with open(root_path, 'r', encoding="utf-8") as json_file:
    search_data = json.load(json_file)
    print(len(search_data))

    helpers.bulk(es, search_data)