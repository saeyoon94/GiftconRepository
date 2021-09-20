from bs4 import BeautifulSoup
import os
import json
import copy

root_path = "C:/Users/mutal/Desktop/project/GiftconRepository/src/main/resources/static/search_data/"
path_dir = root_path + "html"
files = os.listdir(path_dir)
elastic_jsons = []
brands = []

for file in files :

    with open(path_dir + "/" + file, 'r', encoding="utf-8") as html:
        content = html.read()
        soup = BeautifulSoup(content, "html.parser")
        titles = soup.find_all("p", class_="LeisureListItem_title__U-d8s")
        print(len(titles))

        with open("C:/Users/mutal/Downloads/test.json", "r", encoding="utf-8") as st_json:

            st_python_brand = json.load(st_json)
            st_python_menu = copy.deepcopy(st_python_brand)

            st_python_brand["name"] = file.split(".")[0] + "_brand"
            st_python_brand["responses"][0]["messages"][0]["speech"].append(file.split(".")[0])


            st_python_menu["name"] = file.split(".")[0] + "_menu"
            st_python_menu["responses"][0]["messages"][0]["speech"].append(file.split(".")[0])


            for title in titles:

                elastic_json_brand = {}
                elastic_json_menu1 = {}
                elastic_json_menu2 = {}

                text = title.get_text()
                brand_name = text.split("]")[0][1:]
                menu = "]".join(text.split("]")[1:]).strip()
                #print(brand_name[1:], "/", menu)
                #print(menu)
                print(text)

                user_say_brand = {'isTemplate': False, 'data': [{'text': brand_name, 'userDefined': False}], 'count': 0,
                        'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}
                user_say_menu_1 = {'isTemplate': False, 'data': [{'text': text, 'userDefined': False}], 'count': 0,
                                  'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}
                user_say_menu_2 = {'isTemplate': False, 'data': [{'text': menu, 'userDefined': False}], 'count': 0,
                                  'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}

                elastic_json_brand["_index"] = "text_classifier"
                elastic_json_brand["_source"] = {"category" : file[:-5], "type" : "brand_name", "text" : brand_name}
                elastic_json_menu1["_index"] = "text_classifier"
                elastic_json_menu1["_source"] = {"category" : file[:-5], "type" : "menu", "text" : menu}
                elastic_json_menu2["_index"] = "text_classifier"
                elastic_json_menu2["_source"] = {"category": file[:-5], "type": "menu", "text": text}

                if brand_name not in brands :
                    #print(brand_name)
                    brands.append(brand_name)
                    st_python_brand["userSays"].append(user_say_brand)
                    elastic_jsons.append(elastic_json_brand)

                #st_python_menu["userSays"].append(user_say_menu_1)
                st_python_menu["userSays"].append(user_say_menu_2) #다이얼로그플로우 인텐트 학습문구 제한으로 인해 제외

                elastic_jsons.append(elastic_json_menu1)
                elastic_jsons.append(elastic_json_menu2)
                #print(len(elastic_jsons))


            with open("C:/Users/mutal/Downloads/" + file + "_brand.json", "w", encoding="utf-8") as w:
                json.dump(st_python_brand, w, ensure_ascii=False)
            with open("C:/Users/mutal/Downloads/" + file + "_menu.json", "w", encoding="utf-8") as w:
                json.dump(st_python_menu, w, ensure_ascii=False)

for i in elastic_jsons :
    print(i)
    #if i["_source"]["class"] == "brand_name" :
        #print(i)

with open(root_path + "json" + "/elastic_nlu.json", "w", encoding="utf-8") as w :
    json.dump(elastic_jsons, w, ensure_ascii=False)






















