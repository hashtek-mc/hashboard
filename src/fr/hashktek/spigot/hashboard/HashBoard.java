package fr.hashktek.spigot.hashboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;

public class HashBoard
{

    private final Scoreboard scoreboard;
    private HashSideBar sideBar;


    /**
     * Create a custom scoreboard (Used for the teams, the tablist and the sidebar)
     */
    public HashBoard()
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        this.scoreboard = manager.getNewScoreboard();
        this.sideBar = null;
    }

    /**
     * Remove the team from the scoreboard
     * @param hashTeam The team to unregister
     */
    public void unregisterTeam(HashTeam hashTeam)
    {
        hashTeam.getBoardTeam().unregister();
    }

    /**
     * Add the team to the scoreboard.
     * @param hashTeam The team to register
     * @return The team itself
     */
    public Team registerTeam(HashTeam hashTeam)
    {
        Team team = this.scoreboard.registerNewTeam(hashTeam.getTablistPriority());

        team.setPrefix(hashTeam.getPrefix());
        team.setSuffix(hashTeam.getSuffix());
        hashTeam.getPlayersUUID().forEach(
            uuid -> team.addEntry(Bukkit.getPlayer(uuid).getName())
        );

        return team;
    }

    /**
     * Set the scoreboard to a list of player.
     * @param players The list of player to add the scoreboard to.
     */
    public void setToPlayers(Player... players)
    {
        for (Player player : players)
            player.setScoreboard(scoreboard);
    }

    /**
     * Get the HashSideBar of the scoreboard.
     * @return The sidebar of the scoreboard.
     */
    public @Nullable HashSideBar getSideBar()
    {
        return this.sideBar;
    }

    /**
     * @param sideBar The sidebar to set to the scoreboard
     */
    protected void setSideBar(HashSideBar sideBar)
    {
        this.sideBar = sideBar;
        this.sideBar.setBoard(this);
    }

    /**
     * @return The scoreboard
     */
    protected Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

}
