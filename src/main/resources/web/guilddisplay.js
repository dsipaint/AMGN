function hideGuilds()
{
    $(".guild").hide();
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
        this.state = {
            guilds: [],
            selectedguild: {}
        }
        
        this.selectGuild = this.selectGuild.bind(this);
        this.updateGuildList = this.updateGuildList.bind(this);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/guilds", this.updateGuildList);
    }

    updateGuildList(data)
    {
        this.setState({
            guilds: data
        });
    }

    selectGuild(id)
    {
        hideGuilds();

        if(id == "global")
        {
            this.setState({
                selectedguild: {
                    name: "Global Settings",
                    id: "global"
                }
            });
            return;
        }

        for(var i = 0; i < this.state.guilds.length; i++){
            if(this.state.guilds[i].id == id)
            {
                this.setState({
                    selectedguild: this.state.guilds[i]
                });

                break;
            }
        }
    }

    render()
    {
        return(
            <div>
                <div id="selectedguild" onClick={toggleGuilds}>{this.state.selectedguild.name === undefined ? "Select Guild" : <div><img id="selectedguildimg" src={this.state.selectedguild.picture} width="30" height="30"></img><div>{this.state.selectedguild.name}<span class="nodisplay">{this.state.selectedguild.id}</span></div></div>}</div>
                <div class="guild" onClick={() => this.selectGuild("global")}>Global Settings</div>
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