package me.tulio.yang.nametags;

import me.tulio.yang.Yang;
import me.tulio.yang.match.participant.MatchGamePlayer;
import me.tulio.yang.profile.Profile;
import me.tulio.yang.profile.ProfileState;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class YangTags extends NametagProvider {

    public YangTags() {
        super("YangTags", 1);
    }

    @Override
    public NametagInfo fetchNametag(Player target, Player viewer) {
        Profile targetProfile = Profile.get(target.getUniqueId());
        Profile viewerProfile = Profile.get(viewer.getUniqueId());
        if (viewerProfile.getState() == ProfileState.LOBBY || viewerProfile.getState() == ProfileState.QUEUEING) {
            if (viewerProfile.getParty() != null && viewerProfile.getParty().containsPlayer(target.getUniqueId())) {
                return createNametag(ChatColor.valueOf(
                        Yang.get().getMainConfig().getString("NAMETAGS.PARTY_COLOR")).toString()
                        , "");
            }
        }
        else if (viewerProfile.getState() == ProfileState.FIGHTING) {
            MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead()) {
                    return createNametag(viewerProfile.getMatch().getRelationColor(viewer, target).toString(), "");
                }
            }
        }
        else if (viewerProfile.getState() == ProfileState.EVENT) {
            if (targetProfile.getState() == ProfileState.EVENT) {
                return createNametag(ChatColor.valueOf(
                        Yang.get().getMainConfig().getString("NAMETAGS.EVENT_COLOR")).toString()
                        , "");
            }
        }
        else if (viewerProfile.getState() == ProfileState.SPECTATING) {
            MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) {
                    return createNametag(viewerProfile.getMatch().getRelationColor(viewer, target).toString(), "");
                }
            }
        }
        else if (targetProfile.getState() == ProfileState.STAFF_MODE) {
            return createNametag(Yang.get().getMainConfig().getString("NAMETAGS.STAFF_FORMAT")
                    .replace("%color%", targetProfile.getColor()), "");
        }

        if (targetProfile.getClan() != null) {
            return createNametag(
                    Yang.get().getMainConfig().getString("NAMETAGS.CLAN_FORMAT")
                    .replace("%color%", targetProfile.getClan().getColor().toString())
                    .replace("%clan%", targetProfile.getClan().getName())
                    , "");
        }

        return createNametag(Yang.get().getMainConfig().getString("NAMETAGS.DEFAULT_COLOR").contains("%color%") ?
                targetProfile.getColor() :
                ChatColor.valueOf(Yang.get().getMainConfig().getString("NAMETAGS.DEFAULT_COLOR")).toString()
                , "");
    }
}