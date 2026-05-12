package com.terrafirmamagica.common.entity.buffalo;

import org.jetbrains.annotations.NotNull;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.entities.livestock.DairyAnimal;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.config.animals.ProducingMammalConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class TFMBuffalo extends DairyAnimal {
    public TFMBuffalo(EntityType<? extends DairyAnimal> type, Level level, TFCSounds.EntityId sounds, ProducingMammalConfig config) {
        super(type, level, sounds, config);
    }

    public static TFMBuffalo makeTFMBuffalo(EntityType<? extends DairyAnimal> type, Level level) {
        return new TFMBuffalo(type, level, TFCSounds.COW, TFCConfig.SERVER.cowConfig);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, (double) 25.0F).add(Attributes.MOVEMENT_SPEED, 0.15).add(Attributes.ATTACK_DAMAGE, (double) 2.0F);
    }

    public static boolean spawnRules(EntityType<? extends TFMBuffalo> type, LevelAccessor level, MobSpawnType spawn, BlockPos pos, RandomSource rand) {
        return level.getBlockState(pos).isAir();
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() - 0.2F;
    }

    @Override
    public @NotNull TagKey<Item> getFoodTag() {
        return TFCTags.Items.COW_FOOD;
    }
}
