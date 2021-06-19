var express = require('express');
var path = require('path');
var app = express();
var fetch = require('node-fetch');
var geohash = require('ngeohash');
var SpotifyWebApi = require('spotify-web-api-node');
token='';

var router = express.Router({caseSensitive: true});

const { response, request } = require('express');
/*const scopes = [
  'ugc-image-upload',
  'user-read-playback-state',
  'user-modify-playback-state',
  'user-read-currently-playing',
  'streaming',
  'app-remote-control',
  'user-read-email',
  'user-read-private',
  'playlist-read-collaborative',
  'playlist-modify-public',
  'playlist-read-private',
  'playlist-modify-private',
  'user-library-modify',
  'user-library-read',
  'user-top-read',
  'user-read-playback-position',
  'user-read-recently-played',
  'user-follow-read',
  'user-follow-modify'
];

const spotifyApi = new SpotifyWebApi({
  redirectUri: 'http://127.0.0.1:8888/',
  clientId: '75d46b239f9d46bb9d9c9d451569d052',
  clientSecret: "6562532df849416eb58a3205dd033105"
});

var client_id = '75d46b239f9d46bb9d9c9d451569d052'; // Your client id
var client_secret = ' 6562532df849416eb58a3205dd033105'; // Your secret
var redirect_uri = 'http://127.0.0.1:8888/';
*/
app.use(express.static(path.join(__dirname, 'public')));


app.get('/',function(request, response){
    response.sendFile(__dirname + '/public/search.html');
});


//const token = "BQDYYZoLOzKAt-SLLfYZDQdnOIKIgnwAWENLntoLVMKD0ENGZt8fN-1FpXFGo8GOOfbC8AyEJeM62mMsCpiKSOI-GTwL4yFyZYfVQiEUF3qALRw5X5SU3FfZGU-zjmx3UHmeoqPMtM_KdP0V9TXTJ8nTgfyXXz3PrXoCRpA1aRSCrwPiUtcvIpxfOYNjA3Sm2T-B7cPRj_OYmYqEzS4EhnonBoa3yzguqQuciXNsvxRP3NTJd69nSoYOMCJBL1NIIw7q0-MR1vATJNgj9sxzHZWQQgu2-LYBetGmmqTaiWvGW3-L";


//spotifyApi.setAccessToken("BQCQO8RowG3MpHBzeSGqVGb-GKbMdKncTOq7I8FKog0SelrPQu3cC8EvKXE-rdLt1FbV_YhtSqJCTvUnAHU");

/*async function gettoken(artist){
  spotifyApi.clientCredentialsGrant().then(
    function(data) {
      token = data.body['access_token'];
      var detail = getartist(artist, token);
      console.log(detail);
      return detail;
    },
    function(err) {
    }
  );
}*/

//gettoken("artist");
/*app.get("/spotify", async(request, response) =>{
  const artist = request.params.artist;
  console.log(artist);
  const data = gettoken(artist);
  
  response.json(data);
});*/

/*async function getartist(artist, token){
  var to = token;
  spotifyApi.setAccessToken(to);
  var data = await spotifyApi.searchArtists('David Bowie', { limit: 5, offset: 1 });
 
  //console.log(data);
  return data;
}*/

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
    console.log(url);
    const req = await fetch(url);
    const json = await req.json();
    response.json(json);
});

app.get('/', (request, response) => {
  res.redirect(spotifyApi.createAuthorizeURL(scopes));
});

/*app.get('/spotify', async(request, response) => {
  var spotifyApi = new SpotifyWebApi({
    clientId: '75d46b239f9d46bb9d9c9d451569d052',
    clientSecret: '6562532df849416eb58a3205dd033105',
    redirectUri: 'http://127.0.0.1:8888/'
  });
    spotifyApi.getArtistAlbums(
      '43ZHCT0cAZBISjO8DG9PnE',
      { limit: 10, offset: 20 },
      function(err, data) {
        if (err) {
          console.error('Something went wrong!');
        } else {
          console.log(data.body);
          response.json(data);
        }
      }
    );
});
*/

/*app.get('/spotify', function(req, res) {
  var scopes = 'user-read-private user-read-email';
  res.redirect('https://accounts.spotify.com/authorize' +
    '?response_type=code' +
    '&client_id=' + my_client_id +
    (scopes ? '&scope=' + encodeURIComponent(scopes) : '') +
    '&redirect_uri=' + encodeURIComponent(redirect_uri));
});*/
  

var server = app.listen(8888, function () {
  console.log('Server running at http://127.0.0.1:8888/');
});

