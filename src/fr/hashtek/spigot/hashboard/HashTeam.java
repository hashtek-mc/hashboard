package fr.hashtek.spigot.hashboard;

import fr.hashtek.spigot.hashboard.exceptions.AlreadyInTeamException;
import fr.hashtek.spigot.hashboard.exceptions.StrangeException;
import fr.hashtek.spigot.hashboard.exceptions.TeamSizeException;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

/**
 * HashTeam is a class that allow you to manage and customize teams.
 * You can :
 * <ol>
 *     <li>{@link HashTeam#HashTeam(int, String, String, int, HashBoard) Create a team with a custom prefix/suffix/size to the team}</li>
 *     <li>{@link HashTeam#add(Player) Add players to the team}</li>
 *     <li>{@link HashTeam#remove(Player) Remove a player from the team}</li>
 *     <li>{@link HashTeam#has(Player) Check if a player is in the team}</li>
 * </ol>
 */
public class HashTeam
{

    private Team boardTeam;

    private final ArrayList<UUID> playersUUID;

    private final String tablistPriority;

    private final String prefix;
    private final String suffix;

    private final int teamSize;

    /**
     * Create a new team.
     *
     * @param tablistPriority The priority of the team in the tablist.
     * @param prefix The prefix of the team.
     * @param suffix The suffix of the team.
     * @param teamSize The team size. Set to 0 for no size.
     */
    public HashTeam(int tablistPriority, String prefix, String suffix, int teamSize)
    {
        this.playersUUID = new ArrayList<UUID>();
        this.tablistPriority = String.valueOf(tablistPriority);
        this.prefix = prefix;
        this.suffix = suffix;
        this.teamSize = teamSize;
    }

    /**
     * Create a new team.
     *
     * @param tablistPriority The priority of the team in the tablist.
     * @param prefix The prefix of the team.
     * @param suffix The suffix of the team.
     * @param teamSize The team size.
     * @param board Required if you want to display the team of the player above his head
     */
    public HashTeam(int tablistPriority, String prefix, String suffix, int teamSize, HashBoard board)
    {
        this.playersUUID = new ArrayList<UUID>();
        this.tablistPriority = String.valueOf(tablistPriority);
        this.prefix = prefix;
        this.suffix = suffix;
        this.teamSize = teamSize;
        this.setBoard(board);
    }

    /**
     * Add a player to the team.
     *
     * @param player The player to add in the team
     * @throws AlreadyInTeamException If the player is already in the team.
     * @throws TeamSizeException If the team is full
     * @throws StrangeException If the team is more than full. (WTF ?)
     */
    public void add(Player player) throws AlreadyInTeamException, TeamSizeException, StrangeException
    {
        UUID playerUUID = player.getUniqueId();

        if (this.teamSize != 0) {
            if (this.getLength() == this.teamSize && this.teamSize > 0)
                throw new TeamSizeException("The team '" + this.tablistPriority + "' is full.");
            else if (this.getLength() > this.teamSize && this.teamSize > 0)
                throw new StrangeException("The team '" + this.tablistPriority + "' is more than full. (WTF ?)");
        }

        if (this.has(playerUUID))
            throw new AlreadyInTeamException(
                "The player '" + player.getName() + "' is already in the team."
            );

        this.playersUUID.add(playerUUID);
        this.boardTeam.addEntry(player.getName());
    }

    /**
     * Remove a player from the team.
     *
     * @param player The player to remove from the team.
     */
    public void remove(Player player)
    {
        UUID playerUUID = player.getUniqueId();

        if (this.playersUUID.remove(playerUUID))
            this.boardTeam.removeEntry(player.getName());
    }

    /**
     * Check if the team has a player.
     *
     * @param player The player to check.
     * @return true if the player is found, otherwise false.
     */
    public boolean has(Player player)
    {
        return this.has(player.getUniqueId());
    }

    /**
     * Check if the team has a player.
     *
     * @param playerUUID The player's UUID to check.
     * @return true if the player is found, otherwise false.
     */
    public boolean has(UUID playerUUID)
    {
        return this.playersUUID.contains(playerUUID);
    }

    /**
     * Register the team to the scoreboard. (Required if you want to display the team above the player head)
     *
     * @param board The board containing the scoreboard.
     */
    public void setBoard(HashBoard board)
    {
        this.boardTeam = board.registerTeam(this);
    }

    /**
     * Get the UUID of the players present in the team.
     *
     * @return The list of UUID of the players present in the team.
     */
    public ArrayList<UUID> getPlayersUUID()
    {
        return this.playersUUID;
    }

    /**
     * Get the number of players currently present in the team.
     *
     * @return The number of players currently present in the team.
     */
    public int getLength()
    {
        return this.getPlayersUUID().size();
    }

    /**
     * Get the prefix of the team.
     *
     * @return The prefix of the team.
     */
    public String getPrefix()
    {
        return this.prefix;
    }

    /**
     * Get the suffix of the team.
     *
     * @return The suffix of the team.
     */
    public String getSuffix()
    {
        return this.suffix;
    }

    /**
     * Get the prefix of the team.
     *
     * @return The prefix of the team. (Represented as a String, but it's a number)
     */
    public String getTablistPriority()
    {
        return this.tablistPriority;
    }

    /**
     * Get the board of the team.
     *
     * @return the board of the team.
     */
    protected Team getBoardTeam()
    {
        return this.boardTeam;
    }

}
