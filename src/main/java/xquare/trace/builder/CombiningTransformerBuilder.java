package xquare.trace.builder;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import xquare.trace.tooling.Instrumenter;

import java.lang.instrument.Instrumentation;

public class CombiningTransformerBuilder implements Instrumenter.MethodTransformer {

    final Instrumentation inst;


    public CombiningTransformerBuilder(Instrumentation inst) {
        this.inst = inst;
    }

    @Override
    public void applyAdvice(ElementMatcher<? super TypeDescription> typeMatcher, ElementMatcher<? super MethodDescription> matcher, String adviceClass) {
        try {
            new AgentBuilder.Default()
                    .type(typeMatcher)
                    .transform(new AgentBuilder.Transformer.ForAdvice()
                            .include(Class.forName(adviceClass).getClassLoader())
                            .advice(matcher, adviceClass))
                            .installOn(inst);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
