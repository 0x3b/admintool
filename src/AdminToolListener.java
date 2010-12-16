/**
 * Listener class for AdminTool
 * Listens for events being fired by hMod
 * @author Ayron
 */

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminToolListener extends PluginListener {
    private ArrayList<String> enabledUsers = new ArrayList<String>();
    private ArrayList<String> toolArgs = new ArrayList<String>();
    private ArrayList<String> userModes = new ArrayList<String>();
    private static final Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onBlockRightClicked(Player player, Block blockClicked, Item item) {
        String playerName = player.getName();
        if(player.canUseCommand("/admintool") && item.getType() == Item.Type.Book) {
            if(enabledUsers.contains(playerName)) {
                int userIndex = enabledUsers.indexOf(playerName);
                String userMode = userModes.get(userIndex).toString();
                String toolArg = toolArgs.get(userIndex).toString();
                if(userMode.equals("ba")) {
                    int x = blockClicked.getX();
                    int y = blockClicked.getY();
                    int z = blockClicked.getZ();
                    String[] arguments = toolArg.split(",");
                    int blockType = Integer.parseInt(arguments[0]);
                    int blockCount = Integer.parseInt(arguments[1]);
                    String direction = arguments[2];
                    player.sendMessage("Creating " + blockCount + " blocks of type " + blockType + " in the direction " + direction);
                    for(int offset = 1; offset <= blockCount; offset++) {
                        if(direction.equals("X+")) {
                            Block targetBlock = new Block(blockType, x + offset, y, z);
                            targetBlock.update();
                        }
                        if(direction.equals("X-")) {
                            Block targetBlock = new Block(blockType, x - offset, y, z);
                            targetBlock.update();
                        }
                        if(direction.equals("Y+")) {
                            Block targetBlock = new Block(blockType, x, y + offset, z);
                            targetBlock.update();
                        }
                        if(direction.equals("Y-")) {
                            Block targetBlock = new Block(blockType, x, y - offset, z);
                            targetBlock.update();
                        }
                        if(direction.equals("Z+")) {
                            Block targetBlock = new Block(blockType, x, y, z + offset);
                            targetBlock.update();
                        }
                        if(direction.equals("Z-")) {
                            Block targetBlock = new Block(blockType, x, y, z - offset);
                            targetBlock.update();
                        }
                    }
                    return;
                }
                if(userMode.equals("db")) {
                    player.sendMessage("Destroying block type " + blockClicked.getType() + " at " + blockClicked.getX() + "," + blockClicked.getY() + "," + blockClicked.getZ());
                    blockClicked.setType(0);
                    blockClicked.update();
                    return;
                }
            }
            player.sendMessage("You do not have a tool enabled!");
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
                if(player.canUseCommand("/admintool_batchadd"))
                    player.sendMessage("batchadd[ba]");
                if(player.canUseCommand("/admintool_destroyblock"))
                    player.sendMessage("destroyblock[db]");
                return true;
            }

            if(split[1].equalsIgnoreCase("batchadd") || split[1].equalsIgnoreCase("ba")) {
                if(player.canUseCommand("/admintool_batchadd")) {
                    if(split.length < 3) {
                        player.sendMessage("Usage: /admintool batchadd COMMAND");
                        player.sendMessage("COMMAND can be one of:");
                        player.sendMessage("disable, enable BLOCK_TYPE COUNT DIRECTION, status");
                        player.sendMessage("BLOCK_TYPE is the numerical ID of the block type");
                        player.sendMessage("COUNT is the number of blocks to place");
                        player.sendMessage("DIRECTION is the direction to place them");
                        player.sendMessage("(i,e. Y+ for vertically up)");
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("disable")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            String userMode = userModes.get(userIndex).toString();
                            if(userMode.equals("ba")) {
                                enabledUsers.remove(userIndex);
                                toolArgs.remove(userIndex);
                                userModes.remove(userIndex);
                            }
                            player.sendMessage(Colors.Rose + "Batch Add tool disabled");
                        }
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("enable")) {
                        String args;
                        int blockCount, blockType;

                        if(split.length != 6 || (!split[5].equalsIgnoreCase("X+") && !split[5].equalsIgnoreCase("X-") && !split[5].equalsIgnoreCase("Y+") && !split[5].equalsIgnoreCase("Y-") && !split[5].equalsIgnoreCase("Z+") && !split[5].equalsIgnoreCase("Z-"))) {
                            player.sendMessage("Argument error");
                            player.sendMessage("Please see /admintool batchadd");
                            return true;
                        }

                        blockType = Integer.parseInt(split[3].trim());
                        blockCount = Integer.parseInt(split[4].trim());
                        args = blockType + "," + blockCount + "," + split[5].toUpperCase();

                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            toolArgs.set(userIndex, args);
                            userModes.set(userIndex, "ba");
                        } else {
                            enabledUsers.add(playerName);
                            toolArgs.add(args);
                            userModes.add("ba");
                        }
                        player.sendMessage(Colors.LightGreen + "Batch Add tool enabled with arguments " + args);
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("status")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            String userMode = userModes.get(userIndex).toString();
                            if(userMode.equals("ba")) {
                                player.sendMessage("Batch Add tool is " + Colors.Green + "ON");
                                return true;
                            }
                        }
                        player.sendMessage("Batch Add tool is " + Colors.Red + "OFF");
                        return true;
                    }
                } else {
                    player.sendMessage("Sorry, you are not allowed to use that tool");
                    logger.log(Level.INFO, playerName + " tried to use the tool " + split[1]);
                }
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
                            if(userMode.equals("db")) {
                                enabledUsers.remove(userIndex);
                                toolArgs.remove(userIndex);
                                userModes.remove(userIndex);
                            }
                            player.sendMessage(Colors.Rose + "Destroy Block tool disabled");
                        }
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("enable")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            toolArgs.set(userIndex, "");
                            userModes.set(userIndex, "db");
                        } else {
                            enabledUsers.add(playerName);
                            toolArgs.add("");
                            userModes.add("db");
                        }
                        player.sendMessage(Colors.LightGreen + "Destroy Block tool enabled");
                        return true;
                    }

                    if(split[2].equalsIgnoreCase("status")) {
                        if(enabledUsers.contains(playerName)) {
                            int userIndex = enabledUsers.indexOf(playerName);
                            String userMode = userModes.get(userIndex).toString();
                            if(userMode.equals("db")) {
                                player.sendMessage("Destroy Block tool is " + Colors.Green + "ON");
                                return true;
                            }
                        }
                        player.sendMessage("Destroy Block tool is " + Colors.Red + "OFF");
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
