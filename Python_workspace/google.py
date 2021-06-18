#셀레니움 라이브러리 받아오는 것 브라우저를 열수 있는 드라이브모듈
from selenium import webdriver
#키이벤트를 돕는 키 모듈
from selenium.webdriver.common.keys import Keys
import time #셔라
import urllib.request#이미지다운 다운
import csv#csv 사용시
import json

#원하는 키워드 및 갯수 입력해주세요
keyword = ("스타트업 드라마")
cnt = (5)

#csv 세팅
csvOutput = keyword+'_test_crawling_01.csv'
csv_open = open(csvOutput, 'w+', encoding='utf-8', newline='')
csv_writer = csv.writer(csv_open)
csv_writer.writerow(('count','title','imgUrl', 'linkUrl'))
print("...csv세팅완료...")

#json 세팅 
jsonOutput = keyword+'_test_crawling_01.json'
json_open = open(jsonOutput, 'w+', encoding='utf-8')
json_open.write("[")
print("크롬드라이버전")
#크롬 실행 및 검색
driver = webdriver.Chrome('/Users/ilsan/Desktop/Work/Python_workspace/selenium/chromedriver') #다운받은 크롬드라이버를 선택
driver.get("https://www.google.co.kr/imghp?hl=ko&tab=wi&ogbl")
time.sleep(1)
elem = driver.find_element_by_name("q")
elem.send_keys(keyword)
elem.send_keys(Keys.RETURN)

# 스크롤 기다리는 시간
SCROLL_PAUSE_TIME = 1.1
# Get scroll height 브라우저 길이를 저장
last_height = driver.execute_script("return document.body.scrollHeight")

while True: #무한반복
    # Scroll down to bottom 브라우저 끝 까지 내려줘라
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")

    # Wait to load page 로딩 기다리는 타임
    time.sleep(SCROLL_PAUSE_TIME)

    # Calculate new scroll height and compare with last scroll height
    new_height = driver.execute_script("return document.body.scrollHeight")
    if new_height == last_height: #길이가 같다면 더이상 내릴께 없다.
        try:
            driver.find_element_by_css_selector(".mye4qd").click()#결과더보기 버튼 누르기 
        except: #결과 더보기 버튼 이후 더보기 없을때 
            break
    last_height = new_height #길이가 같지 않아 더 내려줘라.


time.sleep(2)
#자 이미지와 정보를 뽑자
# driver.find_elements_by_css_selector(".rg_i.Q4LuWd")[0].click()  #1개 첫 사진일때 경우
images = driver.find_elements_by_css_selector(".rg_i.Q4LuWd") #여러개일때

count = 1
for image in images: 
    try:
        image.click()
        time.sleep(1.5)
        #copy- full XPath를 이용해서 완전 구체적으로 잡아준다.
        print(count)
        title = driver.find_element_by_xpath('/html/body/div[2]/c-wiz/div[3]/div[2]/div[3]/div/div/div[3]/div[2]/c-wiz/div[1]/div[3]/div[2]/a').text
        print(title)
        time.sleep(3)
        if(count == 1 ):
            time.sleep(2)
        #페이스북 넘어오는것은 어쩔 수 없이 data:image/ 식으로 넘어온다. 다운로드 방법을 해야만 하는 경우가 필요
        imgUrl = driver.find_element_by_xpath('/html/body/div[2]/c-wiz/div[3]/div[2]/div[3]/div/div/div[3]/div[2]/c-wiz/div[1]/div[1]/div/div[2]/a/img').get_attribute("src")
        linkUrl = driver.find_element_by_xpath('/html/body/div[2]/c-wiz/div[3]/div[2]/div[3]/div/div/div[3]/div[2]/c-wiz/div[1]/div[3]/div[2]/a').get_attribute("href")
        print(imgUrl)
        print(linkUrl)
        #다운로드
        print("이미지다운전")
        urllib.request.urlretrieve(imgUrl, str(keyword)+"_"+str(count)+ ".jpg")
        time.sleep(2)
        # CSV 에 저장하자
        print("csv작정전")
        csv_writer.writerow((count, title, imgUrl, linkUrl))
        naver={'count': count, 'title': title, 'imgUrl': imgUrl, 'linkUrl': linkUrl}
        #ensure_ascii=False는 이스케이프 대로 표시하는것이 아니라 한글의 경우 그대로 잘나오게 함
        
        #원하는 개수에 break
        
        if(count==cnt):
            json_open.write(json.dumps(naver, indent=4, ensure_ascii=False))
            break
        else:
            json_open.write(json.dumps(naver, indent=4, ensure_ascii=False)+",")
        count = count +1
    except:
        pass
json_open.write("]")
csv_open.close()
json_open.close()
driver.close()