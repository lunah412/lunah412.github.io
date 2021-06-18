
IP='';
currentloc='';
eventdata = '';
venue = "";
var venlat = "";
var venlng = "";
const months = ["Jan", "Feb", "Mar","Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];



async function getsuggest() {
    var keyword = document.getElementById('keyword').value;
    const url = 'suggest/' + keyword;
    const response =  await fetch(url);
    const json = await response.json();
    console.log(json);
}

$(document).ready(function() { 
    $('input[type=radio][name=location]').change(function() {
        if(document.getElementById("locationtext").disabled == true){
            document.getElementById("locationtext").disabled  = false;
        }else{
            document.getElementById("locationtext").disabled  = true;
        }
        
    }); 
});

function defaultR(){
    var radius = document.getElementById("radius").value;
    console.log(radius);
    if (radius =='10'){document.getElementById("radius").value =''};
    if (radius ==''){document.getElementById("radius").value='10'};
}

$(document).ready(function () {
    var keyword = document.getElementById('keyword').value;
    $.ajax({
        type: "GET",
        url: "https://ipinfo.io?token=510d747d9cbc52",
        async: true,
        dataType: "json",
        success: function (json) {
            if (json)
            {
            IP=json['ip'];
            
            currentloc=json['loc'];
            
            $('#keyword').on("input",function() {
                if(document.getElementById('keyword').value != ""){
                    document.getElementById("search").disabled = false;
                    document.getElementById("search").style.backgroundImage= "url('searchdarkbtn.jpg')"
                }else{
                    document.getElementById("search").disabled = true;
                    document.getElementById("search").style.backgroundImage= "url('searchbtn.jpg')"
                }
                
            }); 
            }
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });
    
});

function getcode() {
    var my_client_id = '75d46b239f9d46bb9d9c9d451569d052'; // Your client id
    var client_secret = ' 6562532df849416eb58a3205dd033105'; // Your secret
    var redirect_uri = 'http://127.0.0.1:8888/';
    var scopes = 'user-read-private user-read-email';
    document.location.href = 'https://accounts.spotify.com/authorize' +'?response_type=code' +'&client_id=' + my_client_id +'&scope=' + scopes +'&redirect_uri=' + encodeURI(redirect_uri);
};

function Rest_Data() {
    switchart();
    backtolist();
    document.getElementById("keyword").value='';
    document.getElementById("Category").options[0].selected = true;
    document.getElementById("unit").options[0].selected = true;
    document.getElementById("from").checked = true;
    document.getElementById("title").innerHTML = "";
    document.getElementById("header").style.display = "none";
    document.getElementById("radius").value = 10;
    //document.getElementById("locationtext").value = "";
    document.getElementById("SetCont").innerHTML='';
    document.getElementById("eventinfo").innerHTML="";
    document.getElementById("venusinfo").innerHTML="";
    document.getElementById("title").innerHTML="";
    document.getElementById("artist").innerHTML="";
    
}

async function showevent(){
    var keyword = document.getElementById('keyword').value;
    var unit = document.getElementById("unit").value;
    var radius = document.getElementById("radius").value;
    if(document.getElementById("radius").value == ""){
        radius = 10;
    }
    console.log(radius);
    var location = currentloc;
    var category = document.getElementById("Category").value;
    $.ajax({
        type: "GET",
        url: "/search",
        data: {"keyword": keyword, "location" : location, "unit":unit, "radius":radius,
                "category": category},
        async: true,
        dataType: "json",
        success: function (data) {
            eventdata = data._embedded;
            var data = data._embedded;
            console.log(data);
            if(data){
                //对json进行升序排序函数
                var asc = function(x,y) {
                    return (x.dates.start.localDate > y.dates.start.localDate) ? 1 : -1
                }
                data.events.sort(asc);
                document.getElementById("progress").innerHTML = "";
                //document.getElementById("detail").innerHTML = "<div col-sm col-md><button id=\"detail\">c</button>";
                
                var table =  "<div class=\"col-sm-2 col-md-2 offset-md-11\"><button  id=\"detailbtn\">Detail &gt;</button></div><table class=\"table\" style=\"margin:auto\"><tr><td>#</td><td>Date</td><td>Event</td><td>Category</td><td>Venue Info</td><td>Favorite</td></tr>"
                for (i = 0; i < data.events.length; i++) {
                    var j = i + 1;
                    if (data.events[i].dates.start.localTime == null){date =""}
                    else{
                        date = data.events[i].dates.start.localDate;
                    }
                    var cate ="";
                    if(data.events[i].classifications[0].subGenre){
                        cate = data.events[i].classifications[0].subGenre.name;
                        if(data.events[i].classifications[0].genre){
                            cate += " | " + data.events[i].classifications[0].genre.name;
                            
                        }
                        if(data.events[i].classifications[0].segment){
                            cate += " | " + data.events[i].classifications[0].segment.name;
                        }
                    }else{
                        if(data.events[i].classifications[0].genre){
                            cate +=  data.events[i].classifications[0].genre.name;
                            if(data.events[i].classifications[0].segment){
                                cate += " | " + data.events[i].classifications[0].segment.name;
                            }
                        }else{
                            if(data.events[i].classifications[0].segment){
                                cate +=  data.events[i].classifications[0].segment.name;
                            }
                        }
                    }
                    
                    if(data.events[i]._embedded.venues[0].name == null){
                        venues = "";
                    }else{
                        venues = data.events[i]._embedded.venues[0].name;
                    }
                    if(data.events[i].name.length >= 35){
                        eventname = data.events[i].name.substring(0,35);
                    }else{
                        eventname = data.events[i].name;
                    }
                    table += "<tr><td>" + j + "</td><td>" + date + "</td><td><a href = \"#detail\" onclick='detail(\""+data.events[i].id+"\")' >" + eventname +"</a></td><td>"+ cate +"</td><td>" + venues + "</td><td>star</td></tr>";
                    
                }
                table += "</table>";
                document.getElementById("SetCont").innerHTML = table;
            }else{
                document.getElementById("progress").innerHTML = "";
                var table = "<table style=\"margin:auto;\"><tr><td>No Records has been found</td></tr></table><br/><hr/>";
                document.getElementById("SetCont").innerHTML = table;
            }

        
            
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });
}

async function search(){
    if(validation() && validlocation()){
        document.getElementById("header").style.display = "block";
        document.getElementById("progress").innerHTML = "<div class=\"progress\" style=\"margin-top: 80px;\"><div class=\"progress-bar progress-bar-striped progress-bar-animated progress2\"  role=\"progressbar\" aria-valuenow=\"75\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 50%\"></div></div>\""
        showevent();
        console.log(document.getElementById("SetCont").innerHTML);
    }
    
}

async function detail(id) {
    switchart();
    document.getElementById("SetCont").style.display = "none";
    document.getElementById("detail").style.transform = "translateX(-5%)";
    document.getElementById("detail").style.opacity = 1;
    var eventInfo = document.getElementById("tab").value;
    console.log(id);
    
// credentials are optional


   
    $.ajax({
        type: "GET",
        url: "/detailinfo",
        data: {"id":id},
        async: true,
        dataType: "json",
        success: function (eventdetail) {
            console.log(eventdetail);
            eventname = eventdetail.name;
            var table = "<table class=\"table table-striped win noshow\"  id = \"info\" style=\"margin-top: 10px;width:90%\"> <tr><td><b>Artist/Team(s)</b></td>";
            
            if(eventdetail._embedded.attractions){
                artist = eventdetail._embedded.attractions[0].name;
                for(i = 1; i < eventdetail._embedded.attractions.length;i++ ){
                    artist += " | " + eventdetail._embedded.attractions[i].name;
                }
                table += "<td>" +  artist  +"</td></tr>";
            }
            venue = eventdetail._embedded.venues[0].name;
            writetwitter();
            
            table += "<tr><td><b>Venue</b></td><td>" + venue + "</td></tr>"
            var time =  eventdetail.dates.start.localDate;
            time = getMonth(time);
            console.log(time);
            table += "<tr><td><b>Time</b></td><td>" + time +"</td></tr>";
            genre = "";
            if(eventdetail.classifications[0].subGenre){
                genre += eventdetail.classifications[0].subGenre.name;
                if( eventdetail.classifications[0].genre){
                    genre += " | " + eventdetail.classifications[0].genre.name;
                }
                if(eventdetail.classifications[0].segment){
                    genre += " | " + eventdetail.classifications[0].segment.name;
                }  
            }else{
                if( eventdetail.classifications[0].genre){
                    genre +=  eventdetail.classifications[0].genre.name;
                    if(eventdetail.classifications[0].segment){
                        genre += " | " + eventdetail.classifications[0].segment.name;
                    }  
                }else{
                    if(eventdetail.classifications[0].segment){
                        genre +=  eventdetail.classifications[0].segment.name;
                    } 
                }
            }
            table += "<tr><td><b>Category</b></td><td>" + genre + "</td></tr>";
            
            if(eventdetail.priceRanges == null){}
            else{
                 table += "<tr><td><b>Price Range</b></td><td>"+ eventdetail.priceRanges[0].min;
            } 
            if(eventdetail.priceRanges == null){}
            else{
                table += " - " + eventdetail.priceRanges[0].max + " USD</td></tr>";
            } 
            if(eventdetail.dates.status.code == null){}
            else{
                table += "<tr><td><b>Ticket Status</b></td><td>" + eventdetail.dates.status.code + "</td></tr>";
            }
            table += "<tr><td><b>Buy Ticket At</b></td><td><a href=\"" + eventdetail.url + "\" target=\"_blank\">TicketMaster</a></td></tr>";
            if(eventdetail.seatmap){
                table += "<tr><td><b>Seat Map</b></td><td><a href= \"#\"onclick='Myopen(\"login\",\"" + eventdetail.seatmap.staticUrl + "\")' >" + "View Seat Map Here</a></td></tr>";
            }
            
            table += "</table>";
            document.getElementById("eventinfo").innerHTML = table;

            var venustable = "<table class=\"table venus win noshow\" id=\"venus\" style=\"margin:auto\"><tr><td>";
            if(eventdetail._embedded.venues[0].address.line1 ){
                venustable += "<b>Address</b></td><td>" + eventdetail._embedded.venues[0].address.line1 + "</td></tr>";
            }
            if(eventdetail._embedded.venues[0].city.name ){
                venustable += "<tr><td><b>City</b></td><td>" + eventdetail._embedded.venues[0].city.name;
            }
            if(eventdetail._embedded.venues[0].state.name ){
                venustable += ", " + eventdetail._embedded.venues[0].state.name + "</td></tr>";
            }
            if(eventdetail._embedded.venues[0].boxOfficeInfo){
                venustable += "<tr><td><b>Phone Number</b></td><td>" + eventdetail._embedded.venues[0].boxOfficeInfo.phoneNumberDetail + "</td></tr>";
            }
            if(eventdetail._embedded.venues[0].boxOfficeInfo ){
                venustable += "<tr><td><b>Open Hours</b></td><td>" + eventdetail._embedded.venues[0].boxOfficeInfo.openHoursDetail + "</td></tr>";
            }
            if(eventdetail._embedded.venues[0].generalInfo ){
                venustable += "<tr><td><b>General Rule</b></td><td>" + eventdetail._embedded.venues[0].generalInfo.generalRule + "</td></tr>";
            }
            if(eventdetail._embedded.venues[0].generalInfo ){
                venustable += "<tr><td><b>Child Rule</b></td><td>" + eventdetail._embedded.venues[0].generalInfo.childRule + "</td></tr>";
            }
            
            venlat = eventdetail._embedded.venues[0].location.latitude;
            venlng = eventdetail._embedded.venues[0].location.longitude;
            
            venustable += "</table><br><div class=\"venus win noshow\" id=\"map\"></div>";
            
            console.log(venustable);
            
            var gscript = document.getElementById("map");
            var s = document.createElement('script');
            s.src =  "https://maps.googleapis.com/maps/api/js?key=AIzaSyBk-a17k8-G3w8viEj1qoahVOXO3Q4g-jw&callback=initMap";
            document.body.appendChild(s);
            //s.innerHTML = googlemap;
            //document.body.appendChild(s);


            document.getElementById("venusinfo").innerHTML = venustable;
            var artistdetail =  "<p style=\"text-align:center;font-weight:600;\">";
            document.querySelector(".title").innerHTML = "<p style=\"text-align:center;font-size:18px;font-weight: 600;\">" + eventname + "</p>";
            document.querySelector(".artist").innerHTML = eventname;
        
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });
    
}

function validation() {
    var x;
    var text="";
    x = document.getElementById("keyword").value;
    if (x == "") {
      text = "<p style=\"color:red;\">Please enter a keyword.";
      document.getElementById("warning").innerHTML = text;
      return false;
    }
    document.getElementById("warning").innerHTML = text;
    return true;
}

function validlocation(){
    var x;
    var text="";
    x = document.getElementById("locationtext").value;
    if(document.getElementById("location").checked){
        console.log(x.value);
        if (x=="") {
            text = "<p style=\"color:red;\">Please enter a location.";
            console.log(text);
            document.getElementById("warning1").innerHTML = text;
            return false;
          }
    }
    document.getElementById("warning1").innerHTML = text;
    return true;
}

function writetwitter(){
    name1 = eventname.split(" ");
    name2=name1[0];
    for(i=1;i < name1.length;i++){
        name2 += "%20" + name1[i];
    }
    venue1 = venue.split(" ");
    venue2=venue1[0];
    for(i=1;i < venue1.length;i++) {
        venue2 += "%20" + venue1[i];
    }
    var link = "https://twitter.com/intent/tweet?text=Check%20out%20" + name2 + "%20located%20at%20" +venue2+"%2E%23CSCI571EventSearch";
    var text = "<a class=\"twitter-share-button\" href=\"" + link + "\" target=\"_blank\"><button onclick=\"\" class=\"twitter\"></button></a><button onclick = \"\" class=\"like\"></button>";
    document.getElementById("buttons").innerHTML = text;
}

function backtolist(){
    document.getElementById("SetCont").style.display = "block";
}

function getartist(){

}
function getMonth(date){
    mon = date.split("-");
    time = months[Number(mon[1]) - 1] + " " + mon[2] + ", " + mon[0];
    return time;
}
async function initMap() {
    // The location of Uluru
    const uluru = {lat:Number(venlat), lng: Number(venlng)};
    // The map, centered at Uluru
    const map = new google.maps.Map(document.getElementById("map"), {
      zoom: 16,
      center: uluru,
    });
    // The marker, positioned at Uluru
    const marker = new google.maps.Marker({
      position: uluru,
      map: map,
    });
}
  
function Myopen(divID, link){                                 //根据传递的参数确定显示的层
    var notClickDiv=document.getElementById("notClickDiv");    //获取id为notClickDiv的层
    notClickDiv.style.display='block';                        //设置层显示
    notClickDiv.style.width=document.body.clientWidth;
    notClickDiv.style.height=document.body.clientHeight;
    document.getElementById("seatmap").style.backgroundImage = "url("+link+")";
     document.getElementById(divID).style.display='block';     
     document.getElementById(divID).style.margin = "0 auto";                       //设置由divID所指定的层显示     
     //document.getElementById(divID).style.left=(document.body.clientWidth-240)/2;        //设置由divID所指定的层的左边距
     //document.getElementById(divID).style.top=(document.body.clientHeight-139)/2;        //设置由divID所指定的层的顶边框
}

function myClose(divID){
   divID.style.display='none';            //设置id为login的层隐藏
    //设置id为notClickDiv的层隐藏
    document.getElementById("notClickDiv").style.display='none';    
}

function switchart() {
    document.getElementById("artisttab").classList.add("active");
    document.getElementById("detailtab").classList.remove("active");
    document.getElementById("venustab").classList.remove("active");
    var spanArr = document.querySelectorAll(".win");
    for(var j=0;j<spanArr.length;j++){
        spanArr[j].classList.remove("show");
        spanArr[j].classList.add("noshow");
    }
    document.querySelector("#artist").classList.remove("noshow");
    document.querySelector("#artist").classList.add("show");
   
}

function switchdetail() {
    document.getElementById("artisttab").classList.remove("active");
    document.getElementById("detailtab").classList.add("active");
    document.getElementById("venustab").classList.remove("active");
    var spanArr = document.querySelectorAll(".win");
    for(var j=0;j<spanArr.length;j++){
        spanArr[j].classList.remove("show");
        spanArr[j].classList.add("noshow");
    }
    document.getElementById("info").classList.remove("noshow");
    document.getElementById("info").classList.add("show");
}

function switchvenue() {
    document.getElementById("artisttab").classList.remove("active");
    document.getElementById("detailtab").classList.remove("active");
    document.getElementById("venustab").classList.add("active");
    var spanArr = document.querySelectorAll(".win");
    for(var j=0;j<spanArr.length;j++){
        spanArr[j].classList.remove("show");
        spanArr[j].classList.add("noshow");
    }
    document.querySelectorAll(".venus")[0].classList.remove("noshow");
    document.querySelectorAll(".venus")[0].classList.add("show");
    document.querySelectorAll(".venus")[1].classList.remove("noshow");
    document.querySelectorAll(".venus")[1].classList.add("show");
}



