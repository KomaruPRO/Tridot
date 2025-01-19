package pro.komaru.tridot.core.struct.wraps;

import pro.komaru.tridot.core.struct.func.Func;
import pro.komaru.tridot.core.struct.func.Prov;
import pro.komaru.tridot.core.math.Mathf;

public class RangeClamp {
    public Prov<Integer> min;
    public Prov<Integer> max;

    public int current;

    public RangeClamp(Prov<Integer> min, Prov<Integer> max) {
        this.min = min; this.max = max;
        current = min.get();
    }
    public void update(Func<Integer,Integer> f) {
        current = Mathf.clamp(f.get(current),min.get(),max.get());
    }
    public int current() {
        return current;
    }
    public boolean peek() {
        return current+1 < max.get();
    }
    public boolean next() {
        if(!peek()) return false;
        current++;
        return true;
    }
    public boolean peekBack() {
        return current-1 >= min.get();
    };
    public boolean back() {
        if(!peekBack()) return false;
        current--;
        return true;
    }

    public boolean peekCurrent() {
        return current >= min.get() && current < max.get();
    }
}
