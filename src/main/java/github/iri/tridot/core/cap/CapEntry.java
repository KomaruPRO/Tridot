package github.iri.tridot.core.cap;

import github.iri.tridot.utilities.func.*;
import github.iri.tridot.utilities.struct.*;
import net.minecraftforge.common.capabilities.*;

public class CapEntry<T> {
    public Prov<CapProvider> prov;
    public Prov<Capability<T>> instance;
    public String modId;
    public String capId;
    public int id;
}
