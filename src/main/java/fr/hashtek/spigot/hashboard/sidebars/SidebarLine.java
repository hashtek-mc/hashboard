package fr.hashtek.spigot.hashboard.sidebars;

/**
 * A sidebar line manager.
 */
class SidebarLine
{

    private final int index;
    private String previousValue;
    private String value;
    private boolean _hasBeenEdited;

    /**
     * Create a new SidebarLine.
     *
     * @param index The index of the line.
     * @param value The value of the line.
     */
    SidebarLine(int index, String value)
    {
        this.index = index;
        this.previousValue = value;
        this.value = value;
        this._hasBeenEdited = false;
    }

    /**
     * Edit the line.
     *
     * @param value The new value of the line.
     */
    public void edit(String value)
    {
        this.previousValue = this.value;
        this.value = value;
        this._hasBeenEdited = true;
    }

    /**
     * Check if the line has been modified or not.
     *
     * @return {@code true} if the value of the line has been edited, {@code false} otherwise.
     */
    public boolean hasBeenEdited()
    {
        return this._hasBeenEdited;
    }

    /**
     * Get the value of the line.
     *
     * @return  The value of the line.
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * Get the previous value of the line.
     *
     * @return  The previous value of the line.
     */
    public String getPreviousValue()
    {
        return this.previousValue;
    }

    /**
     * Get the index of the line.
     *
     * @return The index of the line.
     */
    public int getIndex()
    {
        return this.index;
    }

    /**
     * <b style="color: white; background-color: red">WARNING</b>
     * <br>This is an internal method. You are NOT supposed to call this method by yourself.
     * <br><br> Save the score.
     */
    public void save()
    {
        this.previousValue = this.value;
        this._hasBeenEdited = false;
    }

}
