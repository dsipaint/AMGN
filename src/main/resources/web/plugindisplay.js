var id = 0;

function givename()
{
    id++;
    return "nested-item" + id;
}

function getSelectedGuild()
{
    return $(".nodisplay").text();
}

class Config extends React.Component
{
    constructor(props)
    {
        super(props);
    }

    render()
    {
        if(Array.isArray(this.props.item))
            return <ListItem list={this.props.item} updatehook={this.props.updatehook} updatekey={this.props.updatekey} addmore="true" removemore="true"/>
        else
            return <ObjectItem object={this.props.item} updatehook={this.props.updatehook} updatekey={this.props.updatekey}/>
            
    }
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
        if($("#" + this.props.updatekey.replace("\.", "_")).val() == "")
            return;

        var updatekeyref = this.props.updatekey.replace("\.", "_");
        this.setState({
            list: [...this.state.list, $("#" + updatekeyref).val()]
        });

        this.props.updatehook(this.props.updatekey, this.state.list);
        
        $("#" + this.props.updatekey.replace("\.", "_")).val("");
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
                this.props.removemore == "true" ? <i class="fa-solid fa-xmark removeelement" onClick={() => {this.removeElement(element)}}></i> : ""
            );
        };

        removebutton = removebutton.bind(this);
        var updatehookref = this.props.updatehook;
        var updatekeyref = this.props.updatekey;

        return (
            <div class="listwrapper">
                <h2>{updatekeyref.split(".")[updatekeyref.split(".").length - 1].replace(new RegExp("_", 'g'), " ") + ":"}</h2>
                <div class="list">
                    {this.state.list.map(function(item, i){
                        switch(typeof item)
                        {
                            case "object":
                                if(Array.isArray(item))
                                {
                                    return (
                                        <div class="completelistitem">
                                            <ListItem list={item} updatehook={updatehookref} updatekey={updatekeyref + "." + i} addmore="true" removemore="true"/>
                                            {removebutton(item)}
                                        </div>
                                    );
                                }
                                else
                                {
                                    return (
                                        <div class="completelistitem">
                                            <ObjectItem object={item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                            {removebutton(item)}
                                        </div>
                                    );   
                                }
                            
                            case "undefined":
                                return;

                            case "number":
                                return (
                                    <div class="completelistitem">
                                        <NumberItem item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                        {removebutton(item)}
                                    </div>
                                );                                

                            default:
                                return (
                                    <div class="completelistitem">
                                        <DefaultItem item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                        {removebutton(item)}
                                    </div>
                                );
                        }
                    })
                    }
                    {this.props.addmore == "true" ? <div class="listinput"><input type="text" id={this.props.updatekey.replace("\.", "_")}></input>
                    <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i></div> : ""}
                </div>
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
        var updatehookref = this.props.updatehook;
        var updatekeyref = this.props.updatekey;

        return (
            <div class="objectdisplay">
                {
                    Object.keys(this.state.object).map((key) => {
                        var item = this.state.object[key];
                        switch(typeof item)
                        {
                            case "object":
                                if(Array.isArray(item))
                                    return <ListItem list={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key} addmore="true" removemore="true"/>
                                else
                                    return <ObjectItem object={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>;
                                    
                            case "undefined":
                                return;

                            case "boolean":
                                return <BooleanItem name={key.replace(new RegExp("_", 'g'), " ")} value={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>;

                            case "number":
                                return <NumberItem name={key.replace(new RegExp("_", 'g'), " ")} item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>

                            default:
                                return <DefaultItem name={key.replace(new RegExp("_", 'g'), " ")} item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>;
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
        this.state = {
            value: props.value,
            id: givename()
        }; 

        this.onValueChange = this.onValueChange.bind(this);
    }

    componentDidMount()
    {
        $("#" + this.state.id).prop("checked", this.state.value);
        $("#" + this.state.id).change(this.onValueChange);
    }

    onValueChange(event)
    {
        this.setState({
            value: !this.state.value
        });
        
        this.props.updatehook(this.props.updatekey, this.state.value);
    }

    render()
    {
        return (
            <div class="inputwrapper">
                <div class="objectname">
                    {this.props.name ? this.props.name + ":" : ""}
                </div>
                <input id={this.state.id} type="checkbox"></input>
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

        this.props.updatehook(this.props.updatekey, $("#" + this.state.id).val());
    }

    render()
    {
        return (
            <div class="inputwrapper">
                <div class="objectname">
                    {this.props.name ? this.props.name + ":" : ""}
                </div>
                <input type="text" id={this.state.id} class="listinput"></input>
            </div> 
        );
    }
}

class NumberItem extends React.Component
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
        var val = $("#" + this.state.id).val();
        if(val.match("-?\\d+"))
            val = Number(val);

        this.setState({
            item: val
        });

        this.props.updatehook(this.props.updatekey, val);
    }

    render()
    {
        return (
            <div class="inputwrapper">
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

        this.getPluginNames = this.getPluginNames.bind(this);
        this.selectPlugin = this.selectPlugin.bind(this);
        this.setPluginInfoInState = this.setPluginInfoInState.bind(this);
        this.setPluginInfo = this.setPluginInfo.bind(this);

        this.setNetworkInfoInState = this.setNetworkInfoInState.bind(this);
        this.setNetworkInfo = this.setNetworkInfo.bind(this);

        this.setPropertiesForChildren = this.setPropertiesForChildren.bind(this);
        this.setProperty = this.setProperty.bind(this);

        this.refreshConfig = this.refreshConfig.bind(this);

        //and use a jquery listener to detect a change to the selected guild
        //then have this listener trigger a re-render, all from within the plugindisplay class
        $("div").on("click", ".guild", this.refreshConfig);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/plugins", this.getPluginNames);
        await $.get("/webpanel/api/networkinfo", this.setNetworkInfoInState)
    }

    async refreshConfig(event)
    {
        // event.stopPropagation();
        this.forceUpdate();
        $.get("/webpanel/api/plugins", (data) => {this.selectPlugin(getSelectedGuild() == "global" ? "" : data[0])});
    }

    getPluginNames(data)
    {
        this.setState({
            plugins: data
        });
    }

    async selectPlugin(name)
    {
        this.setState({
            selectedplugin: {}
        });

        if(name == "")
            return;

        console.log(getSelectedGuild());

        await $.ajax({
            url: "/webpanel/api/plugininfo?name=" + name + "&guild=" + getSelectedGuild(),
            type: "GET",
            success: this.setPluginInfoInState,
            error: function(data) {
                console.log(data.responseJSON);
            }
        });
    }

    setPluginInfoInState(data)
    {
        this.setState({
            selectedplugin: data
        });
    }

    setPluginInfo()
    {
        $.ajax("/webpanel/api/plugininfo?name=" + this.state.selectedplugin.name + "&guild=" + getSelectedGuild(), {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(this.state.selectedplugin.config)
        });
    }

    setNetworkInfoInState(data)
    {
        this.setState({
            networkinfo: data
        });
    }

    setNetworkInfo()
    {
        $.ajax("/webpanel/api/networkinfo", {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify({
                operators: this.state.networkinfo.operators,
                guild_data: this.state.networkinfo.guild_data
            })
        });
    }

    setPropertiesForChildren(path, value)
    {
        var newstate = this.setProperty(this.state, path, value);

        this.setState(newstate);
    }

    setProperty(obj, path, value){
        const [head, ...rest] = path.split('.')
    
        //if array, we want to send an edited version of the array back up, similar to the above but treating it like an array instead
        if(Array.isArray(obj))
        {
            var newarr = [...obj]
            newarr[head] = (rest.length ? this.setProperty(newarr[head], rest.join('.'), value)
                : value);
            return newarr;
        }

        //if object we return like this
        return {
            ...obj,
            [head]: rest.length
                ? this.setProperty(obj[head], rest.join('.'), value)
                : value
        }
    }

    render()
    {
        var updatehookref = this.setPropertiesForChildren;
        return(
            <div>
                <div id="pluginlist">
                    {getSelectedGuild() == "global" ? 
                        <div class="plugin" onClick={() => {this.selectPlugin("")}}>
                            Network Settings
                        </div>
                        :
                        ""
                    }
                    {this.state.plugins.map((name) =>(
                        <div class="plugin" onClick={() => {this.selectPlugin(name)}}>
                            {name}
                        </div>
                    ))}
                </div>

                {
                    this.state.selectedplugin.name === undefined && getSelectedGuild() == "global" ? 
                    
                    <div id="pluginsettings">
                        <div id="plugintitle">
                            <h1>Edit Network Settings</h1>
                        </div>
                        <div id="options">
                            {
                                this.state.networkinfo.guild_data === undefined ? "No network settings to show" :
                                <div>
                                    {this.state.networkinfo.operators !== undefined ? <ListItem list={this.state.networkinfo.operators} updatehook={this.setPropertiesForChildren} updatekey="networkinfo.operators" addmore="true" removemore="true"/> : ""}
                                    <ListItem list={this.state.networkinfo.guild_data} updatehook={this.setPropertiesForChildren} updatekey="networkinfo.guild_data" addmore="false" removemore="false"/>
                                </div>
                            }
                        </div>
                        <div id="savesettings"  onClick={this.setNetworkInfo}>Save Settings</div>
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
                            {
                                this.state.selectedplugin.config ?
                                this.state.selectedplugin.config.map(function(config, i){
                                    return <Config item={config.data} updatehook={updatehookref} updatekey={"selectedplugin.config." + i + ".data"}/>;
                                }) + <div id="savesettings"  onClick={this.setPluginInfo}>Save Settings</div>
                                :
                                <h2>Sorry! This plugin has no config</h2>
                            }
                        </div>
                    </div>
                }
            </div>
        );
    }
}

const domContainer = document.querySelector('#content');
const root = ReactDOM.createRoot(domContainer);
root.render(<PluginConfig />);