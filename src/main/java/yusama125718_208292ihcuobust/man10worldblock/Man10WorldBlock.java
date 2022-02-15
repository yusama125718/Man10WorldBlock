package yusama125718_208292ihcuobust.man10worldblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.invoke.SwitchPoint;
import java.util.*;

import static javax.swing.UIManager.get;

public final class Man10WorldBlock extends JavaPlugin implements Listener, CommandExecutor, TabCompleter
{
    JavaPlugin mblocker;
    boolean system;
    List<UUID> allowplayer = new ArrayList<>();
    List<UUID> blockplayer = new ArrayList<>();
    List<String> targetworld = new ArrayList<>();

    @Override
    public void onEnable()
    {
        this.mblocker = this;
        saveDefaultConfig();
        system = mblocker.getConfig().getBoolean("System");
        String addplayer;
        try
        {
            for (int i = 0; i < Objects.requireNonNull(mblocker.getConfig().getList("allowplayerlist")).size(); i++)
            {
                blockplayer.add(UUID.fromString(Objects.requireNonNull(mblocker.getConfig().getStringList("allowplayerlist")).get(i)));
            }
        }
        catch (NullPointerException e)
        {
            Bukkit.broadcast("§b[Man10WorldBlocker]§r許可するプレイヤーのロードに失敗しました","mspawn.op");
        }
        try
        {
            for (int i = 0; i < Objects.requireNonNull(mblocker.getConfig().getList("blockplayerlist")).size(); i++)
            {
                blockplayer.add(UUID.fromString(Objects.requireNonNull(mblocker.getConfig().getStringList("blockplayerlist")).get(i)));
            }
        }
        catch (NullPointerException e)
        {
            Bukkit.broadcast("§b[Man10WorldBlocker]§rブロックするプレイヤーのロードに失敗しました","mspawn.op");
        }
        targetworld.addAll(mblocker.getConfig().getStringList("targetworldlist"));
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!sender.hasPermission("mwblocker.op"))
        {
            sender.sendMessage("§c[Man10WorldBlocker]You don't have permissions!");
            return true;
        }
        if (blockplayer.contains(((Player) sender).getUniqueId()))
        {
            sender.sendMessage("§c[Man10WorldBlocker]You don't have permissions!");
            return true;
        }
        switch (args.length)
        {
            case 1:
            {
                if (args[0].equals("help"))
                {
                    sender.sendMessage("§b[Man10WorldBlocker]§7 /mblocker [on/off] §e: プラグインのon/offを切り替えます");
                    sender.sendMessage("§b[Man10WorldBlocker]§7 /mblocker allow [add/delete] [プレイヤー名] §e: ワールド移動を許可する人のリストを追加/削除します");
                    sender.sendMessage("§b[Man10WorldBlocker]§7 /mblocker block [add/delete] [プレイヤー名] §e: mblockerを許可しない人のリストを追加/削除します");
                    sender.sendMessage("§b[Man10WorldBlocker]§7 /mblocker world [add/delete] [ワールド名] §e: 寄生するワールドのリストを追加/削除します");
                    return true;
                }
                if (args[0].equals("on"))
                {
                    if (system)
                    {
                        sender.sendMessage("§b[Man10WorldBlocker]§c すでにonになっています");
                        return true;
                    }
                    system = true;
                    mblocker.getConfig().set("System",system);
                    mblocker.saveConfig();
                    sender.sendMessage("§b[Man10WorldBlocker]§e onにしました");
                    return true;
                }
                if (args[0].equals("off"))
                {
                    if (!system)
                    {
                        sender.sendMessage("§b[Man10WorldBlocker]§c すでにoffになっています");
                        return true;
                    }
                    system = false;
                    mblocker.getConfig().set("System",system);
                    mblocker.saveConfig();
                    sender.sendMessage("§b[Man10WorldBlocker]§e offにしました");
                    return true;
                }
                sender.sendMessage("§b[Man10WorldBlocker]§f/mblocer help でhelpを表示します");
                return true;
            }
            case 3:
            {
                if (args[0].equals("allow"))
                {
                    if (args[1].equals("add"))
                    {
                        Player addplayer = Bukkit.getPlayerExact(args[2]);
                        if (addplayer == null)
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは存在しません");
                            return true;
                        }
                        if (allowplayer.contains(addplayer.getUniqueId()))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーはすでに追加されています");
                            return true;
                        }
                        if (blockplayer.contains(addplayer.getUniqueId()))
                        {
                            blockplayer.remove(addplayer.getUniqueId());
                            mblocker.getConfig().set("blockplayerlist",blockplayer);
                            mblocker.saveConfig();
                        }
                        allowplayer.add(addplayer.getUniqueId());
                        List<String> addlist = mblocker.getConfig().getStringList("allowplayerlist");
                        addlist.add(addplayer.getUniqueId().toString());
                        mblocker.getConfig().set("allowplayerlist",addlist);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 追加しました");
                        return true;
                    }
                    if (args[1].equals("delete"))
                    {
                        Player deleteplayer = Bukkit.getPlayerExact(args[2]);
                        if (deleteplayer == null)
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは存在しません");
                            return true;
                        }
                        if (!allowplayer.contains(deleteplayer.getUniqueId()))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは追加されていません");
                            return true;
                        }
                        allowplayer.remove(deleteplayer.getUniqueId());
                        List<String> addlist = mblocker.getConfig().getStringList("allowplayerlist");
                        addlist.remove(deleteplayer.getUniqueId().toString());
                        mblocker.getConfig().set("allowplayerlist",addlist);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 削除しました");
                        return true;
                    }
                }
                if (args[0].equals("block"))
                {
                    if (args[1].equals("add"))
                    {
                        Player addplayer = Bukkit.getPlayerExact(args[2]);
                        if (addplayer == null)
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは存在しません");
                            return true;
                        }
                        if (addplayer.equals(sender))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§c自分自身を追加することはできません");
                            return true;
                        }
                        if (blockplayer.contains(addplayer.getUniqueId()))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§e§cそのプレイヤーはすでに追加されています");
                            return true;
                        }
                        blockplayer.add(addplayer.getUniqueId());
                        List<String> addlist = mblocker.getConfig().getStringList("blockplayerlist");
                        addlist.add(addplayer.getUniqueId().toString());
                        mblocker.getConfig().set("blockplayerlist",addlist);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 追加しました");
                        return true;
                    }
                    if (args[1].equals("delete"))
                    {
                        Player deleteplayer = Bukkit.getPlayerExact(args[2]);
                        if (deleteplayer == null)
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは存在しません");
                            return true;
                        }
                        if (!blockplayer.contains(deleteplayer.getUniqueId()))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのプレイヤーは追加されていません");
                            return true;
                        }
                        blockplayer.remove(deleteplayer.getUniqueId());
                        List<String> addlist = mblocker.getConfig().getStringList("blockplayerlist");
                        addlist.remove(deleteplayer.getUniqueId().toString());
                        mblocker.getConfig().set("blockplayerlist",addlist);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 削除しました");
                        return true;
                    }
                }
                if (args[0].equals("world"))
                {
                    List<String> worlds = new ArrayList<>();
                    for (int i = 0; i<Bukkit.getWorlds().size(); i++)
                    {
                        worlds.add(Bukkit.getWorlds().get(i).getName());
                    }
                    if (args[1].equals("add"))
                    {
                        String addworld = args[2];
                        if (!worlds.contains(addworld))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのワールドは存在しません");
                            return true;
                        }
                        if (targetworld.contains(addworld))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのワールドはすでに対象にされています");
                            return true;
                        }
                        targetworld.add(addworld);
                        mblocker.getConfig().set("targetworldlist",targetworld);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 追加しました");
                        return true;
                    }
                    if (args[1].equals("delete"))
                    {
                        String deleteworld = args[2];
                        if (!worlds.contains(deleteworld))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのワールドは存在しません");
                            return true;
                        }
                        if (!targetworld.contains(deleteworld))
                        {
                            sender.sendMessage("§b[Man10WorldBlocker]§cそのワールドは対象にされていません");
                            return true;
                        }
                        targetworld.remove(deleteworld);
                        mblocker.getConfig().set("targetworldlist",targetworld);
                        mblocker.saveConfig();
                        sender.sendMessage("§b[Man10WorldBlocker]§e 削除しました");
                        return true;
                    }
                }
                sender.sendMessage("§b[Man10WorldBlocker]§f/mblocer help でhelpを表示します");
                return true;
            }
            default:
            {
                sender.sendMessage("§b[Man10WorldBlocker]§f/mblocer help でhelpを表示します");
                return true;
            }
        }
    }

    @EventHandler
    public void PlayerChangedWorldEvent(PlayerChangedWorldEvent event)
    {
        Player eventplayer = event.getPlayer();
        if (!allowplayer.contains(eventplayer.getUniqueId())&&!(event.getPlayer().hasPermission("mwblocer.op")&&!blockplayer.contains(event.getPlayer().getUniqueId())))
        {
            if (targetworld.contains(eventplayer.getLocation().getWorld().getName()))
            {
                Location playerlocation = eventplayer.getLocation();
                playerlocation.setWorld(event.getFrom());
                eventplayer.teleport(playerlocation);
                eventplayer.setHealth(0);
                eventplayer.sendMessage("§b[Man10WorldBlocker]§f あなたは許可されていないワールドに入ろうとしたのでリスポーンしました。");
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        if (!sender.hasPermission("mwblocker.op")||blockplayer.contains(((Player) sender).getUniqueId()))
        {
            return null;
        }
        if (args.length == 1)
        {
            if (args[0].length() == 0)
            {
                return Arrays.asList("allow","block","off","on","world");
            }
            else
            {
                if("allow".startsWith(args[0]))
                {
                    return Collections.singletonList("allow");
                }
                else if("block".startsWith(args[0]))
                {
                    return Collections.singletonList("block");
                }
                else if ("on".startsWith(args[0]) && "off".startsWith(args[0]))
                {
                    return Arrays.asList("on","off");
                }
                else if("on".startsWith(args[0]))
                {
                    return Collections.singletonList("on");
                }
                else if("off".startsWith(args[0]))
                {
                    return Collections.singletonList("off");
                }
                else if("world".startsWith(args[0]))
                {
                    return Collections.singletonList("world");
                }
            }
        }
        if (args.length == 2)
        {
            if (args[1].length() == 0)
            {
                if (args[0].equals("allow")||args[0].equals("block")||args[0].equals("world"))
                {
                    return Arrays.asList("add","delete");
                }
            }
            else if (args[0].equals("allow")||args[0].equals("block")||args[0].equals("world"))
            {
                if ("add".startsWith(args[1]))
                {
                    return Collections.singletonList("add");
                }
                if ("delete".startsWith(args[1]))
                {
                    return Collections.singletonList("delete");
                }
            }
        }
        if (args.length == 3)
        {
            if (args[0].equals("world"))
            {
                ArrayList<String> list = new ArrayList<>();
                for (World world : Bukkit.getWorlds())
                {
                    list.add(world.getName());
                }
                return list;
            }
        }
        return null;
    }

    @Override
    public void onDisable() {}
}
