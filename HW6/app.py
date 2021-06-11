from flask import Flask, redirect, url_for, jsonify, render_template
import requests
from flask import Flask, request, current_app
from geolib import geohash
import json

app = Flask(__name__, static_url_path='')
google_key = 'AIzaSyDwI6gjmJPDJ-He6rMtw9eNjLlq0To5Sv8'
searchPlace_url = "https://maps.googleapis.com/maps/api/place/textsearch/json"
placeDetail_url = "https://maps.googleapis.com/maps/api/place/details/json"


@app.route('/', methods=["GET"])
def home():
    return redirect(url_for('static', filename='search.html'))


@app.route('/SearchInfo',methods=['GET','POST'])
def SearchInfo():
    geohashSet=''
    keywd = request.args.get('keywd', '')
    Category = request.args.get('Category', '')
    distance = request.args.get('distance', '')
    location = request.args.get('location', '')
    query = request.args.get('query', '')
    IP = request.args.get('IP', '')

    if (location=="Here"):
        # events_url = f'http://api.ipstack.com/check?access_key=2b3f0b3b1c8f37c8fa1df00aa77d9145'
        # response = requests.get(events_url)
        # json = response.json()
        # IP=json['ip']


        events_url = f'https://ipinfo.io/'+IP+'/json?token=510d747d9cbc52'
        response = requests.get(events_url)

        json = response.json()

        query=json['city']

    # print(query)

    events_url = f'https://maps.googleapis.com/maps/api/geocode/json?address='+query+'&key=AIzaSyDwI6gjmJPDJ-He6rMtw9eNjLlq0To5Sv8'
    response = requests.get(events_url)

    json = response.json()

    geohashSet = geohash.encode(json['results'][0]['geometry']['location']['lat'], json['results'][0]['geometry']['location']['lng'], 7)


    events_url = f'https://app.ticketmaster.com/discovery/v2/events.json?unit=miles'

    if (Category=='default'):
        response = requests.get(events_url, params={
            'keyword': keywd,
            'apikey': 'B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8',
            'radius': distance,
            'geoPoint': geohashSet

        })
    else:

        response = requests.get(events_url, params={
            'keyword': keywd,
            'segmentId': Category,
            'apikey': 'B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8',
            'radius': distance,
            'geoPoint': geohashSet

        })

    # print(name)

    # TODO: Catch any errors (invalid API key, server errors, timeouts etc.)

    json = response.json()

    # print(json)

    if '_embedded' in json:
        return jsonify(response.json())
    else:
        return jsonify([])



@app.route('/GetEvDe', methods=['GET', 'POST'])
def GetEvDe():
    Id = request.args.get('Id', '')
    # print(Id)

    events_url = f'https://app.ticketmaster.com/discovery/v2/events/'+Id+'?apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8'
    response = requests.get(events_url)
    json = response.json()

    # s = ['张三', '年龄', '姓名']
    # t = {}
    # for num in range(1, 5):
    #     t[str(num)] = s
    # return jsonify(t)




    dic = {}
    if (json['name']):
        # print(json['name'])
        temp= {'Name':json['name']}
        dic.update(temp)
    if('start' in json['dates']):
        # print(str(json['dates']['start']['localDate']) + ' ' + str(json['dates']['start']['localTime']))
        Temp=str(json['dates']['start']['localDate'])
        if ('localTime' in json['dates']['start']):
            Temp=Temp + ' ' + str(json['dates']['start']['localTime'])

        temp = {'Date':Temp}
        dic.update(temp)


    if('attractions' in json['_embedded']):
        TempD=[]
        Temp = {'name' : 'name', 'url' : 'url' }
        for i in range(len(json['_embedded']['attractions'])):
            Temp['name'] = str(json['_embedded']['attractions'][i]['name'])
            Temp['url'] = str(json['_embedded']['attractions'][i]['url'])
            TempD.append(Temp)

        temp = {'Artist/Team': TempD}
        dic.update(temp)


    if ('venues' in json['_embedded']):
        temp = {'Venue': json['_embedded']['venues'][0]['name']}
        dic.update(temp)

    if ('classifications' in json):
        temp = {'Genre': str(json['classifications'][0]['subGenre']['name']) + '|' + str(
        json['classifications'][0]['genre']['name']) + '|' + str(
        json['classifications'][0]['segment']['name']) + '|' + str(
        json['classifications'][0]['subType']['name']) + '|' + str(json['classifications'][0]['type']['name'])}
        dic.update(temp)

    if ('priceRanges' in json):
        temp = {'Price Ranges': str(json['priceRanges'][0]['min']) + '-' + str(json['priceRanges'][0]['max']) + ' ' + str(
        json['priceRanges'][0]['currency'])}
        dic.update(temp)

    if ('status' in json['dates']):
        temp = {'Ticket Status': json['dates']['status']['code']}
        dic.update(temp)

    if ('url' in json):
        temp = {'Buy Ticket At':json['url']}
        dic.update(temp)

    if ('seatmap' in json):
        temp = {'Seat Map':json['seatmap']['staticUrl']}
        dic.update(temp)

    return jsonify(dic)




if __name__ == '__main__':
    # This is used when running locally only. When deploying to Google App
    # Engine, a webserver process such as Gunicorn will serve the app. This
    # can be configured by adding an `entrypoint` to app.yaml.
    app.run(host='127.0.0.1', port=8080, debug=True)
