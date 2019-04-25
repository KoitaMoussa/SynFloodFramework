package testSynFlooding;
import java.io.FileNotFoundException;

import exception.DEVS_Exception;
import simulator.RootCoordinator;
import synFloodExemples.GeneralScenario;
public class TestGeneralScenario {
	public static void main(String[] args) throws DEVS_Exception, FileNotFoundException {
		int HackerNumber,
			ClientNumber,
			memorySize;
		double serverTimer;
		HackerNumber=500;
		ClientNumber=300; 
		memorySize=1024;
		serverTimer=75;
		GeneralScenario gsc = new GeneralScenario("ScenarioGeneral", "Ce scenario implique un Server, une structure de Reseau"+ClientNumber+" Clients legitime et "+HackerNumber+" Hackers comme clients mal-intentionnés", ClientNumber, HackerNumber, memorySize,serverTimer);
		RootCoordinator root = new RootCoordinator(gsc.getSimulator());
		root.init(0.0);
		root.run(500.0);
		gsc.Statistique();
	}
}
