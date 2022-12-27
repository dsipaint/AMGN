function toggleLoginVisibility()
{
    //look through cookies
    var cookies = document.cookie.split("; ");
    for(var i = 0; i < cookies.length; i++)
    {
        var cookie = cookies[i].split("=");
        if(cookie[0] == "discord_token")
        {
            $.ajax({
                url: "https://discord.com/api/v10/users/@me",
                headers: {
                    "Authorization": "Bearer " + cookie[1]
                },
                success: function(data) {
                    $("#username").text(data.username);
                    $("#userpfp").attr("src", "https://cdn.discordapp.com/avatars/" + data.id + "/" + data.avatar + ".png");
                }
            });

            $(".token").show();
            return;
        }
    }

    $(".notoken").show();
}

function clearcookies()
{
    document.cookie = "discord_token=;path=/webpanel;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "discord_username=;path=/webpanel;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    document.cookie = "discord_pfp=;path=/webpanel;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    location.reload();
}