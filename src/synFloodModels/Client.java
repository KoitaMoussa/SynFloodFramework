package synFloodModels;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import model.AtomicModel;
import SynFloodLib.ClientConfiguration;
import SynFloodLib.Message;
import SynFloodLib.MessageType;
import exception.DEVS_Exception;

public class Client extends AtomicModel {
	ClientConfiguration configuration;
	int nb_connection;
	int nb_demandes;
	ArrayList<Message> demandes;
	String serverAddress;
	int serverPort;
	Message currentMessage;
	Random r=new Random();
	String clientAddress;
	int clientPort;
	ArrayList<Integer>Sequenceliste;
	
	public Client(String name,String desc) {
		super(name,desc);
		addInputPortStructure(new Message(),name_+".inputSC","entree du client");
		addOutputPortStructure(new Message(),name_+".outputCN","sortie du client vers le reseau");
		addOutputPortStructure(new Message(),name_+".outputCC","port de sortie du Client vers le collecteur");	
		demandes=new ArrayList<Message>();
		nb_connection=0;
		nb_demandes=0;
		Sequenceliste = new ArrayList<Integer>();
		configuration=ClientConfiguration.SENDING;
		currentMessage=new Message(MessageType.SYN,getAddress(),getServerAddress(),new Date(), getPort(), getServerPort());
	}
	@Override
	public void deltaInt() {
//		System.err.println(this.id_+" : "+this.name_+" .DeltaInt");
		if(configuration==ClientConfiguration.SENDING){
			configuration=ClientConfiguration.WAITING;
//			nb_connection++;
		}else if(configuration==ClientConfiguration.CONFIRMING) {
			configuration=ClientConfiguration.SENDING;
		}else if(configuration==ClientConfiguration.WAITING){
			configuration=ClientConfiguration.SENDING;
		}
	}
	@Override
	public void deltaExt(double arg0) throws DEVS_Exception {//		System.err.println(this.id_+" : "+this.name_+" .DeltaExt");
		Message m=(Message)getInputPortData(name_+".inputSC");
		if ((configuration==ClientConfiguration.WAITING)&&(m.getType()==MessageType.SYN_ACK)){
			currentMessage=m;
			currentMessage.setSource(clientAddress);
			currentMessage.setDestination(serverAddress);
			currentMessage.setPortDestination(serverPort);
			currentMessage.setPortSource(clientPort);
			currentMessage.setType(MessageType.ACK);
			currentMessage.setNumeroSequence(m.getNumeroSequence()-1);
			demandes.add(currentMessage);
			configuration=ClientConfiguration.CONFIRMING;
		}
	}
	@Override
	public void lambda() throws DEVS_Exception {
//		System.err.println(this.id_+" : "+this.name_+" .Lambda");		
		if(configuration==ClientConfiguration.SENDING){	
//			currentMessage=demandes.get(0);
			currentMessage.setDestination(serverAddress);
			currentMessage.setPortDestination(serverPort);
			currentMessage.setSource(clientAddress);
			currentMessage.setPortSource(clientPort);
			currentMessage.setDateEmission(new Date());
			currentMessage.setType(MessageType.SYN);
			currentMessage.setNumeroSequence(r.nextInt(1000));
			//Garde le numero sequence de chaque message SYN
			Sequenceliste.add(currentMessage.getNumeroSequence());
			nb_demandes++;
//			System.err.println("NOMBRE DE SYN ENVOYE PAR LE CLT :"+nb_demandes);
//			demandes.add(currentMessage);
			this.setOutputPortData(name_+".outputCN",currentMessage);
			this.setOutputPortData(name_+".outputCC",currentMessage);
		}else if(configuration==ClientConfiguration.CONFIRMING){
			currentMessage=demandes.get(0);
			//pour eviter que le Client repond avec ACK a un message SYN des hackers
			for(int i=0;i<Sequenceliste.size();i++){
				//Compare le numero sequence du message SYN_ACK aux numeros des messages SYN stocké
				if(currentMessage.getNumeroSequence()==Sequenceliste.get(i)){
					nb_connection++;
					Sequenceliste.remove(i);
					currentMessage.setDateEmission(new Date());
					currentMessage.setType(MessageType.ACK);
//					System.err.println(" NOMBRE DE ACK ENVOYE PAR LE CLT :"+nb_connection);
					this.setOutputPortData(name_+".outputCN",currentMessage);
					this.setOutputPortData(name_+".outputCC",currentMessage);	
				}
			}
		}
	}
	@Override
	public double ta() {
//		System.err.println(this.id_+" : "+this.name_+" .Time Advance");
		double time = 0;
		if (configuration==ClientConfiguration.SENDING)
			time=1.0;//time=0.2;
		else if (configuration==ClientConfiguration.WAITING)
			time=r.nextDouble()*10;//DEVS_Real.POSITIVE_INFINITY;
		else if(configuration==ClientConfiguration.CONFIRMING)
			time=1.0;//time=0.2;
			return time;
	}
	public ClientConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ClientConfiguration configuration) {
		this.configuration = configuration;
	}

	public int getNb_connection() {
		return nb_connection;
	}

	public void setNb_connection(int nb_connection) {
		this.nb_connection = nb_connection;
	}

	public int getNb_demandesEchouees() {
		return nb_demandes;
	}

	public void setNb_demandesEchouees(int nb_demandesEchouees) {
		this.nb_demandes = nb_demandesEchouees;
	}

	public ArrayList<Message> getDemandes() {
		return demandes;
	}

	public void setDemandes(ArrayList<Message> demandes) {
		this.demandes = demandes;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getAddress() {
		return clientAddress;
	}

	public void setAddress(String address) {
		this.clientAddress = address;
	}

	public int getPort() {
		return clientPort;
	}

	public void setPort(int port) {
		this.clientPort = port;
	}
}
