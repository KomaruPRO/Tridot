package pro.komaru.tridot.client.gfx.particle;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.util.*;
import net.minecraftforge.api.distmarker.*;
import pro.komaru.tridot.client.render.TridotRenderTypes.*;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public abstract class ScreenParticle {
    public final ClientLevel level;
    public static final RandomSource random = RandomSource.create();

    public double xOld;
    public double yOld;
    public double x;
    public double y;

    public double xMotion;
    public double yMotion;
    public double xMoved;
    public double yMoved;

    public boolean removed;
    public int age;
    public int lifetime;
    public float gravity;
    public float size = 1;
    public float rCol = 1.0F;
    public float gCol = 1.0F;
    public float bCol = 1.0F;
    public float alpha = 1.0F;
    public float friction = 0.98F;
    public float roll;
    public float oRoll;

    protected ScreenParticle(ClientLevel pLevel, double pX, double pY) {
        this.level = pLevel;
        this.setSize(0.2F);
        this.x = pX;
        this.y = pY;
        this.xOld = pX;
        this.yOld = pY;
        this.lifetime = (int) (4.0F / (random.nextFloat() * 0.9F + 0.1F));
    }

    public ScreenParticle(ClientLevel pLevel, double pX, double pY, double pXSpeed, double pYSpeed) {
        this(pLevel, pX, pY);
        this.xMotion = pXSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        this.yMotion = pYSpeed + (Math.random() * 2.0D - 1.0D) * (double) 0.4F;
        double d0 = (Math.random() + Math.random() + 1.0D) * (double) 0.15F;
        double d1 = Math.sqrt(this.xMotion * this.xMotion + this.yMotion * this.yMotion);
        this.xMotion = this.xMotion / d1 * d0 * (double) 0.4F;
        this.yMotion = this.yMotion / d1 * d0 * (double) 0.4F + (double) 0.1F;
    }

    public void setParticleSpeed(double pXd, double pYd) {
        this.xMotion = pXd;
        this.yMotion = pYd;
    }

    public ScreenParticle setSize(float size) {
        this.size = size;
        return this;
    }

    public void setColor(float pParticleRed, float pParticleGreen, float pParticleBlue) {
        this.rCol = pParticleRed;
        this.gCol = pParticleGreen;
        this.bCol = pParticleBlue;
    }

    protected void setAlpha(float pAlpha) {
        this.alpha = pAlpha;
    }

    public void setLifetime(int pParticleLifeTime) {
        this.lifetime = pParticleLifeTime;
    }

    public int getLifetime() {
        return this.lifetime;
    }

    public void tick() {
        this.xOld = this.x;
        this.yOld = this.y;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yMotion -= 0.04D * (double) this.gravity;
            this.xMotion *= this.friction;
            this.yMotion *= this.friction;
            this.x += xMotion;
            this.y += yMotion;
            this.xMoved += xMotion;
            this.yMoved += yMotion;
        }
    }

    public abstract void render(BufferBuilder bufferBuilder);

    public abstract ScreenParticleRenderType getRenderType();

    public void remove() {
        this.removed = true;
    }

    public boolean isAlive() {
        return !this.removed;
    }
}