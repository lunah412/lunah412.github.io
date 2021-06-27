var express = require('express');
var path = require('path');
var app = express();
var fetch = require('node-fetch');
var geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');
const { json } = require('express');

var router = express.Router({caseSensitive: true});


const spotifyApi = new SpotifyWebApi({
  redirectUri: 'http://127.0.0.1:8888/',
  clientId: '75d46b239f9d46bb9d9c9d451569d052',
  clientSecret: "6562532df849416eb58a3205dd033105"
});

app.use(express.static(path.join(__dirname, 'public')));


app.get('/',function(request, response){
    response.sendFile(__dirname + '/public/search.html');
});

app.get("/artistinfo", async(request, response) =>{
  var artist = request.query.artist;
  var gettoken =  await spotifyApi.clientCredentialsGrant();
  var token = gettoken.body["access_token"];
  spotifyApi.setAccessToken(token);
  var data = await spotifyApi.searchArtists(artist, { limit: 2, offset: 0 });
  response.json(data.body.artists);
});

app.get('/suggest/:keyword', async(request, response) => {
  const keyword = request.params.keyword;
  const suggest_url = 'https://app.ticketmaster.com/discovery/v2/suggest?apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8&keyword='+keyword;
  const fetch_response = await fetch(suggest_url);
  const json = await fetch_response.json();
  response.json(json);
});

app.get('/search', async(request, response) => {
    const keyword = "&keyword="+ request.query.keyword;
    var location = request.query.location;
    var category = request.query.category;
    var location = location.split(',');
    var lat = location[0];
    var long = location[1];
    const unit = "&unit="+ request.query.unit;
    const radius = "&radius=" + request.query.radius;
    const geoloc =  geohash.encode(lat, long);
    var key = "&apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8";
    var eventurl = "https://app.ticketmaster.com/discovery/v2/events.json?";
    //eventurl = eventurl + keyword + key;
    if(category == "Default"){
      eventurl = eventurl + keyword +'&latlong='+ location + radius+ unit + key;
    }else{
      eventurl = eventurl + keyword +'&latlong='+ location + radius + unit + "&segmentId=" + category + key;
    }
    
    const event_res = await fetch(eventurl);
    const json = await event_res.json(); 
    response.json(json);
});

app.get('/detailinfo', async(request, response) => {
    const id = request.query.id;
    var url = "https://app.ticketmaster.com/discovery/v2/events/"+id+".json?apikey=B9VBFrZZwHLDXynytSpmFUAswhW3kSQ8";
    const req = await fetch(url);
    const json = await req.json();
    response.json(json);
});

var server = app.listen(process.env.PORT || 8888, function () {
  const host = server.address().address;
  const port = server.address().port;
  console.log('Server running at http://127.0.0.1:8888/');
});

