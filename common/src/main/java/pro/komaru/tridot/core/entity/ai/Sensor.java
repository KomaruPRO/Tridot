package pro.komaru.tridot.core.entity.ai;

public abstract class Sensor {
    public final int updateInterval;

    protected Sensor(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public void update() {}
}
