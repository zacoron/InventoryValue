package net.runelite.client.plugins.inventoryvalue;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import java.awt.Color;


@ConfigGroup("inventoryvalue")
public interface InventoryValueConfig extends Config
{
    @ConfigItem(
            position = 1,
            keyName = "valueColor",
            name = "Value Text Color",
            description = "Change the color of the value text"
    )
    default Color valueColor() { return Color.WHITE; }

    @ConfigItem(
            position = 2,
            keyName = "profitTracker",
            name = "Profit Tracker",
            description = "Track the profit made from the given inventory value"
    )
    default boolean profitTracker() { return false; }

    @ConfigItem(
            position = 3,
            keyName = "initialInventoryValue",
            name = "Initial Inventory Value",
            description = "Manually enter the initial value of inventory to track relative profit"
    )
    default int initialInventoryValue() { return 0; }

    @ConfigItem(
            position = 4,
            keyName = "integrateAmmo",
            name = "Integrate Ammo",
            description = "Integrate the cost of ammo used to better calculate profit"
    )
    default boolean integrateAmmo() { return false; }

    @ConfigItem(
            position = 5,
            keyName = "initialAmmo",
            name = "Initial Ammo Count",
            description = "Manually enter the initial amount of ammo"
    )
    default int initialAmmo() { return 0; }

    @ConfigItem(
            position = 6,
            keyName = "profitColor",
            name = "Profit Text Color",
            description = "Change the color of the profit text"
    )
    default Color profitColor() { return Color.GREEN; }
}
