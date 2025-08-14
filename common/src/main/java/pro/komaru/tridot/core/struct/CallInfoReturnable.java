package pro.komaru.tridot.core.struct;

public class CallInfoReturnable<R> extends CallInfo {

    private R returnedValue;

    public void returnValue(R value) {
        cancel();
        this.returnedValue = value;
    }

    public R getReturnedValue() {
        return returnedValue;
    }
}
