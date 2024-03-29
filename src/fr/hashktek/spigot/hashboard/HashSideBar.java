package fr.hashktek.spigot.hashboard;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

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
     * Add a line on the top of the sidebar.
     * @param value The value of the new line.
     * @return The sidebar itself.
     */
    public HashSideBar addLine(String value)
    {
        StringBuilder valueBuilder = new StringBuilder().append(value);
        Set<Integer> keySet = null;
        int index = 0;

        if (!this.lines.isEmpty()) {
            keySet = this.lines.keySet();
            for (Integer key : keySet) {
                if (this.lines.get(key).contentEquals(valueBuilder))
                    valueBuilder.append(" ");
                if (key > index)
                    index = key;
            }
        }

        this.setLine(index + 1, valueBuilder.toString());
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

        this.lines.put(index, value);

        return this;
    }

    /**
     * Set multiple lines to the same value in the sidebar.
     * @param value The value of the line
     * @param indexes The indexes.
     * @return The sidebar itself.
     */
    public HashSideBar setLines(String value, int... indexes)
    {
        StringBuilder spaces = new StringBuilder();

        for (int index : indexes) {
            this.setLine(index, value + spaces);
            spaces.append(" ");
        }
        return this;

    }

    /**
     * Set the lines between `from` and `to` to value.
     * @param value The value to set to the lines.
     * @param from The start of the filling.
     * @param to The end of the filling.
     * @return The sidebar itself.
     */
    public HashSideBar fillLines(String value, int from, int to)
    {
        int[] indexes = this.getIndexes(from, to, false);

        this.setLines(value, indexes);
        return this;
    }

    /**
     * Remove a line from the sidebar.
     * @param index The index of the line.
     * @return The sidebar itself.
     */
    public HashSideBar removeLine(int index)
    {
        if (this.lines.containsKey(index))
            this.board.getScoreboard().resetScores(this.lines.remove(index));
        return this;
    }

    /**
     * Remove multiple lines from the sidebar.
     * @param indexes The indexes of the lines to remove from the sidebar.
     * @return The sidebar itself.
     */
    public HashSideBar removeLines(int... indexes)
    {
        for (int index : indexes)
            this.removeLine(index);
        return this;
    }

    /**
     * Remove the lines in the range from `from` to `to`.
     * @param from The start of the clearing.
     * @param to The end of the clearing.
     * @return The sidebar itself.
     */
    public HashSideBar clearLines(int from, int to)
    {
        int[] indexes = this.getIndexes(from, to, true);

        this.removeLines(indexes);
        return this;
    }

    /**
     * Removes every line in the sidebar.
     *
     * @return  The sidebar itself.
     */
    public HashSideBar flush()
    {
        int highestKey = 0;

        for (int line : this.lines.keySet())
            if (line > highestKey)
                highestKey = line;

        this.clearLines(0, highestKey);
        this.lines.clear();
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

    /**
     * Get the indexes from `from` to `to` in an array.
     * @param from The start of the indexes
     * @param to The end of the indexes
     * @return The indexes in an array
     */
    private int[] getIndexes(int from, int to, boolean checkIndex)
    {
        int min = Math.min(from, to);
        int max = Math.max(from, to);
        int index = 0;
        final int[] indexes = new int[max - min];

        for (int i = 0; i < indexes.length; i++) {
            index = max - i;
            if (!checkIndex || this.lines.containsKey(index))
                indexes[i] = index;
        }

        return indexes;
    }

}
