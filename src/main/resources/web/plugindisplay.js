class ListItem extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            list: props.list
        }

        this.addElement = this.addElement.bind(this);
    }

    addElement()
    {
        if($("#" + this.props.listname).val() == "")
            return;

        this.setState({
            list: [...this.state.list, $("#" + this.props.listname).val()]
        });

        $("#" + this.props.listname).val("");
    }

    render()
    {
        console.log(this.state.list);
        return (
            <div>
                <ul>
                    {this.state.list.map(function(item){
                        return <li>{item}</li>;
                    })}
                </ul>
                <input type="text" id={this.props.listname} class="listinput"></input><span id="addelement" onClick={this.addElement}>+</span>
            </div>
        );
    }
}

class PluginConfig extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            plugins: [],
            selectedplugin: {},
            networkinfo: {}
        }

        this.updatePluginNames = this.updatePluginNames.bind(this);
        this.selectPlugin = this.selectPlugin.bind(this);
        this.setNetworkInfoInState = this.setNetworkInfoInState.bind(this);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/plugins", this.updatePluginNames);
        await $.get("/webpanel/api/networkinfo", this.setNetworkInfoInState)
    }

    updatePluginNames(data)
    {
        this.setState({
            plugins: data
        });
    }

    setNetworkInfoInState(data)
    {
        this.setState({
            networkinfo: data
        });
    }

    selectPlugin(plugin)
    {
        this.setState({
            selectedplugin: plugin
        });
    }

    render()
    {
        return(
            <div>
                <div id="pluginlist">
                    <div class="plugin" onClick={() => {this.selectPlugin({})}}>
                        Network Settings
                    </div>
                    {this.state.plugins.map(({name, picture, description, version, author}) =>(
                        <div class="plugin" onClick={() => {this.selectPlugin({name, picture, description, version, author})}}>
                            {name}
                        </div>
                    ))}
                </div>

                {
                    this.state.selectedplugin.name === undefined ? 
                    
                    <div>
                        <div id="plugintitle">
                            <h1>Edit Network Settings</h1>
                        </div>
                        <div id="options">
                            {
                                this.state.networkinfo.operators === undefined ? "No network settings to show" :
                                <div>
                                    <h2>Operators:</h2>
                                    <ListItem list={this.state.networkinfo.operators} listname="operatorlist"/>
                                </div>
                            }
                        </div>
                        <div id="savesettings"  onclick="">Save Settings</div>
                    </div>

                    :

                    <div id="pluginsettings">  
                        <div id="plugintitle">
                            <img src={this.state.selectedplugin.picture} alt="plugin image" width="70" height="70"/>
                            <div id="plugintitletxt">
                                <h1>{this.state.selectedplugin.name + " " + this.state.selectedplugin.version}</h1>
                                <em>{"by " + this.state.selectedplugin.author}</em>
                            </div>
                        </div>
                        <p>{this.state.selectedplugin.description}</p>
                        <div id="options">
                            Options go here
                        </div>
                        <div id="savesettings"  onclick="">Save Settings</div>
                    </div>
                }
            </div>
        );
    }
}

const domContainer = document.querySelector('#content');
const root = ReactDOM.createRoot(domContainer);
root.render(<PluginConfig />);