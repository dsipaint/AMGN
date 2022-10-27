class PluginConfig extends React.Component
{
    constructor(props)
    {
        super(props);
    }

    render()
    {
        return(
            <div>
                <div id="plugintitle">
                    <img src="{plugin image}" alt="plugin image" width="70" height="70"/>
                    <h1>{pluginname}</h1>
                </div>
                <p>{Plugindescription}</p>
                <div id="options">
                    options go here
                </div>
                <div id="savesettings"  onclick="">Save Settings</div>
            </div>
        );
    }
}

const domContainer = document.querySelector('#maincontent');
const root = ReactDOM.createRoot(domContainer);
root.render(<PluginConfig />);