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
                <div id="pluginlist">
                    <div class="plugin" onclick="">
                        Plugin name 1
                    </div>
                    <div class="plugin" onclick="">
                        Plugin name 2
                    </div>
                    <div class="plugin" onclick="">
                        Plugin name 3
                    </div>
                    <div class="plugin" onclick="">
                        Plugin name 4
                    </div>
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