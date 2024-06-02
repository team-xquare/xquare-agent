package xquare.trace.instrumentation;

import com.google.auto.service.AutoService;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import xquare.trace.tooling.InstrumenterModule;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@AutoService(InstrumenterModule.class)
public class RestControllerInstrumentation extends InstrumenterModule.Tracing {
    @Override
    public void methodAdvice(MethodTransformer transformer) {
        transformer.applyAdvice(
                ElementMatchers.isAnnotatedWith(ElementMatchers.named("org.springframework.web.bind.annotation.RestController")),
                ElementMatchers.isMethod(),
                ControllerAdvice.class.getName()
        );
    }

    public static class ControllerAdvice {
        public static final ThreadLocal<AtomicInteger> callDepth = ThreadLocal.withInitial(AtomicInteger::new);

        @Advice.OnMethodEnter
        public static void onEnter(@Advice.Origin String method, @Advice.AllArguments Object[] args) {
            int depth = callDepth.get().getAndIncrement();
            System.out.println(getIndentation(depth) + "Entering controller method: " + method + " with arguments: " + Arrays.toString(args));
        }

        @Advice.OnMethodExit
        public static void onExit(@Advice.Origin String method) {
            int depth = callDepth.get().decrementAndGet();
            System.out.println(getIndentation(depth) + "Exiting controller method: " + method);
        }

        public static int getCallDepth() {
            return callDepth.get().get();
        }

        public static String getIndentation(int depth) {
            char[] chars = new char[depth * 2];
            Arrays.fill(chars, ' ');
            return new String(chars);
        }
    }
}
