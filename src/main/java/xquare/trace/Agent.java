package xquare.trace;

import xquare.trace.builder.AgentInstaller;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String agentArgs, Instrumentation inst) {
        AgentInstaller.installBytebuddyAgent(inst);
    }
}
