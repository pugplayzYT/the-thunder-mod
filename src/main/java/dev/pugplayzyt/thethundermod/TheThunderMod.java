package dev.pugplayzyt.thethundermod;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;

@Mod(TheThunderMod.MOD_ID)
public final class TheThunderMod {
    public static final String MOD_ID = "thethundermod";
    private static final int THUNDER_DURATION_TICKS = 6 * 60 * 20;

    public TheThunderMod() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        BlockPos placedPos = event.getPos();

        // The last block placed can be any block in the 3x3 dirt base or the
        // cobblestone trigger, so inspect nearby possible center positions.
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                BlockPos baseCenter = placedPos.offset(dx, 0, dz);
                if (tryTrigger(level, baseCenter, placedPos)) {
                    return;
                }

                // If the cobblestone itself was placed one block above the base,
                // the base center is one block lower than the placement event.
                BlockPos lowerBaseCenter = baseCenter.below();
                if (tryTrigger(level, lowerBaseCenter, placedPos)) {
                    return;
                }
            }
        }
    }

    private boolean tryTrigger(ServerLevel level, BlockPos baseCenter, BlockPos placedPos) {
        BlockPos triggerPos = baseCenter.above();

        if (!level.getBlockState(triggerPos).is(Blocks.COBBLESTONE)) {
            return false;
        }

        // Require a complete flat 3x3 dirt base.
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (!level.getBlockState(baseCenter.offset(x, 0, z)).is(Blocks.DIRT)) {
                    return false;
                }
            }
        }

        // Only react when the block that was just placed actually belongs to
        // this structure. This prevents unrelated nearby placements retriggering it.
        boolean placedIsTrigger = placedPos.equals(triggerPos);
        boolean placedIsBase = placedPos.getY() == baseCenter.getY()
                && Math.abs(placedPos.getX() - baseCenter.getX()) <= 1
                && Math.abs(placedPos.getZ() - baseCenter.getZ()) <= 1;

        if (!placedIsTrigger && !placedIsBase) {
            return false;
        }

        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            bolt.moveTo(
                    triggerPos.getX() + 0.5,
                    triggerPos.getY(),
                    triggerPos.getZ() + 0.5
            );
            level.addFreshEntity(bolt);
        }

        // Six minutes exactly at 20 ticks/second. No chat messages are sent.
        level.setWeatherParameters(0, THUNDER_DURATION_TICKS, true, true);
        return true;
    }
}
