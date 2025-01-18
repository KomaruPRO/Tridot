package pro.komaru.tridot.core.cap;

import pro.komaru.tridot.utilities.func.*;
import pro.komaru.tridot.utilities.struct.*;
import net.minecraftforge.common.capabilities.*;
import pro.komaru.tridot.utilities.func.Prov;
import pro.komaru.tridot.utilities.struct.CapProvider;

public class CapEntry<T> {
    public Prov<CapProvider> prov;
    public Prov<Capability<T>> instance;
    public String modId;
    public String capId;
    public int id;
}
