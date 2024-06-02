package xquare.trace.instrumentation;

import com.google.auto.service.AutoService;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import xquare.trace.tooling.InstrumenterModule;

import java.lang.reflect.Method;
import java.util.Arrays;

@AutoService(InstrumenterModule.class)
public class RepositoryInstrumentation extends InstrumenterModule.Tracing {
    @Override
    public void methodAdvice(MethodTransformer transformer) {
        transformer.applyAdvice(
                ElementMatchers.hasSuperType(ElementMatchers.named("org.springframework.data.repository.Repository")),
                ElementMatchers.isMethod(),
                JpaRepositoryAdvice.class.getName()
        );
    }

    public static class JpaRepositoryAdvice {
        @Advice.OnMethodEnter
        public static void onEnter(@Advice.Origin Method method, @Advice.AllArguments Object[] args) {
            if (RestControllerInstrumentation.ControllerAdvice.getCallDepth() > 0) {
                int depth = RestControllerInstrumentation.ControllerAdvice.getCallDepth();
                RestControllerInstrumentation.ControllerAdvice.callDepth.get().incrementAndGet();
                String declaringClass = method.getDeclaringClass().getName();
                String methodName = method.getName();
                System.out.println(RestControllerInstrumentation.ControllerAdvice.getIndentation(depth) + "Entering JPA Repository method: " + "." + methodName + " with arguments: " + Arrays.toString(args));
            }
        }

        @Advice.OnMethodExit
        public static void onExit(@Advice.Origin Method method) {
            if (RestControllerInstrumentation.ControllerAdvice.getCallDepth() > 0) {
                int depth = RestControllerInstrumentation.ControllerAdvice.callDepth.get().decrementAndGet();
                String declaringClass = method.getDeclaringClass().getName();
                String methodName = method.getName();
                System.out.println(RestControllerInstrumentation.ControllerAdvice.getIndentation(depth) + "Exiting JPA Repository method: " + "." + methodName);
            }
        }
    }
}
