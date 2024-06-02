package xquare.trace.tooling;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface Instrumenter {
    interface HasMethodAdvice extends Instrumenter {
        void methodAdvice(MethodTransformer transformer);
    }

    interface MethodTransformer {
        void applyAdvice(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super MethodDescription> matcher, String adviceClass);
    }
}
