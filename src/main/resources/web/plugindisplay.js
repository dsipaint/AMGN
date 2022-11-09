function sayhi()
{
    console.log("hi!!!");
}

class PluginConfig extends React.Component
{
    constructor(props)
    {
        super(props);
        this.state = {
            plugins: []
        }

        this.updatePluginNames = this.updatePluginNames.bind(this);
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

    render()
    {
        return(
            <div>
                <div id="pluginlist">
                    {this.state.plugins.map(({name}) =>(
                        <div class="plugin" onClick={sayhi}>
                            {name}
                        </div>
                    ))}
                </div>
                <div id="pluginsettings">
                    <div id="plugintitle">
                        <img src="" alt="plugin image" width="70" height="70"/>
                        <h1>pluginname</h1>
                    </div>
                    <p>Plugindescription</p>
                    <div id="options">
                        options go here
                    </div>
                    <div id="savesettings"  onclick="">Save Settings</div>
                </div>
            </div>
        );
    }
}

const domContainer = document.querySelector('#content');
const root = ReactDOM.createRoot(domContainer);
root.render(<PluginConfig />);