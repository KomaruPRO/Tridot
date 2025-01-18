package pro.komaru.tridot.core.cap;

import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.core.struct.*;
import pro.komaru.tridot.utilities.func.*;

public class CapEntry<T> {
    public Prov<CapProvider> prov;
    public Prov<Capability<T>> instance;
    public String modId;
    public String capId;
    public int id;
}
