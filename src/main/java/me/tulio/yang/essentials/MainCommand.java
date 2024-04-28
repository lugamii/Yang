package me.tulio.yang.essentials;

import me.tulio.yang.Yang;
import me.tulio.yang.arena.command.ArenaCommand;
import me.tulio.yang.arena.command.ArenasCommand;
import me.tulio.yang.chat.impl.command.ClearChatCommand;
import me.tulio.yang.chat.impl.command.MuteChatCommand;
import me.tulio.yang.chat.impl.command.SlowChatCommand;
import me.tulio.yang.clan.commands.ClanCommand;
import me.tulio.yang.duel.command.*;
import me.tulio.yang.essentials.command.*;
import me.tulio.yang.event.command.EventCommand;
import me.tulio.yang.event.command.EventsCommand;
import me.tulio.yang.event.game.command.EventHostCommand;
import me.tulio.yang.event.game.map.vote.command.EventMapVoteCommand;
import me.tulio.yang.kit.command.EditorKitCommand;
import me.tulio.yang.kit.command.KitCommand;
import me.tulio.yang.kit.command.KitsCommand;
import me.tulio.yang.leaderboard.commands.*;
import me.tulio.yang.match.command.*;
import me.tulio.yang.party.command.PartyCommand;
import me.tulio.yang.profile.category.commands.CategoryCommand;
import me.tulio.yang.profile.command.FlyCommand;
import me.tulio.yang.profile.command.FollowCommand;
import me.tulio.yang.profile.command.ViewMatchCommand;
import me.tulio.yang.profile.conversation.command.MessageCommand;
import me.tulio.yang.profile.conversation.command.ReplyCommand;
import me.tulio.yang.profile.deatheffects.commands.DeathEffectCommand;
import me.tulio.yang.profile.meta.option.command.*;
import me.tulio.yang.profile.modmode.commands.StaffModeCommand;
import me.tulio.yang.tournament.commands.TournamentCommand;

public class MainCommand {

    public static void init() {
        if (Yang.get().getMainConfig().getBoolean("MESSAGE-REPLY-BOOLEAN")) {
            new MessageCommand();
            new ReplyCommand();
        }
        new ArenaCommand();
        new ArenasCommand();
        new DuelCommand();
        new DuelAcceptCommand();
        new EventCommand();
        new EventHostCommand();
        new EventsCommand();
        new EventMapVoteCommand();
        new RematchCommand();
        new SpectateCommand();
        new StopSpectatingCommand();
        new FlyCommand();
        new ViewMatchCommand();
        new PartyCommand();
        new KitCommand();
        new KitsCommand();
        new ViewInventoryCommand();
        new ToggleScoreboardCommand();
        new ToggleSpectatorsCommand();
        new ToggleDuelRequestsCommand();
        new ClanCommand();
        new CategoryCommand();
        new TournamentCommand();
        new ClearCommand();
        new DayCommand();
        new GameModeCommand();
        new YangCommand();
        new HealCommand();
        new LangCommand();
        new LocationCommand();
        new MoreCommand();
        new NightCommand();
        new PingCommand();
        new RenameCommand();
        new SetSlotsCommand();
        new SetSpawnCommand();
        new ShowAllPlayersCommand();
        new ShowPlayerCommand();
        new SpawnCommand();
        new SudoAllCommand();
        new SudoCommand();
        new SunsetCommand();
        new TeleportWorldCommand();
        new OptionsCommand();
        new ClearChatCommand();
        new SlowChatCommand();
        new MuteChatCommand();
        new EloCommand();
        new SetEloCommand();
        new ResetEloCommand();
        new CreateWorldCommand();
        new StatsCommand();
        new LeaderboardCommand();
        new RankedCommand();
        new UnRankedCommand();
        new ResetCommand();
        new ToggleGlobalChatCommand();
        new TogglePrivateMessagesCommand();
        new ToggleScoreboardCommand();
        new ToggleSoundsCommand();
        new ToggleSpectatorsCommand();
        new FollowCommand();
        new ForceQueueCommand();
        new RemoveWorldCommand();
        new ListWorldsCommand();
        new EditorKitCommand();
        new TimerCommand();
        new ServerInfoCommand();
        new CategoryCommand();
        new DeathEffectCommand();
        if (Yang.get().getMainConfig().getBoolean("MOD_MODE")) new StaffModeCommand();
        if (Yang.get().isLunarClient()) {
            new RallyCommand();
            new FocusCommand();
        }
    }
}
