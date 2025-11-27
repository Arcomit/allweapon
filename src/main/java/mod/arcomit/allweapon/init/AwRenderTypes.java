package mod.arcomit.allweapon.init;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-25 14:51
 * @Description: TODO
 */
public class AwRenderTypes extends RenderStateShard {

    public AwRenderTypes(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    protected static final RenderStateShard.TransparencyStateShard LIGHTNING_ADDITIVE_TRANSPARENCY =
            new RenderStateShard.TransparencyStateShard(
                    "lightning_additive_transparency", () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
                        GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });

    private static final Map<ResourceLocation, RenderType> QUADS_BLEND_LUMINOUS_CACHE = new HashMap<>();

    public static RenderType getQuadsBlendLuminous(ResourceLocation texture) {
        return QUADS_BLEND_LUMINOUS_CACHE.computeIfAbsent(
                texture,
                t -> {
                    RenderType.CompositeState state = RenderType.CompositeState.builder()
                            .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER)
                            .setOutputState(ITEM_ENTITY_TARGET)
                            .setCullState(RenderStateShard.NO_CULL)
                            .setTextureState(new RenderStateShard.TextureStateShard(t, true, true))
                            .setTransparencyState(LIGHTNING_ADDITIVE_TRANSPARENCY)
                            .setLightmapState(RenderStateShard.LIGHTMAP)
                            .setOverlayState(OVERLAY)
                            .setWriteMaskState(COLOR_WRITE)
                            .createCompositeState(false);

                    return RenderType.create(
                            "quads_blend_luminous_" + t,
                            DefaultVertexFormat.NEW_ENTITY,
                            VertexFormat.Mode.QUADS,
                            256,
                            false,
                            true,
                            state);
                }
        );
    }
}
