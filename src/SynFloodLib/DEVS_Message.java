package SynFloodLib;
import types.DEVS_Type;
public class DEVS_Message extends DEVS_Type {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Message value;// = new Message(MessageType.SYN, "src", "dest", new Date(), 0, 0);

	public DEVS_Message() {

	}
	public DEVS_Message(Message s){
		value=s;
	}

	@Override
	public boolean equals(DEVS_Type arg0) {
		// TODO Auto-generated method stub
		if (((Message )arg0.
				getValue()).
				getNumeroSequence()
				==
				this
				.value.
				getNumeroSequence())
		return true;
		else return false;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object arg0) {
		value=(Message)arg0;

	}

	@Override
	public String toString() {		
		return this.
				value.
				getSource()+ " "+
				this.value.getDestination()+" "+
				this.value.getPortSource()+" "+
				this.value.getPortDestination()+" "+
				this.getValue();
	}

}
