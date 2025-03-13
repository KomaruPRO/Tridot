package pro.komaru.tridot.ocore.cap;

import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.ocore.struct.capability.CapProvider;
import pro.komaru.tridot.ocore.struct.func.Prov;

public class CapEntry<T> {
    public Prov<CapProvider> prov;
    public Prov<Capability<T>> instance;
    public String modId;
    public String capId;
    public int id;
}
