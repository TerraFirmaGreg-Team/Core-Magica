package com.terrafirmamagica.common.event;

import com.terrafirmamagica.TFMCore;

import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.crop.CropBlock;
import net.dries007.tfc.common.entities.livestock.Age;
import net.dries007.tfc.common.entities.livestock.Mammal;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.TotemicBlockTags;
import pokefenn.totemic.api.TotemicEntityUtil;
import pokefenn.totemic.api.event.CeremonyEvent;
import pokefenn.totemic.util.MiscUtil;

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class TFMCeremonyEvents {

    private static final ResourceLocation FERTILITY = ResourceLocation.parse("totemic:fertility");

    @SubscribeEvent
    public static void onFertilityCeremonyEffect(CeremonyEvent.EffectTick event) {
        if (!event.getCeremony().getRegistryName().equals(FERTILITY))
            return;
        if (!(event.getLevel() instanceof ServerLevel level))
            return;
        if (event.getContext().getTime() % 20 != 0)
            return;

        event.setCanceled(true);

        transformSaplings(level, event.getPos());

        var aabb = TotemicEntityUtil.getAABBAround(event.getPos(), 8);

        level.getEntitiesOfClass(Mammal.class, aabb, animal -> !animal.isMale() &&
                animal.getAgeType() == Age.ADULT &&
                !animal.isFertilized() &&
                animal.isReadyToMate()).stream().limit(2).forEach(female -> {
                    // If there is a male use it
                    var optionalMale = level.getEntitiesOfClass(Mammal.class, aabb, male -> male.isMale() &&
                            male.getType() == female.getType() &&
                            male.getAgeType() == Age.ADULT).stream().findFirst();

                    if (optionalMale.isPresent()) {
                        female.onFertilized(optionalMale.get()); // Add male genetics
                    } else {
                        female.setFertilized(true); // Without male random genes
                    }
                    female.setPregnantTime(Calendars.SERVER.getTotalCalendarDays());
                });
    }

    private static final ResourceLocation ANIMAL_GROWTH = ResourceLocation.parse("totemic:animal_growth");

    @SubscribeEvent
    public static void onAnimalGrowthCeremonyEffect(CeremonyEvent.EffectTick event) {
        if (!event.getCeremony().getRegistryName().equals(ANIMAL_GROWTH))
            return;
        if (!(event.getLevel() instanceof ServerLevel level))
            return;
        if (event.getContext().getTime() % 20 != 0)
            return;

        event.setCanceled(true);

        var aabb = TotemicEntityUtil.getAABBAround(event.getPos(), 8);

        // Faster Growth for kids
        level.getEntitiesOfClass(TFCAnimal.class, aabb,
                animal -> animal.getAgeType() == Age.CHILD).forEach(animal -> {
                    if (level.random.nextInt(10) == 0) { // 1 = 100% / 2 = 50% / 10 = 10%
                        long acceleration = (long) (ICalendar.PLAYER_TICKS_IN_DEFAULT_DAY * animal.animalConfig().adulthoodDays().get() * 0.1); // 1.0 = Adult / 0.5 = 50% / 0.1 = 10%
                        animal.setBirthTick(animal.getBirthTick() - acceleration);
                    }
                });

        // Faster Pregnancy
        level.getEntitiesOfClass(Mammal.class, aabb,
                animal -> !animal.isMale() && animal.isFertilized()).forEach(pregnant -> {
                    if (level.random.nextInt(10) == 0) { // 1 = 100% / 2 = 50% / 10 = 10%
                        pregnant.setPregnantTime(
                                pregnant.getPregnantTime() - pregnant.getGestationDays() / 10 //  1 = Instantly / 10 = 10% of reduction
                        );
                    }
                });
    }

    private static final ResourceLocation PLANT_GROWTH = ResourceLocation.parse("totemic:zaphkiel_waltz");

    @SubscribeEvent
    public static void onZaphkielWaltzEffect(CeremonyEvent.EffectTick event) {
        if (!event.getCeremony().getRegistryName().equals(PLANT_GROWTH))
            return;
        if (!(event.getLevel() instanceof ServerLevel level))
            return;
        if (event.getContext().getTime() % 7 != 0)
            return;

        event.setCanceled(true);

        TotemicAPI.get().ceremony().forEachBlockIn(level,
                TotemicEntityUtil.getBoundingBoxAround(event.getPos(), 6),
                (pos, state) -> {
                    if (level.getBlockEntity(pos) instanceof CropBlockEntity crop
                            && state.getBlock() instanceof CropBlock cropBlock) {
                        if (level.random.nextInt(20) == 0) { // 1 = 100% / 2 = 50% / 10 = 10% / 20 = 5%
                            float newGrowth = Math.min(1.0f, crop.getGrowth() + 0.05f); // 1.0 = Instant Maturity / 0.5 = 50% of growth / 0.1 = 10% / 0.01 = 1%
                            crop.setGrowth(newGrowth);
                            crop.setYield(Math.min(1.0f, crop.getYield() + 0.01f));
                            crop.setChanged();
                            int age = newGrowth >= 1.0f
                                    ? cropBlock.getMaxAge()
                                    : Mth.floor(newGrowth * cropBlock.getMaxAge());
                            level.setBlockAndUpdate(pos, state.setValue(cropBlock.getAgeProperty(), age));
                        }
                    } else if (state.isRandomlyTicking() &&
                            state.is(TotemicBlockTags.ZAPHKIEL_WALTZ_GROWABLE)) {
                        // Fallback for vanilla crops
                        if (level.random.nextInt(4) < 3) {
                            state.randomTick(level, pos, level.random);
                        }
                    }
                });

    }

    private static final ResourceLocation AFC_CEDAR_SAPLING = ResourceLocation.parse("afc:wood/sapling/redcedar");

    private static void transformSaplings(Level level, BlockPos pos) {
        Block afcCedar = BuiltInRegistries.BLOCK.get(AFC_CEDAR_SAPLING);

        BlockPos.betweenClosedStream(TotemicEntityUtil.getBoundingBoxAround(pos, 6))
                .filter(p -> {
                    var state = level.getBlockState(p);
                    return state.is(BlockTags.SAPLINGS) && state.getBlock() != afcCedar;
                })
                .findAny()
                .ifPresent(p -> {
                    level.setBlock(p, afcCedar.defaultBlockState(), Block.UPDATE_ALL);
                    MiscUtil.spawnServerParticles(ParticleTypes.HAPPY_VILLAGER, level, Vec3.atCenterOf(p), 10, new Vec3(0.5, 0.5, 0.5), 0);
                });
    }
}
