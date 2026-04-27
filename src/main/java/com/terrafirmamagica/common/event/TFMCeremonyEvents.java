package com.terrafirmamagica.common.event;

import com.terrafirmamagica.TFMCore;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.entities.livestock.Age;
import net.dries007.tfc.common.entities.livestock.Mammal;
import net.dries007.tfc.common.entities.livestock.TFCAnimal;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import pokefenn.totemic.api.TotemicAPI;
import pokefenn.totemic.api.TotemicBlockTags;
import pokefenn.totemic.api.TotemicEntityUtil;
import pokefenn.totemic.api.event.CeremonyEvent;

@EventBusSubscriber(modid = TFMCore.MOD_ID)
public class TFMCeremonyEvents {

    private static final ResourceLocation FERTILITY = ResourceLocation.parse("totemic:fertility");

    @SubscribeEvent
    public static void onFertilityCeremonyEffect(CeremonyEvent.EffectTick event) {
        if (!event.getCeremony().getRegistryName().equals(FERTILITY)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (event.getContext().getTime() % 20 != 0) return;

        event.setCanceled(true);

        var aabb = TotemicEntityUtil.getAABBAround(event.getPos(), 8);

        level.getEntitiesOfClass(Mammal.class, aabb, animal ->
                !animal.isMale() &&
                        animal.getAgeType() == Age.ADULT &&
                        !animal.isFertilized() &&
                        animal.isReadyToMate()
        ).stream().limit(2).forEach(female -> {
            // If there is a male use it
            var optionalMale = level.getEntitiesOfClass(Mammal.class, aabb, male ->
                    male.isMale() &&
                            male.getType() == female.getType() &&
                            male.getAgeType() == Age.ADULT
            ).stream().findFirst();

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
        if (!event.getCeremony().getRegistryName().equals(ANIMAL_GROWTH)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (event.getContext().getTime() % 20 != 0) return;

        event.setCanceled(true);

        var aabb = TotemicEntityUtil.getAABBAround(event.getPos(), 8);

        // Faster Growth for kids
        level.getEntitiesOfClass(TFCAnimal.class, aabb,
                animal -> animal.getAgeType() == Age.CHILD
        ).forEach(animal -> {
            if (level.random.nextInt(10) == 0) {
                long acceleration = (long) (ICalendar.PLAYER_TICKS_IN_DEFAULT_DAY * animal.animalConfig().adulthoodDays().get() * 0.05);
                animal.setBirthTick(animal.getBirthTick() - acceleration);
            }
        });

        // Faster Pregnancy
        level.getEntitiesOfClass(Mammal.class, aabb,
                animal -> !animal.isMale() && animal.isFertilized()
        ).forEach(pregnant -> {
            if (level.random.nextInt(10) == 0) {
                pregnant.setPregnantTime(
                        pregnant.getPregnantTime() - pregnant.getGestationDays() / 50
                );
            }
        });
    }

    private static final ResourceLocation PLANT_GROWTH = ResourceLocation.parse("totemic:zaphkiel_waltz");

    @SubscribeEvent
    public static void onZaphkielWaltzEffect(CeremonyEvent.EffectTick event) {
        if (!event.getCeremony().getRegistryName().equals(PLANT_GROWTH)) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (event.getContext().getTime() % 7 != 0) return;

        event.setCanceled(true);

        TotemicAPI.get().ceremony().forEachBlockIn(level,
                TotemicEntityUtil.getBoundingBoxAround(event.getPos(), 6),
                (pos, state) -> {
                    if (level.getBlockEntity(pos) instanceof CropBlockEntity crop) {
                        if (level.random.nextInt(4) < 3) {
                            float newGrowth = Math.min(1.0f, crop.getGrowth() + 0.05f);
                            crop.setGrowth(newGrowth);

                        }
                    } else if (state.isRandomlyTicking() &&
                            state.is(TotemicBlockTags.ZAPHKIEL_WALTZ_GROWABLE)) {
                        // Fallback for vanilla crops
                        if (level.random.nextInt(4) < 3) {
                            state.randomTick(level, pos, level.random);
                        }
                    }
                }
        );
    }
}