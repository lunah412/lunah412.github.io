var express = require('express');
var path = require('path');
var app = express();

app.use(express.static(path.join(__dirname, 'public')));

app.get('/',function(request,response){
    response.sendFile(__dirname + '/public/search.html');
});



var server = app.listen(8888, function () {
  console.log('Server running at http://127.0.0.1:8888/');
});
