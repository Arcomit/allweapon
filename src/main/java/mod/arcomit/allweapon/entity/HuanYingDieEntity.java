package mod.arcomit.allweapon.entity;

import mods.flammpfeil.slashblade.SlashBladeConfig;
import mods.flammpfeil.slashblade.util.AttackManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-21 16:24
 * @Description: TODO
 */
@SuppressWarnings("removal")
public class HuanYingDieEntity extends Projectile{

    public HuanYingDieEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    /**
     * 设置投射物的射击方向和速度
     * @param shooter 发射者
     * @param pitch 俯仰角
     * @param yaw 偏航角
     * @param velocity 速度
     * @param inaccuracy 不精确度
     */
    public void shoot(Entity shooter, float pitch, float yaw, float velocity, float inaccuracy) {
        float x = -Mth.sin(yaw * ((float)Math.PI / 180F)) * Mth.cos(pitch * ((float)Math.PI / 180F));
        float y = -Mth.sin(pitch * ((float)Math.PI / 180F));
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

    private static final EntityDataAccessor<String> MODEL =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
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
                    HuanYingDieEntity.class,
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
                    HuanYingDieEntity.class,
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
                    HuanYingDieEntity.class,
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
        if (value != null && attachedEntity != value) {
            this.entityData.set(ATTACHED_ENTITY_ID, value.getId());
            this.attachedEntity = value;
        }else {
            this.entityData.set(ATTACHED_ENTITY_ID, -1);
            this.attachedEntity = null;
        }
    }

    private static final EntityDataAccessor<Float> YAW_CORRECTION =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
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
                    HuanYingDieEntity.class,
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
                    HuanYingDieEntity.class,
                    EntityDataSerializers.FLOAT
            );
    public void setBasicDamage(float value) {
        this.entityData.set(BASIC_DAMAGE, value);
    }
    public float getBasicDamage() {
        return this.entityData.get(BASIC_DAMAGE);
    }

    private Entity trackingTarget = null;
    private static final EntityDataAccessor<Integer> TRACKING_TARGET_ID =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
                    EntityDataSerializers.INT
            );
    @Nullable
    public Entity getTrackingTarget() {
        if (trackingTarget == null) {
            int id = this.entityData.get(TRACKING_TARGET_ID);
            if (0 <= id) {
                trackingTarget = this.level().getEntity(id);
            }
        }
        return trackingTarget;
    }
    public void setTrackingTarget(@Nullable Entity value) {
        if (value != null) {
            this.entityData.set(TRACKING_TARGET_ID, value.getId());
            this.trackingTarget = value;
        } else {
            this.entityData.set(TRACKING_TARGET_ID, -1);
            this.trackingTarget = null;
        }
    }

    private static final EntityDataAccessor<Float> TRACKING_STRENGTH =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
                    EntityDataSerializers.FLOAT
            );
    public void setTrackingStrength(float value) {
        this.entityData.set(TRACKING_STRENGTH, Mth.clamp(value, 0.0F, 1.0F));
    }
    public float getTrackingStrength() {
        return this.entityData.get(TRACKING_STRENGTH);
    }

    // 自动追踪相关
    private static final EntityDataAccessor<Boolean> AUTO_TRACKING =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
                    EntityDataSerializers.BOOLEAN
            );
    public void setAutoTracking(boolean value) {
        this.entityData.set(AUTO_TRACKING, value);
    }
    public boolean isAutoTracking() {
        return this.entityData.get(AUTO_TRACKING);
    }

    private static final EntityDataAccessor<Float> AUTO_TRACKING_RANGE =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
                    EntityDataSerializers.FLOAT
            );
    public void setAutoTrackingRange(float value) {
        this.entityData.set(AUTO_TRACKING_RANGE, Math.max(0.0F, value));
    }
    public float getAutoTrackingRange() {
        return this.entityData.get(AUTO_TRACKING_RANGE);
    }

    private static final EntityDataAccessor<Integer> BURST_TIMER =
            SynchedEntityData.defineId(
                    HuanYingDieEntity.class,
                    EntityDataSerializers.INT
            );
    public void setBurstTimer(int value) {
        this.entityData.set(BURST_TIMER, value);
    }
    public int getBurstTimer() {
        return this.entityData.get(BURST_TIMER);
    }

    //默认属性
    @Override
    protected void defineSynchedData() {
        this.entityData.define(MODEL, "allweapon:model/util/huanyingdie.obj");
        this.entityData.define(TEXTURE, "slashblade:model/util/ss.png");
        this.entityData.define(COLOR, 0xFF69B4);
        this.entityData.define(ATTACHED_ENTITY_ID, -1);
        this.entityData.define(YAW_CORRECTION, 0.0F);
        this.entityData.define(ROLL, 0.0F);
        this.entityData.define(BASIC_DAMAGE, 1.0F);
        this.entityData.define(TRACKING_TARGET_ID, -1);
        this.entityData.define(TRACKING_STRENGTH, 0.1F);
        this.entityData.define(AUTO_TRACKING, true);
        this.entityData.define(AUTO_TRACKING_RANGE, 64.0F);
        this.entityData.define(BURST_TIMER, 20);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putString("Model", this.getModel().toString());
        compound.putString("Texture", this.getTexture().toString());
        compound.putInt("Color", this.getColor());
        compound.putFloat("Roll", this.getRoll());
        compound.putFloat("BasicDamage", this.getBasicDamage());
        compound.putFloat("TrackingStrength", this.getTrackingStrength());
        compound.putBoolean("AutoTracking", this.isAutoTracking());
        compound.putFloat("AutoTrackingRange", this.getAutoTrackingRange());
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
        if (compound.contains("TrackingStrength")) {
            this.setTrackingStrength(compound.getFloat("TrackingStrength"));
        }
        if (compound.contains("AutoTracking")) {
            this.setAutoTracking(compound.getBoolean("AutoTracking"));
        }
        if (compound.contains("AutoTrackingRange")) {
            this.setAutoTrackingRange(compound.getFloat("AutoTrackingRange"));
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            updateTrail();
        }

        Entity attachedEntity = this.getAttachedEntity();

        if (attachedEntity != null) {
            // 如果附着的实体死亡，跟随破裂
            if (!attachedEntity.isAlive() || attachedEntity.isRemoved()) {
                this.burst();
                return;
            }

            // 跟随附着实体移动
            double newX = attachedEntity.getX();
            double newY = attachedEntity.getY() + attachedEntity.getEyeHeight() * 0.5;
            double newZ = attachedEntity.getZ();
            this.setPos(newX, newY, newZ);

            //
            int explosionTimer = this.getBurstTimer();
            if (explosionTimer > 0) {
                setBurstTimer(explosionTimer - 1);
            } else {
                hitEntity(attachedEntity);
                this.burst();
            }
            return;
        }

        // 自动追踪逻辑--------------------------------------------
        this.autoFindTarget();

        // 追踪逻辑--------------------------------------------
        Entity trackingTarget = this.getTrackingTarget();
        if (trackingTarget != null && trackingTarget.isAlive() && !trackingTarget.isRemoved()) {
            float trackingStrength = this.getTrackingStrength();
            if (trackingStrength > 0.0F) {
                // 计算目标位置
                Vec3 targetPos = trackingTarget.position().add(0.0D, trackingTarget.getEyeHeight() * 0.5D, 0.0D);
                Vec3 currentPos = this.position();
                Vec3 directionToTarget = targetPos.subtract(currentPos).normalize();

                // 获取当前速度并归一化
                Vec3 currentVelocity = this.getDeltaMovement();
                double currentSpeed = currentVelocity.length();

                // 如果速度太小，使用最小速度
                if (currentSpeed < 0.1D) {
                    currentSpeed = 0.5D;
                }

                // 将当前方向与目标方向混合
                Vec3 currentDirection = currentVelocity.normalize();
                Vec3 newDirection = currentDirection.scale(1.0D - trackingStrength)
                        .add(directionToTarget.scale(trackingStrength))
                        .normalize();

                // 应用新的速度向量
                this.setDeltaMovement(newDirection.scale(currentSpeed));
            }
        } else if (trackingTarget != null) {
            // 如果目标已死亡或被移除，清除追踪目标
            this.setTrackingTarget(null);
        }

        Vec3 movement = this.getDeltaMovement();

        // 碰撞逻辑--------------------------------------------
        // 检测方块碰撞和实体碰撞
        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(movement);

        EntityHitResult entityHitResult = ProjectileUtil.getEntityHitResult(
                this.level(),
                this,
                startPos, endPos,
                this.getBoundingBox()
                        .expandTowards(this.getDeltaMovement())
                        .inflate(1.0D),
                this::canHitEntity
        );

        // 处理碰撞
        if (entityHitResult != null && entityHitResult.getType() != HitResult.Type.MISS && !this.level().isClientSide) {
            this.onHitEntity(entityHitResult);
        }

        // 移动逻辑--------------------------------------------
        this.move(MoverType.SELF, movement);

        this.setDeltaMovement(this.getDeltaMovement().scale(0.99D));

        if (!this.level().isClientSide && movement.horizontalDistanceSqr() > 1.0E-7D) {
            this.setYRot((float)(Mth.atan2(movement.x, movement.z) * (180D / Math.PI)));
            this.setXRot((float)(Mth.atan2(movement.y, movement.horizontalDistance()) * (180D / Math.PI)));
        }

    }

    private final List<Vec3> trailPositions = new ArrayList<>();
    private static final int MAX_TRAIL_POINTS = 20;
    private static final double MIN_MOVE_DISTANCE_SQ = 0.0025;
    private static final int TRAIL_FADE_TICKS = 2;
    private int ticksSinceLastMove = 0;

    public List<Vec3> getTrailPositions() {
        return trailPositions;
    }

    private void updateTrail() {
        Vec3 currentPos = this.position();

        // 检查是否移动了足够的距离
        boolean hasMoved = trailPositions.isEmpty() ||
                currentPos.distanceToSqr(trailPositions.get(0)) >= MIN_MOVE_DISTANCE_SQ;

        if (hasMoved) {
            trailPositions.add(0, currentPos);
            if (trailPositions.size() > MAX_TRAIL_POINTS) {
                trailPositions.remove(MAX_TRAIL_POINTS);
            }
            ticksSinceLastMove = 0;
        } else {
            ticksSinceLastMove++;
            // 静止时快速移除拖尾
            if (ticksSinceLastMove >= TRAIL_FADE_TICKS) {
                // 最多移除两个顶点
                int removeCount = Math.min(2, trailPositions.size());
                if (removeCount > 0) {
                    trailPositions.subList(trailPositions.size() - removeCount, trailPositions.size()).clear();
                }
            }
        }
    }

    /**
     * 自动寻找并追踪附近的目标
     * 优先追踪最近的敌对生物
     */
    private void autoFindTarget() {
        if (!isAutoTracking()) {
            return;
        }

        Entity currentTarget = this.getTrackingTarget();
        // 如果当前已有有效目标，不重新搜索
        if (currentTarget != null && currentTarget.isAlive() && !currentTarget.isRemoved()) {
            return;
        }

        Entity owner = this.getOwner();
        double searchRange = getAutoTrackingRange();
        AABB searchBox = this.getBoundingBox().inflate(searchRange);

        Entity nearestTarget = null;
        double nearestDistance = Double.MAX_VALUE;

        for (Entity entity : this.level().getEntities(this, searchBox)) {
            // 跳过不可攻击的实体
            if (!this.canHitEntity(entity)) {
                continue;
            }

            // 只追踪生物实体
            if (!(entity instanceof LivingEntity)) {
                continue;
            }

            // 如果有主人，优先追踪对主人有敌意的目标
            if (owner instanceof LivingEntity livingOwner) {
                LivingEntity livingEntity = (LivingEntity) entity;

                // 优先锁定攻击过主人和主人攻击过的生物
                if (livingEntity.getLastHurtByMob() == livingOwner ||
                        livingOwner.getLastHurtByMob() == livingEntity) {
                    nearestTarget = entity;
                    break;
                }
            }
            // 计算与实体的距离，寻找最近的目标
            double distance = this.distanceToSqr(entity);
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestTarget = entity;
            }
        }

        // 设置找到的目标
        if (nearestTarget != null) {
            setTrackingTarget(nearestTarget);
        }
    }

    //判断能否攻击实体
    @Override
    protected boolean canHitEntity(Entity entity) {
        if (entity.isSpectator()) return false;
        if (!entity.canBeHitByProjectile()) return false;
        Entity owner = this.getOwner();
        if (owner != null){
            //发射者，发射者坐骑上的其它乘客，发射者的坐骑，发射者的乘客都不能被攻击
            if (owner == entity || owner.isPassengerOfSameVehicle(entity) || owner.getVehicle() == entity || owner == entity.getRootVehicle()) {
                return false;
            }
        }

        return true;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity targetEntity = entityHitResult.getEntity();

        // 对实体造成伤害
        hitEntity(targetEntity);

        // 附着到目标实体
        this.setAttachedEntity(targetEntity);
        this.setYawCorrection(targetEntity.getYRot());

        // 立即传送到目标位置
        double attachX = targetEntity.getX();
        double attachY = targetEntity.getY() + targetEntity.getEyeHeight() * 0.5;
        double attachZ = targetEntity.getZ();
        this.setPos(attachX, attachY, attachZ);
    }

    public void hitEntity(Entity targetEntity) {
        Entity owner = this.getOwner();

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
        // 计算伤害
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
    }

    // 破碎消失
    public void burst(){
        this.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        if (!this.level().isClientSide()) {
            if (this.level() instanceof ServerLevel) {
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(),
                        16, 0.5, 0.5, 0.5, 0.25f);
            }
        }
        if (this.level().isClientSide) {
            trailPositions.clear(); // 清空拖尾
        }
        this.discard();
    }

    // 自动清理机制
    @Override
    public void checkDespawn() {
        super.checkDespawn();
        if (!this.level().isClientSide) {
            double instantDespawnDistance = 200.0D; // 200格外立即消失
            // 检查是否有玩家在附近
            if (!this.level().hasNearbyAlivePlayer(
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    instantDespawnDistance
            )) {
                this.discard();
            }
        }
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

