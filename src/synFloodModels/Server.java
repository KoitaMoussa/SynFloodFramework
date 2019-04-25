package synFloodModels;

import interfaces.DefenseStrategy;
import interfaces.StrategyFirst;
import interfaces.StrategyFourth;
import interfaces.StrategySecond;
import interfaces.StrategyThird;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.sl.usermodel.Line;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import model.AtomicModel;
import SynFloodLib.Memory;
import SynFloodLib.Message;
import SynFloodLib.MessageType;
import SynFloodLib.ServerConfiguration;
import exception.DEVS_Exception;
/**
 * @author EL SOFT
 */
public class Server extends AtomicModel implements DefenseStrategy,StrategyFirst,StrategySecond,StrategyThird,StrategyFourth{
	int port;
	String address;
	Memory memory;
	int capacite;
	private int nb_demandes,
	demandes_bloque,
	nb_demandes_Client,
	nb_demandes_hacker,
	clientsCount,
	client_response,
	hacker_response,
	nb_reponse;
	private int[]Sequenceliste;
	double serverTimer;
	Random rand;
	HSSFWorkbook wb;int compteur;
	// créer un nouveau fichier
	FileOutputStream out; HSSFSheet sheet ;
	 Row titrerow;
	 Cell TitreCellTemps=null;
	 Cell TitreCellPLOSS=null;;
	 Cell TitreCellP_REG=null;
	 Cell TitreCellP_ATT=null;
	 Cell TitreCellFonction=null;
	ServerConfiguration configuration;Message m,msg;
	PrintStream dataTab;
	double PLOSS, P_REQ_REG,P_REQ_HAC,fonctionT1 = 0.0,fonctionT2 = 0 ,Timer=100.0;boolean drapeau = false;
	int temps=1,periode,index=1,colonne=1;
	public Server(String name,String desc,double Timer) throws FileNotFoundException {
		super(name,desc);
		addInputPortStructure(new Message(),name_+".inputNCS","entree du server");
		addInputPortStructure(new Message(),name_+".inputNHS","entree du server");
		addOutputPortStructure(new Message(),name_+".outputSN","sortie du server");
		addOutputPortStructure(new Message(),name_+".outputSC","port de sortie du Server vers le collecteur");
		serverTimer=Timer;
		memory=new Memory(capacite); 
		Sequenceliste = new int[capacite];
		configuration=ServerConfiguration.WAITING;
		nb_demandes=0;m=new Message();msg=new Message();rand=new Random();
		 compteur = 0;
		 periode=temps;
		 try {
			out= new FileOutputStream ("workbook.xls");
			  wb = new HSSFWorkbook();
		         sheet = wb.createSheet("RESULTAT");
			// créer une nouvelle feuille
//			 s = wb.createSheet("RESULTAT");
			
//			sheet.createRow(1).createCell(0).setCellValue("TIME");
			 titrerow=sheet.createRow(0);
			 titrerow.createCell(0,CellType.NUMERIC);
			 titrerow.createCell(1, CellType.NUMERIC);
			 titrerow.createCell(2,CellType.NUMERIC);
			 titrerow.createCell(3, CellType.NUMERIC);
			 titrerow.createCell(4, CellType.NUMERIC);
			 titrerow.getCell(0).setCellValue("Temps");
			 titrerow.getCell(1).setCellValue("P_Loss_Request");
			 titrerow.getCell(2).setCellValue("P_Regular_Request");
			 titrerow.getCell(3).setCellValue("P_Irregular_Request");
			 titrerow.getCell(4).setCellValue("Fonction F(t)");
//			 TitreCellTemps.setCellValue("Temps");
//			 TitreCellPLOSS.setCellValue("P_Loss_Request");
//			 TitreCellP_REG.setCellValue("P_Regular_Request");
//			 TitreCellP_ATT.setCellValue("P_Irregular_Request");
//			 TitreCellFonction.setCellValue("Fonction F(t)");
//			wb.write(out);
			// déclare une référence d'objet de index
//			@SuppressWarnings({ "rawtypes", "unused" })
//			Line r = null;
//			s.setRowBreak(6);
//		
//			// déclare une référence d'objet de cellule
//			@SuppressWarnings("unused")
//			Cell c = null;
//			// crée 3 styles de cellules
//			@SuppressWarnings("unused")
//			CellStyle cs = wb.createCellStyle ();
//			@SuppressWarnings("unused")
//			CellStyle cs2 = wb.createCellStyle ();
//			@SuppressWarnings("unused")
//			CellStyle cs3 = wb.createCellStyle ();
//			@SuppressWarnings("unused")
//			DataFormat df = wb.createDataFormat ();
			
			dataTab = new PrintStream("DATATAB"+".txt");
			String separateur=" || ";
			dataTab.println("TEMPS"+separateur+"P_LOSS"+separateur+"CLTS"+ separateur+"HACK"+ separateur+" F(t-1) "+ separateur+" F(t).");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void deltaInt() {
//		System.err.println(this.id_+" : "+this.name_+" .deltaInt");
		if((configuration==ServerConfiguration.APPROVING)){
//			memory.supprimerConnection(memory.getConnection()[0].getNumeroSequence());
			configuration=ServerConfiguration.WAITING;
		}else if (configuration==ServerConfiguration.DDOS){
//			memory=new Memory(capacite);
//			Sequenceliste = new int[capacite];
//			configuration=ServerConfiguration.WAITING;
		}
	}
	@SuppressWarnings("null")
	@Override
	public void deltaExt(double arg0) throws DEVS_Exception{
		//Début du deltaExt
		//message from legal client
		Message m=(Message)getInputPortData(name_+".inputNCS");
		//message from hacker
		Message msg=(Message)getInputPortData(name_+".inputNHS");
			if ((m.getType()==MessageType.SYN)&&(configuration==ServerConfiguration.WAITING)){
				nb_demandes_Client++;
				nb_demandes++;
				//Storing of Number sequences of messages
				if(Sequenceliste.length>nb_demandes){
					Sequenceliste[nb_demandes]=m.getNumeroSequence();
					m.setDateReception(new Date());
					int seq = m.getNumeroSequence();
					seq++;
					m.setNumeroSequence(seq);
				}//Suppression of old message
				if(nb_demandes<memory.getcapacite()){
					for(int i=0;i<memory.getNbMessage();i++){
						double t=memory.getConnection()[i].getTimer();
						memory.getConnection()[i].setTimer(t-arg0);
						if((memory.getConnection()[i].getTimer()<=0)&&(memory.getNbMessage()>0)) {
							int seq=memory.getConnection()[i].getNumeroSequence();
							memory.getConnection()[i].afficher();
							System.err.println("Message Deleted");
							memory.supprimerConnection(seq);
						}
					}m.setTimer(serverTimer);
					if(m.getType()!=null) memory.ajouterConnection(m);
				}configuration=ServerConfiguration.APPROVING;
			}else if ((m.getType()==MessageType.SYN)&&(configuration==ServerConfiguration.APPROVING)){
				nb_demandes_Client++;
				nb_demandes++;
				//Storing of Number sequences of messages
				if(Sequenceliste.length>nb_demandes){
					Sequenceliste[nb_demandes]=m.getNumeroSequence();
					m.setDateReception(new Date());
					int seq = m.getNumeroSequence();
					seq++;
					m.setNumeroSequence(seq);
				}//Suppression of old message
				if(nb_demandes<memory.getcapacite()){
					for(int i=0;i<memory.getNbMessage();i++){
						double t=memory.getConnection()[i].getTimer();
						memory.getConnection()[i].setTimer(t-arg0);
						if((memory.getConnection()[i].getTimer()<=0)&&(memory.getNbMessage()>0)) {
							int seq=memory.getConnection()[i].getNumeroSequence();
							memory.getConnection()[i].afficher();
							System.err.println("Message Deleted");
							memory.supprimerConnection(seq);
						}
					}m.setTimer(serverTimer);
					if(m.getType()!=null) memory.ajouterConnection(m);
				}configuration=ServerConfiguration.APPROVING;
			}else if ((configuration==ServerConfiguration.WAITING)&&(m.getType()==MessageType.ACK)){
				for(int i=0;i<Sequenceliste.length;i++){
					if(Sequenceliste[i]==m.getNumeroSequence()){
						clientsCount++;
						memory.supprimerConnection(m.getNumeroSequence());
						Sequenceliste[i]=(Integer) null;
					}	
				}
				configuration=ServerConfiguration.WAITING;
			}else if ((configuration==ServerConfiguration.APPROVING)&&(m.getType()==MessageType.ACK)){
				for(int i=0;i<Sequenceliste.length;i++){
					if(Sequenceliste[i]==m.getNumeroSequence()){
						clientsCount++;
						memory.supprimerConnection(m.getNumeroSequence());
						Sequenceliste[i]=(Integer) null;
					}	
				}configuration=ServerConfiguration.APPROVING;
			}
			if((msg.getType()==MessageType.SYN)&&(configuration==ServerConfiguration.WAITING)){
				nb_demandes_hacker++;
				nb_demandes++;
				configuration=ServerConfiguration.APPROVING;
				//Storing of Number sequences of messages
				if(Sequenceliste.length>nb_demandes){
					Sequenceliste[nb_demandes]=msg.getNumeroSequence();
					msg.setDateReception(new Date());
					int seq = msg.getNumeroSequence();
					seq++;
					msg.setNumeroSequence(seq);
				}//Suppression of old message
				if(nb_demandes<memory.getcapacite()){
					for(int i=0;i<memory.getNbMessage();i++){
						double t=memory.getConnection()[i].getTimer();
						memory.getConnection()[i].setTimer(t-arg0);
						if(memory.getConnection()[i].getTimer()<=0){
							memory.supprimerConnection(memory.getConnection()[i].getNumeroSequence());
						}
					}msg.setTimer(serverTimer);
					if(msg.getType()!=null)memory.ajouterConnection(msg);	
				}
				configuration=ServerConfiguration.APPROVING;
			}else if((msg.getType()==MessageType.SYN)&&(configuration==ServerConfiguration.APPROVING)){
				nb_demandes_hacker++;
				nb_demandes++;
				configuration=ServerConfiguration.APPROVING;
				//Storing of Number sequences of messages
				if(Sequenceliste.length>nb_demandes){
					Sequenceliste[nb_demandes]=msg.getNumeroSequence();
					msg.setDateReception(new Date());
					int seq = msg.getNumeroSequence();
					seq++;
					msg.setNumeroSequence(seq);
				}//Suppression of old message
				if(nb_demandes<memory.getcapacite()){
					for(int i=0;i<memory.getNbMessage();i++){
						double t=memory.getConnection()[i].getTimer();
						memory.getConnection()[i].setTimer(t-arg0);
						if(memory.getConnection()[i].getTimer()<=0){
							memory.supprimerConnection(memory.getConnection()[i].getNumeroSequence());
						}
					}msg.setTimer(serverTimer);
					if(msg.getType()!=null)memory.ajouterConnection(msg);	
				}
//				configuration=ServerConfiguration.APPROVING;
			}
			if((memory.Isfull())&&(Sequenceliste.length <= nb_demandes)){
				System.out.println(" Memory is full at time : "+this.getSimulator().getTL());
				demandes_bloque++;
				configuration=ServerConfiguration.DDOS;
			}
			//Fin du deltaExt
			
			PLOSS = getDemandes_bloque()*100.0/getNb_demandes();
			P_REQ_REG=getNb_demandes_Client()*100.0/getNb_demandes();
			P_REQ_HAC=getNb_demandes_hacker()*100.0/getNb_demandes();
			
			String temp=String.valueOf(this.getSimulator().getTL());
			//Arrondisement du temps
			double tn=this.getSimulator().getTL();
			
			if(tn<10){
				 temps = Integer.parseInt(temp.substring(0,1));	
			}else if((tn>=10)&&(tn<100)){
				 temps = Integer.parseInt(temp.substring(0,2));	
			}else if((tn>=100)&&(tn<1000)){
			temps = Integer.parseInt(temp.substring(0,3));	
			}else if((tn>=1000)&&(tn<10000)){
				 temps = Integer.parseInt(temp.substring(0,4));	
			}//Arrondissement de PLOSS
			double plosss = 0;
			if(PLOSS<10){
				 plosss = Double.parseDouble(String.valueOf(PLOSS).substring(0,3));
			}else if((PLOSS>=10)&&(PLOSS<100)){
				 plosss =Double.parseDouble(String.valueOf(PLOSS).substring(0, 4)); 
			}else if((PLOSS>=100)&&(PLOSS<1000)){
				 plosss =Double.parseDouble(String.valueOf(PLOSS).substring(0, 5));
			}
			//Arrondissement de P_REQ_REG
			double P_REG = 0;
			if(P_REQ_REG<10){
				P_REG = Double.parseDouble(String.valueOf(P_REQ_REG).substring(0, 3));
			}else if((P_REQ_REG>=10)&&(P_REQ_REG<100)){
				P_REG =Double.parseDouble(String.valueOf(P_REQ_REG).substring(0, 4)); 
			}else if((P_REQ_REG>=100)&&(P_REQ_REG<1000)){
				P_REG =Double.parseDouble(String.valueOf(P_REQ_REG).substring(0, 5));
			}
			double P_HACK = 0;
			//Arrondissement de P_REQ_HACK
			if(P_REQ_HAC<10){
				P_HACK = Double.parseDouble(String.valueOf(P_REQ_HAC).substring(0, 3));	
			}else if((P_REQ_HAC>=10)&&(P_REQ_HAC<100)){
				P_HACK =Double.parseDouble(String.valueOf(P_REQ_HAC).substring(0, 4)); 
			}else if((P_REQ_HAC>=100)&&(P_REQ_HAC<1000)){
				P_HACK =Double.parseDouble(String.valueOf(P_REQ_HAC).substring(0, 5));
			}
			//faire un compteur supplementary et condition supplementary pour
			//savoir si le temps de prélèvement a augmenter ou non
			if(periode==temps){
				drapeau=true;
			}//periode
				if((temps%1==0)&&(drapeau==true)&&(temps==periode)){
					periode+=1;
					drapeau=false;
					//calcul des fonctions de defenses
						fonctionT2 = P_REQ_REG/(P_REQ_HAC*PLOSS);
//						dataTab.println("  "+temps+"  |  "+plosss+"  |  "+P_REG+"  |  "+P_HACK);
					try {
						dataTab.println("  "+temps+"  |  "+plosss+"  |  "+P_REG+"  |  "+P_HACK+"  |  "+fonctionT1+"  |  "+fonctionT2);
					
//					sheet.createRow(index).createCell(colonne++).setCellValue(temps);//colonne++;
//					sheet.createRow(index).createCell(colonne++).setCellValue(temps);//colonne++;
//					sheet.createRow(index).createCell(colonne++).setCellValue(temps);//colonne++;
//					sheet.createRow(index).createCell(colonne++).setCellValue(temps);//colonne++;
//					sheet.createRow(index).createCell(colonne++).setCellValue(temps);colonne=1;
					Row row=sheet.createRow(index++);
					colonne=0;
//					 titrerow=sheet.createRow(0);
					 row.createCell(colonne,CellType.NUMERIC);
					 row.getCell(colonne).setCellValue(temps);
					 colonne++;
					 row.createCell(colonne, CellType.NUMERIC);
					 row.getCell(colonne).setCellValue(plosss);
					 colonne++;
					 row.createCell(colonne,CellType.NUMERIC);
					 row.getCell(colonne).setCellValue(P_REG);
					 colonne++;
					 row.createCell(colonne, CellType.NUMERIC);
					 row.getCell(colonne).setCellValue(P_HACK);
					 colonne++;
//					 row.createCell(colonne, CellType.NUMERIC);
//					 row.getCell(colonne++).setCellValue(fonctionT2);
					
//					titrerow=sheet.getRow(index++);
//					TitreCellTemps.setCellValue(temps);
//					TitreCellPLOSS.setCellValue(plosss);
//					TitreCellP_REG.setCellValue(P_REG);
//					TitreCellP_ATT.setCellValue(P_HACK);
//					TitreCellFonction.setCellValue(fonctionT2);
//					s.createRow(index).createCell(colonne).setCellValue(temps);
//					colonne++;
//					s.createRow(index).createCell(colonne).setCellValue(PLOSS);
//					colonne++;
//					s.createRow(index).createCell(colonne).setCellValue(P_REG);
//					colonne++;
//					s.createRow(index).createCell(colonne).setCellValue(P_HACK);
//					colonne++;
//					s.createRow(index).createCell(colonne).setCellValue(fonctionT2);
//					colonne=1;
//					index++;
						wb.write(out);
					} catch (IOException e) {
						e.printStackTrace();
					}
//					 	int Taille=memory.getcapacite()+1024;
//						System.err.println("Instant T-1 Ancienne valeur F(t-1) : "+fonctionT1+" / Nouvelle Valeur F(t) : "+fonctionT2);
//						if(fonctionT1<=fonctionT2){
//							this.IncreaseMemory(Taille);
//							this.IncreaseTimer(Timer);
//							fonctionT1=fonctionT2;
//						}else if(fonctionT1>fonctionT2){
//							Timer=50;
//							Taille=memory.getcapacite()-64;
//							this.DecreaseMemory(Taille);
//							this.DecreaseTimer(Timer);
//							fonctionT1=fonctionT2;
//						}
					fonctionT1=fonctionT2;
					  File file = new File("C:/resultat/synfloodData.xls");
				        file.getParentFile().mkdirs();
				 
				        FileOutputStream outFile = null;
						try {
							outFile = new FileOutputStream(file);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				        try {
							wb.write(outFile);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        System.out.println("Created file: " + file.getAbsolutePath());
					}
//				System.err.println("Instant T-1 Ancienne valeur F(t-1) : "+fonctionT1+" / Nouvelle Valeur F(t) : "+fonctionT2);
						compteur++;
					System.out.println("compteur : "+compteur);
			System.err.println(" Time last : "+this.getSimulator().getTL()+" : Nombre de client Connecté : "+clientsCount);
			System.err.println(" Demande totale des SYN clients : "+nb_demandes_Client);
			System.err.println(" Demande totale des SYN hackers : "+nb_demandes_hacker);
			System.err.println(" Semi-connexion total des clients : "+(client_response));
			System.err.println(" Semi-connexion total des hackers : "+(hacker_response));
			System.err.println(" Demande totale bloquées : "+demandes_bloque);
			System.err.println(" Demande totale clients et hackers : "+nb_demandes);
			System.err.println(" Nombre total de Message SYN_ACK envoyés par le Server : "+nb_reponse);
			System.err.println(" Probabilite de requetes perdues : "+PLOSS+" % ");
			System.err.println(" Probabilité d'occupation Memoire par des requetes de Clients regulières : "+P_REQ_REG+" % ");
			System.err.println(" Probabilité d'occupation Memoire par des requetes de Hackers : "+P_REQ_HAC+" % ");
			System.err.println(" Probabilité de SYN_ACK envoyés au total(Clients + Hackers) : "+100.0*getNb_reponse()/getNb_demandes()+" % ");
			System.err.println(" Probabilité des clients connectés : "+100.0*getClientsCount()/getNb_demandes()+" % ");
	}
	@Override
	public void lambda() throws DEVS_Exception {
//		System.err.println(this.id_+" : "+this.name_+" .lambda");
		if(configuration==ServerConfiguration.APPROVING){
			if((memory.getConnection().length>0)/**||(!memory.Isfull())*/){
				m=memory.getConnection()[0];
				nb_reponse++;
//				memory.afficher();
				if(m.getSource()==this.address){
					m.setSource(getAddress());
					m.setDestination(null);
					hacker_response++;
				}else if(m.getSource()!=this.address) {
					String t=m.getSource();
					m.setSource(this.getAddress());
					m.setDestination(t);
					client_response++;
				}
				m.setType(MessageType.SYN_ACK);
				setOutputPortData(name_+".outputSN",m);
				setOutputPortData(name_+".outputSC",m);
			}
		}
	}
	@Override
	public double ta() {
//		System.err.println(this.id_+" : "+this.name_+" .TimeAdvance");
		double time=0.0;
		if(configuration==ServerConfiguration.APPROVING)
			time=0.0001;//time=0.1;
		else if(configuration==ServerConfiguration.DDOS)
			time=rand.nextDouble()*100;//DEVS_Real.POSITIVE_INFINITY;
		else if(configuration==ServerConfiguration.WAITING)
			time=serverTimer;
			return time;
	}
	public void setSequenceliste(int[] sequenceliste) {
		Sequenceliste = sequenceliste;
	}
	public void Statistique(){
		double PLOSS, P_REQ_REG,P_REQ_HAC;
		PLOSS = this.getDemandes_bloque()*100.0/this.getNb_demandes();
		P_REQ_REG=this.getNb_demandes_Client()*100.0/this.getNb_demandes();
		P_REQ_HAC=this.getNb_demandes_hacker()*100.0/this.getNb_demandes();
		System.err.println(" Time last : "+this.getSimulator().getTL()+"--- Nombre de client Connecté : "+clientsCount);
		System.err.println(" Demande totale des SYN clients : "+nb_demandes_Client);
		System.err.println(" Demande totale des SYN hackers : "+(nb_demandes-nb_demandes_Client));
		System.err.println(" Semi-connexion total des clients : "+(nb_reponse-hacker_response));
		System.err.println(" Semi-connexion total des hackers : "+(hacker_response));
		System.err.println(" Demande totale bloquées : "+demandes_bloque);
		System.err.println(" Demande totale clients et hackers : "+nb_demandes);
		System.err.println(" Nombre total de Message SYN_ACK envoyés par le Server : "+nb_reponse);
		System.err.println(" Probabilite de requetes perdues : "+PLOSS+" % ");
		System.err.println(" Probabilité d'occupation Memoire par des requetes de Clients regulières : "+P_REQ_REG+" % ");
		System.err.println(" Probabilité d'occupation Memoire par des requetes de Hackers : "+P_REQ_HAC+" % ");
		System.err.println(" Probabilité de SYN_ACK envoyés au total(Clients + Hackers) : "+100.0*getNb_reponse()/getNb_demandes()+" % ");
		System.err.println(" Probabilité des clients connectés : "+100.0*getClientsCount()/getNb_demandes()+" % ");
	}
	public void incrementer(double reel){
		reel+=0.1;
	}
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public int getNb_demandes() {
		return nb_demandes;
	}

	public void setNb_demandes(int nb_demandes) {
		this.nb_demandes = nb_demandes;
	}

	public ServerConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ServerConfiguration configuration) {
		this.configuration = configuration;
	}
	public int getDemandes_bloque() {
		return demandes_bloque;
	}
	public int getNb_demandes_Client() {
		return nb_demandes_Client;
	}
	public int getNb_demandes_hacker() {
		return nb_demandes_hacker;
	}
	public int getClientsCount() {
		return clientsCount;
	}
	public int getNb_reponse() {
		return nb_reponse;
	}
	public int[] getSequenceliste() {
		return Sequenceliste;
	}
	
	public int getClient_response() {
		return client_response;
	}
	public int getHacker_response() {
		return hacker_response;
	}
	@Override
	public void DecreaseMemory(int Taille) {
		if(Taille>=100)
			this.getMemory().modifierTailleMemoire(Taille);
	}
	@Override
	public void DecreaseTimer(double Timer) {
		if(Timer>=1)
			this.getMemory().setTimer(Timer);
	}
	@Override
	public void IncreaseMemory(int Taille) {
		getMemory().modifierTailleMemoire(Taille);
	}
	@Override
	public void IncreaseTimer(double Timer) {
		getMemory().setTimer(Timer);
	}

}
