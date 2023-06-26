package top.pigest.disabletheend.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.GameProfileArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import top.pigest.disabletheend.util.Mute;
import top.pigest.disabletheend.util.TimeUtil;

import java.util.Collection;
import java.util.Date;

public class MuteCommand {

    private static final SimpleCommandExceptionType TIME_FORMAT_EXCEPTION = new SimpleCommandExceptionType(Text.literal("指定的时间不符合规范"));
    private static final SimpleCommandExceptionType ALREADY_MUTED_EXCEPTION = new SimpleCommandExceptionType(Text.literal("指定的玩家均已被禁言"));
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((CommandManager.literal("mute").requires(source -> source.hasPermissionLevel(2)))
                        .then(CommandManager.argument("targets", GameProfileArgumentType.gameProfile())
                                .then(CommandManager.argument("time", StringArgumentType.string())
                                        .then(CommandManager.argument("reason", StringArgumentType.string())
                                                .executes(context -> execute(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets"), StringArgumentType.getString(context, "time"), StringArgumentType.getString(context, "reason"))))
                                        .executes(context -> execute(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets"), StringArgumentType.getString(context, "time"), null))
                                ).executes(context -> execute(context.getSource(), GameProfileArgumentType.getProfileArgument(context, "targets"), "permanent", null))));

    }


    private static int execute(ServerCommandSource source, Collection<GameProfile> target, String arg, String reason) throws CommandSyntaxException {
        int time = parse(arg);
        int k = 0;
        for(GameProfile gameProfile: target) {
            if(!Mute.isMutedWithExpiry(gameProfile)) {
                Mute.mute(gameProfile, reason, time != -1 ? new Date(System.currentTimeMillis() + time * 1000L).toString() : "permanent");
                k++;
            }
        }
        if(k == 0) {
            throw ALREADY_MUTED_EXCEPTION.create();
        }
        MutableText text;
        if(k == 1) {
            text = Text.literal("已禁言" + target.iterator().next().getName());
        } else {
            text = Text.literal("已禁言" + k + "个玩家");
        }
        text.append(Text.literal("，时长为"));
        if(time == -1) {
            text.append(Text.literal("无限期"));
        } else {
            text.append(Text.literal(TimeUtil.formatTime(time)));
        }
        source.sendFeedback(() -> text, true);
        return k;
    }

    public static int parse(String arg) throws CommandSyntaxException {
        int seconds = 0;
        int minutes = 0;
        int hours = 0;
        int days = 0;
        int temp = 0;
        int status = 0;
        if(arg.equals("permanent")) {
            return -1;
        }
        for (int i = 0; i < arg.length(); i ++) {
            if(arg.charAt(i) >= '0' && arg.charAt(i) <= '9') {
                temp *= 10;
                temp += (arg.charAt(i) - '0');
                continue;
            } else if((arg.charAt(i) == 'd' || arg.charAt(i) == '天') && status == 0) {
                status = 1;
                days = temp;
                temp = 0;
                continue;
            } else if((arg.charAt(i) == 'h' || arg.charAt(i) == '时') && status <= 1) {
                status = 2;
                hours = temp;
                temp = 0;
                continue;
            } else if((arg.charAt(i) == 'm' || arg.charAt(i) == '分') && status <= 2) {
                status = 3;
                minutes = temp;
                temp = 0;
                continue;
            } else if((arg.charAt(i) == 's' || arg.charAt(i) == '秒') && status <= 3) {
                status = 4;
                seconds = temp;
                temp = 0;
                continue;
            }
            throw TIME_FORMAT_EXCEPTION.create();
        }
        if(temp != 0) {
            throw TIME_FORMAT_EXCEPTION.create();
        }
        if(seconds >= 60 || minutes >= 60 || hours >= 24) {
            throw TIME_FORMAT_EXCEPTION.create();
        }
        return days * 60 * 60 * 24 + hours * 60 * 60 + minutes * 60 + seconds;
    }
}
