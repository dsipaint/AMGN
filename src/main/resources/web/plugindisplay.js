var id = 0;

function givename()
{
    id++;
    return "nested-item" + id;
}

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
        return (
            <div>
                <ul>
                    {this.state.list.map(function(item){
                        switch(typeof item)
                        {
                            case "object":
                                if(Array.isArray(item))
                                    return <ListItem list={item} listname={givename()}/>
                                else
                                    return <ObjectItem object={item} objectname={givename()} />;
                            
                            case "undefined":
                                return;

                            case "boolean":
                                return "Boolean item TODO: need to make";

                            default:
                                return <DefaultItem item={item} />;
                        }
                    })}
                    {this.props.addmore == "true" ? <div><input type="text" id={this.props.listname} class="listinput"></input><span id="class" onClick={this.addElement}>+</span></div> : ""}
                </ul>
            </div>
        );
    }
}

class ObjectItem extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            object: props.object
        }
    }

    render()
    {
        return (
            <div class="objectdisplay">
                {
                    Object.keys(this.state.object).map((key) => {
                        var item = this.state.object[key];
                        switch(typeof item)
                        {
                            case "object":
                                if(Array.isArray(item))
                                    return <ListItem list={item} listname={givename()}/>
                                else
                                    return <ObjectItem object={item} objectname={givename()}/>;
                                    
                            case "undefined":
                                return;

                            case "boolean":
                                return "Boolean item TODO: need to make";

                            default:
                                return <DefaultItem item={item} name={key}/>;
                        }
                    })
                }
            </div>
        );
    }
}

class BooleanItem extends React.Component
{
    constructor(props)
    {
        super(props);
    }

    render()
    {
        return (
            <div>
                <div class="objectname">
                    {this.props.name ? this.props.name : ""}
                </div>
                <input type="checkbox"></input>
            </div>
        );
    }
}

class DefaultItem extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            item: props.item
        }
    }

    render()
    {
        return (
            <div>
                <div class="objectname">
                    {this.props.name ? this.props.name + ":" : ""}
                </div>
                <input type="text" value={this.state.item} class="listinput"></input>
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
                                    <ListItem list={this.state.networkinfo.operators} listname="operatorlist" addmore="true"/>
                                    <h2>Guild Data:</h2>
                                    <ListItem list={this.state.networkinfo.guild_data} listname="guildlist" addmore="false"/>
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