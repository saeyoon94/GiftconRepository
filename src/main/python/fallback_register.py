from elasticsearch import Elasticsearch, helpers

es = Elasticsearch('localhost:9200')

docs = []
fallback_keywords = ["Q All 87% 1", "메시지카드 열기", "감동카드보내기", "지금 배달 주문하기", "유효기간", "교환권 저장", "|||", "SMARTCON", "Gifty Shot",
                     "수신번호", "사용처", "문의내용", "선물하기", "kakaotalk", "주문번호", "사용가능", "사용기간", "혜택등급", "쿠프마케팅",
                     "사용안내", "gifticon", "상품명", "교환처", "inumber", "모바일 선물쿠폰 C콘", "Ccon", "쿠폰번호", "감사합니다", "교환수량"]

for keyword in fallback_keywords :
    elastic_json_fallback = {}
    elastic_json_fallback["_index"] = "text_classifier"
    elastic_json_fallback["_source"] = {"category": "None", "type": "fallback", "text": keyword}
    docs.append(elastic_json_fallback)

helpers.bulk(es, docs)