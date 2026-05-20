package com.terrafirmamagica.mixins.common.registrate;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.google.common.collect.ListMultimap;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateProvider;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

@Mixin(value = AbstractRegistrate.class, remap = false)
public interface AbstractRegistrateAccessor {
    @Accessor
    ListMultimap<ProviderType<?>, @NotNull NonNullConsumer<? extends RegistrateProvider>> getDatagens();

    @Accessor
    NonNullSupplier<Boolean> getDoDatagen();
}
