package fr.hashktek.spigot.hashboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;

public class HashSideBar
{

    private HashBoard board;
    private Objective objective;

    private String title;
    private final HashMap<Integer, String> lines;


    /**
     * Create a sidebar
     * @param board The board where the sidebar will be set.
     */
    public HashSideBar(HashBoard board)
    {
        this.title = "NULL";
        this.lines = new HashMap<Integer, String>();
        this.setBoard(board);
    }


    /**
     * Set the title of the sidebar
     * @param title The new title of the sidebar.
     * @return The sidebar itself
     */
    public HashSideBar setTitle(String title)
    {
        if (this.objective != null)
            this.objective.setDisplayName(title);
        this.title = title;
        return this;
    }

    /**
     * Set a line in the sidebar.
     * @param index The index of the line.
     * @param value The value of the line.
     * @return The sidebar itself.
     */
    public HashSideBar setLine(int index, String value)
    {
        String previousValue = this.lines.get(index);

        if (this.board != null)
            this.updateSideBarLine(index, previousValue, value);

        if (previousValue == null) {
            this.lines.put(index, value);
        } else {
            this.lines.put(index, value);
        }

        return this;
    }

    /**
     * @param board The board.
     */
    protected void setBoard(HashBoard board)
    {
        Objective previousObjective = board.getScoreboard().getObjective("sidebar");

        if (previousObjective != null)
            previousObjective.unregister();

        this.objective = board.getScoreboard().registerNewObjective("sidebar", "dummy");
        this.board = board;
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.objective.setDisplayName(this.title);
        this.lines.forEach(
            (index, value) -> updateSideBarLine(index, null, value)
        );
    }

    /**
     * @param index The index of the line to update.
     * @param previousValue The previous value of the line.
     * @param newValue The new value of the line.
     */
    private void updateSideBarLine(int index, String previousValue, String newValue)
    {
        if (previousValue != null)
            this.board.getScoreboard().resetScores(previousValue);
        this.objective.getScore(newValue).setScore(index);
    }

}
