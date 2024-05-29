package com.example.swt.widgets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;




public class FISCALDIFF {

	
	//DICHIARAZIONE VARIABILI
	static String tmp;
	static String result;
	static String sqlinsfisiva;
	static String cfg[] = new String[100];
	public static String dbengine="";
	public static String dbHostName;
	public static String dbPort;
	public static String dbUser;
	public static String dbPasswd;
	public static String dbDatabase;
	public static String CodiceNegozio="0000";
	public static String CdaLenght="0";
	public static int CdaInt=13;
	public static int i=0; 
	public static int il=0;
	public static int lines=0;
	public static int count=0;
	public static String recordarray[];
	public static int tot=0; 
	
	public static final Display display = new Display();
	public static final Shell shell = new Shell(display);
	
	static final Button cb[]=new Button[1000];
	static final Label negcod[]=new Label[1000];
	static final Label negnpo[]=new Label[1000];
	static final Label negzre[]=new Label[1000];
	static final Label negdat[]=new Label[1000];
	static final Label negsco[]=new Label[1000];
	static final Label negven[]=new Label[1000];
	static final Label negfis[]=new Label[1000];
	static final Label negdif[]=new Label[1000];
	static final Label negfisiva[]=new Label[1000];
	static final Label negdifiva[]=new Label[1000];
	static final Button nextday[]=new Button[1000];
	static final Button fiscaliva[]=new Button[1000];
	
	static String day =new String(); 
	static String month =new String();
	static String year =new String();
	static String datafis= new String();
	
	
	
	static Button confermadata;
	public static Label dataconfermata;
	static ProgressBar progressBar1;
	
	static GridLayout grid;
	static GridLayout grid2;
	static Group first;
	static Group second;
	
    static ScrolledComposite firstScroll;
    static Composite firstContent;
    static Composite secondContent;
    static GridData firstData;
    static GridData secondData;
    
    
    public static DateTime datainizio;
    
    static SimpleDateFormat formatter;  
    static Date date; 
    static Date datelog; 
    static PrintStream fileStream;
    
    static ArrayList<String> neg;
    static Button tuttibutton;
    
	public static void main(String[] args) {
		
		//RECUPRO GRAFICA WINDOWS
		  try
	        {UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
	        catch (ClassNotFoundException e1)
	        {e1.printStackTrace();}
	        catch (InstantiationException e1)
	        {e1.printStackTrace();}
	        catch (IllegalAccessException e1)
	        {e1.printStackTrace();}
	        catch (UnsupportedLookAndFeelException e1)
	        {e1.printStackTrace();}
				
		// CONFIGURAZIONE LAYOUT
		shell.setText("FISCAL DIFF");
		shell.setLayout(new GridLayout(2, false));
		//final GridLayout 
		grid= new GridLayout();
		grid.numColumns = 13;
		//GridLayout 
		grid2= new GridLayout();
		grid2.numColumns = 2;
		
		//DICHIARAZIONI CONTENITORI LAYOUT
		//final Group 
		first = new Group(shell, SWT.NONE);
	    first.setText("NEGOZI");
	    first.setLayout(new GridLayout(1, false));
	    //GridData 
	    firstData = new GridData(SWT.FILL, SWT.FILL, true, false);
	    firstData.heightHint = 400;
	    firstData.widthHint = 1000;
	    first.setLayoutData(firstData);
	    //final ScrolledComposite 
	    firstScroll = new ScrolledComposite(first, SWT.V_SCROLL);
	    firstScroll.setLayout(new GridLayout(1, false));
	    firstScroll.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    //final Composite 
	    firstContent = new Composite(firstScroll, SWT.NONE);
	    firstContent.setLayout(new GridLayout());
	    firstContent.setLayout(grid);
		//Group 
	    second = new Group(shell, SWT.NONE);
	    //GridData 
	    secondData = new GridData(SWT.FILL, SWT.FILL, true, false);
	    secondData.heightHint = 400;
	    secondData.widthHint = 500;
	    second.setText("SCELTA RAPIDA");
	    second.setLayout(new GridLayout(1, false));
	    second.setLayoutData(secondData);
		//Composite 
	    secondContent = new Composite(second, SWT.NONE);
	    secondContent.setLayout(new GridLayout());
	    secondContent.setLayout(grid2);
  	   	    
		//ArrayList<String> 
		neg= new ArrayList<String>();
		

		//PREPARAZIONE SCRITTURA LOG SU FILE
		//SimpleDateFormat 
		formatter = new SimpleDateFormat("ddMMyyyy");  
	    //Date 
	    date = new Date(); 
	    //Date 
	    datelog = new Date(); 
	    //PrintStream fileStream;
	    try {
			System.setOut(new PrintStream(new FileOutputStream("Fiscaldiff-"+formatter.format(date)+".log", true)));
	 	} catch (FileNotFoundException e3) {
			e3.printStackTrace();
		}
		
		// INIZIALIZZAZIONE LOG 
		System.out.println(" ");
		System.out.println("**********************************");
		System.out.println("Esecuzione FISCALDIFF del: "+datelog);
		System.out.println("**********************************");
		
		// LETTURA FILE CFG PER RECUPERARE INFO CONNESSIONE AL DB
		cfgread();
	
		
		//INTESTAZIONE COLONNE
		Label sel=new Label(firstContent, SWT.NONE);
		sel.setText("SEL");
		Label negz=new Label(firstContent, SWT.NONE);
		negz.setText("NEGOZIO");
		Label npo=new Label(firstContent, SWT.NONE);
		npo.setText("CASSA");
		Label zre=new Label(firstContent, SWT.NONE);
		zre.setText("AZZERAMENTO");
		Label dat=new Label(firstContent, SWT.NONE);
		dat.setText("DATA");
		Label sco=new Label(firstContent, SWT.NONE);
		sco.setText("DIFF NUM SCO");
	    Label cont=new Label(firstContent, SWT.NONE);
		cont.setText("CONTABILE");
		Label fis=new Label(firstContent, SWT.NONE);
		fis.setText("FISCALE");
		Label diff=new Label(firstContent, SWT.NONE);
		diff.setText("DIFFERENZA");
		Label fisiva=new Label(firstContent, SWT.NONE);
		fisiva.setText("FISCALE IVA");
		Label diffiva=new Label(firstContent, SWT.NONE);
		diffiva.setText("DIFF IVA");
		Label next=new Label(firstContent, SWT.NONE);
		next.setText("NEXT-DAY");
		Label iva=new Label(firstContent, SWT.NONE);
		iva.setText("FISCAL IVA");
		
		//DEFINISCO LABEL E CHECKBOX TUTTO
	    Label tuttinegozi= new Label(secondContent, SWT.NONE);
	    tuttinegozi.setText("Allinea TUTTO");
		final Button tutti =new Button(secondContent, SWT.CHECK);
		
		//ASSOCIO AZIONE AL CHECKBOX TUTTI I NEGOZI
		tutti.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e2) {
						if (tutti.getSelection()) {
							System.out.println("tot1 "+tot);
							int i=1;
							while (tot>=i)
							{
								cb[i].setSelection(true); 
								fiscaliva[i].setSelection(true);
								i++;
							}
						}
						else {
							int i=1;
							while (tot>=i)
							{
								cb[i].setSelection(false); //SE FALSE IMPOSTO TUTTI A FALSE
								fiscaliva[i].setSelection(false);
								i++;
							}
						}
					}
				});
	    
	    
	 
		//DEFINISCO LABEL E CHECKBOX TUTTI I NEGOZI DISALLINEATI
	    Label tuttineg= new Label(secondContent, SWT.NONE);
		tuttineg.setText("Tutti i negozi disallineati");
		final Button tuttibutton =new Button(secondContent, SWT.CHECK);
		
		//ASSOCIO AZIONE AL CHECKBOX TUTTI I NEGOZI DISALLINEATI
				tuttibutton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e2) {
						if (tuttibutton.getSelection()) {
							System.out.println("tot1 "+tot);
							int i=1;
							while (tot>=i)
							{
								if (cb[i].getEnabled())
								{
									cb[i].setSelection(true); //SE TRUE IMPOSTO TUTTI A TRUE
								}
								i++;
							}
						}
						else {
							int i=1;
							while (tot>=i)
							{
								cb[i].setSelection(false); //SE FALSE IMPOSTO TUTTI A FALSE
								i++;
							}
						}
					}
				});
		
		//DEFINISCO LABEL E CHECKBOX TUTTI I FISCAL
	    Label tuttifis= new Label(secondContent, SWT.NONE);
	    tuttifis.setText("Tutti i FISCALI disallineati");
		final Button tuttifiscali =new Button(secondContent, SWT.CHECK);
		
		//ASSOCIO AZIONE AL CHECKBOX TUTTI I FISCALI
		tuttifiscali.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e2) {
				if (tuttifiscali.getSelection()) {
					System.out.println("tot1 "+tot);
					int i=1;
					while (tot>=i)
					{
						if (fiscaliva[i].getEnabled())
						{
							fiscaliva[i].setSelection(true); //SE TRUE IMPOSTO TUTTI A TRUE
						}
						i++;
					}
				}
				else {
					int i=1;
					while (tot>=i)
					{
						fiscaliva[i].setSelection(false); //SE FALSE IMPOSTO TUTTI A FALSE
						i++;
					}
				}
			}
		});
		
		//DEFINISCO LABEL DATA
		Label data= new Label(secondContent, SWT.NONE);
		data.setText("CAMBIA DATA : ");
		
		//DEFINISCO CALENDARIO DATA
		datainizio= new DateTime(secondContent, SWT.CALENDAR);
	    datainizio.setDate(datainizio.getYear(), datainizio.getMonth(), datainizio.getDay()-1);
		

		System.out.println(""+datainizio.getYear()+""+(datainizio.getMonth()+1)+""+datainizio.getDay());
		
		//DEFINISCO TASTO PER CAMBIO DATA
		confermadata = new Button(secondContent, SWT.PUSH);
		confermadata.setText("CONFERMA DATA");
		//final Label 
		dataconfermata= new Label(secondContent, SWT.NONE);
		dataconfermata.setText(""+datainizio.getDay()+"/"+(datainizio.getMonth()+1)+"/"+datainizio.getYear());
		
		//DEFINISCOPROGRESS BAR
		progressBar1 = new ProgressBar(secondContent, SWT.NULL);
        progressBar1.setMinimum(1);
        progressBar1.setMaximum(tot);
        progressBar1.setSelection(1);	
		
		//RECUPERO DATI DAL DB
		caricadati();
		
		//IMPOSTO PROGRESSBAR
		progressBar1.setMaximum(tot);
		progressBar1.setSelection(tot);
		
		//IMPOSTO LAYOUT
		firstScroll.setContent(firstContent);
	    firstScroll.setExpandHorizontal(true);
	    firstScroll.setExpandVertical(true);
	    firstScroll.setMinSize(firstContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	    tot=0+i;
	    
	    //ASSOCIO AZIONE AL CONFERMA DATA
		confermadata.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e1) {
				//IMPOSTO DATA
				dataconfermata.setText(""+datainizio.getDay()+"/"+(datainizio.getMonth()+1)+"/"+datainizio.getYear());
				
				//IMPOSTO LAYOUT
				firstContent = new Composite(firstScroll, SWT.NONE);
			    firstContent.setLayout(new GridLayout());
			    firstContent.setLayout(grid);
			    firstScroll.setContent(firstContent);
			    firstScroll.setExpandHorizontal(true);
			    firstScroll.setExpandVertical(true);
			    firstScroll.setMinSize(firstContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			    
			    //RIPULISCO CAMPI
				i=1;
				while (tot>=i)
				{
					cb[i].dispose();
					negcod[i].dispose();
					negnpo[i].dispose();
					negzre[i].dispose();
					negdat[i].dispose();
					negsco[i].dispose();
					negven[i].dispose();
					negfis[i].dispose();
					negdif[i].dispose();
					nextday[i].dispose();
					negdifiva[i].dispose();
					negfisiva[i].dispose();
					fiscaliva[i].dispose();
					i++;
				}
				
				i=0;

				//INTESTAZIONE COLONNE
				Label sel=new Label(firstContent, SWT.NONE);
				sel.setText("SEL");
				Label negz=new Label(firstContent, SWT.NONE);
				negz.setText("NEGOZIO");
				Label npo=new Label(firstContent, SWT.NONE);
				npo.setText("CASSA");
				Label zre=new Label(firstContent, SWT.NONE);
				zre.setText("AZZERAMENTO");
				Label dat=new Label(firstContent, SWT.NONE);
				dat.setText("DATA");
				Label sco=new Label(firstContent, SWT.NONE);
				sco.setText("DIFF NUM SCO");
				Label cont=new Label(firstContent, SWT.NONE);
				cont.setText("CONTABILE");
				Label fis=new Label(firstContent, SWT.NONE);
				fis.setText("FISCALE");
				Label diff=new Label(firstContent, SWT.NONE);
				diff.setText("DIFFERENZA");
				Label fisiva=new Label(firstContent, SWT.NONE);
				fisiva.setText("FISCALE IVA");
				Label diffiva=new Label(firstContent, SWT.NONE);
				diffiva.setText("DIFF IVA");
				Label next=new Label(firstContent, SWT.NONE);
				next.setText("NEXT-DAY");
				Label iva=new Label(firstContent, SWT.NONE);
				iva.setText("FISCAL IVA");
				
				//RECUPERO DATI DAL DB
				caricadati();
				
				tot=i;
				progressBar1.setMaximum(tot);
				progressBar1.setSelection(i);
				
				first.layout() ;// uses Composite.layout(true)
				first.layout(true) ;
				first.layout(true, true);
				firstScroll.layout() ;// uses Composite.layout(true)
				firstScroll.layout(true) ;
				firstScroll.layout(true,true);
				firstContent.layout() ;// uses Composite.layout(true)
				firstContent.layout(true) ;
				firstContent.layout(true,true);
				
				firstScroll.setContent(firstContent);
			    firstScroll.setExpandHorizontal(true);
			    firstScroll.setExpandVertical(true);
			    firstScroll.setMinSize(firstContent.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
			    tuttibutton.setSelection(false);
			    
			    
			    
				}
		});

		
		
		//DEFINISCO TASTO PER COMPLETARE ALLINEARE I DATI
		Button avanti = new Button(shell, SWT.PUSH);
		avanti.setText("ALLINEA FISCALI");
		
		////ASSOCIO AZIONE AL TASTO ALLINEA
		avanti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e1) {
					
				//CONTO IL NUMERO DI NEGOZIO SELEZIONATI PER ASSICURARMI NE SIA STATO SELEZIONATO ALMENO UNO
					int i=1;
					count=0;
					boolean check=false;
					while (tot>=i)
					{
						if(cb[i].getSelection()) 
						{
							check=true;
							count++;
						}
						i++;
					}
				System.out.println("numero negozi "+count);
				
				//SE ALMENO UN NEGOZIO E' STATO SELEZIONATO
				if (check) 
				{
					//RECUPERO DATA
					String day =new String(""+datainizio.getDay());
					String daynext =new String(""+(datainizio.getDay()+1));
					String month =new String(""+(datainizio.getMonth()+1));
					String year =new String(""+datainizio.getYear());
					if (day.length()==1) day="0"+day;
					if (month.length()==1) month="0"+month;
					String datafis= new String(""+year+month+day);
					String datafisnext= new String(""+year+month+daynext);
					System.out.println("datafis "+datafis);
					
						//SE IL DB E' SQLSERVER MI CONNETTO A SQLSERVER
								if (dbengine.equals("mssql")) 
									{
									try
										{
										Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
										String url="jdbc:sqlserver://"+dbHostName+";databaseName="+dbDatabase;
										Connection con= DriverManager.getConnection(url, dbUser, dbPasswd);
										Statement s1 = con.createStatement();
										//System.out.println("OK ");
										i=1;
										//PER OGNI NEGOZIO SLEZIONATO
										while (tot>=i)  
										{
											if(cb[i].getSelection()) 
											{
												//COPIO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE SU FISCAL CLOSE STOR SE LA TABELLA ESISTE
												System.out.println("insert into fiscal_close_stor select * from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
												s1.executeUpdate("insert into fiscal_close_stor select * from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
											
												//ELIMINO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE
												System.out.println("delete from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
												s1.executeUpdate("delete from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
											
												//INSERISCO IL RECORD PER ALLINEAMNETO SU FISCAL_CLOSE
												System.out.println("insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"','"+datafis+"','23:59','"+negven[i].getText()+"','0','"+negzre[i].getText()+"','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','"+negven[i].getText()+"')");
												s1.executeUpdate("insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"','"+datafis+"','23:59','"+negven[i].getText()+"','0','"+negzre[i].getText()+"','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','"+negven[i].getText()+"')");
											
												//SE SELEZIONATO NEXTDAY INSERICO RECORD NEGATIVO PER LA GIORNATA SEGUENTE
												if(nextday[i].getSelection()) 
												{
													//COPIO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE SU FISCAL CLOSE STOR
													//ResultSet rsmov = s1.executeQuery("insert into fiscal_close_stor select * from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i]+"' and fis_npo='"+negnpo[i]+"'");
													//System.out.println("MOV: "+rsmov);
													//ELIMINO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE
													//ResultSet rsdel = s1.executeQuery("delete from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i]+"' and fis_npo='"+negnpo[i]+"'");
													//System.out.println("DEL: "+rsdel);
													//INSERISCO IL RECORD PER ALLINEAMNETO SU FISCAL_CLOSE
													//insert into fiscal_close values(NEG,EX MANUAL ,NPO,DAT,23:59  ,fiscal,0,9999 ,N ,23:59 ,S  ,9999,0,0,0,0,0,0,0,0,0,0)
													String negvennext="-"+negven[i].getText();
													System.out.println(negvennext);
													int datafisnextdaymssql= Integer.parseInt(datafis)+1;
													System.out.println("insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"','"+datafisnextdaymssql+"','23:59','"+negvennext+"','0','9999','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','0')");
													String sqlinsnext ="insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"','"+datafisnextdaymssql+"','23:59','"+negvennext+"','0','9999','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','0')";
													s1.executeUpdate(sqlinsnext);
													System.out.println("INSnext: "+sqlinsnext);
												}
												
												//SE SELEZIONATO FISCAL IVA 
												if(fiscaliva[i].getSelection()) 
												{
													//ELIMINO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE
													System.out.println("delete from fiscal_close_iva where fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"' and fis_zre='"+negzre[i].getText()+"'");
													s1.executeUpdate("delete from fiscal_close_iva where fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"' and fis_zre='"+negzre[i].getText()+"'");
												
													//INSERISCO IL RECORD PER ALLINEAMNETO SU FISCAL_CLOSE IVA
													sqlinsfisiva=("insert into fiscal_close_iva (fis_neg, fis_npo, fis_zre, fis_ali, fis_importo, fis_imposta, fis_natura) \r\n" + 
										                     " select ven_neg, ven_npo, '"+negzre[i].getText()+"',  \r\n" + 
										                     " (case when ven_ali > 1 then 100*(ven_ali-1) else 0 end) as IVA,  \r\n" +
										                     " sum( case when ven_sgn='-' then -ven_tot else ven_tot end ) as IMPORTO ,  \r\n" +
										                     " sum( case when ven_sgn='-' then -ven_pru else ven_pru end ) as IMPOSTA,  \r\n" +
										                     " (case when ven_ali <=1 then   \r\n" +
										                     "    ( select max(iva_natura) from tabiva where iva_cod = (case when ven_ali<=1 then ven_iva else '' end)  )   \r\n" +
										                     "  else '' end) as NATURA   \r\n" +
										                     " from vendite_stor   \r\n" +
										                     " where ven_neg='"+negcod[i].getText()+"' and ven_dat='"+datafis+"' and ven_npo='"+negnpo[i].getText()+"'   \r\n" +
										                     " and ven_cda='DDDD'  \r\n" +
										                     "group by ven_neg,ven_npo,ven_dat,ven_ali, (case when ven_ali<=1 then ven_iva else '' end)  \r\n" +
										                     " order by ven_neg,ven_npo,ven_dat " );
													s1.executeUpdate(sqlinsfisiva);
													System.out.println("INSiva: "+sqlinsfisiva);
												}
											}
											i++;
										}
										s1.close();
										System.out.println("close "+s1.isClosed());
										}
									catch (Exception e)
										{
										MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
										dialog.setText("ATTENZIONE");
										dialog.setMessage("ERRORE7: "+e+ " Ultimo record: "+sqlinsfisiva);
										dialog.open();
										System.out.println("E7"+e);
										}
									}
								else
									//SE IL DB E' POSTGRESQL MI CONNETTO A POSTGRESQL
									if (dbengine.equals("postgresql")) 
										{
										try
											{
											String url="jdbc:postgresql://"+dbHostName+":"+dbPort+"/"+dbDatabase;
											Connection con= DriverManager.getConnection(url, dbUser, dbPasswd);
											Statement s1 = con.createStatement();
											i=1;
											//PER OGNI NEGOZIO SLEZIONATO
											while (tot>=i)  
											{
												if(cb[i].getSelection()) 
												{
													//
													//COPIO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE SU FISCAL CLOSE STOR SE LA TABELLA NON ESISTE
											//		String sqlmovnew = "select * into fiscal_close_stor from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'";
											//		s1.executeUpdate(sqlmovnew);
													//COPIO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE SU FISCAL CLOSE STOR SE LA TABELLA ESISTE
													String sqlmov = "insert into fiscal_close_stor select * from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'";
													s1.executeUpdate(sqlmov);
										//			ResultSet rsmov = s1.executeQuery("insert into fiscal_close_stor select * from fiscal_close where fis_dat=CURRENT_DATE-1 and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
													System.out.println("MOV: "+sqlmov);
													//ELIMINO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE
													String sqldel ="delete from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'";
													s1.executeUpdate(sqldel);
										//			ResultSet rsdel = s1.executeQuery("delete from fiscal_close where fis_dat=CURRENT_DATE-1 and fis_neg='"+negcod[i].getText()+"' and fis_npo='"+negnpo[i].getText()+"'");
													System.out.println("DEL: "+sqldel);
													//INSERISCO IL RECORD PER ALLINEAMNETO SU FISCAL_CLOSE
													//insert into fiscal_close values(NEG,EX MANUAL ,NPO,DAT,23:59  ,fiscal,0,9999 ,N ,23:59 ,S  ,9999,0,0,0,0,0,0,0,0,0,0)
													String sqlins ="insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"','"+datafis+"','23:59','"+negven[i].getText()+"','0','9999','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','0')";
													s1.executeUpdate(sqlins);
										//			ResultSet rsins = s1.executeQuery("insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"',CURRENT_DATE-1,'23:59','"+negven[i].getText()+"','0','9999','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','0'");
													System.out.println("INS: "+sqlins);
													//SE SELEZIONATO NEXTDAY INSERICO RECORD NEGATIVO PER LA GIORNATA SEGUENTE
													if(nextday[i].getSelection()) 
													{
														//COPIO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE SU FISCAL CLOSE STOR
														//ResultSet rsmov = s1.executeQuery("insert into fiscal_close_stor select * from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i]+"' and fis_npo='"+negnpo[i]+"'");
														//System.out.println("MOV: "+rsmov);
														//ELIMINO EVENTUALI RECORD PRESENTI SU FISCAL CLOSE
														//ResultSet rsdel = s1.executeQuery("delete from fiscal_close where fis_dat='"+datafis+"' and fis_neg='"+negcod[i]+"' and fis_npo='"+negnpo[i]+"'");
														//System.out.println("DEL: "+rsdel);
														//INSERISCO IL RECORD PER ALLINEAMNETO SU FISCAL_CLOSE
														//insert into fiscal_close values(NEG,EX MANUAL ,NPO,DAT,23:59  ,fiscal,0,9999 ,N ,23:59 ,S  ,9999,0,0,0,0,0,0,0,0,0,0)
														String negvennext="-"+negven[i].getText();
														System.out.println(negvennext);
														String sqlinsnext ="insert into fiscal_close values('"+negcod[i].getText()+"','ES MANUAL','"+negnpo[i].getText()+"',date '"+datafis+"' + integer '1','23:59','"+negvennext+"','0','9999','N','23:59','S','9999','0','0','0','0','0','0','0','0','0','0')";
														s1.executeUpdate(sqlinsnext);
														System.out.println("INSnext: "+sqlinsnext);
													}
												}
												i++;
											}
											s1.close();
											System.out.println("close "+s1.isClosed());
											}
										catch (Exception e)
											{
											MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
											dialog.setText("ATTENZIONE");
											dialog.setMessage("ERRORE8"+e);
											dialog.open();
											System.out.println("E8 "+e);
											}
										}
				
					MessageBox dialog = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("CONFERMATO");
					dialog.setMessage("ALLINEAMENTO COMPLETATO");
					dialog.open();
					
				}
				//AVVISO SE NON E' STATO SELEZIONATO NESSUN NEGOZIO
				else {
					MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					dialog.setText("ATTENZIONE");
					dialog.setMessage("DEVI SELEZIONARE ALMENO UN NEGOZIO");
					dialog.open();
				}
			confermadata.notifyListeners(SWT.Selection, new Event());
			}
			
		});
		
				
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}


	// LETTURA FILE CFG PER RECUPERARE INFO CONNESSIONE AL DB
	static void cfgread() {
			try {
				// LEGGO FILE E CFG E CARICO ARRAY dbconnection
				BufferedReader reader = new BufferedReader(new FileReader("CFG_FISCAL.ini"));
				while ((tmp=reader.readLine()) != null)
				{
					int i=0;
					cfg[i] = tmp;
									
					//RECUPERO IL MOTORE DEL DATABASE
					if (cfg[i].contains("DB_SERVER_Protocol")) 
						{
						System.out.println(cfg[i]);
						dbengine=cfg[i].replace("DB_SERVER_Protocol=", "");
						//System.out.println(dbengine);
						}
					//RECUPERO L'HOST DEL DATABASE
					if (cfg[i].contains("DB_SERVER_HostName")) 
						{
						System.out.println(cfg[i]);
						dbHostName=cfg[i].replace("DB_SERVER_HostName=", "");
						//System.out.println(dbHostName);
						}
					//RECUPERO LA PORTA DEL DATABASE
					if (cfg[i].contains("DB_SERVER_Port")) 
						{
						System.out.println(cfg[i]);
						dbPort=cfg[i].replace("DB_SERVER_Port=", "");
						//System.out.println(dbPort);
						}
					//RECUPERO LA USER DEL DATABASE
					if (cfg[i].contains("DB_SERVER_User")) 
						{
						System.out.println(cfg[i]);
						dbUser=cfg[i].replace("DB_SERVER_User=", "");
						//System.out.println(dbUser);
						}
					//RECUPERO LA PASSWORD DEL DATABASE
					if (cfg[i].contains("DB_SERVER_Passwd")) 
						{
						System.out.println(cfg[i]);
						dbPasswd=cfg[i].replace("DB_SERVER_Passwd=", "");
						//System.out.println(dbPasswd);
						}
					//RECUPERO IL NOME DEL DATABASE
					if (cfg[i].contains("DB_SERVER_Database")) 
						{
						System.out.println(cfg[i]);
						dbDatabase=cfg[i].replace("DB_SERVER_Database=", "");
						//System.out.println(dbDatabase);
						}
					//RECUPERO LA DIMENSIONE DI CDA
					if (cfg[i].contains("CDA_LENGHT")) 
						{
						System.out.println(cfg[i]);
						CdaLenght=cfg[i].replace("CDA_LENGHT=", "");
						//System.out.println(CdaLenght);
						if (CdaLenght.equals("1")) CdaInt=31;
						//System.out.println(CdaInt);
						}
					i++;
				}
				reader.close();
				}
			catch (FileNotFoundException e)
			{
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				dialog.setText("ATTENZIONE");
				dialog.setMessage("ERRORE1"+e);
				dialog.open();
				System.out.println("E1"+e);
			} 
			catch (IOException e2) 
			{
				MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
				dialog.setText("ATTENZIONE");
				dialog.setMessage("ERRORE2"+e2);
				dialog.open();
				System.out.println("E2"+e2);
			}
	 }
	 

	 
	 // RECUPERO DATI DAL DATABASE
	 static void caricadati() {
		 	
		    //DEFINISCO DATA DI RICERCA	
		    String day =new String(""+datainizio.getDay());
			String month =new String(""+(datainizio.getMonth()+1));
			String year =new String(""+datainizio.getYear());
			if (day.length()==1) day="0"+day;
			if (month.length()==1) month="0"+month;
			datafis= new String(""+year+month+day);
			System.out.println("Nuova data  "+year+month+day);
			
			i=0;
			//SE IL DB E' SQLSERVER MI CONNETTO A SQLSERVER
			if (dbengine.equals("mssql")) 
				{
				try
					{
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
					String url="jdbc:sqlserver://"+dbHostName+";databaseName="+dbDatabase;
					Connection con= DriverManager.getConnection(url, dbUser, dbPasswd);
					Statement s1 = con.createStatement();
					//RECUPERO I NEGOZI
					ResultSet rsneg1 = s1.executeQuery("select distinct  vendite_stor.ven_neg,  vendite_stor.ven_npo, CAST( vendite_stor.ven_dat as DATE), "
													+ "sum(CASE when  vendite_stor.ven_sgn='+' then  vendite_stor.ven_tot else ( vendite_stor.ven_tot*(-1)) end) as vendite, "
													+ "case when fis.fiscal is null then 0 else fis.fiscal end, "
													+ "case when fis.fiscal is null then (sum(CASE when  vendite_stor.ven_sgn='+' then  vendite_stor.ven_tot else ( vendite_stor.ven_tot*(-1)) end)-0) else (sum(CASE when  vendite_stor.ven_sgn='+' then  vendite_stor.ven_tot else ( vendite_stor.ven_tot*(-1)) end)-fis.fiscal) end as diff, "
													+ "vendite_stor.ven_zre, case when iva.importo is null then 0 else iva.importo end, "
													+ "case when fis.fiscal is null then (case when iva.importo is null then 0 else iva.importo*(-1) end) else (case when iva.importo is null then fis.fiscal else fis.fiscal-iva.importo end) end, "
													+ "vensco.scodiff  "
														+ "from vendite_stor  "
															+ "full outer join (select distinct fis_neg, fis_npo, fis_dat, sum(fis_vnet) as fiscal, fis_zre from fiscal_close where fis_dat='"+datafis+"' group by fis_neg, fis_npo, fis_dat, fis_zre)  as fis	on  vendite_stor.ven_neg=fis_neg and  vendite_stor.ven_npo=fis_npo and  vendite_stor.ven_dat=fis_dat and vendite_stor.ven_zre=fis_zre "
																	+ "full outer join (select sum(fis_importo) as importo, fis_neg, fis_npo, fis_zre from fiscal_close_iva group by fis_neg, fis_npo, fis_zre) as iva on  vendite_stor.ven_neg=iva.fis_neg and  vendite_stor.ven_npo=iva.fis_npo and  vendite_stor.ven_zre=iva.fis_zre full outer join (select distinct ven_neg, ven_npo, max(ven_nsc)-count(distinct ven_nsc) as scodiff "
																	+ "from vendite_stor where ven_sta='T' and ven_dat = '"+datafis+"' group by ven_neg, ven_npo) as vensco on vendite_stor.ven_neg=vensco.ven_neg and  vendite_stor.ven_npo=vensco.ven_npo "
																			+ "where  vendite_stor.ven_dat='"+datafis+"' and  vendite_stor.ven_sta='T' and  vendite_stor.ven_not not in ('FAT','DDT') and  vendite_stor.ven_zre<>'0' "
																					+ "group by  vendite_stor.ven_neg,  vendite_stor.ven_npo,  vendite_stor.ven_zre,  vendite_stor.ven_dat, fis.fiscal, iva.importo, vensco.scodiff "
																					+ "order by  vendite_stor.ven_neg,  vendite_stor.ven_npo");

					//CARICO IL RISULTATO DELLA QUERY
					while (rsneg1.next())
						{
						i++;
						progressBar1.setSelection(i);
						
						cb[i]=new Button(firstContent, SWT.CHECK);
						negcod[i]= new Label(firstContent, SWT.NONE);
						negcod[i].setText(""+rsneg1.getString(1));
						negnpo[i]= new Label(firstContent, SWT.NONE);
						negnpo[i].setText(""+rsneg1.getString(2));
						negzre[i]= new Label(firstContent, SWT.NONE);
						negzre[i].setText(""+rsneg1.getString(7));
						negdat[i]= new Label(firstContent, SWT.NONE);
						negdat[i].setText(""+rsneg1.getString(3));
						negsco[i]= new Label(firstContent, SWT.NONE);
						negsco[i].setText(""+rsneg1.getString(10));
						negven[i]= new Label(firstContent, SWT.NONE);
						negven[i].setText(""+rsneg1.getFloat(4));
						negfis[i]= new Label(firstContent, SWT.NONE);
						negfis[i].setText(""+rsneg1.getFloat(5));
						negdif[i]= new Label(firstContent, SWT.NONE);
						negdif[i].setText(""+rsneg1.getFloat(6));
						negfisiva[i]= new Label(firstContent, SWT.NONE);
						negfisiva[i].setText(""+rsneg1.getFloat(8));
						negdifiva[i]= new Label(firstContent, SWT.NONE);
						negdifiva[i].setText(""+rsneg1.getFloat(9));
						negfisiva[i].setEnabled(false);
						nextday[i]=new Button(firstContent, SWT.CHECK);
						fiscaliva[i]=new Button(firstContent, SWT.CHECK);
						
						result= "negozio: "+rsneg1.getString(1)+" "+rsneg1.getString(2)+" "+rsneg1.getString(3)+" "+rsneg1.getString(4)+" "+rsneg1.getString(5)+" "+rsneg1.getString(6)+" "+rsneg1.getString(7)+" "+rsneg1.getString(8)+" "+rsneg1.getString(9)+" "+rsneg1.getString(10);
						System.out.println(result);
						nextday[i].setEnabled(false);
						
						//VERIFICO SE CI SONO DIFFERENZE SUI TOTALI  ED ABILITO I CHECKBOX
						if (negdif[i].getText().contentEquals("0.0")) 
						{
							cb[i].setEnabled(false);
							nextday[i].setEnabled(false);
							fiscaliva[i].setEnabled(false);
							negdif[i].setBackground(new org.eclipse.swt.graphics.Color(display, 124, 255, 0));
						}
						else
						{negdif[i].setBackground(new org.eclipse.swt.graphics.Color(display, 255, 0, 0));} 
						
						//VERIFICO SE CI SONO DIFFERENZE SUL NUMERO SCONTRINI  ED ABILITO I CHECKBOX
						if (negsco[i].getText().contentEquals("0")) 
						{{negsco[i].setBackground(new org.eclipse.swt.graphics.Color(display, 124, 255, 0));}}
						else
						{
							cb[i].setEnabled(false);
							nextday[i].setEnabled(false);
							fiscaliva[i].setEnabled(false);
							negsco[i].setBackground(new org.eclipse.swt.graphics.Color(display, 255, 165, 0));
						}
						}
					s1.close();
					}
				catch (Exception e)
				{
					MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
					dialog.setText("ATTENZIONE");
					dialog.setMessage("ERRORE5: "+e+" Last record "+result);
					dialog.open();
					System.out.println("ERRORE5: "+e+" Last record "+result);
				}
				}
			else
				//SE IL DB E' POSTGRESQL MI CONNETTO A POSTGRESQL
				if (dbengine.equals("postgresql")) 
					{
					try
						{
						String url="jdbc:postgresql://"+dbHostName+":"+dbPort+"/"+dbDatabase;
						Connection con= DriverManager.getConnection(url, dbUser, dbPasswd);
						Statement s1 = con.createStatement();
						//RECUPERO IL CODICE NEGOZIO
						ResultSet rsneg = s1.executeQuery("select distinct ven_neg, ven_npo, ven_dat, sum(ven_tot) as vendite, case when fis.fiscal is null then 0 else fis.fiscal end, \r\n" + 
								"case when fis.fiscal is null then (sum(ven_tot)-0) else (sum(ven_tot)-fis.fiscal) end as diff from vendite_stor full outer join  \r\n" + 
								"(select distinct fis_neg, fis_npo, fis_dat, sum(fis_day) as fiscal from fiscal_close where fis_dat='"+datafis+"' group by fis_neg, fis_npo, fis_dat)  as fis \r\n" + 
								"on ven_neg=fis_neg and ven_npo=fis_npo and ven_dat=fis_dat   \r\n" + 
								"where ven_dat='"+datafis+"' and ven_sta='T' and ven_sgn='+' and ven_not not in ('FAT','DDT') group by ven_neg, ven_npo, ven_dat, fis.fiscal order by ven_neg, ven_npo");
						
						
						while (rsneg.next())
							{
							i++;
							progressBar1.setSelection(i);

							cb[i]=new Button(firstContent, SWT.CHECK);
							negcod[i]= new Label(firstContent, SWT.NONE);
							negcod[i].setText(""+rsneg.getString(1));
							negnpo[i]= new Label(firstContent, SWT.NONE);
							negnpo[i].setText(""+rsneg.getString(2));
							negdat[i]= new Label(firstContent, SWT.NONE);
							negdat[i].setText(""+rsneg.getString(3));
							negven[i]= new Label(firstContent, SWT.NONE);
							negven[i].setText(""+rsneg.getFloat(4));
							negfis[i]= new Label(firstContent, SWT.NONE);
							negfis[i].setText(""+rsneg.getFloat(5));
							negdif[i]= new Label(firstContent, SWT.NONE);
							negdif[i].setText(""+rsneg.getFloat(6));
							nextday[i]=new Button(firstContent, SWT.CHECK);
							fiscaliva[i]=new Button(firstContent, SWT.CHECK);
							
							//System.out.println(rsneg.getString(1));
							System.out.println(negdif[i].getText());
							if (negdif[i].getText().contentEquals("0.0")) {
								cb[i].setEnabled(false);
								nextday[i].setEnabled(false);
								fiscaliva[i].setEnabled(false);
							}
							}
						s1.close();
						}
					catch (Exception e)
					{
						MessageBox dialog = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
						dialog.setText("ATTENZIONE");
						dialog.setMessage("ERRORE6"+e);
						dialog.open();
						System.out.println("EE6"+e);
					}
					}
	 }

}
