package fr.hashtek.spigot.hashboard.packets;

import fr.hashtek.spigot.hashboard.sidebars.SidebarLine;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardScore;

import java.util.HashMap;

public class PacketScore
{

    public static HashMap<Integer, PacketPlayOutScoreboardScore> getScoresPackets(String objectiveId, HashMap<Integer, SidebarLine> lines)
    {
        HashMap<Integer, PacketPlayOutScoreboardScore> linesPackets = new HashMap<Integer, PacketPlayOutScoreboardScore>();

        lines.forEach((index, line) -> {
            if (!line.hasBeenEdited())
                return;

            PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line.getPreviousValue());
            PacketManager.setField(packet, "b", objectiveId);
            PacketManager.setField(packet, "c", line.getValue());
            PacketManager.setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.CHANGE);
            linesPackets.put(index, packet);
        });

        return linesPackets;
    }

    public static PacketPlayOutScoreboardScore setScore(String objectiveId, SidebarLine line)
    {

    }

    public static PacketPlayOutScoreboardScore removeScore(String objectiveId, String line)
    {
        PacketPlayOutScoreboardScore packet = new PacketPlayOutScoreboardScore(line);

        PacketManager.setField(packet, "b", objectiveId);
        PacketManager.setField(packet, "d", PacketPlayOutScoreboardScore.EnumScoreboardAction.REMOVE);

        return packet;
    }

}
