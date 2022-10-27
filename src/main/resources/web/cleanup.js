function toggleLoginVisibility()
{
    //look through cookies
    var cookies = document.cookie.split(";")
    for(var i = 0; i < cookies.length; i++)
    {
        var cookie = cookies[i].split("=");
        if(cookie[0] == "discordtoken")
        {
            $(".notoken").show();
            return;
        }
    }

    $(".token").show();
}