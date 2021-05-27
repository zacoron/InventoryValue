package net.runelite.client.plugins.inventoryvalue;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.OverlayManager;


@PluginDescriptor(
        name = "Inventory Value",
        description = "Show the GE value of all items currently in your inventory",
        tags = {"inventory", "value", "price", "overlay"}
)
public class InventoryValuePlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private InventoryValueOverlay overlay;

    @Inject
    private InventoryValueConfig config;

    @Provides
    InventoryValueConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(InventoryValueConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }


}