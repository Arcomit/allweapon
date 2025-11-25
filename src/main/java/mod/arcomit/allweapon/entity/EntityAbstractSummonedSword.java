//package mod.arcomit.allweapon.entity;
//
//import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
//import mods.flammpfeil.slashblade.SlashBlade;
//import mods.flammpfeil.slashblade.ability.StunManager;
//import mods.flammpfeil.slashblade.entity.IShootable;
//import mods.flammpfeil.slashblade.entity.Projectile;
//import mods.flammpfeil.slashblade.event.SlashBladeEvent;
//import mods.flammpfeil.slashblade.util.AttackManager;
//import mods.flammpfeil.slashblade.util.EnumSetConverter;
//import mods.flammpfeil.slashblade.util.NBTHelper;
//import mods.flammpfeil.slashblade.util.TargetSelector;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtUtils;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.monster.EnderMan;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.projectile.ProjectileUtil;
//import net.minecraft.world.item.alchemy.PotionUtils;
//import net.minecraft.world.item.enchantment.EnchantmentHelper;
//import net.minecraft.world.level.ClipContext;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.*;
//import net.minecraft.world.phys.shapes.VoxelShape;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.entity.PartEntity;
//import net.minecraftforge.network.NetworkHooks;
//import net.minecraftforge.network.PlayMessages;
//import org.jetbrains.annotations.NotNull;
//
//import javax.annotation.Nullable;
//import java.util.EnumSet;
//import java.util.List;
//import java.util.Optional;
//
//import static mods.flammpfeil.slashblade.SlashBladeConfig.SLASHBLADE_DAMAGE_MULTIPLIER;
//
///**
// * 抽象召唤剑实体类
// * 继承自Projectile（抛射物）并实现IShootable接口
// */
//public class EntityAbstractSummonedSword extends Projectile implements IShootable {
//    // ========== 实体数据访问器 ==========
//    /** 颜色数据访问器 */
//    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.INT);
//    /** 标志位数据访问器 */
//    private static final EntityDataAccessor<Integer> FLAGS = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.INT);
//    /** 命中实体ID数据访问器 */
//    private static final EntityDataAccessor<Integer> HIT_ENTITY_ID = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.INT);
//    /** 偏移偏航角数据访问器 */
//    private static final EntityDataAccessor<Float> OFFSET_YAW = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.FLOAT);
//    /** 翻滚角度数据访问器 */
//    private static final EntityDataAccessor<Float> ROLL = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.FLOAT);
//    /** 穿透等级数据访问器 */
//    private static final EntityDataAccessor<Byte> PIERCE = SynchedEntityData.defineId(EntityAbstractSummonedSword.class,
//            EntityDataSerializers.BYTE);
//    /** 模型名称数据访问器 */
//    private static final EntityDataAccessor<String> MODEL = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.STRING);
//    /** 延迟数据访问器 */
//    private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData
//            .defineId(EntityAbstractSummonedSword.class, EntityDataSerializers.INT);
//
//    // ========== 实例变量 ==========
//    /** 在地面的刻数 */
//    private int ticksInGround;
//    /** 是否在地面 */
//    private boolean inGround;
//    /** 所在方块状态 */
//    private BlockState inBlockState;
//    /** 在空中的刻数 */
//    private int ticksInAir;
//    /** 伤害值 */
//    private double damage = 1.0D;
//
//    /** 已命中的实体ID集合（用于穿透） */
//    private IntOpenHashSet alreadyHits;
//
//    /** 命中的实体 */
//    private Entity hitEntity = null;
//
//    /** 在地面上的存活时间（5秒 = 100刻） */
//    /** 在地面上的存活时间（5秒 = 100刻） */
//    static final int ON_GROUND_LIFE_TIME = 20 * 5;
//
//    // ========== 音效 ==========
//    /** 命中实体的音效 */
//    private final SoundEvent hitEntitySound = SoundEvents.TRIDENT_HIT;
//    /** 命中玩家的音效 */
//    private final SoundEvent hitEntityPlayerSound = SoundEvents.TRIDENT_HIT;
//    /** 命中地面的音效 */
//    private final SoundEvent hitGroundSound = SoundEvents.TRIDENT_HIT_GROUND;
//
//    /** 获取命中实体的音效 */
//    protected SoundEvent getHitEntitySound() {
//        return this.hitEntitySound;
//    }
//
//    /** 获取命中玩家的音效 */
//    protected SoundEvent getHitEntityPlayerSound() {
//        return this.hitEntityPlayerSound;
//    }
//
//    /** 获取命中地面的音效 */
//    protected SoundEvent getHitGroundSound() {
//        return this.hitGroundSound;
//    }
//
//    /**
//     * 构造函数
//     * @param entityTypeIn 实体类型
//     * @param worldIn 世界对象
//     */
//    public EntityAbstractSummonedSword(EntityType<? extends Projectile> entityTypeIn, Level worldIn) {
//        super(entityTypeIn, worldIn);
//        this.setNoGravity(true); // 设置无重力
//        // this.setGlowing(true); // 设置发光（已注释）
//    }
//
//    /**
//     * 创建实例（用于网络同步）
//     * @param packet 生成实体数据包
//     * @param worldIn 世界对象
//     * @return 召唤剑实体实例
//     */
//    public static EntityAbstractSummonedSword createInstance(PlayMessages.SpawnEntity packet, Level worldIn) {
//        return new EntityAbstractSummonedSword(SlashBlade.RegistryEvents.SummonedSword, worldIn);
//    }
//
//    /**
//     * 定义同步数据
//     * 初始化所有需要在客户端和服务端之间同步的实体数据
//     */
//    @Override
//    protected void defineSynchedData() {
//        super.defineSynchedData();
//        this.entityData.define(COLOR, 0x3333FF); // 默认颜色（蓝色）
//        this.entityData.define(FLAGS, 0); // 标志位
//        this.entityData.define(HIT_ENTITY_ID, -1); // 命中实体ID（-1表示未命中）
//        this.entityData.define(OFFSET_YAW, 0f); // 偏移偏航角
//        this.entityData.define(ROLL, 0f); // 翻滚角度
//        this.entityData.define(PIERCE, (byte) 0); // 穿透等级
//        this.entityData.define(MODEL, ""); // 模型名称
//        this.entityData.define(DELAY, 10); // 延迟时间
//    }
//
//    /**
//     * 保存额外数据到NBT
//     * @param compound NBT标签
//     */
//    @Override
//    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
//        super.addAdditionalSaveData(compound);
//
//        NBTHelper.getNBTCoupler(compound).put("Color", this.getColor()).put("life", (short) this.ticksInGround)
//                .put("inBlockState", (this.inBlockState != null ? NbtUtils.writeBlockState(this.inBlockState) : null))
//                .put("inGround", this.inGround).put("damage", this.damage).put("crit", this.getIsCritical())
//                .put("clip", this.isNoClip()).put("PierceLevel", this.getPierce()).put("model", this.getModelName())
//                .put("Delay", this.getDelay());
//    }
//
//    /**
//     * 从NBT读取额外数据
//     * @param compound NBT标签
//     */
//    @Override
//    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
//        super.readAdditionalSaveData(compound);
//
//        NBTHelper.getNBTCoupler(compound).get("Color", this::setColor)
//                .get("life", ((Integer v) -> this.ticksInGround = v))
//                .get("inBlockState",
//                        ((CompoundTag v) -> this.inBlockState = NbtUtils
//                                .readBlockState(this.level().holderLookup(Registries.BLOCK), v)))
//                .get("inGround", ((Boolean v) -> this.inGround = v))
//                .get("damage", ((Double v) -> this.damage = v), this.damage).get("crit", this::setIsCritical)
//                .get("clip", this::setNoClip).get("PierceLevel", this::setPierce).get("model", this::setModelName)
//                .get("Delay", this::setDelay);
//    }
//
//    /**
//     * 获取实体生成数据包
//     * @return 客户端游戏数据包
//     */
//    @Override
//    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
//        return NetworkHooks.getEntitySpawningPacket(this);
//    }
//
//    /**
//     * 射击方法
//     * @param x X方向分量
//     * @param y Y方向分量
//     * @param z Z方向分量
//     * @param velocity 速度
//     * @param inaccuracy 不准确度
//     */
//    @Override
//    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
//        // 标准化方向向量，添加随机偏移（基于不准确度），然后缩放到指定速度
//        Vec3 vec3d = (new Vec3(x, y, z)).normalize()
//                .add(this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
//                        this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy,
//                        this.random.nextGaussian() * (double) 0.0075F * (double) inaccuracy)
//                .scale(velocity);
//        this.setDeltaMovement(vec3d);
//        float f = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
//        this.setPos(this.position());
//        // 根据运动方向设置偏航角和俯仰角
//        this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * (double) (180F / (float) Math.PI)));
//        this.setXRot((float) (Mth.atan2(vec3d.y, f) * (double) (180F / (float) Math.PI)));
//        this.yRotO = this.getYRot();
//        this.xRotO = this.getXRot();
//        this.ticksInGround = 0;
//    }
//
//    /**
//     * 判断是否应该在指定距离渲染
//     * @param distance 距离的平方
//     * @return 是否渲染
//     */
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public boolean shouldRenderAtSqrDistance(double distance) {
//        double d0 = this.getBoundingBox().getSize() * 10.0D;
//        if (Double.isNaN(d0)) {
//            d0 = 1.0D;
//        }
//
//        d0 = d0 * 64.0D * getViewScale();
//        return distance < d0 * d0;
//    }
//
//    /**
//     * 插值移动到指定位置和旋转（客户端）
//     * @param x X坐标
//     * @param y Y坐标
//     * @param z Z坐标
//     * @param yaw 偏航角
//     * @param pitch 俯仰角
//     * @param posRotationIncrements 位置旋转增量
//     * @param teleport 是否传送
//     */
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements,
//                       boolean teleport) {
//        this.setPos(x, y, z);
//        this.setRot(yaw, pitch);
//    }
//
//    /**
//     * 插值运动（客户端）
//     * @param x X方向运动
//     * @param y Y方向运动
//     * @param z Z方向运动
//     */
//    @Override
//    @OnlyIn(Dist.CLIENT)
//    public void lerpMotion(double x, double y, double z) {
//        this.setDeltaMovement(x, y, z);
//        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
//            float f = Mth.sqrt((float) (x * x + z * z));
//            this.setXRot((float) (Mth.atan2(y, f) * (double) (180F / (float) Math.PI)));
//            this.setYRot((float) (Mth.atan2(x, z) * (double) (180F / (float) Math.PI)));
//            this.xRotO = this.getXRot();
//            this.yRotO = this.getYRot();
//            this.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
//            this.ticksInGround = 0;
//        }
//
//    }
//
//    /**
//     * 标志状态枚举
//     * Critical: 暴击状态
//     * NoClip: 无碰撞状态（穿透方块）
//     */
//    public enum FlagsState {
//        Critical, NoClip,
//    }
//
//    /** 标志位集合 */
//    protected EnumSet<FlagsState> flags = EnumSet.noneOf(FlagsState.class);
//    /** 整数形式的标志位 */
//    protected int intFlags = 0;
//
//    /**
//     * 设置标志位
//     * @param value 要设置的标志状态
//     */
//    protected void setFlags(FlagsState value) {
//        this.flags.add(value);
//        refreshFlags();
//    }
//
//    /**
//     * 移除标志位
//     * @param value 要移除的标志状态
//     */
//    protected void removeFlags(FlagsState value) {
//        this.flags.remove(value);
//        refreshFlags();
//    }
//
//    /**
//     * 刷新标志位
//     * 在客户端和服务端之间同步标志位数据
//     */
//    private void refreshFlags() {
//        if (this.level().isClientSide()) {
//            // 客户端：从实体数据读取标志位
//            int newValue = this.entityData.get(FLAGS);
//            if (intFlags != newValue) {
//                intFlags = newValue;
//                flags = EnumSetConverter.convertToEnumSet(FlagsState.class, intFlags);
//            }
//        } else {
//            // 服务端：将标志位写入实体数据
//            int newValue = EnumSetConverter.convertToInt(this.flags);
//            if (this.intFlags != newValue) {
//                this.entityData.set(FLAGS, newValue);
//                this.intFlags = newValue;
//            }
//        }
//    }
//
//    /**
//     * 设置是否暴击
//     * @param value 是否暴击
//     */
//    public void setIsCritical(boolean value) {
//        if (value) {
//            setFlags(FlagsState.Critical);
//        } else {
//            removeFlags(FlagsState.Critical);
//        }
//    }
//
//    /**
//     * 获取是否暴击
//     * @return 是否暴击
//     */
//    public boolean getIsCritical() {
//        refreshFlags();
//        return flags.contains(FlagsState.Critical);
//    }
//
//    /**
//     * 设置是否无碰撞（穿透方块）
//     * @param value 是否无碰撞
//     */
//    public void setNoClip(boolean value) {
//        this.noPhysics = value;
//        if (value) {
//            setFlags(FlagsState.NoClip);
//        } else {
//            removeFlags(FlagsState.NoClip);
//        }
//    }
//
//    /**
//     * 获取是否无碰撞（禁止命中方块）
//     * @return 是否无碰撞
//     */
//    public boolean isNoClip() {
//        if (!this.level().isClientSide()) {
//            return this.noPhysics;
//        } else {
//            refreshFlags();
//            return flags.contains(FlagsState.NoClip);
//        }
//    }
//
//    /**
//     * 每刻更新方法
//     */
//    @Override
//    public void tick() {
//        super.tick();
//
//        // 如果已经命中实体
//        if (getHitEntity() != null) {
//            Entity hits = getHitEntity();
//
//            // 如果命中的实体死亡，爆炸消失
//            if (!hits.isAlive()) {
//                this.burst();
//            } else {
//                // 跟随命中的实体移动（位于其眼睛高度的一半位置）
//                this.setPos(hits.getX(), hits.getY() + hits.getEyeHeight() * 0.5f, hits.getZ());
//
//                // 延迟倒计时
//                int delay = getDelay();
//                delay--;
//                setDelay(delay);
//
//                // 延迟结束后爆炸
//                if (!this.level().isClientSide() && delay < 0) {
//                    this.burst();
//                }
//            }
//
//            return;
//        }
//
//        // 是否禁止命中方块
//        boolean disallowedHitBlock = this.isNoClip();
//
//        // 检查是否在地面
//        BlockPos blockpos = this.getOnPos();
//        BlockState blockstate = this.level().getBlockState(blockpos);
//        if (!blockstate.isAir() && !disallowedHitBlock) {
//            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
//            if (!voxelshape.isEmpty()) {
//                for (AABB axisalignedbb : voxelshape.toAabbs()) {
//                    if (axisalignedbb.move(blockpos).contains(new Vec3(this.getX(), this.getY(), this.getZ()))) {
//                        this.inGround = true;
//                        break;
//                    }
//                }
//            }
//        }
//
//        // 如果在水中或雨中，熄灭火焰
//        if (this.isInWaterOrRain()) {
//            this.clearFire();
//        }
//
//        // 如果在地面
//        if (this.inGround && !disallowedHitBlock) {
//            if (this.inBlockState != blockstate && this.level().noCollision(this.getBoundingBox().inflate(0.06D))) {
//                // 方块被破坏，爆炸消失
//                this.burst();
//            } else if (!this.level().isClientSide()) {
//                // 在方块上，尝试消失
//                this.tryDespawn();
//            }
//        } else {
//            // 处理姿态
//            Vec3 motionVec = this.getDeltaMovement();
//            if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
//                float f = Mth.sqrt((float) motionVec.horizontalDistanceSqr());
//                this.setYRot((float) (Mth.atan2(motionVec.x, motionVec.z) * (double) (180F / (float) Math.PI)));
//                this.setXRot((float) (Mth.atan2(motionVec.y, f) * (double) (180F / (float) Math.PI)));
//                this.yRotO = this.getYRot();
//                this.xRotO = this.getXRot();
//            }
//
//            // 处理在空中的情况
//            ++this.ticksInAir;
//            Vec3 positionVec = this.position();
//            Vec3 movedVec = positionVec.add(motionVec);
//            // 方块射线追踪
//            HitResult raytraceresult = this.level().clip(
//                    new ClipContext(positionVec, movedVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
//            if (raytraceresult.getType() != HitResult.Type.MISS) {
//                movedVec = raytraceresult.getLocation();
//            }
//
//            // 循环处理穿透
//            while (this.isAlive()) {
//                // 实体射线追踪
//                EntityHitResult entityraytraceresult = this.getRayTrace(positionVec, movedVec);
//                if (entityraytraceresult != null) {
//                    raytraceresult = entityraytraceresult;
//                }
//
//                // 如果命中实体，检查是否为有效目标
//                if (raytraceresult != null && raytraceresult.getType() == HitResult.Type.ENTITY) {
//                    Entity entity = null;
//                    if (raytraceresult instanceof EntityHitResult) {
//                        entity = ((EntityHitResult) raytraceresult).getEntity();
//                    }
//                    Entity entity1 = this.getShooter();
//                    // 检查目标是否可攻击（不攻击友方）
//                    if (entity instanceof LivingEntity && entity1 instanceof LivingEntity) {
//                        if (!TargetSelector.test.test((LivingEntity) entity1, (LivingEntity) entity)) {
//                            raytraceresult = null;
//                            entityraytraceresult = null;
//                        }
//                    }
//                }
//
//                // 处理命中
//                if (raytraceresult != null && !(disallowedHitBlock && raytraceresult.getType() == HitResult.Type.BLOCK)
//                        && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
//                    this.onHit(raytraceresult);
//                    this.hasImpulse = true;
//                }
//
//                // 如果没有穿透能力或没有命中实体，退出循环
//                if (entityraytraceresult == null || this.getPierce() <= 0) {
//                    break;
//                }
//
//                raytraceresult = null;
//            }
//
//            motionVec = this.getDeltaMovement();
//            double mx = motionVec.x;
//            double my = motionVec.y;
//            double mz = motionVec.z;
//            // 如果是暴击，生成暴击粒子效果
//            if (this.getIsCritical()) {
//                for (int i = 0; i < 4; ++i) {
//                    this.level().addParticle(ParticleTypes.CRIT, this.getX() + mx * (double) i / 4.0D,
//                            this.getY() + my * (double) i / 4.0D, this.getZ() + mz * (double) i / 4.0D, -mx, -my + 0.2D,
//                            -mz);
//                }
//            }
//
//            // 更新位置
//            this.setPos(this.getX() + mx, this.getY() + my, this.getZ() + mz);
//            float f4 = Mth.sqrt((float) motionVec.horizontalDistanceSqr());
//            // 根据穿透模式设置旋转角度
//            if (disallowedHitBlock) {
//                this.setYRot((float) (Mth.atan2(-mx, -mz) * (double) (180F / (float) Math.PI)));
//            } else {
//                this.setYRot((float) (Mth.atan2(mx, mz) * (double) (180F / (float) Math.PI)));
//            }
//
//            // 标准化旋转角度（保持在-180到180度之间）
//            for (this.setXRot((float) (Mth.atan2(my, f4) * (double) (180F / (float) Math.PI))); this.getXRot()
//                    - this.xRotO < -180.0F; this.xRotO -= 360.0F) {
//            }
//
//            while (this.getXRot() - this.xRotO >= 180.0F) {
//                this.xRotO += 360.0F;
//            }
//
//            while (this.getYRot() - this.yRotO < -180.0F) {
//                this.yRotO -= 360.0F;
//            }
//
//            while (this.getYRot() - this.yRotO >= 180.0F) {
//                this.yRotO += 360.0F;
//            }
//
//            // 平滑旋转过渡
//            this.setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));
//            this.setYRot(Mth.lerp(0.2F, this.yRotO, this.getYRot()));
//            float f1 = 0.99F; // 空气阻力系数
//            // 如果在水中，生成气泡粒子
//            if (this.isInWater()) {
//                for (int j = 0; j < 4; ++j) {
//                    this.level().addParticle(ParticleTypes.BUBBLE, this.getX() - mx * 0.25D, this.getY() - my * 0.25D,
//                            this.getZ() - mz * 0.25D, mx, my, mz);
//                }
//            }
//
//            // 应用空气阻力
//            this.setDeltaMovement(motionVec.scale(f1));
//            // 应用重力（如果有）
//            if (!this.isNoGravity() && !disallowedHitBlock) {
//                Vec3 vec3d3 = this.getDeltaMovement();
//                this.setDeltaMovement(vec3d3.x, vec3d3.y - (double) 0.05F, vec3d3.z);
//            }
//
//            // 检查是否在方块内部
//            this.checkInsideBlocks();
//        }
//
//        // 超时自动移除（100刻后）
//        if (!this.level().isClientSide() && ticksInGround <= 0 && 100 < this.tickCount) {
//            this.remove(RemovalReason.DISCARDED);
//        }
//
//    }
//
//    /**
//     * 尝试消失
//     * 在地面停留超过ON_GROUND_LIFE_TIME后爆炸消失
//     */
//    protected void tryDespawn() {
//        ++this.ticksInGround;
//        if (ON_GROUND_LIFE_TIME <= this.ticksInGround) {
//            this.burst();
//        }
//
//    }
//
//    /**
//     * 命中处理（路由到具体的命中类型）
//     * @param raytraceResultIn 射线追踪结果
//     */
//    @Override
//    protected void onHit(HitResult raytraceResultIn) {
//        HitResult.Type type = raytraceResultIn.getType();
//        switch (type) {
//            case ENTITY:
//                this.onHitEntity((EntityHitResult) raytraceResultIn);
//                break;
//            case BLOCK:
//                this.onHitBlock((BlockHitResult) raytraceResultIn);
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 命中方块处理
//     * @param blockraytraceresult 方块射线追踪结果
//     */
//    @Override
//    protected void onHitBlock(BlockHitResult blockraytraceresult) {
//        BlockState blockstate = this.level().getBlockState(blockraytraceresult.getBlockPos());
//        this.inBlockState = blockstate;
//        Vec3 vec3d = blockraytraceresult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
//        this.setDeltaMovement(vec3d);
//        Vec3 vec3d1 = this.position().subtract(vec3d.normalize().scale(0.05F));
//        this.setPos(vec3d1.x, vec3d1.y, vec3d1.z);
//        this.playSound(this.getHitGroundSound(), 1.0F, 2.2F / (this.random.nextFloat() * 0.2F + 0.9F));
//        this.inGround = true;
//        this.setIsCritical(false);
//        this.setPierce((byte) 0);
//        this.resetAlreadyHits();
//        blockstate.onProjectileHit(this.level(), blockstate, blockraytraceresult, this);
//    }
//
//    /**
//     * 强制命中实体
//     * @param target 目标实体
//     */
//    public void doForceHitEntity(Entity target) {
//        onHitEntity(new EntityHitResult(target));
//    }
//
//    /**
//     * 命中实体处理
//     * @param entityHitResult 实体射线追踪结果
//     */
//    @Override
//    protected void onHitEntity(EntityHitResult entityHitResult) {
//        Entity targetEntity = entityHitResult.getEntity();
//
//        // 发布召唤剑命中实体事件
//        SlashBladeEvent.SummonedSwordOnHitEntityEvent event = new SlashBladeEvent.SummonedSwordOnHitEntityEvent(this, targetEntity);
//        MinecraftForge.EVENT_BUS.post(event);
//
//        // 计算基础伤害
//        int i = Mth.ceil(this.getDamage());
//        // 处理穿透逻辑
//        if (this.getPierce() > 0) {
//            if (this.alreadyHits == null) {
//                this.alreadyHits = new IntOpenHashSet(5);
//            }
//
//            // 如果穿透次数用完，爆炸消失
//            if (this.alreadyHits.size() >= this.getPierce() + 1) {
//                this.burst();
//                return;
//            }
//
//            this.alreadyHits.add(targetEntity.getId());
//        }
//
//        // 如果是暴击，增加额外伤害
//        if (this.getIsCritical()) {
//            i += this.random.nextInt(i / 2 + 2);
//        }
//
//        // 创建伤害源
//        Entity shooter = this.getShooter();
//        DamageSource damagesource;
//        if (shooter == null) {
//            damagesource = this.damageSources().indirectMagic(this, this);
//        } else {
//            damagesource = this.damageSources().indirectMagic(this, shooter);
//            if (shooter instanceof LivingEntity) {
//                Entity hits = targetEntity;
//                if (targetEntity instanceof PartEntity) {
//                    hits = ((PartEntity<?>) targetEntity).getParent();
//                }
//                ((LivingEntity) shooter).setLastHurtMob(hits);
//            }
//        }
//
//        // 处理火焰效果
//        int fireTime = targetEntity.getRemainingFireTicks();
//        if (this.isOnFire() && !(targetEntity instanceof EnderMan)) {
//            targetEntity.setSecondsOnFire(5);
//        }
//
//        // 重置无敌时间以允许连续伤害
//        targetEntity.invulnerableTime = 0;
//        // 计算伤害倍率
//        float scale = 1f;
//        if (shooter instanceof LivingEntity living) {
//            scale = (float) (AttackManager.getSlashBladeDamageScale(living) * SLASHBLADE_DAMAGE_MULTIPLIER.get());
//        }
//        float damageValue = i * scale;
//        // 尝试对目标造成伤害
//        if (targetEntity.hurt(damagesource, damageValue)) {
//            Entity hits = targetEntity;
//            if (targetEntity instanceof PartEntity) {
//                hits = ((PartEntity<?>) targetEntity).getParent();
//            }
//
//            // 如果命中的是生物实体
//            if (hits instanceof LivingEntity targetLivingEntity) {
//
//                // 施加眩晕效果
//                StunManager.setStun(targetLivingEntity);
//
//                // 如果没有穿透，将剑附着到目标身上
//                if (!this.level().isClientSide() && this.getPierce() <= 0) {
//                    setHitEntity(hits);
//                }
//
//                // 应用附魔效果
//                if (!this.level().isClientSide() && shooter instanceof LivingEntity) {
//                    EnchantmentHelper.doPostHurtEffects(targetLivingEntity, shooter);
//                    EnchantmentHelper.doPostDamageEffects((LivingEntity) shooter, targetLivingEntity);
//                }
//
//                // 应用药水效果
//                affectEntity(targetLivingEntity, getPotionEffects(), 1.0f);
//
//                // 如果命中玩家，通知射手
//                if (targetLivingEntity != shooter && targetLivingEntity instanceof Player
//                        && shooter instanceof ServerPlayer) {
//                    ((ServerPlayer) shooter).playNotifySound(this.getHitEntityPlayerSound(), SoundSource.PLAYERS, 0.18F,
//                            0.45F);
//                }
//            }
//
//            // 播放命中音效
//            this.playSound(this.getHitEntitySound(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
//            // 如果没有穿透且没有附着实体，则爆炸消失
//            if (this.getPierce() <= 0 && (getHitEntity() == null || !getHitEntity().isAlive())) {
//                this.burst();
//            }
//        } else {
//            // 伤害未成功，恢复目标火焰状态
//            targetEntity.setRemainingFireTicks(fireTime);
//            this.ticksInAir = 0;
//            // 如果速度很低，消失或减少穿透等级
//            if (!this.level().isClientSide() && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
//                if (getPierce() <= 1) {
//                    this.burst();
//                } else {
//                    this.setPierce((byte) (getPierce() - 1));
//                }
//            }
//        }
//
//    }
//
//    /** 获取颜色 */
//    public int getColor() {
//        return this.getEntityData().get(COLOR);
//    }
//
//    /** 设置颜色 */
//    public void setColor(int value) {
//        this.getEntityData().set(COLOR, value);
//    }
//
//    /** 获取穿透等级 */
//    public byte getPierce() {
//        return this.getEntityData().get(PIERCE);
//    }
//
//    /** 设置穿透等级 */
//    public void setPierce(byte value) {
//        this.getEntityData().set(PIERCE, value);
//    }
//
//    /** 获取延迟 */
//    public int getDelay() {
//        return this.getEntityData().get(DELAY);
//    }
//
//    /** 设置延迟 */
//    public void setDelay(int value) {
//        this.getEntityData().set(DELAY, value);
//    }
//
//    /**
//     * 获取实体射线追踪结果
//     * @param startPos 起始位置
//     * @param endPos 结束位置
//     * @return 命中的实体（如果有）
//     */
//    @Nullable
//    protected EntityHitResult getRayTrace(Vec3 startPos, Vec3 endPos) {
//        return ProjectileUtil.getEntityHitResult(this.level(),
//                this,
//                startPos,
//                endPos,
//                this.getBoundingBox()
//                        .expandTowards(this.getDeltaMovement())
//                        .inflate(1.0D),
//                (entity) -> entity.canBeHitByProjectile() && !entity.isSpectator()
//                        && (entity != this.getShooter() || this.ticksInAir >= 5)
//                        && (this.alreadyHits == null || !this.alreadyHits.contains(entity.getId())));
//    }
//
//    /** 获取射手（发射者） */
//    @Nullable
//    @Override
//    public Entity getShooter() {
//        return this.getOwner();
//    }
//
//    /** 设置射手（发射者） */
//    @Override
//    public void setShooter(Entity shooter) {
//        setOwner(shooter);
//    }
//
//    /**
//     * 获取药水效果列表
//     * @return 药水效果列表
//     */
//    public List<MobEffectInstance> getPotionEffects() {
//        List<MobEffectInstance> effects = PotionUtils.getAllEffects(this.getPersistentData());
//
//        if (effects.isEmpty()) {
//            effects.add(new MobEffectInstance(MobEffects.POISON, 1, 1));
//        }
//
//        return effects;
//    }
//
//    /**
//     * 爆炸消失
//     * 播放音效，生成粒子，对周围实体施加效果后移除自身
//     */
//    public void burst() {
//        this.playSound(SoundEvents.GLASS_BREAK, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
//
//        if (!this.level().isClientSide()) {
//            if (this.level() instanceof ServerLevel) {
//                ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT, this.getX(), this.getY(), this.getZ(),
//                        16, 0.5, 0.5, 0.5, 0.25f);
//            }
//
//            this.burst(getPotionEffects(), null);
//        }
//
//        super.remove(RemovalReason.DISCARDED);
//    }
//
//    /**
//     * 爆炸并对周围实体施加效果
//     * @param effects 要施加的药水效果列表
//     * @param focusEntity 焦点实体（受到完整效果）
//     */
//    public void burst(List<MobEffectInstance> effects, @Nullable Entity focusEntity) {
//        // 获取范围内可攻击的实体
//        List<Entity> list = TargetSelector.getTargettableEntitiesWithinAABB(this.level(), 2, this);
//
//        list.stream().filter(e -> e instanceof LivingEntity).map(e -> (LivingEntity) e).forEach(e -> {
//            double distanceSq = this.distanceToSqr(e);
//            if (distanceSq < 9.0D) {
//                // 根据距离计算效果强度（距离越近强度越高）
//                double factor = 1.0D - Math.sqrt(distanceSq) / 4.0D;
//                if (e == focusEntity) {
//                    factor = 1.0D;
//                }
//
//                affectEntity(e, effects, factor);
//            }
//        });
//    }
//
//    /**
//     * 对实体施加药水效果
//     * @param focusEntity 目标实体
//     * @param effects 药水效果列表
//     * @param factor 效果强度因子
//     */
//    public void affectEntity(LivingEntity focusEntity, List<MobEffectInstance> effects, double factor) {
//        for (MobEffectInstance effectinstance : getPotionEffects()) {
//            MobEffect effect = effectinstance.getEffect();
//            if (effect.isInstantenous()) {
//                // 瞬间效果（如瞬间伤害）
//                effect.applyInstantenousEffect(this, this.getShooter(), focusEntity, effectinstance.getAmplifier(),
//                        factor);
//            } else {
//                // 持续效果（如中毒）
//                int duration = (int) (factor * (double) effectinstance.getDuration() + 0.5D);
//                if (duration > 0) {
//                    focusEntity.addEffect(new MobEffectInstance(effect, duration, effectinstance.getAmplifier(),
//                            effectinstance.isAmbient(), effectinstance.isVisible()));
//                }
//            }
//        }
//    }
//
//    /**
//     * 重置已命中实体列表
//     */
//    public void resetAlreadyHits() {
//        if (this.alreadyHits != null) {
//            alreadyHits.clear();
//        }
//    }
//
//    /**
//     * 设置命中的实体
//     * @param hitEntity 被命中的实体
//     */
//    public void setHitEntity(Entity hitEntity) {
//        if (hitEntity != this) {
//            this.entityData.set(HIT_ENTITY_ID, hitEntity.getId());
//
//            this.entityData.set(OFFSET_YAW, this.random.nextFloat() * 360);
//
//            this.setDelay(20 * 5);
//        }
//    }
//
//    /**
//     * 获取命中的实体
//     * @return 命中的实体（如果有）
//     */
//    @Nullable
//    public Entity getHitEntity() {
//        if (hitEntity == null) {
//            int id = this.entityData.get(HIT_ENTITY_ID);
//            if (0 <= id) {
//                hitEntity = this.level().getEntity(id);
//            }
//        }
//        return hitEntity;
//    }
//
//    /** 获取偏移偏航角 */
//    public float getOffsetYaw() {
//        return this.entityData.get(OFFSET_YAW);
//    }
//
//    /** 获取翻滚角度 */
//    public float getRoll() {
//        return this.entityData.get(ROLL);
//    }
//
//    /** 设置翻滚角度 */
//    public void setRoll(float value) {
//        this.entityData.set(ROLL, value);
//    }
//
//    /** 设置伤害值 */
//    public void setDamage(double damageIn) {
//        this.damage = damageIn;
//    }
//
//    /** 获取伤害值 */
//    @Override
//    public double getDamage() {
//        return this.damage;
//    }
//
//    /** 默认模型名称 */
//    private static final String defaultModelName = "slashblade:model/util/ss";
//
//    /**
//     * 设置模型名称
//     * @param name 模型名称
//     */
//    public void setModelName(String name) {
//        this.entityData.set(MODEL, Optional.ofNullable(name).orElse(defaultModelName));
//    }
//
//    /**
//     * 获取模型名称
//     * @return 模型名称
//     */
//    public String getModelName() {
//        String name = this.entityData.get(MODEL);
//        if (name.isEmpty()) {
//            name = defaultModelName;
//        }
//        return name;
//    }
//
//    /** 默认模型资源位置 */
//    private static final ResourceLocation defaultModel = new ResourceLocation(defaultModelName + ".obj");
//    /** 模型资源位置（懒加载） */
//    public LazyOptional<ResourceLocation> modelLoc = LazyOptional
//            .of(() -> new ResourceLocation(getModelName() + ".obj"));
//    /** 默认纹理资源位置 */
//    private static final ResourceLocation defaultTexture = new ResourceLocation(defaultModelName + ".png");
//    /** 纹理资源位置（懒加载） */
//    public LazyOptional<ResourceLocation> textureLoc = LazyOptional
//            .of(() -> new ResourceLocation(getModelName() + ".png"));
//
//    /** 获取模型资源位置 */
//    public ResourceLocation getModelLoc() {
//        return modelLoc.orElse(defaultModel);
//    }
//
//    /** 获取纹理资源位置 */
//    public ResourceLocation getTextureLoc() {
//        return textureLoc.orElse(defaultTexture);
//    }
//
//    /**
//     * 推动实体时的处理
//     * 重写以禁止碰撞时的速度改变
//     */
//    @Override
//    public void push(@NotNull Entity entityIn) {
//        // 抑制碰撞导致的速度改变
//        // super.applyEntityCollision(entityIn);
//    }
//
//    // 待实现：与射击攻击的抵消、将pierce值作为HP的近战攻击抵消
//}
