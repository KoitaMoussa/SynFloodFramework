package testSynFlooding;

import java.io.FileNotFoundException;

import exception.DEVS_Exception;
import simulator.Coordinator;
import simulator.RootCoordinator;
import synFloodExemples.Scenario;
public class TestScenario {

	public TestScenario() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws DEVS_Exception, FileNotFoundException {
		// TODO Auto-generated method stub
		Scenario s=new Scenario("Scenario", "ce Scenario implique 1 server, 2 Hacker, 3 clients et 1 reseau");
		Coordinator sc =new Coordinator(s);
		RootCoordinator r =new RootCoordinator(sc);
		r.init(0);
		r.run(1000);
	}
}