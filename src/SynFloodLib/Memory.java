package SynFloodLib;

public class Memory {
	private Message[] connection;
	private int capacité;
	private int nb_message;
	@SuppressWarnings("unused")
	private boolean full;
	public Memory(int cap){
		capacité= cap;
		connection= new Message[capacité];
		nb_message=0;
		full=false;
	}
	public Message[] getConnection(){
		return connection;
	}
	public int getcapacite(){
		return capacité;
	}
	public void ajouterConnection(Message m){
		if((nb_message>=0)&&(nb_message<capacité)){
			connection[nb_message]=m;
			nb_message++;
		}
		else full=true;
	}
	public boolean Isfull(){
		if(nb_message==capacité)
			return true;
		return false;
		
	}
	public void supprimerConnection(int n){
		int position = 0;
		for(int i=0; i<nb_message; i++){
			if(connection[i].getNumeroSequence()==n){
				position=i;
				break;
			}
		}
		for(int j=position; j<nb_message-1; j++){
	     connection[j]=connection[j+1];
		}
		nb_message--;
	}
	public void afficher(){
		for (int i=0; i<nb_message; i++)
			connection[i].afficher();
	}
	public int getNbMessage(){
		return nb_message;
	}
	public void setTimer(double timer){
		if(this.getNbMessage()>1){
			for(int i=0;i<this.getNbMessage();i++){
				connection[i].setTimer(timer);
			}
		}
	}
	public void modifierTailleMemoire(int taille){
		//Creation d'un tableau temporaire
		Message [] tabTempo = new Message[getConnection().length];
		//Copie des valeurs de la memoire dans le tableau temporaire
		for(int i=0;i<getConnection().length;i++){
			tabTempo[i]=getConnection()[i];
		}
		if(taille>0){
			capacité=taille;	
			connection = new Message[taille];	
		}
		//Copie des valeurs du tableau temporaire dans la memoire
		if(tabTempo.length<connection.length){
			for (int i=0;i<tabTempo.length;i++){
				connection[i]=tabTempo[i];
			}
		}else if(tabTempo.length>=connection.length){
//			int tailleTemp;
//			 tailleTemp=tabTempo.length;
//			 System.err.println(" taille tempo : "+tailleTemp);
			for(int i=0;i<connection.length;i++){
					 connection[i]=tabTempo[i];	 				
			}
		}
	}
}
