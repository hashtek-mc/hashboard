package fr.hashtek.spigot.hashboard.enums;

public enum ObjectiveMode
{

    CREATE(0),
    DELETE(1),
    EDIT(2),
    ;

    private final int mode;

    ObjectiveMode(int mode)
    {
        this.mode = mode;
    }

    public int getMode()
    {
        return this.mode;
    }

}