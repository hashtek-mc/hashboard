package fr.hashtek.spigot.hashboard.objectives;

import fr.hashtek.spigot.hashboard.packets.PacketManager;
import net.minecraft.server.v1_8_R3.IScoreboardCriteria;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardDisplayObjective;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;

public class ObjectiveManager
{

    /**
     * Create a new objective packet to be sent to a {@link net.minecraft.server.v1_8_R3.PlayerConnection}.
     * An objective packet can be used to create custom tablist or custom sidebars.
     *
     * @param   mode        The objective mode.
     * @param   id          The unique identifier of the objective. (Use the player's name for example, or team name)
     * @param   displayName The display name of the objective.
     * @return              The created (or got) packet of the objective, ready to be sent.
     */
    public static PacketPlayOutScoreboardObjective createObjective(ObjectiveMode mode, String id, String displayName)
    {
        PacketPlayOutScoreboardObjective packet = createObjective(mode, id);

        PacketManager.setField(packet, "b", displayName);
        return packet;
    }

    /**
     * Create a new objective packet to be sent to a {@link net.minecraft.server.v1_8_R3.PlayerConnection}.
     * An objective packet can be used to create custom tablist or custom sidebars.
     *
     * @param   mode    The objective mode.
     * @param   id      The unique identifier of the objective. (Use the player's name for example, or team name)
     * @return          The created (or got) packet of the objective, ready to be sent.
     */
    public static PacketPlayOutScoreboardObjective createObjective(ObjectiveMode mode, String id)
    {
        PacketPlayOutScoreboardObjective packet = new PacketPlayOutScoreboardObjective();

        PacketManager.setField(packet, "a", id);
        PacketManager.setField(packet, "d", mode);

        switch (mode) {
            case CREATE:
            case EDIT:
                PacketManager.setField(packet, "b", "NULL");
                PacketManager.setField(packet, "c", IScoreboardCriteria.EnumScoreboardHealthDisplay.INTEGER);
            case DELETE:
                break;
        }
        return packet;
    }

    public static PacketPlayOutScoreboardDisplayObjective getDisplayObjectiveSlot(ObjectiveSlot slot, String id)
    {
        PacketPlayOutScoreboardDisplayObjective packet = new PacketPlayOutScoreboardDisplayObjective();

        PacketManager.setField(packet, "a", slot.getSlot());
        PacketManager.setField(packet, "b", id);
        return packet;
    }

}