class GuildList extends React.Component
{
    guildheight = 35;

    constructor(props)
    {
        super(props);
        this.state = {
            guilds: [],
            selectedguild: {
                name: "Global Settings",
                id: "global"
            },
            arrowdown: true
        }
        
        this.selectGuild = this.selectGuild.bind(this);
        this.updateGuildList = this.updateGuildList.bind(this);
        this.hideGuilds = this.hideGuilds.bind(this);
        this.toggleGuilds = this.toggleGuilds.bind(this);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/guilds", this.updateGuildList);

        $(document).on("click", (event) =>
        {
            var parents = $(event.target).parents();
            var inguildselector = false;
            for(var i = 0; i < parents.length; i++)
            {
                if($(parents[i]).attr("id") !== undefined && $(parents[i]).attr("id").includes("guildselector"))
                {
                    inguildselector = true;
                    break;
                }
            }

            if(!inguildselector)
                this.hideGuilds();
        });
    }

    hideGuilds()
    {
        $("#guildselector").height(this.guildheight + "px");
        this.setState({
            arrowdown: true
        });
    }

    toggleGuilds()
    {
        if(!this.state.arrowdown)
            this.hideGuilds();
        else
        {
            var guilds = $(".guild");
            var combinedheight = 0;
            for(var i = 0; i < guilds.length; i++)
                combinedheight += $(guilds[i]).outerHeight();

            $("#guildselector").height((this.guildheight + combinedheight) + "px");
        }

        this.setState({
            arrowdown: !this.state.arrowdown
        });
    }

    updateGuildList(data)
    {
        this.setState({
            guilds: data
        });
    }

    selectGuild(id)
    {
        this.hideGuilds();

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
                <div id="selectedguild" onClick={this.toggleGuilds}>
                    <div>
                        {
                            this.state.selectedguild.id !== "global" ?
                                <img id="selectedguildimg" src={this.state.selectedguild.picture} width="30" height="30"></img>
                            :
                            ""
                        }
                        <div>
                            {this.state.selectedguild.name}<span class="nodisplay">{this.state.selectedguild.id}</span>
                            <i class="fa-solid fa-caret-down" style={{ transform: this.state.arrowdown ? '' : 'rotate(-180deg) translateY(5px)'}}></i>
                        </div>
                    </div>
                </div>
                <div class="guild" style={{textAlign: "center", paddingTop: "15px"}} onClick={() => this.selectGuild("global")}>Global Settings</div>
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