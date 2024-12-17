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

function displayMessage(msg, success)
{
    if(success)
        $("#success_err_msg").css("background-color", "green");
    else
        $("#success_err_msg").css("background-color", "red");

    $("#success_err_msg").text(msg);
    $("#success_err_msg").show();
    $("#success_err_msg").fadeOut(5000);
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
            return <ObjectItem object={this.props.item} updatehook={this.props.updatehook} updatekey={this.props.updatekey} addmore="true" removemore="true"/>
            
    }
}

class ListItem extends React.Component
{
    globalidcounter = 0;

    constructor(props)
    {
        super(props);

        var parsedlist = []
        for(var i = 0; i < props.list.length; i++)
        {
            parsedlist.push({
                id: this.globalidcounter,
                item: props.list[i]
            });

            this.globalidcounter++;
        }

        this.state = {
            list: parsedlist
        }

        this.addElement = this.addElement.bind(this);
        this.removeElement = this.removeElement.bind(this);
    }

    formatListForState(oldlist)
    {
        var newlist = []
        for(var i = 0; i < oldlist.length; i++)
        {
            newlist.push(oldlist[i].item)
        }

        return newlist;
    }

    addElement()
    {
        var updatekeyref = this.props.updatekey.replaceAll("\.", "_");

        var newchild = {
            id: this.globalidcounter,
            item: null
        }
        this.globalidcounter++;

        if($("#" + updatekeyref).is("input"))
        {
            if($("#" + updatekeyref).attr("type") == "text")
                newchild.item = $("#" + updatekeyref).val();
            else if($("#" + updatekeyref).attr("type") == "checkbox")
                newchild.item = $("#" + updatekeyref)[0].checked

            ReactDOM.flushSync(() =>
                {
                    this.setState({
                        list: [...this.state.list, newchild]
                    });
                }
            );

            this.props.updatehook(this.props.updatekey, this.formatListForState(this.state.list));
            $("#" + updatekeyref).val("");
        }
        else if($("#" + updatekeyref).hasClass("addobject"))
        {
            newchild.item = {};
            ReactDOM.flushSync(() =>
                {
                    this.setState({
                        list: [...this.state.list, newchild]
                    });
                }
            );

            this.props.updatehook(this.props.updatekey, this.formatListForState(this.state.list));
        }
        else if($("#" + updatekeyref).hasClass("addlist"))
        {
            newchild.item = []
            ReactDOM.flushSync(() =>
            {
                this.setState({
                    list: [...this.state.list, newchild]
                });
            });

            this.props.updatehook(this.props.updatekey, this.formatListForState(this.state.list));
        }
        else if(this.state.list.length == 0)
        {
            switch($("#" + updatekeyref).next().find(":selected").text())
            {
                case "object":
                    newchild.item = {};
                    this.setState({
                        list: [newchild]
                    });
                    break;
                
                case "true/false":
                    newchild.item = true;
                    this.setState({
                        list: [newchild]
                    });
                    break;

                case "list":
                    newchild.item = [];
                    this.setState({
                        list: [newchild]
                    });
                    break;

                case "number":
                    newchild.item = -1;
                    this.setState({
                        list: [newchild]
                    });
                    break;

                case "text":
                    newchild.item = "";
                    this.setState({
                        list: [newchild]
                    });
                    break;
            }
        }

        return "";
    }

    removeElement(index)
    {
        var newlist = this.state.list.slice();
        newlist.splice(index, 1)

        ReactDOM.flushSync(() => {
            this.setState({
                list: newlist
            });
        });
        
        this.props.updatehook(this.props.updatekey, this.formatListForState(this.state.list));
    }

    render()
    {
        var removebutton = function (index, shape)
        {
            var listname = updatekeyref.split(".")[updatekeyref.split(".").length - 1]
            return (
                this.props.removemore == "true" ? <i class={"fa-solid fa-" + shape + " removeelement"} onClick={() => {this.removeElement(index)}} title={"Remove item from " + listname}></i> : ""
            );
        };

        removebutton = removebutton.bind(this);

        var addmoreinput = function()
        {
            //if list is empty, let user select type of list
            if(this.state.list.length == 0)
            {
                return (
                    <div class="addnew">
                        <span id={this.props.updatekey.replaceAll("\.", "_")} class="emptylist">
                            &gt; new 
                        </span>
                        <select>
                            <option>object</option>
                            <option>true/false</option>
                            <option>list</option>
                            <option>number</option>
                            <option>text</option>
                        </select>
                        <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                    </div>
                );
            }

            switch(typeof this.state.list[this.state.list.length - 1].item)
            {
                case "object":
                    if(Array.isArray(this.state.list[this.state.list.length - 1].item))
                        return (
                            <div class="addnew">
                                <span id={this.props.updatekey.replaceAll("\.", "_")} class="addlist">
                                    &gt; new list
                                </span>
                                <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                            </div>
                        );
                    else
                        return (
                            <div class="addnew">
                                <span id={this.props.updatekey.replaceAll("\.", "_")} class="addobject">
                                    &gt; new object
                                </span>
                                <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                            </div>
                        );

                case "number":
                    return (
                        <div class="addnew">
                            <input type="number" id={this.props.updatekey.replaceAll("\.", "_")}></input>
                            <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                        </div>
                    );


                case "boolean":
                    return (
                        <div class="addnew">
                            <input type="checkbox" id={this.props.updatekey.replaceAll("\.", "_")}></input>
                            <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                        </div>
                    );

                case "undefined":
                    return (
                        <div class="addnew">
                            <span id={this.props.updatekey.replaceAll("\.", "_")} class="addobject">
                                &gt; new object
                            </span>
                            <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                        </div>
                    );

                default:
                    return (
                        <div class="addnew">
                            <input type="text" id={this.props.updatekey.replaceAll("\.", "_")}></input>
                            <i id="addelement" class="fa-solid fa-plus addelement" onClick={this.addElement}></i>
                        </div>
                    );
            }
            
        }

        addmoreinput = addmoreinput.bind(this);

        var updatehookref = this.props.updatehook;
        var updatekeyref = this.props.updatekey;
        var addmore = this.props.addmore;
        var removemore = this.props.removemore;

        return (
            <div class="listwrapper">
                {
                    this.props.name !== undefined ?
                        <h2>{this.props.name.replaceAll("_", " ") + ":"}</h2>
                        :
                        ""
                }
                <div class="list">
                    {this.state.list.map(function(listchild, i){
                        switch(typeof listchild.item)
                        {
                            case "object":
                                if(Array.isArray(listchild.item))
                                {
                                    return (
                                        <div class="completelistitem" key={listchild.id}>
                                            <ListItem list={listchild.item} updatehook={updatehookref} updatekey={updatekeyref + "." + i} addmore={addmore} removemore={removemore}/>
                                            {removebutton(i, "xmark")}
                                        </div>
                                    );
                                }
                                else
                                {
                                    return (
                                        <div class="completelistitem" key={listchild.id}>
                                            <ObjectItem object={listchild.item} updatehook={updatehookref} updatekey={updatekeyref + "." + i} addmore={addmore} removemore={removemore}/>
                                            {removebutton(i, "xmark")}
                                        </div>
                                    );
                                }
                            
                            case "undefined":
                                return;

                            case "number":
                                return (
                                    <div class="completelistitem" key={listchild.id}>
                                        <NumberItem item={listchild.item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                        {removebutton(i, "minus")}
                                    </div>
                                ); 
                                
                            case "boolean":
                                return (
                                    <div class="completelistitem" key={listchild.id}>
                                        <BooleanItem value={listchild.item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                        {removebutton(i, "minus")}
                                    </div>
                                );

                            default:
                                return (
                                    <div class="completelistitem" key={listchild.id}>
                                        <DefaultItem item={listchild.item} updatehook={updatehookref} updatekey={updatekeyref + "." + i}/>
                                        {removebutton(i, "minus")}
                                    </div>
                                );
                        }
                    })
                    }
                    {
                        this.props.addmore == "true" ?
                        addmoreinput()
                        :
                        ""
                    }
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

        this.addnewfield = this.addnewfield.bind(this);
        this.removefield = this.removefield.bind(this);
    }

    removefield(name)
    {
        ReactDOM.flushSync(() =>{
            var newobj = this.state.object;
            delete newobj[name]
            this.setState({
                object: newobj
            });
        });

        this.props.updatehook(this.props.updatekey, this.state.object);
    }

    addnewfield()
    {
        var name = $("#" + this.props.updatekey.replaceAll(".", "_") + "-addmorename").val();
        if(name === "" || name === undefined || name === null)
            return "";

        var value = $("#" + this.props.updatekey.replaceAll(".", "_") + "-select").val();
        switch(value)
        {
            case "object":
                ReactDOM.flushSync(() =>
                {
                    this.setState({
                        object: {
                            ...this.state.object,
                            [name]: {}
                        }
                    });
                });
    
                this.props.updatehook(this.props.updatekey, this.state.object);
                break;

            case "true/false":
                ReactDOM.flushSync(() =>
                {
                    this.setState({
                        object: {
                            ...this.state.object,
                            [name]: true
                        }
                    });
                });
    
                this.props.updatehook(this.props.updatekey, this.state.object);
                break;

            case "list":
                ReactDOM.flushSync(() =>
                {
                    this.setState({
                        object: {
                            ...this.state.object,
                            [name]: []
                        }
                    });
                });
    
                this.props.updatehook(this.props.updatekey, this.state.object);
                break;

            case "number":
                ReactDOM.flushSync(() =>
                {
                    this.setState({
                        object: {
                            ...this.state.object,
                            [name]: -1
                        }
                    });
                });
    
                this.props.updatehook(this.props.updatekey, this.state.object);
                break;

            case "text":
                ReactDOM.flushSync(() =>
                {
                    this.setState({
                        object: {
                            ...this.state.object,
                            [name]: ""
                        }
                    });
                });
    
                this.props.updatehook(this.props.updatekey, this.state.object);
                break;
        }

        $("#" + this.props.updatekey.replaceAll(".", "_") + "-addmorename").val("");
    }

    render()
    {
        var updatehookref = this.props.updatehook;
        var updatekeyref = this.props.updatekey;
        var addmore = this.props.addmore;
        var removemore = this.props.removemore;

        var removebutton = function (name, shape)
        {
            return (
                this.props.removemore == "true" ? 
                    <i class={"fa-solid fa-" + shape + " removeelement"} onClick={() => {this.removefield(name)}} title={"Remove " + name}></i>
                    :
                    ""
            );
        };
        removebutton = removebutton.bind(this);

        return (
            <div class="objectwrapper">
                {
                    this.props.name !== undefined ?
                        <h2>{this.props.name.replaceAll("_", " ") + ":"}</h2>
                        :
                        ""
                }
                <div class="object">
                    {
                        Object.keys(this.state.object).map((key) => {
                            var item = this.state.object[key];
                            switch(typeof item)
                            {
                                case "object":
                                    if(Array.isArray(item))
                                    {
                                        return (
                                            <div class="completeobjectfield">
                                                {removebutton(key, "xmark")}
                                                <ListItem list={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key} addmore={addmore} removemore={removemore} name={key}/>
                                            </div>
                                        );
                                    }
                                    else
                                    {
                                        if(item === undefined || item === null)
                                        {
                                            return (
                                                <div class="completeobjectfield"> 
                                                    <DefaultItem name={key.replace(new RegExp("_", 'g'), " ")} item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>
                                                    {removebutton(key, "minus")}
                                                </div>
                                            );
                                        }

                                        return (
                                            <div class="completeobjectfield">
                                                <ObjectItem object={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key} addmore={addmore} removemore={removemore} name={key}/>
                                                {removebutton(key, "xmark")}
                                            </div>
                                        );
                                    }
                                        
                                case "undefined":
                                    return;

                                case "boolean":
                                    return (
                                        <div class="completeobjectfield">
                                            <BooleanItem name={key.replace(new RegExp("_", 'g'), " ")} value={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>
                                            {removebutton(key, "minus")}
                                        </div>
                                    );

                                case "number":
                                    return (
                                        <div class="completeobjectfield">
                                            <NumberItem name={key.replace(new RegExp("_", 'g'), " ")} item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>
                                            {removebutton(key, "minus")}
                                        </div>
                                    );

                                default:
                                    return (
                                        <div class="completeobjectfield">
                                            <DefaultItem name={key.replace(new RegExp("_", 'g'), " ")} item={item} updatehook={updatehookref} updatekey={updatekeyref + "." + key}/>
                                            {removebutton(key, "minus")}
                                        </div>
                                    );
                            }
                        })
                    }
                    {
                        this.props.addmore == "true" ?
                            <div class="addmoretoobject">
                                    <h4>&gt; new field:</h4>
                                    <span class="addmorename">Name:</span>
                                    <input type="text" id={this.props.updatekey.replaceAll(".", "_") + "-addmorename"}></input>
                                    <select id={this.props.updatekey.replaceAll(".", "_") + "-select"}>
                                        <option>object</option>
                                        <option>true/false</option>
                                        <option>list</option>
                                        <option>number</option>
                                        <option>text</option>
                                    </select>
                                    <i class="fa-solid fa-plus addelement" onClick={this.addnewfield}></i>
                            </div>
                            :
                            ""
                    }
                </div>
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
        ReactDOM.flushSync(() =>
        {
            this.setState({
                value: !this.state.value
            });
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
        ReactDOM.flushSync(() =>{
            this.setState({
                item: $("#" + this.state.id).val()
            });
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

        ReactDOM.flushSync(() =>
        {
            this.setState({
                item: val
            });
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
                <input type="number" id={this.state.id} class="listinput"></input>
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
            selectedplugin: {
                name: "internal-network" //by default, we start in the network settings
            },
            networkinfo: {},
            permissioninfo: {}
        }

        this.getPluginNames = this.getPluginNames.bind(this);
        this.selectPlugin = this.selectPlugin.bind(this);
        this.setPluginInfoInState = this.setPluginInfoInState.bind(this);
        this.setPluginInfo = this.setPluginInfo.bind(this);

        this.renderGlobalSettings = this.renderGlobalSettings.bind(this);

        this.setNetworkInfoInState = this.setNetworkInfoInState.bind(this);
        this.setNetworkInfo = this.setNetworkInfo.bind(this);

        this.setPermissionInfoInState = this.setPermissionInfoInState.bind(this);
        this.setPermissionInfo = this.setPermissionInfo.bind(this);

        this.setPropertiesForChildren = this.setPropertiesForChildren.bind(this);
        this.setProperty = this.setProperty.bind(this);

        this.addConfigForPlugin = this.addConfigForPlugin.bind(this);
        this.deleteLocalConfig = this.deleteLocalConfig.bind(this);
        this.resetConfig = this.resetConfig.bind(this);

        this.refreshConfig = this.refreshConfig.bind(this);

        this.addToWhitelist = this.addToWhitelist.bind(this);
        this.addToBlacklist = this.addToBlacklist.bind(this);
        this.removeFromWhitelist = this.removeFromWhitelist.bind(this);
        this.removeFromBlacklist = this.removeFromBlacklist.bind(this);

        //and use a jquery listener to detect a change to the selected guild
        //then have this listener trigger a re-render, all from within the plugindisplay class
        $("div").on("click", ".guild", this.refreshConfig);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/plugins", this.getPluginNames);
        await $.get("/webpanel/api/networkinfo", this.setNetworkInfoInState);
        await $.get("/webpanel/api/permissions", this.setPermissionInfoInState);
    }

    async refreshConfig(event)
    {
        // event.stopPropagation();
        this.forceUpdate();
        $.get("/webpanel/api/plugins", (data) => {this.selectPlugin(getSelectedGuild() == "global" ? "internal-network" : data[0])});
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
            selectedplugin: {
                name: name
            }
        });

        if(name == "internal-network" || name == "internal-permission")
            return;

        console.log(getSelectedGuild());

        await $.ajax({
            url: "/webpanel/api/plugininfo?name=" + name + "&guild=" + getSelectedGuild(),
            type: "GET",
            success: this.setPluginInfoInState,
            error: function(data) {
                console.log(data.responseJSON);
                displayMessage("Could not display plugin", false);
            }
        });

        var updatewlfunc = function(data) {
            if(data.result)
            {
                this.setState({
                    selectedplugin: {
                        ...this.state.selectedplugin,
                        whitelist: true
                    }
                });
            }
        };
        updatewlfunc = updatewlfunc.bind(this);

        await $.ajax({
            url: "/webpanel/api/whitelist?plugin=" + name + "&guild=" + getSelectedGuild(),
            type: "GET",
            success: function(data){
                updatewlfunc(data);
            },
            error: function(data) {
                console.log(data.responseJSON);
            }
        });

        var updateblfunc = function(data) {
            if(data.result)
            {
                this.setState({
                    selectedplugin: {
                        ...this.state.selectedplugin,
                        blacklist: true
                    }
                });
            }
        }
        updateblfunc = updateblfunc.bind(this);

        await $.ajax({
            url: "/webpanel/api/blacklist?plugin=" + name + "&guild=" + getSelectedGuild(),
            type: "GET",
            success: function(data){
                updateblfunc(data);
            },
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
        $.ajax({
            url: "/webpanel/api/plugininfo?name=" + this.state.selectedplugin.name + "&guild=" + getSelectedGuild(), 
            method: "PUT",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(this.state.selectedplugin.config),
            success: function(succ) {
                displayMessage("Plugin settings saved!", true);
            },
            error: function(err) {
                displayMessage("There was a problem saving the settings", false);
            }
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
                guild_data: this.state.networkinfo.guild_data
            }),
            success: function(succ) {
                displayMessage("Plugin settings saved!", true);
            },
            error: function(err) {
                displayMessage("There was a problem saving the settings", false);
            }
        });
    }

    setPermissionInfoInState(data) {
        this.setState({
            permissioninfo: data
        });
    }

    setPermissionInfo() {
        $.ajax("/webpanel/api/permissions", {
            method: "PUT",
            contentType: "application/json",
            data: JSON.stringify(this.state.permissioninfo),
            success: function(succ) {
                displayMessage("Plugin settings saved!", true);
            },
            error: function(err) {
                displayMessage("There was a problem saving the settings", false);
            }
        });
    }

    //this can be used to render either the network settings or the permissions settings
    renderGlobalSettings(name)
    {
        if(name === "internal-network")
        {
            return (
            <div id="pluginsettings">
                <div id="plugintitle">
                    <h1>Edit Network Settings</h1>
                </div>
                <div id="options">
                    {
                        this.state.networkinfo.guild_data === undefined ? "No network settings to show" :
                        <div>
                            <ListItem list={this.state.networkinfo.guild_data} updatehook={this.setPropertiesForChildren} updatekey="networkinfo.guild_data" addmore="false" removemore="false"/>
                        </div>
                    }
                </div>
                <div id="buttondeck">
                    <div class="savesettings"  onClick={this.setNetworkInfo}>Save Settings</div>
                </div>
            </div>);
        }

        return (
            <div id="pluginsettings">
                <div id="plugintitle">
                    <h1>Edit Permission Settings</h1>
                </div>
                <div id="options">
                    {
                        this.state.permissioninfo === undefined ? "No permission settings to show" :
                        <div>
                            <ObjectItem object={this.state.permissioninfo} updatehook={this.setPropertiesForChildren} updatekey="permissioninfo" addmore="true" removemore="true"/>
                        </div>
                    }
                </div>
                <div id="buttondeck">
                    <div class="savesettings"  onClick={this.setPermissionInfo}>Save Settings</div>
                </div>
            </div>);
    }

    addConfigForPlugin()
    {
        $.ajax("/webpanel/api/plugininfo?name=" + this.state.selectedplugin.name + "&guild=" + getSelectedGuild(), {
            method: "POST",
            success: (data) => {
                this.setState({
                    selectedplugin: {
                        ...this.state.selectedplugin,
                        config: data
                    }
                });
            }
        });
    }

    deleteLocalConfig()
    {
        $.ajax("/webpanel/api/plugininfo?name=" + this.state.selectedplugin.name + "&guild=" + getSelectedGuild(), {
            method: "DELETE",
            success: (data) => {
                this.setState({
                    selectedplugin: {
                        ...this.state.selectedplugin,
                        config: undefined
                    }
                });
            }
        });
    }

    resetConfig()
    {
        this.setState({
            selectedplugin: {
                ...this.state.selectedplugin,
                config: undefined
            }
        });

        $.ajax({
            method: "POST",
            url: "/webpanel/api/resetpluginconfig?guild=" + getSelectedGuild() + "&plugin=" + this.state.selectedplugin.name,
            success: (data) => {
                this.setState({
                    selectedplugin: {
                        ...this.state.selectedplugin,
                        config: data
                    }
                });
            },
            error: (err) => {
                displayMessage("There was a problem resetting the config", false);
            }
        });
    }

    addToWhitelist()
    {
        var updatefunc = function(data) {
            this.setState({
                selectedplugin: {
                    ...this.state.selectedplugin,
                    whitelist: true
                }
            })
        }
        updatefunc = updatefunc.bind(this);

        $.ajax({
            method: "PUT",
            url: "/webpanel/api/whitelist?guild=" + getSelectedGuild() + "&plugin=" + this.state.selectedplugin.name,
            success: updatefunc,
            error: function(err) {
                displayMessage("There was a problem updating the whitelist", false);
            }
        });
    }

    addToBlacklist()
    {
        var updatefunc = function(data) {
            this.setState({
                selectedplugin: {
                    ...this.state.selectedplugin,
                    blacklist: true
                }
            })
        }
        updatefunc = updatefunc.bind(this);

        $.ajax({
            method: "PUT",
            url: "/webpanel/api/blacklist?guild=" + getSelectedGuild() + "&plugin=" + this.state.selectedplugin.name,
            success: updatefunc,
            error: function(err) {
                displayMessage("There was a problem updating the blacklist", false);
            }
        });
    }

    removeFromWhitelist()
    {
        var updatefunc = function(data) {
            this.setState({
                selectedplugin: {
                    ...this.state.selectedplugin,
                    whitelist: false
                }
            })
        }
        updatefunc = updatefunc.bind(this);

        $.ajax({
            method: "DELETE",
            url: "/webpanel/api/whitelist?guild=" + getSelectedGuild() + "&plugin=" + this.state.selectedplugin.name,
            success: updatefunc,
            error: function(err) {
                displayMessage("There was a problem updating the whitelist", false);
            }
        });
    }

    removeFromBlacklist()
    {
        var updatefunc = function(data) {
            this.setState({
                selectedplugin: {
                    ...this.state.selectedplugin,
                    blacklist: false
                }
            })
        }
        updatefunc = updatefunc.bind(this);

        $.ajax({
            method: "DELETE",
            url: "/webpanel/api/blacklist?guild=" + getSelectedGuild() + "&plugin=" + this.state.selectedplugin.name,
            success: updatefunc,
            error: function(err) {
                displayMessage("There was a problem updating the blacklist", false);
            }
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
                        <div>
                            <div class="plugin" onClick={() => {this.selectPlugin("internal-network")}}>
                                Network Settings
                            </div>
                            <div class="plugin" onClick={() => {this.selectPlugin("internal-permission")}}>
                                Permission Settings
                            </div>
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
                    getSelectedGuild() == "global" && this.state.selectedplugin.name.startsWith("internal-") ?
                    
                    //network settings
                    this.renderGlobalSettings(this.state.selectedplugin.name)

                    :

                    //specific plugin settings
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
                                <div>
                                    {this.state.selectedplugin.config.map(function(config, i){
                                        return <Config item={config.data} updatehook={updatehookref} updatekey={"selectedplugin.config." + i + ".data"}/>;
                                    })}
                                </div>
                                :
                                <div>
                                    <h2>Sorry! This plugin has no config</h2>
                                    {
                                        getSelectedGuild() == "global" ? 
                                            <p>The selected guild has no config for this plugin. If this plugin is supposed to have a config, either try reloading the plugin or contacting the developer.</p>
                                            : 
                                            <p>The selected guild has no config for this plugin. This means either this guild is using the Global Settings for this plugin, or this plugin has no config to edit. Either edit the Global Settings for this plugin, or add a local config for this guild using the button below.</p>
                                    }
                                </div>
                            }
                            <div id="buttondeck">
                                {
                                    this.state.selectedplugin.config ?
                                    <div class="savesettings"  onClick={this.setPluginInfo}>Save Settings</div>
                                    :
                                    ""
                                }
                                {
                                    !this.state.selectedplugin.config ?
                                    ""
                                    :
                                    getSelectedGuild() !== "global" ?
                                    <div class="removesettings" onClick={this.deleteLocalConfig}>Use Global Settings</div>
                                    :
                                    <div class="savesettings" onClick={this.addConfigForPlugin}>Add Local Config</div>
                                }
                                {
                                    getSelectedGuild() == "global" ? 
                                    ""
                                    :
                                    this.state.selectedplugin.whitelist ? 
                                        <div class="removesettings" onClick={this.removeFromWhitelist}>Remove from Whitelist</div>
                                        :
                                        <div class="savesettings" onClick={this.addToWhitelist}>Add to Whitelist</div>
                                }
                                {
                                    getSelectedGuild() == "global" ?
                                    ""
                                    :
                                    this.state.selectedplugin.blacklist ? 
                                        <div class="removesettings" onClick={this.removeFromBlacklist}>Remove from Blacklist</div>
                                        :
                                        <div class="savesettings" onClick={this.addToBlacklist}>Add to Blacklist</div>
                                }
                                {
                                    this.state.selectedplugin.config ? 
                                    <div class="removesettings" onClick={this.resetConfig}>Reset Config</div>
                                    :
                                    ""
                                }
                            </div>
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