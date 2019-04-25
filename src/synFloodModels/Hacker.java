package synFloodModels;
import interfaces.HackStrategy;

import java.util.Date;
import java.util.Random;

import model.AtomicModel;
import SynFloodLib.HackerConfiguration;
import SynFloodLib.Message;
import SynFloodLib.MessageType;
import exception.DEVS_Exception;
public class Hacker extends AtomicModel implements HackStrategy{
	HackerConfiguration configuration;
	String serverAddress;
	int serverPort;
	int numeroSequence,nb_demandes; 
	Random r;
	public Hacker(String name, String desc) {
		super(name,desc);
		addOutputPortStructure(new Message(),name_+".outputHN","port de sortie du Hacker vers le reseau");
		addOutputPortStructure(new Message(),name_+".outputHC","port de sortie du Hacker vers le collecteur");
		configuration=HackerConfiguration.SENDING; 
		r=new Random();
	}
	@Override
	public void deltaExt(double arg0) throws DEVS_Exception {
		System.err.println(this.id_+" : "+this.name_+" .DeltaExt");
		//undefined
	}
	@Override
	public void deltaInt() {
//		System.err.println(this.id_+" : "+this.name_+" .DeltaInt");
		if(configuration==HackerConfiguration.WAITING)
			configuration=HackerConfiguration.SENDING;
		else if(configuration==HackerConfiguration.SENDING)
			configuration=HackerConfiguration.WAITING;
	}
	@Override
	public void lambda() throws DEVS_Exception {	
//		System.err.println(this.id_+" : "+this.name_+" .Lambda");
		if(configuration==HackerConfiguration.SENDING){
			Message msg=new Message();
			msg.setType(MessageType.SYN);
			msg.setDestination(serverAddress);
			msg.setPortDestination(serverPort);
			msg.setDateEmission(new Date());
			msg.setSource(null);
			msg.setPortSource(0);
			msg.setNumeroSequence(r.nextInt(1000));

			nb_demandes++;
//			System.err.println("NOMBRE DE SYN ENVOYE PAR LE Hack :"+nb_demandes);
			setOutputPortData(name_+".outputHN",msg);
			setOutputPortData(name_+".outputHC",msg);
			msg=null;
		}
	}
	@Override
	public double ta() {
//		System.err.println(this.id_+" : "+this.name_+" .TimeAdvance");
		double time=0.0;
		if(configuration==HackerConfiguration.SENDING)
			time=1.0;//time= 0.5;//	
		else if(configuration==HackerConfiguration.WAITING)
			time= r.nextDouble()*10;
		return time;
	}
	public String getServerAddress(){
		return serverAddress;
	}
	public int getServerPort(){
		return serverPort;
	}
	public void setServerAddress(String n){
		serverAddress=n;
	}
	public void setServerPort(int p){
		serverPort=p;
	}

}
