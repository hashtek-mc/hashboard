package fr.hashtek.spigot.hashboard.packets.teams;

import fr.hashtek.spigot.hashboard.packets.PacketManager;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A class that allow you to easily manage the PacketPlayOutScoreboardTeam.
 */
public class PacketTeamManager extends PacketManager
{

    private final String teamId;
    private final Constructor<?> packetPlayOutScoreboardTeam;

    /**
     * Create a new instance of the PacketTeamManager. <br/>
     * NOTE - Only one PacketTeamManager by team should be created.
     *
     * @param       teamId      The team id.
     * @throws      Exception   If an error has occurred in the NMS package.
     */
    public PacketTeamManager(String teamId)
            throws Exception
    {
        this.teamId = teamId;
        this.packetPlayOutScoreboardTeam = this.getNMSClass("PacketPlayOutScoreboardTeam").getConstructor();
    }

    /**
     * Get the corresponding PacketPlayOutScoreboardTeam for creating / updating the team.<br/>
     * <br/>
     * NOTE - If the {@code mode} is none of {@link PacketTeamMode#CREATE_TEAM} or {@link PacketTeamMode#UPDATE_TEAM_INFO}, then
     *          it is recommended to use either the {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode)}
     *          or the {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode, Collection)} depending on your needs.<br/>
     * <br/>
     * WARNING - If you want to add / remove players to / from the team, it is recommended to use the
     *              {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode, Collection)}
     *              as this method will effectively add or remove the players in the list to / from the team.
     *
     * @param       mode            Either the packet correspond to the team creation / update / deletion.
     * @param       tagVisibility   The visibility of the tag.
     * @param       color           The main color of the team.
     * @param       prefix          The prefix of the team.
     * @param       suffix          The suffix of the team.
     * @param       players         The list of players present in the team.
     * @param       flags           Other settings of the team.
     * @return                      The corresponding PacketPlayOutScoreboardTeam.
     * @throws      Exception       If an error has occurred in the NMS package.
     */
    public Object PacketPlayOutScoreboardTeam(
        PacketTeamMode mode,
        PacketTeamTagVisibility tagVisibility,
        PacketTeamColor color,
        String prefix,
        String suffix,
        Collection<String> players,
        PacketTeamFlags... flags
    )
            throws Exception
    {
        final Object packet;

        switch (mode) {
            case ADD_PLAYER:
            case REMOVE_PLAYER:
                return this.PacketPlayOutScoreboardTeam(mode, players);
            case DELETE_TEAM:
                return this.PacketPlayOutScoreboardTeam(mode);
            default:
                break;
        }

        packet = this.packetPlayOutScoreboardTeam.newInstance();
        PacketManager.setField(packet, "a", this.teamId);
        PacketManager.setField(packet, "b", "");
        PacketManager.setField(packet, "c", prefix);
        PacketManager.setField(packet, "d", suffix);
        PacketManager.setField(packet, "e", tagVisibility.getVisibility());
        PacketManager.setField(packet, "f", color.getCode());
        PacketManager.setField(packet, "g", players);
        PacketManager.setField(packet, "h", PacketTeamMode.CREATE_TEAM.getMode());
        PacketManager.setField(packet, "i", Arrays.asList(flags));
        return packet;
    }

    /**
     * Get the corresponding PacketPlayOutScoreboardTeam for adding / removing players from the team.<br/>
     * <br/>
     * WARNING - If you want to create or update the team, it is recommended to use the
     *              {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode, PacketTeamTagVisibility, PacketTeamColor, String, String, Collection, PacketTeamFlags...)}
     *              method instead of this one, as it will allow you to fully customize your team.<br/>
     * <br/>
     * WARNING - If you want to delete the team, it is recommended to use the
     *              {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode)}
     *              method instead of this one.
     *
     * @param       mode            Either the packet correspond to a team creation / a team update / a team deletion.
     * @param       players         The list of players present in the team.
     * @return                      The corresponding PacketPlayOutScoreboardTeam.
     * @throws      Exception       If an error has occurred in the NMS package.
     */
    public Object PacketPlayOutScoreboardTeam(PacketTeamMode mode, Collection<String> players)
            throws Exception
    {
        final Object packet = this.packetPlayOutScoreboardTeam.newInstance();

        switch (mode) {
            case CREATE_TEAM:
            case UPDATE_TEAM_INFO:
                return this.PacketPlayOutScoreboardTeam(mode, PacketTeamTagVisibility.VISIBLE, PacketTeamColor.RESET, "", "", Collections.emptyList());
            case DELETE_TEAM:
                return this.PacketPlayOutScoreboardTeam(mode);
            default:
                break;
        }

        PacketManager.setField(packet, "a", this.teamId);
        PacketManager.setField(packet, "g", players);
        PacketManager.setField(packet, "h", mode.getMode());
        return packet;
    }

    /**
     * Get the corresponding PacketPlayOutScoreboardTeam for adding / removing players from the team.<br/>
     * <br/>
     * WARNING - If you want to create or update the team, it is recommended to use the
     *              {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode, PacketTeamTagVisibility, PacketTeamColor, String, String, Collection, PacketTeamFlags...)}
     *              method instead of this one, as it will allow you to fully customize your team.<br/>
     * <br/>
     * WARNING - If you want to add / remove players to / from the team, it is recommended
     *              to use the {@link PacketTeamManager#PacketPlayOutScoreboardTeam(PacketTeamMode, Collection)}
     *              as this method will effectively add or remove the players in the list to / from the team.
     *
     * @param       mode            Either the packet correspond to a team creation / a team update / a team deletion.
     * @return                      The corresponding PacketPlayOutScoreboardTeam.
     * @throws      Exception       If an error has occurred in the NMS package.
     */
    public Object PacketPlayOutScoreboardTeam(PacketTeamMode mode)
            throws Exception
    {
        final Object packet = this.packetPlayOutScoreboardTeam.newInstance();

        switch (mode) {
            case CREATE_TEAM:
            case UPDATE_TEAM_INFO:
                return this.PacketPlayOutScoreboardTeam(mode, PacketTeamTagVisibility.VISIBLE, PacketTeamColor.RESET, "", "", Collections.emptyList());
            case ADD_PLAYER:
            case REMOVE_PLAYER:
                return this.PacketPlayOutScoreboardTeam(mode, Collections.emptyList());
            default:
                break;
        }

        PacketManager.setField(packet, "a", this.teamId);
        PacketManager.setField(packet, "h", mode.getMode());
        return packet;
    }

}
