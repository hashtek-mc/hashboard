package fr.hashtek.spigot.hashboard.sidebars;

/**
 * A display name manager for the sidebar.
 */
class SidebarDisplayName
{

    private String title;
    private boolean hasChanged;

    /**
     * Create a new display name.
     */
    public SidebarDisplayName()
    {
        this.title = null;
        this.hasChanged = false;
    }

    /**
     * Check if the title changed.
     *
     * @return {@code true} if the title changed, {@code false} otherwise.
     */
    public boolean checkIfHasChanged()
    {
        return this.hasChanged;
    }

    /**
     * Set a new display name.
     *
     * @param title The new display name.
     */
    public void set(String title)
    {
        this.title = title;
        this.hasChanged = true;
    }

    /**
     * Get the display name.
     *
     * @return the display name.
     */
    public String get()
    {
        return this.title;
    }

    /**
     * Validate the display name.
     * <br><strong style="color: white; background-color: red">WARNING</strong>
     * Call this method after updating the players' sidebar.
     */
    public void validate()
    {
        this.hasChanged = false;
    }

}
