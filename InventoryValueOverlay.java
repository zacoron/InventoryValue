package net.runelite.client.plugins.inventoryvalue;

import java.awt.*;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.LineComponent;


public class InventoryValueOverlay extends Overlay
{
    private static final int INVENTORY_SIZE = 28;

    private final Client client;
    private final InventoryValueConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    ItemManager itemManager;

    @Inject
    private InventoryValueOverlay(Client client, InventoryValueConfig config)
    {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        panelComponent.getChildren().clear();

        final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);
        final Item[] items = itemContainer.getItems();
        int totalPrice = 0;

        // determine the value of each item in inventory
        for(int i =0; i < INVENTORY_SIZE; i++)
        {
            if(i < items.length)
            {
                final Item item = items[i];
                int itemId = item.getId();
                int price = itemManager.getItemPrice(itemId);

                if(price > 0)
                {
                    totalPrice += price * item.getQuantity(); // add value of item * quantity to totalPrice
                }
            }
        }


        int totalPriceWidth = graphics.getFontMetrics().stringWidth(Integer.toString(totalPrice));

        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth("Inventory Value:") + totalPriceWidth + 15,
                0));

        panelComponent.getChildren().add(LineComponent.builder()
                .left("Inventory Value:")
                .right(Integer.toString(totalPrice)).rightColor(config.valueColor())
                .build());


        // display the (optional) profit tracker
        if(config.profitTracker())
        {
            int profit = totalPrice - config.initialInventoryValue(); // calculate profit from inventory
            int ammoUsedCost = 0; // by default, ammoUsedCost is 0 before being determined below (if integrateAmmo is on)

            // if integrateAmmo is enabled
            if(config.integrateAmmo())
            {
                // get the player's equipment
                final ItemContainer container = client.getItemContainer((InventoryID.EQUIPMENT));
                final Item[] equipment = container.getItems();

                // get the weapon from the WEAPON slot in the equipment
                final Item weapon = equipment[EquipmentInventorySlot.WEAPON.getSlotIdx()];
                final ItemComposition weaponComp = itemManager.getItemComposition(weapon.getId());

                // get the ammo from AMMO slot in the equipment
                final Item ammo = equipment[EquipmentInventorySlot.AMMO.getSlotIdx()];

                if(weaponComp.isStackable()) // if the weapon itself is stackable (knives, darts, etc)
                {
                    // calculate the cost of consumed (weapon slot) ammo
                    ammoUsedCost = itemManager.getItemPrice(weapon.getId()) * (config.initialAmmo() - weapon.getQuantity());
                }
                else // if the weapon uses ammo (arrows, bolts, etc)
                {
                    // calculate the cost of consumed (ammo slot) ammo
                    ammoUsedCost = itemManager.getItemPrice(ammo.getId()) * (config.initialAmmo() - ammo.getQuantity());
                }
            }

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Profit:")
                    .right(Integer.toString(profit - ammoUsedCost)).rightColor(config.profitColor())
                    .build());
        }

        return panelComponent.render(graphics);
    }
}
