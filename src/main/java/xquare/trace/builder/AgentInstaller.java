package xquare.trace.builder;

import xquare.trace.tooling.Instrumenter;
import xquare.trace.tooling.InstrumenterModule;

import java.lang.instrument.Instrumentation;
import java.util.ServiceLoader;

public class AgentInstaller {
    public static void installBytebuddyAgent(final Instrumentation inst) {
        ServiceLoader<InstrumenterModule> instrumenterModules = ServiceLoader.load(InstrumenterModule.class);

        CombiningTransformerBuilder transformer = new CombiningTransformerBuilder(inst);

        for(InstrumenterModule module : instrumenterModules) {
            if (module instanceof Instrumenter.HasMethodAdvice) {
                ((Instrumenter.HasMethodAdvice) module).methodAdvice(transformer);
                System.out.println("설치완료");
            }
        }
    }
}
