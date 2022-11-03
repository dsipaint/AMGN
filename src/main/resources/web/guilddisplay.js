class GuildList extends React.Component
{
    constructor(props)
    {
        super(props);
        // $.get("/webpanel/api/guilds", function(data) {
        //     console.log(data);
        //     //store guild data in array
        // });

        //should just say "show guilds" until clicked

        //keep the currently selected guild in state, store null for no guild selected
        //in render function choose how to render based on this state property



        //when clicked, display all guilds
        //when a guild is clicked, display current guilds

        //use an onclick method that will use jquery to show or hide .guild


        //temporary for testing
        this.state = {
            guilds: [{"id":"634667687233978388","name":"New Tester","picture":"https://cdn.discordapp.com/icons/634667687233978388/41a5b3467bfd8992a5e1f487d87822ad.png"},{"id":"700489766642253828","name":"Unnamed Project Dev Server","picture":null},{"id":"614260951167795204","name":"Safe Haven","picture":"https://cdn.discordapp.com/icons/614260951167795204/f95daf2df262f87aba0e64f197f28396.png"}],
            selectedguild: null
        }
    }

    render()
    {

        return(
            <div>
                Select Guild
                {this.state.guilds.map(({picture, name}) =>(
                    <div class="guild"><img src={picture} width="30" height="30"></img><div>{name}</div></div>
                ))}
            </div>
        );
    }
}

const domContainer = document.querySelector('#guildselector');
const root = ReactDOM.createRoot(domContainer);
root.render(<GuildList />);