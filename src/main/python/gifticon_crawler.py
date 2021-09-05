from bs4 import BeautifulSoup
import os
import json
import copy

path_dir = "C:/Users/mutal/Desktop/html"
files = os.listdir(path_dir)

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
                text = title.get_text()
                brand_name = text.split("]")[0][1:]
                menu = "]".join(text.split("]")[1:]).strip()
                #print(brand_name[1:], "/", menu)
                print(menu)
                brands = []

                user_say_brand = {'isTemplate': False, 'data': [{'text': brand_name, 'userDefined': False}], 'count': 0,
                        'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}
                user_say_menu_1 = {'isTemplate': False, 'data': [{'text': text, 'userDefined': False}], 'count': 0,
                                  'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}
                user_say_menu_2 = {'isTemplate': False, 'data': [{'text': menu, 'userDefined': False}], 'count': 0,
                                  'id': '6e3d2bfc-cf30-4cf2-88b2-3326be293583', 'updated': None}

                if brand_name not in brands :
                    brands.append(brand_name)
                    st_python_brand["userSays"].append(user_say_brand)

                #st_python_menu["userSays"].append(user_say_menu_1)
                st_python_menu["userSays"].append(user_say_menu_2) #다이얼로그플로우 인텐트 학습문구 제한으로 인해 제외

            with open("C:/Users/mutal/Downloads/" + file + "_brand.json", "w", encoding="utf-8") as w:
                json.dump(st_python_brand, w, ensure_ascii=False)
            with open("C:/Users/mutal/Downloads/" + file + "_menu.json", "w", encoding="utf-8") as w:
                json.dump(st_python_menu, w, ensure_ascii=False)





















