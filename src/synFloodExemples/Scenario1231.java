package synFloodExemples;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import model.CoupledModel;
import model.Model;
import synFloodModels.Client;
import synFloodModels.Collector;
import synFloodModels.Hacker;
import synFloodModels.Network;
import synFloodModels.Server;

public class Scenario1231 extends CoupledModel {
	Network reseau;
	Server server;
	Client client1,client2,client3;
	Hacker hacker1,hacker2;
	Collector collector;
	public Scenario1231(String name, String descr) throws FileNotFoundException {
		super(name, descr); 
		reseau=new Network("Reseau", "Seul reseau de ce scenario");
		server=new Server("Server", "Seul serveur de ce scenario", id_);
		client1=new Client("Client1", "Client N01 du serveur dans ce scenario");
		client2=new Client("Client2", "Client N02 du serveur dans ce scenario");
		client3=new Client("Client3", "Client N03 du serveur dans ce scenario");
		hacker1=new Hacker("Hacker1", "Hacker N01 du serveur dans ce scenario");
		hacker2=new Hacker("Hacker2", "Hacker N02 du serveur dans ce scenario");
		collector=new Collector("Store", "Modele qui garde le resultat des donnees de simulation");
		//client1
		server.setAddress("Poste1");
		server.setPort(1);
		client1.setAddress("Poste2");
		client1.setPort(2);
		client1.setServerAddress(server.getAddress());
		client1.setServerPort(server.getPort());
		//Client2
		client2.setAddress("Poste3");
		client2.setPort(3);
		client2.setServerAddress(server.getAddress());
		client2.setServerPort(server.getPort());
		//client3
		client3.setAddress("Poste4");
		client3.setPort(4);
		client3.setServerAddress(server.getAddress());
		client3.setServerPort(server.getPort());
		//Hacker 1
		hacker1.setServerAddress(server.getAddress());
		hacker1.setServerPort(server.getPort());
		//Hacker 2
		hacker2.setServerAddress(server.getAddress());
		hacker2.setServerPort(server.getPort());
		//Declaration as submodels
		addSubModel(client1);
		addSubModel(client2);
		addSubModel(client3);
		addSubModel(hacker1);
		addSubModel(hacker2);
		addSubModel(reseau);
		addSubModel(server);
		addSubModel(collector);
		
		//connection customers to network
		addIC(client1.getOutputPortStructure("Client1.outputCN"), reseau.getInputPortStructure("Reseau.inputCN"));
		addIC(client2.getOutputPortStructure("Client2.outputCN"), reseau.getInputPortStructure("Reseau.inputCN"));
		addIC(client3.getOutputPortStructure("Client3.outputCN"), reseau.getInputPortStructure("Reseau.inputCN"));
		addIC(hacker1.getOutputPortStructure("Hacker1.outputHN"), reseau.getInputPortStructure("Reseau.inputHN"));
		addIC(hacker2.getOutputPortStructure("Hacker2.outputHN"), reseau.getInputPortStructure("Reseau.inputHN"));
		
		// connection network to client & server
		addIC(reseau.getOutputPortStructure("Reseau.outputNCS"), server.getInputPortStructure("Server.inputNCS"));
		addIC(reseau.getOutputPortStructure("Reseau.outputNHS"), server.getInputPortStructure("Server.inputNHS"));
		addIC(reseau.getOutputPortStructure("Reseau.outputNSC"), client1.getInputPortStructure("Client1.inputSC"));
		addIC(reseau.getOutputPortStructure("Reseau.outputNSC"), client2.getInputPortStructure("Client2.inputSC"));
		addIC(reseau.getOutputPortStructure("Reseau.outputNSC"), client3.getInputPortStructure("Client3.inputSC"));
		
		//connection server to network
		addIC(server.getOutputPortStructure("Server.outputSN"), reseau.getInputPortStructure("Reseau.inputNSC"));
		
		//connection from others model to Collector
		addIC(client1.getOutputPortStructure("Client1.outputCC"), collector.getInputPortStructure("Store.inputCC"));
		addIC(client2.getOutputPortStructure("Client2.outputCC"), collector.getInputPortStructure("Store.inputCC"));
		addIC(client3.getOutputPortStructure("Client3.outputCC"), collector.getInputPortStructure("Store.inputCC"));
		addIC(hacker1.getOutputPortStructure("Hacker1.outputHC"), collector.getInputPortStructure("Store.inputHC"));
		addIC(hacker2.getOutputPortStructure("Hacker2.outputHC"), collector.getInputPortStructure("Store.inputHC"));
		addIC(server.getOutputPortStructure("Server.outputSC"), collector.getInputPortStructure("Store.inputSC"));
	}

	@Override
	public Model select(ArrayList<Model> arg0) {
		return arg0.get(0);
	}
}
