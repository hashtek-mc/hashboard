package fr.hashtek.spigot.hashboard.objectives;

public enum ObjectiveSlot
{

    LIST(0),
    SIDEBAR(1),
    BELOW_NAME(2)
    ;

    private final int slot;

    ObjectiveSlot(int slot)
    {
        this.slot = slot;
    }

    public int getSlot()
    {
        return slot;
    }

}
