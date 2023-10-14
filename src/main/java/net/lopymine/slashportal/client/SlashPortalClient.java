package net.lopymine.slashportal.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import com.mojang.brigadier.Command;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.*;

public class SlashPortalClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("portal")
                .executes((context -> {
                    FabricClientCommandSource source = context.getSource();
                    ClientPlayerEntity player = source.getPlayer();
                    if (player == null) return 0;

                    Vec3d pos = player.getPos();
                    World world = player.getWorld();

                    if (world == null) return 0;

                    onSlashPortal(pos.x, pos.y, pos.z, world.getRegistryKey().equals(World.NETHER), player);
                    return Command.SINGLE_SUCCESS;
                })))));
    }

    private void onSlashPortal(double d, double i, double k, boolean bl, ClientPlayerEntity player) {
        int x = (int) Math.round(bl ? d * 8 : d / 8);
        int y = (int) Math.round(i);
        int z = (int) Math.round(bl ? k * 8 : k / 8);

        String value = x + " " + y + " " + z;
        String cord = "X: " + x + " Y: " + y + " Z: " + z;

        Formatting formatting = (bl ? Formatting.GREEN : Formatting.RED);

        MutableText literal = bl ? Text.translatable("slash_portal.overworld_text") : Text.translatable("slash_portal.nether_text");

        player.sendMessage(literal.setStyle(Style.EMPTY.withColor(formatting).withClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, value))));
        player.sendMessage(Text.literal(cord).setStyle(Style.EMPTY.withColor(formatting).withClickEvent(new ClickEvent(Action.COPY_TO_CLIPBOARD, value))));
    }
}
