package pro.komaru.tridot.api.entity.ai;

import pro.komaru.tridot.core.entity.ai.Sensor;

public class TestSensor extends Sensor {
    public TestSensor() {
        super(4);
    }

    @Override
    public void update() {
        System.out.println("TestSensor updated");
    }
}
