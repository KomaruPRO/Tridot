package pro.komaru.tridot.registry.item.builders;

import com.google.common.collect.*;
import pro.komaru.tridot.client.render.animation.ItemAnimation;
import pro.komaru.tridot.registry.item.types.ScytheItem;
import pro.komaru.tridot.utilities.math.Interp;
import net.minecraft.core.particles.*;
import net.minecraft.sounds.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.Item.*;
import net.minecraft.world.item.*;

public abstract class AbstractScytheBuilder<T extends ScytheItem> {
    public Tier tier = Tiers.WOOD;
    public Properties itemProperties;
    public SoundEvent attackSound = SoundEvents.EMPTY;
    public SoundEvent cooldownSound = SoundEvents.EMPTY;
    public int attackDamageIn;
    public float attackSpeedIn;
    public float chance = 1;
    public int useTime = 7;
    public int attackUsages = 1;
    public int attackDelay = 5;
    public int minCooldownTime = 15;
    public int cooldownTime = 75;
    public float attackRadius = 3;
    public float screenShakeIntensity = 0.35f;
    public int screenShakeDuration = 4;
    public ItemAnimation animation;
    public Interp screenShakeEasing = Interp.circle;
    public ImmutableList<MobEffectInstance> effects = ImmutableList.of();
    public ParticleOptions particleOptions = ParticleTypes.POOF;

    public AbstractScytheBuilder(int attackDamageIn, float attackSpeedIn, Properties itemProperties) {
        this.attackDamageIn = attackDamageIn;
        this.attackSpeedIn = attackSpeedIn;
        this.itemProperties = itemProperties;
    }

    public AbstractScytheBuilder<T> setTier(Tier tier){
        this.tier = tier;
        return this;
    }

    public AbstractScytheBuilder<T> setUseTIme(int useTime){
        this.useTime = useTime;
        return this;
    }

    /**
     * @param animation Ability animation
     */
    public AbstractScytheBuilder<T> setUseAnimation(ItemAnimation animation){
        this.animation = animation;
        return this;
    }

    public AbstractScytheBuilder<T> setScreenShake(float screenShakeIntensity, int screenShakeDuration){
        this.screenShakeIntensity = screenShakeIntensity;
        this.screenShakeDuration = screenShakeDuration;
        return this;
    }

    public AbstractScytheBuilder<T> setScreenShakeEasing(Interp easing){
        this.screenShakeEasing = easing;
        return this;
    }

    /**
     * @param attackUsages Maximum attacks a scythe can perform before entering cooldown
     * @param attackDelay Delay between multi-attacks
     * */
    public AbstractScytheBuilder<T> setAttackCount(int attackUsages, int attackDelay){
        this.attackUsages = attackUsages;
        this.attackDelay = attackDelay;
        return this;
    }

    /**
     * @param event Sound that will be played when dash is performed
     */
    public AbstractScytheBuilder<T> setAttackSound(SoundEvent event){
        this.attackSound = event;
        return this;
    }

    /**
     * @param event Sound that will be played after cooldown ending
     */
    public AbstractScytheBuilder<T> setCooldownSound(SoundEvent event){
        this.cooldownSound = event;
        return this;
    }

    /**
     * @param particleOptions Particle that will appear after using the ability
     */
    public AbstractScytheBuilder<T> setParticles(ParticleOptions particleOptions){
        this.particleOptions = particleOptions;
        return this;
    }

    /**
     * @param chance Chance of applying effects to target
     * @param pEffects Effects that will be applied to target
     */
    public AbstractScytheBuilder<T> setEffects(float chance, MobEffectInstance... pEffects){
        this.chance = chance;
        this.effects = ImmutableList.copyOf(pEffects);
        return this;
    }

    /**
     * @param pEffects Effects that will be applied to target
     */
    public AbstractScytheBuilder<T> setEffects(MobEffectInstance... pEffects){
        this.effects = ImmutableList.copyOf(pEffects);
        return this;
    }

    /**
     * @param minCooldownTime Applied when no one was hit
     * @param cooldownTime Applied when someone was hit
     */
    public AbstractScytheBuilder<T> setCooldownTime(int minCooldownTime, int cooldownTime){
        this.minCooldownTime = minCooldownTime;
        this.cooldownTime = cooldownTime;
        return this;
    }

    /**
     * Radius of the ability, specified in blocks
     * */
    public AbstractScytheBuilder<T> setAttackRadius(float distance) {
        this.attackRadius = distance;
        return this;
    }

    /**
     * @return Build of ScytheItem with all the configurations you set :p
     */
    public abstract T build();
}
