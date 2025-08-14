package pro.komaru.tridot.core.struct;

public class CallInfo {
    private boolean cancelled;

    public CallInfo() {
        this.cancelled = false;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
