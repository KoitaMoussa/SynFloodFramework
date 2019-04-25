package testSynFlooding;

import java.io.FileNotFoundException;

import simulator.RootCoordinator;
import synFloodExemples.Scenario1231;
import exception.DEVS_Exception;
public class TestScenario1231 {
	public TestScenario1231() {
	}
	public static void main(String[] args) throws DEVS_Exception, FileNotFoundException {
		Scenario1231 s=new Scenario1231("Scenario1231", "ce Scenario implique 1 server, 2 Hacker, 3 clients, 1 reseau et 1 collecteur");
		RootCoordinator r = new RootCoordinator(s.getSimulator());
		r.init(0);
		r.run(30);
	}
}