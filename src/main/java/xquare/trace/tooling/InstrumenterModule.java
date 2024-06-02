package xquare.trace.tooling;

public abstract class InstrumenterModule implements Instrumenter {
    public static abstract class Tracing extends InstrumenterModule implements HasMethodAdvice {

    }
}
