package pro.komaru.tridot.util.struct.data;

public class BoolPair extends Pair<Boolean,Boolean> {
    public static BoolPair of(boolean a, boolean b) {
        return new BoolPair(a,b);
    }
    public static BoolPair ofFalse() {
        return of(false,false);
    }
    public static BoolPair ofTrue() {
        return of(true,true);
    }
    public static BoolPair ofA(boolean bool) {
        return of(bool,false);
    }
    public static BoolPair ofB(boolean bool) {
        return of(false,bool);
    }
    public BoolPair(boolean a, boolean b) {
        super(a,b);
    }
}
