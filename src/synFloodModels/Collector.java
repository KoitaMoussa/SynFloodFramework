package synFloodModels;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import model.AtomicModel;
import types.DEVS_Real;
import SynFloodLib.Message;
import exception.DEVS_Exception;

public class Collector extends AtomicModel{
	PrintStream trajectory;
	public Collector(String name, String desc) {
		super(name, desc);
		addInputPortStructure(new Message(), name_+".inputCC", "Store for data from model Client");
		addInputPortStructure(new Message(), name_+".inputHC", "Store for data from model Hacker");
		addInputPortStructure(new Message(), name_+".inputSC", "Store for data from model Server");
		// State initialization: the name of the file is xxx.txt if the name of the model is xxx
		try {
			trajectory = new PrintStream("SynFLood"+name+".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deltaExt(double arg0) throws DEVS_Exception {
		// simulation time : data received
		double when=1.000;
		when = this.getSimulator().getTL();
		String whenn =null;
		if(when<10){
			 whenn=String.valueOf(when).substring(0, 3);
		}else if((when>=10)&&(when<100)){
			 whenn=String.valueOf(when).substring(0, 4);
		}else if((when>=100)&&(when<1000)){
			 whenn=String.valueOf(when).substring(0, 5);
		}else if((when>=1000)&&(when<10000)){
			 whenn=String.valueOf(when).substring(0, 6);
		}
		// Then store them in the following shape:
		Message receivedC1 = (Message) getInputPortData(name_+".inputCC");
//		trajectory.println(whenn+ " --> " +receivedC1.getTimer()+" : "+ receivedC1.getType()+" : "+receivedC1.getSource()+" : "+receivedC1.getDestination()+" : "+receivedC1.getNumeroSequence());
		Message receivedH1 = (Message) getInputPortData(name_+".inputHC");
//		trajectory.println(whenn + " --> " +receivedH1.getTimer()+" : "+ receivedH1.getType()+" : "+receivedH1.getSource()+" : "+receivedH1.getDestination()+" : "+receivedH1.getNumeroSequence());
		Message receivedS = (Message) getInputPortData(name_+".inputSC");
//		trajectory.println(whenn + " --> " +receivedS.getTimer()+" : "+ receivedS.getType()+" : "+receivedS.getSource()+" : "+receivedS.getDestination()+" : "+receivedS.getNumeroSequence());
		if((receivedC1.getDestination()!=null)||(receivedC1.getSource()!=null)){
			trajectory.println(whenn+ " :::> " +receivedC1.getTimer()+" : "+ receivedC1.getType()+" : "+receivedC1.getSource()+" : "+receivedC1.getDestination()+" : "+receivedC1.getNumeroSequence());	
			receivedC1=new Message();
		}
		else 
		if((receivedH1.getDestination()!=null)||(receivedH1.getSource()==null)){
			trajectory.println(whenn + " :::> " +receivedH1.getTimer()+" : "+ receivedH1.getType()+" : "+receivedH1.getSource()+" : "+receivedH1.getDestination()+" : "+receivedH1.getNumeroSequence());		
			receivedH1=new Message();
		}
		else
		if((receivedS.getSource()!=null)){
			trajectory.println(whenn + " :::> " +receivedS.getTimer()+" : "+ receivedS.getType()+" : "+receivedS.getSource()+" : "+receivedS.getDestination()+" : "+receivedS.getNumeroSequence());	
			receivedS=new Message();
		}
	}

	@Override
	public void deltaInt() {
//		System.err.println(this.id_+" : "+this.name_+" .DeltaInt");
		// TODO Auto-generated method stub
		//undefined
	}

	@Override
	public void lambda() throws DEVS_Exception {
//		System.err.println(this.id_+" : "+this.name_+" .Lambda");
		// TODO Auto-generated method stub
		//undefined
	}

	@Override
	public double ta() {
//		System.err.println(this.id_+" : "+this.name_+" .TimeAdvance");
		return DEVS_Real.POSITIVE_INFINITY;
	}

}
