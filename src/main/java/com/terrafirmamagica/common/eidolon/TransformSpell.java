package com.terrafirmamagica.common.eidolon;

import com.terrafirmamagica.TFMCore;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ModConfigSpec;

import alexthw.eidolon_repraised.api.capability.IMana;
import alexthw.eidolon_repraised.common.spell.StaticSpell;
import alexthw.eidolon_repraised.registries.EidolonCapabilities;
import alexthw.eidolon_repraised.util.KnowledgeUtil;

public class TransformSpell extends StaticSpell {

    private final EntityType<?> sourceType;
    private final EntityType<?> targetType;

    public TransformSpell(ResourceLocation name, int cost, EntityType<?> source, EntityType<?> target) {
        super(name, cost);
        this.sourceType = source;
        this.targetType = target;
    }

    @Override
    public boolean canCast(Level world, BlockPos pos, Player player) {
        if (!KnowledgeUtil.knowsResearch(player,
                ResourceLocation.fromNamespaceAndPath("tfm", "transform_research")))
            return false;

        HitResult ray = rayTrace(player, player.getAttributeValue(
                net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE), 0, false);

        if (ray instanceof EntityHitResult entityHit) {
            if (entityHit.getEntity().getType() == sourceType)
                return true;
        }

        player.displayClientMessage(
                Component.translatable("tfm.message.no_valid_target"), true);
        return false;
    }

    @Override
    public void cast(Level world, BlockPos pos, Player player) {
        if (world.isClientSide)
            return;

        TFMCore.LOGGER.info("[TFM] Cast called, mana before: {}",
                player.getCapability(EidolonCapabilities.MANA_CAPABILITY).getMagic());

        HitResult ray = rayTrace(player, player.getAttributeValue(
                net.minecraft.world.entity.ai.attributes.Attributes.ENTITY_INTERACTION_RANGE), 0, false);

        if (!(ray instanceof EntityHitResult entityHit))
            return;

        Entity target = entityHit.getEntity();
        if (target.getType() != sourceType)
            return;

        Entity newMob = targetType.create(world);
        if (newMob == null)
            return;

        ((ServerLevel) world).sendParticles(
                ParticleTypes.POOF,
                target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                20, 0.3, 0.3, 0.3, 0.05);

        newMob.copyPosition(target);
        target.discard();
        world.addFreshEntity(newMob);

        world.playSound(null, target.blockPosition(),
                SoundEvents.ELDER_GUARDIAN_CURSE, SoundSource.HOSTILE, 1.0f, 1.0f);
        IMana.expendMana(player, getCost());

        TFMCore.LOGGER.info("[TFM] Mana after: {}",
                player.getCapability(EidolonCapabilities.MANA_CAPABILITY).getMagic());
    }

    @Override
    public void buildConfig(ModConfigSpec.Builder builder) {
        super.buildConfig(builder);
    }
}
