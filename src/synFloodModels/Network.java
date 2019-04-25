package synFloodModels;

import SynFloodLib.Message;
import SynFloodLib.MessageType;
import SynFloodLib.NetworkConfiguration;
import exception.DEVS_Exception;
import model.AtomicModel;

import java.util.ArrayList;

import types.DEVS_Real;
public class Network extends AtomicModel{
	ArrayList <Message> liste;
	NetworkConfiguration configuration;
	Message mC1, mH1,mS,msgSend;
	int indicemsgC,indicemsgH,indicemsgS;
	public Network(String name,String desc) {
		super(name,desc);
		//Inputs ports of neetwork model
		addInputPortStructure(new Message(), name_+".inputCN","port d'entrée du réseau pour le client");
		addInputPortStructure(new Message(), name_+".inputHN","port d'entrée du réseau pour le hacker");
		addInputPortStructure(new Message(), name_+".inputNSC","port d'entrée du réseau pour le server");		
		//Outputs ports of neetwork model
		addOutputPortStructure(new Message(),name_+".outputNCS","port de sortie du réseau du client au server");
		addOutputPortStructure(new Message(),name_+".outputNHS","port de sortie du réseau du hacker au server");	
		addOutputPortStructure(new Message(),name_+".outputNSC","port de sortie du réseau vers le client ");	
		liste=new ArrayList<Message>();
		configuration=NetworkConfiguration.WAITING;
		mC1=new Message();mH1=new Message();mS=new Message();msgSend=new Message();
	}
	@Override
	public void deltaInt() {
//		System.err.println(this.id_+" : "+this.name_+" .DeltaInt");
		if ((configuration==NetworkConfiguration.SENDING)){
			if(liste.isEmpty()){
				configuration=NetworkConfiguration.WAITING;
			}else 
				configuration=NetworkConfiguration.SENDING;	
		}
		else if((configuration==NetworkConfiguration.WAITING)){
			if(!liste.isEmpty()){
				configuration=NetworkConfiguration.SENDING;
			}	
		}
	}
	@Override
	public void deltaExt(double arg0) throws DEVS_Exception {
//		System.err.println(this.id_+" : "+this.name_+" .DeltaExt");
		if(configuration==NetworkConfiguration.WAITING){
			//msg from client to server
			mC1= (Message) getInputPortData(name_+".inputCN");
			if (mC1!=null){
				liste.add((Message) mC1);
				indicemsgC=liste.indexOf(mC1);
				configuration=NetworkConfiguration.SENDING;
			}
			//msg from hacker to server
			mH1=(Message) getInputPortData(name_+".inputHN");
			if (mH1!=null){
				liste.add((Message) mH1);indicemsgH=liste.indexOf(mH1);
				configuration=NetworkConfiguration.SENDING;
			}//msg from server to client
			mS=(Message) getInputPortData(name_+".inputNSC");
			if (mS!=null){
				liste.add((Message) mS);indicemsgS=liste.indexOf(mS);
				configuration=NetworkConfiguration.SENDING;
			}	
		}else if(configuration==NetworkConfiguration.SENDING){
			//msg from client to server
			mC1=(Message) getInputPortData(name_+".inputCN");
			if (mC1!=null){
				liste.add((Message) mC1);
				indicemsgC=liste.indexOf(mC1);
				configuration=NetworkConfiguration.SENDING;
			}
			//msg from hacker to server
			mH1=(Message) getInputPortData(name_+".inputHN"); 
			if (mH1!=null){
				liste.add((Message)mH1);indicemsgH=liste.indexOf(mH1);
				configuration=NetworkConfiguration.SENDING;
			}
			//msg from server to client
			mS=(Message) getInputPortData(name_+".inputNSC");
			if (mS!=null){
				liste.add((Message) mS);indicemsgS=liste.indexOf(mS);
				configuration=NetworkConfiguration.SENDING;
			}
		}
		else //configuration=NetworkConfiguration.SENDING;
			System.err.println("Error of message reception from client or hacker");
	}
	
	@Override
	public void lambda() throws DEVS_Exception {
//		System.err.println(this.id_+" : "+this.name_+" .lambda");
		if(configuration==NetworkConfiguration.SENDING){
			if ((mC1.getType()==MessageType.SYN)||(mC1.getType()==MessageType.ACK)){
				setOutputPortData(name_+".outputNCS",(Message)liste.get(indicemsgC));
			}
			else if(mH1.getType()==MessageType.SYN){
				setOutputPortData(name_+".outputNHS",(Message)liste.get(indicemsgH));	
			}		
			else if (mS.getType()==MessageType.SYN_ACK){
				setOutputPortData(name_+".outputNSC",(Message)liste.get(indicemsgS));
			}
						
		}else
			System.err.println("Error of message transmission to server or client");
	}
	public double latence(){
		return 0.5; // voir comment on peut améliorer le calcul de la latence du réseau
	}
	@Override
	public double ta() {
//		System.err.println(this.id_+" : "+this.name_+" .TimeAdvance");
		if((liste.isEmpty())||(configuration==NetworkConfiguration.WAITING))
			return DEVS_Real.POSITIVE_INFINITY;
		else
			return 0.1;//return 0.1;
	}

}
