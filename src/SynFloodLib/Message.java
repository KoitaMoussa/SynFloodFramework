package SynFloodLib;

import java.util.Date;

public class Message  extends DEVS_Message{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int compteur=0;
	private MessageType type;
	private int numero_sequence=0; 
	private String source;
	private String destination; 
	private Date dateEmission;
	private Date dateReception;
	private int portSource;
	private int portDestination;
	private double timer;
	@SuppressWarnings("unused")
	private Object value;
	public Message(){//constructeur par defaut
		type= MessageType.SYN;
		numero_sequence=++compteur;
		source="";
		destination="";
		dateEmission= new Date();
		portSource=0;
		portDestination=0;
		timer=0;
	}
	public Message(MessageType t, String src, String dest, Date dat,int psrc, int pdest){
	    type=t;
	    numero_sequence=++compteur;
	    source=src;
	    destination=dest;
	    dateEmission=dat;
	    portSource=psrc;
	    portDestination=pdest;
	    timer=0;
	}
//	public Message(Object m){
//		value=m;
//	}
    public void setType(MessageType t) {
    	type=t;
    }
    public void setNumeroSequence(int d){
    	numero_sequence=d;
    }
    public void setSource(String src){
    	source=src; 
    }
    public void setDestination(String dest){
    	destination=dest;
    }
    public void setDateEmission(Date dat){
    	dateEmission=dat;
    }
    public void setDateReception(Date dat){
    	dateReception=dat;
    }
    public void setPortSource(int psrc){
    	portSource=psrc;
    }
    public void setPortDestination(int pdest){
    	portDestination=pdest;
    }
    public void setTimer(double t){
    	timer=t;
    }
    public MessageType getType(){
    	return type;
    }
    public int getNumeroSequence(){
    	return numero_sequence;
    }
    public String getSource()
    {
    	return source;
    }
    public String getDestination()
    {
    	return destination;
    }
    public Date getDateEmission()
    {
    	return dateEmission;
    }
    public Date getDateReception()
    {
    	return dateReception;
    }
    public int getPortSource()
    {
    	return portSource;
    }
    public int getPortDestination()
    {
    	return portDestination;
    }
    public double getTimer(){
    	return timer;
    }
    public void afficher()
    {
    	System.err.println("type:"+getType());
    	System.err.println("numero de sequence:"+getNumeroSequence());
    	System.err.println("date:"+getDateEmission());
    	System.err.println("source:"+getSource());
    	System.err.println("destination:"+getDestination());
    	System.err.println("port Source:"+getPortSource());
    	System.err.println("port Destinaton:"+getPortDestination());
    	System.err.println("TIMER:"+getTimer());
    }
    }
