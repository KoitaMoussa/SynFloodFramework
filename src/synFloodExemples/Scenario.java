package synFloodExemples;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import synFloodModels.Client;
import synFloodModels.Hacker;
//import synFloodModels.Hacker;
import synFloodModels.Network;
import synFloodModels.Server;
import model.CoupledModel;
import model.Model;

public class Scenario extends CoupledModel {
	Network reseau;
	Server server;
	Hacker hacker1;
	Client client;

	public Scenario(String name, String descr) throws FileNotFoundException {
		// TODO Auto-generated constructor stub
		super(name, descr); 
		reseau=new Network("Reseau", "Seul reseau de ce scenario");
		server=new Server("Server", "Seul serveur de ce scenario", id_);
		hacker1=new Hacker("Client1", "Client N01 du serveur dans ce scenario");
		client = new Client("client", "client legitime");
		server.setAddress("Poste1");
		server.setPort(1);
		hacker1.setServerAddress(server.getAddress());
		hacker1.setServerPort(server.getPort());
		
		this.addSubModel(hacker1);
		this.addSubModel(reseau);
		this.addSubModel(server);
		this.addSubModel(client);
		
		
//		this.addIC(client.getOutputPortStructure("output"), reseau.getInputPortStructure("inputNetwork"));
		this.addIC(server.getOutputPortStructure("output"), reseau.getInputPortStructure("inputNetwork"));
		this.addIC(reseau.getOutputPortStructure("outputNetwork"), server.getInputPortStructure("input"));
		this.addIC(hacker1.getOutputPortStructure("output"), reseau.getInputPortStructure("inputNetwork"));
		
	}

	@Override
	public Model select(ArrayList<Model> arg0) {
		// TODO Auto-generated method stubS
		return arg0.get(0);
	}

}
