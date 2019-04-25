//package testSynFlooding;
////import java.util.ArrayList;
//import java.awt.Label;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.Random;
//
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.format.CellFormat;
//import org.apache.poi.ss.format.CellFormatResult;
//import org.apache.poi.ss.usermodel.Font;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
////import SynFloodLib.Memory;
////import SynFloodLib.Message;
////import SynFloodLib.MessageType;
////import synFloodModels.Server;
//
//public class Test {
//	
//	public static void main(String[] args) throws IOException {
//		@SuppressWarnings("unused")
//		Random r=new Random();
//		OutputStream out=new FileOutputStream("Fichier.xls");
//		 HSSFWorkbook wb = new HSSFWorkbook();
//	        HSSFSheet s = wb.createSheet("OSGi");
//	      
//	        for(int i=0;i<15;i++){
//	        	for(int f=0;f<20;f++){
//	        		s.createRow(i).createCell(0).setCellValue(f+" With OSGi "+i);
//	        		s.createRow(i).createCell(1).setCellValue(f+" With OSGi "+i);
//	        		s.createRow(i).createCell(2).setCellValue(f+" With OSGi "+i);
//	        	}
//	        	
//	        }
//	        wb.write(out);
////	        s.createRow(0).createCell(0).setCellValue("With OSGi");
////	        s.createRow(1).createCell(1).setCellValue(12.0);
//	       
////	        assertEquals("With OSGi", s.getRow(0).getCell(0).toString());
//
//	 
////		Message m1;
////		m1= new Message();
//////		m1.afficher();
////		m1.setType(MessageType.ACK);
////		m1.setNumeroSequence(1);
////		m1.setSource("client1");
////		m1.setDestination("serveur");
////		m1.setPortSource(1088);
////		m1.setPortDestination(86);
//////		m1.afficher();
////		Message m2;
////		m2= new Message(MessageType.ACK, "client2", "Serveur", new Date(), 1828, 2375);
//////		m2.afficher();
////		Memory memoire;
////		memoire=new Memory(20);
////		memoire.ajouterConnection(m1);
////		memoire.ajouterConnection(m2);
//////		System.err.println("Nombre de Message:"+memoire.getNbMessage()+". Capacite : "+memoire.getcapacite()+" Memoire pleine :  "+memoire.Isfull());
//////		memoire.afficher();
//////		memoire.supprimerConnection(2);
//////		System.err.println("Nombre de Message:"+memoire.getNbMessage()+". Capacite : "+memoire.getcapacite()+" Memoire pleine :  "+memoire.Isfull());
////		ArrayList<Message>msg=new ArrayList<Message>();
////		msg.add(m1);msg.add(m2);msg.add(new Message(MessageType.SYN, "client1", "Serveur", new Date(), 1, 2));
//////		memoire.setTimer(1000);
////		memoire.afficher(); 
////		System.err.println("********************************************************CAPACITE : "+memoire.getConnection().length);
////		memoire.modifierTailleMemoire(1);
////		System.err.println("********************************************************CAPACITE : "+memoire.getConnection().length);
////		memoire.afficher();
////		System.out.println("Statut de la memoire : "+	memoire.Isfull());
////		int indexm1,indexm2,indexm3;
////		indexm1=msg.indexOf(m1);
////		indexm2=msg.indexOf(m2);
////		indexm3=msg.indexOf(null);
////		System.err.println("Message content: "+msg+"; index m1: "+indexm1+" ; index m2 : "+indexm2+"; index m3 : "+indexm3);
////		for (int k=0;k<msg.size();k++){
////			System.err.println(k+"--> msg : "+msg.get(k).getType()+" indexOfmsg :"+msg.indexOf(msg.get(k)));
////		}
////			int nbreAuSort; 
////			for (int i=1; i<=20; i++){ 
////			nbreAuSort = (int) (Math.random()*200+1); 
////			System.out.println(nbreAuSort); 
////			} 
////			System.err.println("*******************************************************************");
////			int Nb = 6;
////			Random rand = new Random();
////			for(int j=0;j<Nb;j++)
////			{ 
////			int i = rand.nextInt(49)+1;
////			System.out.println(+i+"\t"); 
//////			}
////		double periode;periode=5.0;
////		for(double i=0;i<=50.0;i++){
////			if(i%periode==0){
////				System.err.println("La variable i = "+i);
////			}
////		}
////		int []tab1, tab2,tab3;
////		tab1=new int[10];
////		tab2= new int [30];
////		tab3= new int[20];
////		for(int i=0;i<10; i++){
////			tab1[i]=i;
////			System.err.println("---"+tab1[i]);
////		}		tab1=new int[30];
////		
////		for(int i=0;i<20;i++){
////			tab1[i]=i;
////			System.err.println(tab1[i]);
////		}
//		
//		
//		
//		
//		}
//}
