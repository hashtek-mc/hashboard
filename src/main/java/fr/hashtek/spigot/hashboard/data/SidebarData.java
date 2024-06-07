package fr.hashtek.spigot.hashboard.data;

import java.util.ArrayList;
import java.util.HashMap;

public class SidebarData
{

    private final HashMap<Integer, String> lines = new HashMap<Integer, String>();


    public void setLine(int index, String line)
    {
        this.lines.put(index, line);
    }

    public HashMap<Integer, String> getLines()
    {
        return this.lines;
    }

}