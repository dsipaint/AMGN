function toggleLoginVisibility()
{
    //look through cookies
    var cookies = document.cookie.split("; ");
    for(var i = 0; i < cookies.length; i++)
    {
        var cookie = cookies[i].split("=");
        if(cookie[0] == "discord_token")
        {
            for(var j = 0; j < cookies.length; j++)
            {
                var cookie2 = cookies[j].split("=");
                if(cookie2[0] == "discord_username")
                    $("#username").text(cookie2[1]);
                else if(cookie2[0] == "discord_pfp")
                    $("#userpfp").attr("src", cookie2[1]);
            }

            $(".token").show();
            return;
        }
    }

    $(".notoken").show();
}

function clearcookies()
{
    document.cookie = "discord_token=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "discord_username=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "discord_pfp=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    location.reload();
}