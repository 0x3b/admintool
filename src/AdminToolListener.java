/**
 * Listener class for AdminTool
 * Listens for events being fired by hMod
 * @author Ayron
 */

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminToolListener extends PluginListener {
    private ArrayList enabledUsers = new ArrayList();
    private ArrayList toolArgs = new ArrayList();
    private ArrayList userModes = new ArrayList();
    private static final Logger logger = Logger.getLogger("Minecraft");
    
    @Override
    public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
        String playerName = player.getName();
        if(enabledUsers.contains(playerName) && player.canUseCommand("/admintool") && item.getType() == Item.Type.Book) {
            int userIndex = enabledUsers.indexOf(playerName);
            String userMode = userModes.get(userIndex).toString();
            String toolArg = toolArgs.get(userIndex).toString();
            if(userMode.equals("db")) {
                player.sendMessage("Destroying block type " + blockClicked.getType() + " at " + blockClicked.getX() + "," + blockClicked.getY() + "," + blockClicked.getZ());
                blockClicked.setType(0);
                blockClicked.update();
            }
        }
    }

    @Override
    @SuppressWarnings("LoggerStringConcat")
    public boolean onCommand(Player player, String[] split) {
        String playerName = player.getName();

        if((split[0].equalsIgnoreCase("/admintool") || split[0].equalsIgnoreCase("/adm")) && player.canUseCommand("/admintool")) {
            if(split.length == 1) {
                player.sendMessage("Usage: /admintool TOOL");
                player.sendMessage("TOOL can be one of:");
                player.sendMessage("destroyblock[db]");
                return true;
            }

            if(split[1].equalsIgnoreCase("destroyblock") || split[1].equalsIgnoreCase("db")) {
                if(player.canUseCommand("/admintool_destroyblock")) {
                    if(split.length != 3) {
                        player.sendMessage("Usage: /admintool destroyblock COMMAND");
                        player.sendMessage("COMMAND can be one of:");
                        player.sendMessage("disable, enable, status");
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("disable")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            String userMode = userModes.get(userIndex).toString();
                            if (userMode.equals("db")) {
                                enabledUsers.remove(userIndex);
                                toolArgs.remove(userIndex);
                                userModes.remove(userIndex);
                            }
                            player.sendMessage(Colors.LightGreen + "Destroy Block tool disabled");
                        }
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("enable")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            toolArgs.set(userIndex, null);
                            userModes.set(userIndex, "db");
                        } else {
                            enabledUsers.add(playerName);
                            toolArgs.add("null");
                            userModes.add("db");
                        }
                        player.sendMessage(Colors.Rose + "Destroy Block tool enabled");
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("status")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            String userMode = userModes.get(userIndex).toString();
                            if(userMode.equals("db")) {
                                player.sendMessage(Colors.Red + "Destroy Block tool is enabled");
                                return true;
                            }
                        }
                        player.sendMessage(Colors.Green + "Destroy Block tool is disabled");
                        return true;
                    }
                } else {
                    player.sendMessage("Sorry, you are not allowed to use that tool");
                    logger.log(Level.INFO, playerName + " tried to use the tool " + split[1]);
                }
                return true;
            }

            player.sendMessage("Unknown tool");
            return true;
        }

        return false;
    }
}
