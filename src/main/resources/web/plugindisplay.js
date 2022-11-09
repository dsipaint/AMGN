class PluginConfig extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            plugins: [],
            selectedplugin: {}
        }

        this.updatePluginNames = this.updatePluginNames.bind(this);
        this.selectPlugin = this.selectPlugin.bind(this);
    }

    async componentDidMount()
    {
        await $.get("/webpanel/api/plugins", this.updatePluginNames);
    }

    updatePluginNames(data)
    {
        this.setState({
            plugins: data
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
                    
                    <div id="plugintitle">
                        <h1>Edit Network Settings</h1>
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
                            options go here
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