package xquare.trace.instrumentation;

import com.google.auto.service.AutoService;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.engine.spi.RowSelection;
import xquare.trace.tooling.InstrumenterModule;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@AutoService(InstrumenterModule.class)
public class LoaderInstrumentation extends InstrumenterModule.Tracing {
    @Override
    public void methodAdvice(MethodTransformer transformer) {
        System.out.println("class 이름 : " + LoaderAdvice.class.getName());
        transformer.applyAdvice(
                ElementMatchers.hasSuperType(ElementMatchers.named("org.hibernate.loader.Loader")),
                ElementMatchers.isMethod().and(ElementMatchers.named("getResultSet")),
                LoaderAdvice.class.getName()
        );
    }

    public static class LoaderAdvice {
        @Advice.OnMethodEnter
        public static void onEnter(
                @Advice.Argument(0) PreparedStatement st,
                @Advice.Argument(1) RowSelection selection) {

            System.out.println("Entering getResultSet:");
            System.out.println("PreparedStatement: " + st);
            System.out.println("RowSelection: " + selection);
        }

        @Advice.OnMethodExit(onThrowable = Throwable.class)
        public static void onExit(
                @Advice.Return ResultSet rs,
                @Advice.Thrown Throwable throwable) {

            if (throwable != null) {
                System.out.println("Exception thrown in getResultSet: " + throwable);
            } else {
                System.out.println("Exiting getResultSet:");
                System.out.println("ResultSet: " + rs);
            }
        }
    }
}
