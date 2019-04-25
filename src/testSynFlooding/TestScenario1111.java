package testSynFlooding;

import java.io.FileNotFoundException;

import exception.DEVS_Exception;
import simulator.RootCoordinator;
import synFloodExemples.Scenario1111;

public class TestScenario1111 {

	public static void main(String[] args) throws DEVS_Exception, FileNotFoundException {
		Scenario1111 sc = new Scenario1111("Scenario1111","Scenario composé d'un server, d'un reseau, d'un hacker et d'un client");
		RootCoordinator root=new RootCoordinator(sc.getSimulator());
		root.init(0);
		root.run(30);
	}
}
