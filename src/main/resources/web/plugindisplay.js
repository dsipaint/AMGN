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
        this.removeElement = this.removeElement.bind(this);
    }

    addElement()
    {
        if($("#" + this.props.listname).val() == "")
            return;

        this.setState({
            list: [...this.state.list, $("#" + this.props.listname).val()]
        });

        this.props.updatehook(this.props.updatekey, this.state.list);

        $("#" + this.props.listname).val("");
    }

    removeElement(element)
    {
        this.setState({
            list: this.state.list.filter(function(item){
                return item !== element;
            })
        });

        this.props.updatehook(this.props.updatekey, this.state.list);
    }

    render()
    {
        var removebutton = function (element)
        {
            return (
                this.props.removemore == "true" ? <span class="removeelement" onClick={() => {this.removeElement(element)}}>x</span> : ""
            );
        };

        removebutton = removebutton.bind(this);

        return (
            <div>
                <ul>
                    {this.state.list.map(function(item){
                        switch(typeof item)
                        {
                            case "object":
                                if(Array.isArray(item))
                                {
                                    return (
                                        <div>
                                            <ListItem list={item} listname={givename()}/>
                                            {removebutton(item)}
                                        </div>
                                    );
                                }
                                else
                                {
                                    return (
                                        <div>
                                            <ObjectItem object={item} objectname={givename()} />
                                            {removebutton(item)}
                                        </div>
                                    );   
                                }
                            
                            case "undefined":
                                return;

                            case "boolean":
                                return (
                                    <div>
                                        <BooleanItem name={key}/>
                                        {removebutton(item)}
                                    </div>
                                );

                            default:
                                return (
                                    <div>
                                        <DefaultItem item={item} />
                                        {removebutton(item)}
                                    </div>
                                );
                        }
                    })
                    }
                    {this.props.addmore == "true" ? <div><input type="text" id={this.props.listname} class="listinput"></input>
                    <span id="addelement" class="addelement" onClick={this.addElement}>+</span></div> : ""}
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
                                return <BooleanItem name={key}/>;

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
            item: props.item,
            id: givename()
        }

        this.onValueChange = this.onValueChange.bind(this);
    }

    componentDidMount()
    {
        $("#" + this.state.id).val(this.state.item);
        $("#" + this.state.id).on("input", this.onValueChange);
    }

    onValueChange(event)
    {
        this.setState({
            item: $("#" + this.state.id).val()
        });
    }

    render()
    {
        return (
            <div>
                <div class="objectname">
                    {this.props.name ? this.props.name + ":" : ""}
                </div>
                <input type="text" id={this.state.id} class="listinput"></input>
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
        this.setNetworkInfo = this.setNetworkInfo.bind(this);
        this.setPropertiesForChildren = this.setPropertiesForChildren.bind(this);

        this.debugdisplaystate = this.debugdisplaystate.bind(this);
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

    setPropertiesForChildren(key, value)
    {
        var newstate = {...this.state};
        var keypath = key.split(".");
        for(var i = 0; i < keypath.length; i++)
        {
            if(!newstate[keypath[i]])
                newstate[keypath[i]] = {}

            newstate = newstate[keypath[i]];
        }


        newstate[keypath.length - 1] = value;
        this.setState(newstate);
    }

    setNetworkInfo(operators, guildinfo)
    {
        $.ajax("/webpanel/api/networkinfo", {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                operators: operators,
                guild_data: guildinfo
            })
        });
    }

    selectPlugin(plugin)
    {
        this.setState({
            selectedplugin: plugin
        });
    }

    debugdisplaystate()
    {
        console.log(this.state.networkinfo);
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
                                    <ListItem list={this.state.networkinfo.operators} listname="operatorlist" updatehook={this.setPropertiesForChildren} updatekey="networkinfo.operators" addmore="true" removemore="true"/>
                                    <h2>Guild Data:</h2>
                                    <ListItem list={this.state.networkinfo.guild_data} listname="guildlist" addmore="false" removemore="false"/>
                                </div>
                            }
                        </div>
                        <div id="savesettings"  onClick={() => {this.setNetworkInfo(this.state.networkinfo.operators, this.state.networkinfo.guild_data)}}>Save Settings</div>
                        <div onClick={this.debugdisplaystate}>DEBUG BUTTON</div>
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