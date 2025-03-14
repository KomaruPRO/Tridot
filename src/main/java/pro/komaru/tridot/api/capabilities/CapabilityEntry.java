package pro.komaru.tridot.api.capabilities;

import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.util.struct.capability.CapProvider;
import pro.komaru.tridot.util.struct.func.Prov;

public class CapabilityEntry<T> {
    public Prov<CapProvider> prov;
    public Prov<Capability<T>> instance;
    public String modId;
    public String capId;
    public int id;
}
