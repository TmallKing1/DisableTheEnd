package top.pigest.disabletheend.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.ScoreboardObjectiveArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ScoreboardCopyCommand {
    private static final SimpleCommandExceptionType OBJECTIVES_ADD_DUPLICATE_EXCEPTION = new SimpleCommandExceptionType(Text.translatable("commands.scoreboard.objectives.add.duplicate"));
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((CommandManager.literal("scoreboard").requires((source) -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("objectives").then(
                        CommandManager.literal("copy").then(
                                CommandManager.argument("from" , ScoreboardObjectiveArgumentType.scoreboardObjective()).then(
                                        CommandManager.argument("new", StringArgumentType.word()).then(
                                                CommandManager.argument("displayName", TextArgumentType.text()).executes(
                                                        context -> executeCopyObjective(context.getSource(), ScoreboardObjectiveArgumentType.getObjective(context, "from"), StringArgumentType.getString(context, "new"), TextArgumentType.getTextArgument(context, "displayName"))
                                                )
                                        ).executes(
                                                context -> executeCopyObjective(context.getSource(), ScoreboardObjectiveArgumentType.getObjective(context, "from"), StringArgumentType.getString(context, "new"), Text.literal(StringArgumentType.getString(context, "new")))
                                        )
                                )
                        )
                ))));
    }

    private static int executeCopyObjective(ServerCommandSource source, ScoreboardObjective from, String objective, Text displayName) throws CommandSyntaxException {
        Scoreboard scoreboard = source.getServer().getScoreboard();
        if (scoreboard.getNullableObjective(objective) != null) {
            throw OBJECTIVES_ADD_DUPLICATE_EXCEPTION.create();
        } else {
            ScoreboardCriterion criteria = from.getCriterion();
            scoreboard.addObjective(objective, criteria, displayName, criteria.getDefaultRenderType());
            ScoreboardObjective scoreboardObjective = scoreboard.getNullableObjective(objective);
            assert scoreboardObjective != null;
            for (ScoreboardPlayerScore score : scoreboard.getAllPlayerScores(from)){
                scoreboard.getPlayerScore(score.getPlayerName(), scoreboardObjective).setScore(score.getScore());
            }
            source.sendFeedback(() -> Text.literal("成功将记分项").append(from.toHoverableText()).append("复制到新记分项").append(scoreboardObjective.toHoverableText()), true);
            return scoreboard.getObjectives().size();
        }
    }
}
