IP='';

$(document).ready(function () {
    $.ajax({
        type: "GET",
        url: "https://ipinfo.io?token=510d747d9cbc52",
        async: true,
        dataType: "json",
        success: function (json) {

            if (json)
            {
            IP=json['ip'];
            document.getElementById('query').setAttribute('disabled', 'disabled')
            document.getElementById("search").disabled = false;
            }
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });
});


function Rest_Data() {
document.getElementById("keyword").value='';
    var obj = document.getElementById("Category"); //定位id
    var index = obj.selectedIndex; // 选中索引
    var Category = obj.options[index].value; // 选中值
    var distance = document.getElementById("distance").value;

    var obj = document.getElementsByName("location")
    for (var i = 0; i < obj.length; i++) { //遍历Radio
        if (obj[i].checked) {
            LK = obj[i].value;
        }
    }


    document.getElementById("query").value='';
    document.getElementById("SetCont").innerHTML='';
    document.getElementById("NameSet").innerHTML='';
    document.getElementById("ImageCon").innerHTML = '';
    document.getElementById("ContDe").innerHTML = '';
}


function search() {

    var keywd = document.getElementById("keyword").value;
    var obj = document.getElementById("Category"); //定位id
    var index = obj.selectedIndex; // 选中索引
    var Category = obj.options[index].value; // 选中值
    var distance = document.getElementById("distance").value;
    var box1 =document.querySelector(".megbox1");
    var box2 =document.querySelector(".megbox2");

    var obj = document.getElementsByName("location")
    for (var i = 0; i < obj.length; i++) { //遍历Radio
        if (obj[i].checked) {
            LK = obj[i].value;
        }
    }

    var query = document.getElementById("query").value;
    document.getElementById("NameSet").innerHTML='';
    document.getElementById("ImageCon").innerHTML = '';
    document.getElementById("ContDe").innerHTML = '';
    notEmpty()
    if (keywd=='')
    {
        box1.style.display='block';
        return;

    }

    if (LK=='current'&&query=='')
    {
        box2.style.display='block';
        return;

    }

    // alert(IP);

    $.ajax({
        type: "GET",
        url: "/SearchInfo",
        data: {"keywd": keywd, "Category": Category, "distance": distance,"location":LK,"query":query,"IP":IP},
        async: true,
        dataType: "json",
        success: function (data) {

            if (data._embedded) {
                // console.log(data._embedded)
                var HtmlCon = "<table border=\"2\" align='center'><tr><td>Date</td><td>Icon</td><td>Event</td><td>Genre</td><td>Venue</td></tr>"
                for (i = 0; i < data._embedded.events.length; i++) {

                    if (data._embedded.events[i].dates.start.localTime == null) Time = ''
                    else Time = data._embedded.events[i].dates.start.localTime

                    HtmlCon = HtmlCon + "<tr><td>" + data._embedded.events[i].dates.start.localDate + "<br>" + Time + " </td>" +
                        "<td><img src= " + data._embedded.events[i].images[0].url + " width=\"42\" height=\"42\"/></td>" +
                        "<td><a href='#' onclick='GetDetail(\""+data._embedded.events[i].id+"\")'>" + data._embedded.events[i].name + "</a></td>";

                    // console.log(data._embedded.events[i]);
                        if (data._embedded.events[i].classifications) {
                           HtmlCon = HtmlCon +"<td>" + data._embedded.events[i].classifications[0].genre.name + "</td>";
                        }
                        else{

                            HtmlCon = HtmlCon+"<td>defalut</td>" ;

                        }
                        // console.log(data._embedded.events[i]._embedded);
                        if (data._embedded.events[i]._embedded && 'venues' in data._embedded.events[i]._embedded) {
                          HtmlCon = HtmlCon+"<td>" + data._embedded.events[i]._embedded.venues[0].name + "</td></tr>";
                        }
                        else{

                            HtmlCon = HtmlCon+"<td>defalut</td></tr>";

                        }
                        if (data._embedded.events[i].classifications && data._embedded.events[i].classifications[0].genre) {
                            HtmlCon = HtmlCon +"<td>" + data._embedded.events[i].classifications[0].genre.name + "</td>";
                         }

                    }
                HtmlCon = HtmlCon + "</table>"
                document.getElementById("SetCont").innerHTML = HtmlCon;

            } else {


                var HtmlCon = "<table><tr><td>No Records has been found</td></tr></table><br/><hr/>"
                document.getElementById("SetCont").innerHTML = HtmlCon;


            }
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });

}

function notEmpty() {
    var keywd = document.querySelectorAll('.textin')[0];
    var box1 = document.querySelector(".megbox1");
    var location = document.querySelector('.l2').querySelector('.textin');
    var check = document.querySelector('.l2').querySelector('input');
    var box2 = document.querySelector(".megbox2");
    var loEmpty = (check.checked) && (location.value === '');
    if (keywd.value != '') {
        box1.style.display = 'none';
    }else{
        box1.style.display = 'block';
    }
    if (!loEmpty) {
        box2.style.display = 'none';
    }else{
        box2.style.display = 'block';
    }
}

function ClearDiv() {
     var box1 =document.querySelector(".megbox1");
    var box2 =document.querySelector(".megbox2");
    box1.style.display='none';
    box2.style.display='none';

}
function CHERE() {
     var box1 =document.querySelector(".megbox1");
    var box2 =document.querySelector(".megbox2");
    box1.style.display='none';
    box2.style.display='none';
    document.getElementById('query').setAttribute('disabled', 'disabled')
}

function CL() {
    document.getElementById('query').removeAttribute('disabled')
}

function GetDetail(id){
    $.ajax({
        type: "GET",
        url: "/GetEvDe",
         data: {"Id": id},
        async: true,
        dataType: "json",
        success: function (json) {

            if (json)
            {
           // console.log(json);
            document.getElementById("ImageCon").innerHTML = '';
             document.getElementById("ContDe").innerHTML = '';
            HtmlCon='<table style="border:0px;">';

           if ("Name" in json)
           {

                document.getElementById("NameSet").innerHTML ='<h1 >'+json['Name']+'</h1>'
               // HtmlCon=HtmlCon+"<tr><td></td></tr>"

           }

           if ("Date" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>DATE</h2></td></tr>"
                         +"<tr><td>"+json['Date']+"</td></tr>";

           }

            if ("Artist/Team" in json)
           {

               // console.log(json['Artist/Team'])
                tempHtml='';
              // alert(json['Artist/Team'].length);
               for (ii=0;ii<json['Artist/Team'].length;ii++)
               {
                tempHtml=tempHtml+'<a href="'+json['Artist/Team'][ii]['url']+'" target="_blank">'+json['Artist/Team'][ii]['name']+'<a/>|'

               }
               HtmlCon=HtmlCon+"<tr><td><h2>Artist/Team</h2></td></tr>"
                         +"<tr><td>"+tempHtml+"</td></tr>";

           }

             if ("Venue" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>Venue</h2></td></tr>"
                         +"<tr><td>"+json['Venue']+"</td></tr>";

           }

              if ("Genre" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>Genre</h2></td></tr>"
                         +"<tr><td>"+json['Genre']+"</td></tr>";

           }

              if ("Price Ranges" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>Price Ranges</h2></td></tr>"
                         +"<tr><td>"+json['Price Ranges']+"</td></tr>";

           }

               if ("Ticket Status" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>Ticket Status</h2></td></tr>"
                         +"<tr><td>"+json['Ticket Status']+"</td></tr>";

           }

                if ("Buy Ticket At" in json)
           {
               HtmlCon=HtmlCon+"<tr><td><h2>Buy Ticket At</h2></td></tr>"
                         +"<tr><td><a href='"+json['Buy Ticket At']+"' target='_blank'>Ticketmaster</a></td></tr>";

           }

            document.getElementById("ContDe").innerHTML = HtmlCon+"</table>";

           if ("Seat Map" in json)
           {
               document.getElementById("ImageCon").innerHTML = '<img src="'+json['Seat Map']+'"width="600" height="600"/>';

           }




            }
        },
        error: function (xhr, status, err) {
            // This time, we do not end up here!
        }
    });

}



