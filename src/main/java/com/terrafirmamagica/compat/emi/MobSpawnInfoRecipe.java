package com.terrafirmamagica.compat.emi;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.Widget;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SpawnEggItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MobSpawnInfoRecipe implements EmiRecipe {

    private static final Map<ResourceLocation, LivingEntity> ENTITY_CACHE = new HashMap<>();

    private static final int WIDTH = 160;
    private static final int ENTITY_SIZE = 40;
    private static final int LINE_H = 10;
    private static final int PADDING = 4;

    private final MobSpawnData data;
    private final ResourceLocation id;

    public MobSpawnInfoRecipe(MobSpawnData data) {
        this.data = data;
        this.id = ResourceLocation.fromNamespaceAndPath("tfm",
                "mob_spawn/" + data.entityId().getNamespace() + "/" + data.entityId().getPath());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return TFMEmiPlugin.MOB_SPAWN_INFO;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return id;
    }

    @Override
    public int getDisplayWidth() {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        int lines = countInfoLines();
        return LINE_H + ENTITY_SIZE + 6 + (lines * LINE_H) + PADDING;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        SpawnEggItem egg = data.getSpawnEgg();
        return egg != null ? List.of(EmiStack.of(egg)) : List.of();
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of();
    }

    @Override
    public boolean supportsRecipeTree() {
        return false;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int y = 0;

        Component name = data.entityType().getDescription();
        int nameW = Minecraft.getInstance().font.width(name);
        widgets.addText(name.getVisualOrderText(), (WIDTH - nameW) / 2, y, 0xFFFFFF, true);
        y += LINE_H + 2;

        // Mod Entity
        final int entityY = y;
        final int entityCenterX = WIDTH / 2;

        widgets.add(new Widget() {
            final Bounds bounds = new Bounds(entityCenterX - ENTITY_SIZE / 2, entityY, ENTITY_SIZE, ENTITY_SIZE);

            @Override
            public Bounds getBounds() {
                return bounds;
            }

            @Override
            public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
                if (Minecraft.getInstance().level == null) return;
                LivingEntity entity = getOrCreateEntity(data.entityType(), data.entityId());
                if (entity == null) return;

                Window window = Minecraft.getInstance().getWindow();
                PoseStack poseStack = guiGraphics.pose();

                Matrix4f mvpMatrix = new Matrix4f(RenderSystem.getProjectionMatrix())
                        .mul(new Matrix4f(poseStack.last().pose()));
                Vector4f topLeftClip = mvpMatrix.transform(new Vector4f(0, 0, 0, 1));
                int screenX = Math.round((topLeftClip.x / topLeftClip.w + 1) / 2f * window.getGuiScaledWidth());
                int screenY = Math.round((1 - topLeftClip.y / topLeftClip.w) / 2f * window.getGuiScaledHeight());

                int bx  = entityCenterX - ENTITY_SIZE / 2;
                int by  = entityY;
                int bx2 = entityCenterX + ENTITY_SIZE / 2;
                int by2 = entityY + ENTITY_SIZE;

                EntityDimensions dim = entity.getType().getDimensions();
                int scale = (int) Math.min(20 / dim.height(), 20 / dim.width());

                guiGraphics.pose().pushPose();
                guiGraphics.enableScissor(
                        screenX + bx + 1, screenY + by + 1,
                        screenX + bx2 - 1, screenY + by2 - 1);
                InventoryScreen.renderEntityInInventoryFollowsMouse(
                        guiGraphics,
                        -screenX + bx, -screenY + by,
                        screenX + bx2, screenY + by2,
                        scale, 0.0625f,
                        mouseX, mouseY,
                        entity);
                guiGraphics.disableScissor();
                guiGraphics.pose().popPose();
            }
        });

        y += ENTITY_SIZE + 6;

        // Add egg icon
        SpawnEggItem egg = data.getSpawnEgg();
        if (egg != null) {
            widgets.addSlot(EmiStack.of(egg), WIDTH - 18, 0).drawBack(false);
        }

        y = addInfoLines(widgets, y);
    }

    private int addInfoLines(WidgetHolder widgets, int y) {
        // Seasons
        String seasonsStr = data.seasons().stream()
                .map(s -> Component.translatable("tfm.emi.season." + s).getString())
                .reduce((a, b) -> a + ", " + b)
                .orElse("");
        int seasonColor = getSeasonsColor(data.seasons());
        widgets.addText(
                Component.translatable("tfm.emi.spawn.label.seasons").append(
                        Component.literal(seasonsStr).withStyle(s -> s.withColor(seasonColor))
                ).getVisualOrderText(),
                PADDING, y, 0x000000, false);
        y += LINE_H;

        // Spawn type
        String spawnTypeKey = switch (data.spawnType()) {
            case SURFACE -> "tfm.emi.spawn.type.surface";
            case CAVE    -> "tfm.emi.spawn.type.cave";
            case BOTH    -> "tfm.emi.spawn.type.both";
            case WATER   -> "tfm.emi.spawn.type.water";
            case SHORE   -> "tfm.emi.spawn.type.shore";
        };
        widgets.addText(
                Component.translatable("tfm.emi.spawn.label.spawn").append(
                        Component.translatable(spawnTypeKey).withStyle(ChatFormatting.DARK_GRAY)
                ).getVisualOrderText(),
                PADDING, y, 0x000000, false);
        y += LINE_H;

        // Day time if not all
        if (data.dayTime() != MobSpawnData.DayTime.ALL) {
            String dtKey = data.dayTime() == MobSpawnData.DayTime.NIGHT
                    ? "tfm.emi.spawn.time.night" : "tfm.emi.spawn.time.day";
            int dtColor = data.dayTime() == MobSpawnData.DayTime.NIGHT ? 0x3333AA : 0xFFAA00;
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.time").append(
                            Component.translatable(dtKey).withStyle(s -> s.withColor(dtColor))
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        // Temp
        if (data.minTemperature() != null || data.maxTemperature() != null) {
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.temp").append(
                            Component.literal(buildRangeStr(data.minTemperature(), data.maxTemperature(), "°C"))
                                    .withStyle(ChatFormatting.DARK_BLUE)
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        // Ground Water
        if (data.minGroundwater() != null || data.maxGroundwater() != null) {
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.humidity").append(
                            Component.literal(buildRangeStr(data.minGroundwater(), data.maxGroundwater(), "mm"))
                                    .withStyle(ChatFormatting.DARK_AQUA)
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        // Forest
        if (data.minForest() != null || data.maxForest() != null) {
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.forest").append(
                            Component.literal(buildRangeStr(data.minForest(), data.maxForest(), ""))
                                    .withStyle(ChatFormatting.DARK_GREEN)
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        // Height
        if (data.minHeight() != null || data.maxHeight() != null) {
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.height").append(
                            Component.literal(buildRangeStr(data.minHeight(), data.maxHeight(), "Y"))
                                    .withStyle(ChatFormatting.GRAY)
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        // Biome tag tooltip
        if (data.biomeTag() != null) {
            String labelKey = data.biomeTagLabel() != null
                    ? "tfm.emi.spawn.biome." + data.biomeTagLabel()
                    : data.biomeTag();
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.label.biome").append(
                            Component.translatable(labelKey).withStyle(ChatFormatting.DARK_GREEN)
                    ).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            List<Component> tooltip = getBiomeTooltip(data.biomeTag());
            if (!tooltip.isEmpty()) {
                widgets.addTooltipText(tooltip, PADDING, y, WIDTH - PADDING * 2, LINE_H);
            }
            y += LINE_H;
        }

        // Cave variant
        if (data.caveVariant()) {
            widgets.addText(
                    Component.translatable("tfm.emi.spawn.cave_variant")
                            .withStyle(ChatFormatting.DARK_PURPLE).getVisualOrderText(),
                    PADDING, y, 0x000000, false);
            y += LINE_H;
        }

        return y;
    }

    private int countInfoLines() {
        int count = 2; // saisons + spawnType toujours présents
        if (data.dayTime() != MobSpawnData.DayTime.ALL) count++;
        if (data.minTemperature() != null || data.maxTemperature() != null) count++;
        if (data.minGroundwater() != null || data.maxGroundwater() != null) count++;
        if (data.minForest() != null || data.maxForest() != null) count++;
        if (data.minHeight() != null || data.maxHeight() != null) count++;
        if (data.biomeTag() != null) count++;
        if (data.caveVariant()) count++;
        return count;
    }

    private List<Component> getBiomeTooltip(String tagStr) {
        var level = Minecraft.getInstance().level;
        if (level == null) return List.of();
        try {
            var biomeRegistry = level.registryAccess().registry(Registries.BIOME).orElse(null);
            if (biomeRegistry == null) return List.of();

            return biomeRegistry.getTag(TagKey.create(Registries.BIOME, ResourceLocation.parse(tagStr)))
                    .stream()
                    .flatMap(tag -> tag.stream())
                    .map(holder -> holder.unwrapKey()
                            .map(key -> (Component) Component.translatable(
                                    "biome." + key.location().getNamespace() + "."
                                            + key.location().getPath().replace("/", ".")))
                            .orElseGet(() -> Component.literal("?")))
                    .sorted(Comparator.comparing(Component::getString))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return List.of(Component.literal(tagStr).withStyle(ChatFormatting.DARK_GREEN));
        }
    }

    private String buildRangeStr(@Nullable Integer min, @Nullable Integer max, String unit) {
        if (min != null && max != null) return min + unit + " – " + max + unit;
        if (min != null) return "≥ " + min + unit;
        if (max != null) return "≤ " + max + unit;
        return "—";
    }

    private int getSeasonsColor(List<String> seasons) {
        if (seasons.contains("all") || seasons.size() == 4) return 0xAAAAAA;
        if (seasons.contains("summer")) return 0xFFCC00;
        if (seasons.contains("spring")) return 0x66CC44;
        if (seasons.contains("fall")) return 0xFF7722;
        return 0xAADDFF; // winter
    }

    @Nullable
    private static LivingEntity getOrCreateEntity(EntityType<?> type, ResourceLocation id) {
        return ENTITY_CACHE.computeIfAbsent(id, k -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return null;
            var entity = type.create(level);
            return entity instanceof LivingEntity living ? living : null;
        });
    }
}