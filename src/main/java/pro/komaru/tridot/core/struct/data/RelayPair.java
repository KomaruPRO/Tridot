package pro.komaru.tridot.core.struct.data;

import java.io.*;

public class RelayPair<A,B> extends Pair<A,B> implements Serializable {
    private boolean selected = false;
    private boolean selectedA = false;

    public RelayPair() {
        super();
    }

    public RelayPair(A a, B b) {
        super(a,b);
    }
    public RelayPair(A a, B b, boolean first) {
        super(a,b);
        selected = true;
        selectedA = first;
    }

    public RelayPair(Object f, boolean first) {
        selected = true;
        selectedA = first;
        if(selectedA) first((A) f);
        else second((B) f);
    }

    public void selectFirst() {
        selectedA = true;
        selected = true;
    }
    public void selectSecond() {
        selectedA = false;
        selected = true;
    }
    public void selectNone() {
        selected = false;
    }
    public Object selected() {
        return !selected ? null : (selectedA ? first() : second());
    }
    public boolean selectedFirst() {
        return selected && selectedA;
    }
    public boolean selectedSecond() {
        return selected && !selectedA;
    }
}
