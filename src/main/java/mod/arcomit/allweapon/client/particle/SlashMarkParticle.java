package mod.arcomit.allweapon.client.particle;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Getter;
import lombok.Setter;
import mod.arcomit.allweapon.AllWeapon;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-28 12:47
 * @Description: TODO
 */
public class SlashMarkParticle extends SingleQuadParticle {

    @Getter@Setter
    private ResourceLocation texture = AllWeapon.prefix("textures/particle/slash_mark.png");

    protected SlashMarkParticle(
            ClientLevel pLevel,
            double startX,
            double startY,
            double startZ,
            double endX,
            double endY,
            double endZ
            ) {
        super(pLevel, startX, startY, startZ);
        this.xd = endX;
        this.yd = endY;
        this.zd = endZ;
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
        texturemanager.getTexture(this.texture).setFilter(false, true);
        RenderSystem.setShaderTexture(0, this.texture);

    }

    @Override
    protected float getU0() {
        return 0;
    }

    @Override
    protected float getU1() {
        return 1;
    }

    @Override
    protected float getV0() {
        return 0;
    }

    @Override
    protected float getV1() {
        return 1;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return null;
    }
}
