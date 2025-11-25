package mod.arcomit.allweapon.entity;

import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 16:24
 * @Description: TODO
 */
@SuppressWarnings("removal")
public class HuanYingDieEntity2 extends Projectile{

    public HuanYingDieEntity2(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    private static final EntityDataAccessor<String> MODEL =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.STRING
            );
    public ResourceLocation getModel() {
        return new ResourceLocation(this.getEntityData().get(MODEL));
    }
    public void setModel(ResourceLocation value) {
        this.getEntityData().set(MODEL, value.toString());
    }

    private static final EntityDataAccessor<String> TEXTURE =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.STRING
            );
    public ResourceLocation getTexture() {
        return new ResourceLocation(this.getEntityData().get(TEXTURE));
    }
    public void setTexture(ResourceLocation value) {
        this.getEntityData().set(TEXTURE, value.toString());
    }

    private static final EntityDataAccessor<Integer> COLOR =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.INT
            );
    public int getColor() {
        return this.getEntityData().get(COLOR);
    }
    public void setColor(int value) {
        this.getEntityData().set(COLOR, value);
    }

    private Entity attachedEntity = null;
    private static final EntityDataAccessor<Integer> ATTACHED_ENTITY_ID =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.INT
            );
    @Nullable
    public Entity getAttachedEntity() {
        if (attachedEntity == null) {
            int id = this.entityData.get(ATTACHED_ENTITY_ID);
            if (0 <= id) {
                attachedEntity = this.level().getEntity(id);
            }
        }
        return attachedEntity;
    }
    public void setAttachedEntity(Entity value) {
        if (attachedEntity != this) {
            this.entityData.set(ATTACHED_ENTITY_ID, value.getId());
            this.attachedEntity = value;
        }
    }

    private static final EntityDataAccessor<Float> YAW_CORRECTION =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.FLOAT
            );
    public void setYawCorrection(float value) {
        this.entityData.set(YAW_CORRECTION, value);
    }
    public float getYawCorrection() {
        return this.entityData.get(YAW_CORRECTION);
    }

    private static final EntityDataAccessor<Float> ROLL =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.FLOAT
            );
    public void setRoll(float value) {
        this.entityData.set(ROLL, value);
    }
    public float getRoll() {
        return this.entityData.get(ROLL);
    }

    private static final EntityDataAccessor<Float> BASIC_DAMAGE =
            SynchedEntityData.defineId(
                    HuanYingDieEntity2.class,
                    EntityDataSerializers.FLOAT
            );
    public void setBasicDamage(float value) {
        this.entityData.set(BASIC_DAMAGE, value);
    }
    public float getBasicDamage() {
        return this.entityData.get(BASIC_DAMAGE);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MODEL, "allweapon:model/util/huanyingdie.obj");
        this.entityData.define(TEXTURE, "slashblade:model/util/ss.png");
        this.entityData.define(COLOR, 0x3333FF);
        this.entityData.define(ATTACHED_ENTITY_ID, -1);
        this.entityData.define(YAW_CORRECTION, 0.0F);
        this.entityData.define(ROLL, 0.0F);
        this.entityData.define(BASIC_DAMAGE, 1.0F);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putString("Model", this.getModel().toString());
        compound.putString("Texture", this.getTexture().toString());
        compound.putInt("Color", this.getColor());
        compound.putFloat("Roll", this.getRoll());
        compound.putFloat("BasicDamage", this.getBasicDamage());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("Model")) {
            this.setModel(new ResourceLocation(compound.getString("Model")));
        }
        if (compound.contains("Texture")) {
            this.setTexture(new ResourceLocation(compound.getString("Texture")));
        }
        if (compound.contains("Color")) {
            this.setColor(compound.getInt("Color"));
        }
        if (compound.contains("Roll")) {
            this.setRoll(compound.getFloat("Roll"));
        }
        if (compound.contains("BasicDamage")) {
            this.setBasicDamage(compound.getFloat("BasicDamage"));
        }
    }

    /**
     * 设置投射物的射击方向和速度
     * @param shooter 发射者
     * @param pitch 俯仰角
     * @param yaw 偏航角
     * @param pitchOffset 俯仰角偏移
     * @param velocity 速度
     * @param inaccuracy 不精确度
     */
    public void shoot(Entity shooter, float pitch, float yaw, float pitchOffset, float velocity, float inaccuracy) {
        float x = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        float y = -Mth.sin((pitch + pitchOffset) * ((float)Math.PI / 180F));
        float z = Mth.cos(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        this.shoot(x, y, z, velocity, inaccuracy);
        this.setDeltaMovement(this.getDeltaMovement().add(shooter.getDeltaMovement().x, shooter.onGround() ? 0.0D : shooter.getDeltaMovement().y, shooter.getDeltaMovement().z));
    }

    /**
     * 设置投射物的速度向量
     * @param x X方向分量
     * @param y Y方向分量
     * @param z Z方向分量
     * @param velocity 速度大小
     * @param inaccuracy 不精确度
     */
    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vec3 = (new Vec3(x, y, z))
                .normalize()
                .add(this.random.triangle(0.0D, 0.0172275D * (double)inaccuracy),
                        this.random.triangle(0.0D, 0.0172275D * (double)inaccuracy),
                        this.random.triangle(0.0D, 0.0172275D * (double)inaccuracy))
                .scale(velocity);
        this.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    public void tick() {
        super.tick();

        Entity attachedEntity = getAttachedEntity();
        // 如果已经附着实体，则跟随实体移动，且随实体死亡而消失
        if (attachedEntity != null) {
            if (!attachedEntity.isAlive() || attachedEntity.isRemoved()) {
                this.discard();
                return;
            }

            double newX = attachedEntity.getX();
            double newY = attachedEntity.getY() + attachedEntity.getEyeHeight() * 0.5;
            double newZ = attachedEntity.getZ();
            this.setPos(newX, newY, newZ);
            return;
        }

        // 施加重力加速度
        if (!this.level().isClientSide) {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }
        }

        Vec3 movement = this.getDeltaMovement();

        // 碰撞逻辑--------------------------------------------
        // 检测方块碰撞和实体碰撞
        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(movement);
        HitResult hitResult = this.level().clip(new ClipContext(
                startPos,
                endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));

        if (hitResult.getType() != HitResult.Type.MISS) {
            endPos = hitResult.getLocation();
        }

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                startPos, endPos,
                this.getBoundingBox()
                        .expandTowards(this.getDeltaMovement())
                        .inflate(1.0D),
                this::canHitEntity
        );
        if (entityHitResult != null) {
            hitResult = entityHitResult;
        }

        // 处理碰撞
        if (hitResult.getType() != HitResult.Type.MISS && !this.level().isClientSide) {
            this.onHit(hitResult);
        }

        // 移动逻辑--------------------------------------------
        this.move(MoverType.SELF, movement);

        this.setDeltaMovement(this.getDeltaMovement().scale(0.99D));

        if (!this.level().isClientSide && movement.horizontalDistanceSqr() > 1.0E-7D) {
            this.setYRot((float)(Mth.atan2(movement.x, movement.z) * (180D / Math.PI)));
            this.setXRot((float)(Mth.atan2(movement.y, movement.horizontalDistance()) * (180D / Math.PI)));
        }

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity.isSpectator()) return false;
        if (!entity.canBeHitByProjectile()) return false;
        Entity owner = this.getOwner();
        if (owner != null){
            if (owner == entity || owner.isPassengerOfSameVehicle(entity) || owner.getVehicle() == entity || owner == entity.getRootVehicle()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        HitResult.Type type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            this.onHitEntity((EntityHitResult) hitResult);
        } else if (type == HitResult.Type.BLOCK) {
            this.onHitBlock((BlockHitResult) hitResult);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();
        Entity owner = this.getOwner();

        // 创建伤害源
        DamageSource damageSource;
        if (owner == null) {
            damageSource = this.damageSources().indirectMagic(this, this);
        } else {
            damageSource = this.damageSources().indirectMagic(this, owner);
            if (owner instanceof LivingEntity livingOwner) {
                livingOwner.setLastHurtMob(targetEntity);
            }
        }

        // 重置无敌时间以允许连续伤害
        targetEntity.invulnerableTime = 0;

        // 造成伤害
        float damage = this.getBasicDamage();
        if (owner instanceof LivingEntity livingOwner) {
            damage *= (float) (AttackManager.getSlashBladeDamageScale(livingOwner) * SlashBladeConfig.SLASHBLADE_DAMAGE_MULTIPLIER.get());
        }

        if (targetEntity.hurt(damageSource, damage)) {
            // 如果是生物实体，添加击退效果
            if (targetEntity instanceof LivingEntity livingTarget) {
                Vec3 knockback = this.getDeltaMovement().normalize().scale(0.6D);
                livingTarget.knockback(
                        0.4D,
                        -knockback.x,
                        -knockback.z
                );
            }
        }

        // 附着到目标实体
        this.setAttachedEntity(targetEntity);
        this.setYawCorrection(targetEntity.getYRot());

        // 立即传送到目标位置
        double attachX = targetEntity.getX();
        double attachY = targetEntity.getY() + targetEntity.getEyeHeight() * 0.5;
        double attachZ = targetEntity.getZ();
        this.setPos(attachX, attachY, attachZ);
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        //this.discard();
    }

    @Override
    public void setYRot(float pYRot) {
        if (this.getAttachedEntity() != null){
            return;
        }
        super.setYRot(pYRot);
    }

    @Override
    public void setXRot(float pXRot) {
        if (this.getAttachedEntity() != null){
            return;
        }
        super.setXRot(pXRot);
    }
}
