/**
 * Plug-in class for AdminTool
 * @author dririan
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminTool extends Plugin {
    static final AdminToolListener listener = new AdminToolListener();
    private static final Logger logger = Logger.getLogger("Minecraft");
    public String pluginName = "AdminTool";
    public String pluginVersion = "0.1-git";    // TODO: Add some sort of build ID

    /**
     * Logs the unloading of the plug-in.
     */
    @SuppressWarnings("LoggerStringConcat")
    public void disable() {
        logger.log(Level.INFO, "Unloaded " + pluginName);
    }

    /**
     * Logs the loading of the plug-in.
     */
    @SuppressWarnings("LoggerStringConcat")
    public void enable() {
        logger.log(Level.INFO, "Loaded " + pluginName + " v" + pluginVersion);
    }

    /**
     * Registers hooks for all the events the plug-in wants to receive.
     */
    @Override
    public void initialize() {
        etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, listener, this, PluginListener.Priority.HIGH);
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.HIGH);
    }
}
