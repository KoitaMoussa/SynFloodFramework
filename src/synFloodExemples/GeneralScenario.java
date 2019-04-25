package synFloodExemples;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import SynFloodLib.Memory;
import synFloodModels.Client;
import synFloodModels.Collector;
import synFloodModels.Hacker;
import synFloodModels.Network;
import synFloodModels.Server;
import model.CoupledModel;
import model.Model;

public class GeneralScenario extends CoupledModel{
	private Client[] allClients;
	private Hacker[]allHackers;
	Network reseau; 
	Server server;
	Collector collector;
	Memory memory;
	int[]sequenceListe;
	public GeneralScenario(String name, String desc, int Clientnumber,int HackerNumber,int memorySize,double ServerTimer) throws FileNotFoundException {
		super(name, desc);
		//Creation des modèles
		allClients= new Client[Clientnumber];
		allHackers=new Hacker[HackerNumber];
		reseau=new Network("Reseau", "Seul reseau de ce scenario");
		server=new Server("Server", "Seul serveur de ce scenario", ServerTimer);
		memory=new Memory(memorySize);
		sequenceListe = new int[memorySize];
		server.setSequenceliste(sequenceListe);
//		memory.setTimer(ServerTimer);
		server.setMemory(memory);
		collector=new Collector("Store", "Modele qui garde le resultat des donnees de simulation");
		//Creation of all hackers and their ports
		for(int i=0;i<HackerNumber;i++){
			allHackers[i] = new Hacker("Hacker"+i, "Hacker N "+i+" du serveur dans ce scenario");
		}
		//creation of all clients and their port
		for(int i=0;i<Clientnumber;i++){
			allClients[i] = new Client("Client"+i, "Client N° "+i+" du serveur dans ce scenario Client qui demande au server");
		}
		server.setAddress("Poste0");
		server.setPort(0);
		//Clients's configurations
		for(int i=0;i<Clientnumber;i++){
			allClients[i].setAddress("Poste"+(i+1));
			allClients[i].setPort(i+1);
			allClients[i].setServerAddress(server.getAddress());
			allClients[i].setServerPort(server.getPort());
		}
		//Hackers's configurations 
		for(int i=0;i<HackerNumber;i++){
			allHackers[i].setServerAddress(server.getAddress());
			allHackers[i].setServerPort(server.getPort());
		}
		//Adding sub models to coupled model 
		addSubModel(server);
		addSubModel(reseau);
		addSubModel(collector);
		//adding Clients
		for(int j=0;j<Clientnumber;j++){
			addSubModel(allClients[j]);
		}//adding Hackers
		for(int j=0;j<HackerNumber;j++){
			addSubModel(allHackers[j]);
		}
		//connection from Server to network 
		addIC(server.getOutputPortStructure(server.getName()+".outputSN"), reseau.getInputPortStructure(reseau.getName()+".inputNSC"));
		//Creation of coupling IC 
		//connection from allClients to Network
		for(int i=0;i<Clientnumber;i++){
			addIC(allClients[i].getOutputPortStructure(allClients[i].getName() +".outputCN"), reseau.getInputPortStructure(reseau.getName()+".inputCN"));
		}//connection from allHackers to Network
		for(int i=0;i<HackerNumber;i++){
			addIC(allHackers[i].getOutputPortStructure(allHackers[i].getName()+".outputHN"), reseau.getInputPortStructure(reseau.getName()+".inputHN"));
		}
		// connection network to client & server
		
		//connection from allHackers to Server via Network
		for(int i=0;i<+HackerNumber;i++){
			addIC(reseau.getOutputPortStructure(reseau.getName()+".outputNHS"), server.getInputPortStructure(server.getName()+".inputNHS"));
		}
		// connection from allClients to Server via Network
		for(int i=0;i<Clientnumber;i++){
			addIC(reseau.getOutputPortStructure(reseau.getName()+".outputNCS"), server.getInputPortStructure(server.getName()+".inputNCS"));
			addIC(reseau.getOutputPortStructure(reseau.getName()+".outputNSC"), allClients[i].getInputPortStructure(allClients[i].getName()+".inputSC"));
		}
		// connection from others model to Collector
		//connection from Server to collector
		addIC(server.getOutputPortStructure(server.getName()+".outputSC"), collector.getInputPortStructure(collector.getName()+".inputSC"));
		//connection from allClients to collector
		for(int i=0;i<Clientnumber;i++){
			addIC(allClients[i].getOutputPortStructure(allClients[i].getName()+".outputCC"), collector.getInputPortStructure(collector.getName()+".inputCC"));
		}
		//from allHackers to collector
		for(int i=0;i<HackerNumber;i++){
			addIC(allHackers[i].getOutputPortStructure(allHackers[i].getName()+".outputHC"), collector.getInputPortStructure(collector.getName()+".inputHC"));
		}
	}
	@Override
	public Model select(ArrayList<Model> arg0) {
		// TODO Auto-generated method stub
		return arg0.get(0);
	}
	public void Statistique(){
		if(this.getSimulator().getTL() % 5 == 0){
			server.Statistique();	
		}
		
//		double PLOSS, P_REQ_REG,P_REQ_HAC;
//		PLOSS = server.getDemandes_bloque()/server.getNb_demandes();
//		P_REQ_REG=server.getNb_demandes_Client()/server.getNb_demandes();
//		P_REQ_HAC=server.getNb_demandes_hacker()/server.getNb_demandes();
//		System.err.println("Taux de requetes perdues : "+PLOSS+" % ");
//		System.err.println("Taux de requetes regulières : "+P_REQ_REG+" % ");
//		System.err.println("Taux de requetes d'attaques : "+P_REQ_HAC+" % ");
	}

}
