function hideGuilds()
{
    $(".guild").hide();
}

function showGuilds()
{
    $(".guild").show();
}

function toggleGuilds()
{
    $(".guild").toggle();
}

class GuildList extends React.Component
{
    constructor(props)
    {
        super(props);
        // $.get("/webpanel/api/guilds", function(data) {
        //     console.log(data);
        //     //store guild data in array
        // });
        //temporary for testing
        this.state = {
            guilds: [{"id":"634667687233978388","name":"New Tester","picture":"https://cdn.discordapp.com/icons/634667687233978388/41a5b3467bfd8992a5e1f487d87822ad.png"},{"id":"700489766642253828","name":"Unnamed Project Dev Server","picture":null},{"id":"614260951167795204","name":"Safe Haven","picture":"https://cdn.discordapp.com/icons/614260951167795204/f95daf2df262f87aba0e64f197f28396.png"}],
            // selectedguild: {"id":"634667687233978388","name":"New Tester","picture":"https://cdn.discordapp.com/icons/634667687233978388/41a5b3467bfd8992a5e1f487d87822ad.png"}
        }

        this.selectGuild = this.selectGuild.bind(this);
    }

    selectGuild(id)
    {
        for(var i = 0; i < this.state.guilds.length; i++){
            if(this.state.guilds[i].id == id)
            {
                this.setState({
                    selectedguild: this.state.guilds[i]
                });

                break;
            }
        }
        hideGuilds();
    }

    render()
    {
        return(
            <div>
                <div id="selectedguild" onClick={toggleGuilds}>{this.state.selectedguild == null ? "Select Guild" : <span><img src={this.state.selectedguild.picture} width="30" height="30"></img><div>{this.state.selectedguild.name}</div></span>}</div>
                {this.state.guilds.map(({picture, name, id}) =>(
                    <div class="guild" onClick={() => this.selectGuild(id)}><img src={picture} width="30" height="30"></img><div>{name}</div></div>
                ))}
            </div>
        );
    }
}

const domContainer = document.querySelector('#guildselector');
const root = ReactDOM.createRoot(domContainer);
root.render(<GuildList />);