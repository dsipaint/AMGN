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
    document.cookie = "discord_username=John Doe; expires=Thu, 18 Dec 2013 12:00:00 UTC";
    document.cookie = "discord_pfp=John Doe; expires=Thu, 18 Dec 2013 12:00:00 UTC";
    document.cookie = "discord_token=John Doe; expires=Thu, 18 Dec 2013 12:00:00 UTC";
    location.reload();
}