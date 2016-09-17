
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;






import javax.swing.text.Document;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;


public class HTMLPaser implements ActionListener
{
JFrame mainFrame;
JFrame mainFrame2;
	
	JPanel mainpanel;
	JPanel mainpanel2;
	JPanel urlspanel;
	JPanel gamehitspanel;
	JPanel StoredUrlPanel;
	JPanel buttonpanel;
	JPanel workingPanel;
	
	JTextField namefield;
	JTextArea StoredUrlTextBox;
	
	//Strings:
	String playername;
	
	
	//button area:
	JButton AddUrlTolistButton;
	JButton Go;
	JButton clearvalues;
	JButton savenewtooldButton;
	JButton storeURLsAsTextButton;
	JButton RetrieveURLs;
	
	
	 JScrollPane myscrollPane;
	 
	 LinkedList<String> urlList = new LinkedList<String>();
	 File newfile;
	 File oldfile;
	 File StoredURLsFile = new File("C:\\Users\\masterclay\\Desktop\\URLPriceFinderFolder\\StoredUrls.txt");
	 File storedItemTitleFile = new File("C:\\Users\\masterclay\\Desktop\\URLPriceFinderFolder\\ItemTitles.txt");
	 
	 //writers:
	FileWriter fwItem; 
	BufferedWriter bwItem; 
	
	//labels:
	JLabel workingLabel;
	JLabel urlLabel;
	
	
	public static void main(String[] args)
	{
		new HTMLPaser();
	}
	
	//MAIN CONTROL PANEL	
	public HTMLPaser()
	{
		
		try 
		{
		fwItem = new FileWriter(storedItemTitleFile.getAbsoluteFile());
		 bwItem = new BufferedWriter(fwItem);
		 bwItem.flush();
		}
		
		catch (IOException e2) 
    	{
        e2.printStackTrace();
    	}
		PanelInitilization();
		
	}
	
	public LinkedList<String> PanelInitilization()
	{
		
		
		mainFrame = new JFrame();
		mainFrame.setTitle("What's The Price Again?");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainpanel = (JPanel)mainFrame.getContentPane();
		mainpanel.setLayout(new BoxLayout(mainpanel, BoxLayout.Y_AXIS)); //****************mainpanel and now mainframe in this param
		mainpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 , 10));
		
		//name panel
		urlspanel = new JPanel();
		urlspanel.add(new JLabel("Url of the website: "));
		namefield = new JTextField(50);
		urlspanel.add(namefield);
		
		mainpanel.add(urlspanel);
		 
		 
		 //current player's list:
		 StoredUrlPanel = new JPanel();
		 StoredUrlPanel.add(new JLabel("Stored Urls: "));
		 StoredUrlTextBox = new JTextArea(10,50);
		 
		 StoredUrlTextBox.setEditable(false);
		 
		 myscrollPane = new JScrollPane(StoredUrlTextBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		 StoredUrlPanel.add(myscrollPane);
		 
		 mainpanel.add(StoredUrlPanel);
		 
		 
		 //button panel:
		 buttonpanel = new JPanel();
		 AddUrlTolistButton = new JButton("Add Url to list");
		 AddUrlTolistButton.addActionListener(this);
		 Go = new JButton("Start the madness");
		 Go.addActionListener(this);
		 clearvalues = new JButton("Reset Urls");
		 clearvalues.addActionListener(this);
		 savenewtooldButton = new JButton("Save New Prices To File");
		 savenewtooldButton.addActionListener(this);
		 storeURLsAsTextButton = new JButton("Store all URLs in list as a text file to use later");
		 storeURLsAsTextButton.addActionListener(this);
		 RetrieveURLs = new JButton("Retrieve URLs");
		 RetrieveURLs.addActionListener(this);
		 
		 buttonpanel.add(AddUrlTolistButton);
		 buttonpanel.add(Go);
		 buttonpanel.add(clearvalues);
		 buttonpanel.add(savenewtooldButton);
		 buttonpanel.add(storeURLsAsTextButton);
		 buttonpanel.add(RetrieveURLs);
		
		 mainpanel.add(buttonpanel);
		 
		 //initialzing BUT NOT ADDING the waitinglabel:
		 workingPanel = new JPanel();
		 workingLabel = new JLabel("");
		workingPanel.add(workingLabel);
		 
		 mainpanel.add(workingPanel);
		 
		 
		//last thing to do:
		mainFrame.pack();
		mainFrame.setVisible(true);
		
		return urlList;
		
	}
//	public void PanelInitilization2()
//	{
//		mainFrame2 = new JFrame();
//		mainFrame2.setTitle("OutPutting");
//		mainFrame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		
//		mainpanel2 = (JPanel)mainFrame2.getContentPane();
//		mainpanel2.setLayout(new BoxLayout(mainpanel2, BoxLayout.Y_AXIS)); //****************mainpanel and NOT mainframe in this param
//		mainpanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10 , 10));
//		
//		
//		mainpanel.add(new JLabel("Getting price from the following url: "));
//		
//		urlLabel = new JLabel("");
//		mainpanel2.add(urlLabel);
//		
//		mainFrame2.pack();
//		mainFrame2.setVisible(true);
//		
//	}
	
	public File getPrice(File file) {
		
		//JOptionPane.showMessageDialog(mainFrame, "Working...please wait");
		
		
		try 
		{
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
		// if file doesnt exists, then create it
		if (!file.exists()) 
			{
			file.createNewFile();
			}
		bw.flush();
		
		
		
        for (String url : urlList)
        {
        	
        	
            // Connect to the web site AND SET TIMEOUT TO 10 SECONDS //just added:  .timeout(10*1000)
            	org.jsoup.nodes.Document document = Jsoup.connect(url).timeout(10*1000)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:40.0) Gecko/20100101 Firefox/40.1")
                    .referrer("http://www.google.com")   
                    .followRedirects(true)
                    .execute().parse();
            	 //wont work for other websites like best buy
//            	Elements TitleList = document.select("meta");
//            	String titleString = TitleList.attr("title");
            	String titleString = document.title();
            	
            	
            	//AMAZON WAY
            	if(url.contains("amazon") == true)  
            	{
            		Elements trs = ((Element) document).select("span#priceblock_ourprice"); //element vs elemtns?
            		Element price1 = trs.first();
            		
            		
            		//seccond catch to make sure the amazon way works
            		Elements trs2 = ((Element) document).select("span#priceblock_dealprice"); //element vs elemtns?
            		Element price2 = trs2.first();
            		
            		if (price1 != null)
            		{
            			String PriceString1 = price1.text();
            			System.out.println(PriceString1);
            			String str2 = PriceString1.replaceAll("[$,]", "");
            			// double aInt = Double.parseDouble(str2);
            			System.out.println("Price: " + str2);
            			Writetofile(str2, fw, bw, titleString);
            			
            			//getting title section:
            			GetItemTitle(titleString, titleString);
            			//this open up the other file, not the one we are currently in 
            		}
            		else if (price2 != null)
            		{
            			String PriceString2 = price2.text();
            			System.out.println(PriceString2);
            			String str2 = PriceString2.replaceAll("[$,]", "");
            			// double aInt = Double.parseDouble(str2);
            			System.out.println("Price: " + str2);
            			Writetofile(str2, fw, bw, titleString);
            			
            			//getting title section:
            			GetItemTitle(titleString, titleString);
            			//this open up the other file, not the one we are currently in 
            		}
            		else 
            		{
            			Writetofile("No Price Found", fw, bw, titleString);
            			GetItemTitle(titleString, titleString);
            		}
            	}
            	// for Best Buy 
            	else if (titleString.contains("Best Buy"))  
            	{
                Element priceBB = ((Element) document).select(".mini-block-sale-price").first(); 
                String priceWithCurrencyBB = priceBB.text();
                System.out.println("BB" + priceWithCurrencyBB);
                System.out.println(priceWithCurrencyBB);
                String priceAsText = priceWithCurrencyBB.replaceAll( "[$,]", "" );
                priceAsText.replace("On Sale:", "");
                priceAsText.replaceAll(" ", "");
                //double priceAsNumber3 = Double.parseDouble(priceAsText);
                System.out.println("Price: " + priceAsText);
                
                Writetofile(priceAsText, fw, bw, titleString);
                
                //getting item name:
                GetItemTitle(titleString, titleString);
            	}
            	//TARGET WAY
            	else if (titleString.contains("Target")) 
            	{
            		Element priceT = ((Element) document).select(".price").first(); 
                    String priceWithCurrencyT = priceT.text();
                    System.out.println("BB" + priceWithCurrencyT);
                    System.out.println(priceWithCurrencyT);
                    String priceAsText = priceWithCurrencyT.replaceAll( "[$,]", "" );
                    priceAsText.replace("On Sale:", "");
                    priceAsText.replaceAll(" ", "");
                    priceAsText.replaceAll("\\\\n", "");
                    priceAsText = priceAsText.replace("\r\n", "");
                    
                    //double priceAsNumber3 = Double.parseDouble(priceAsText);
                    System.out.println("Price: " + priceAsText);
                    
                    Writetofile(priceAsText, fw, bw, titleString);
                    
                  //getting item name:
                    GetItemTitle(titleString, titleString);
            	}
//            	else if (titleString.contains("Newegg")) 
//            	{
//            		Element priceT = ((Element) document).select(".item-options").first(); 
//            		priceT.removeAttr("");
//                    System.out.println("BB" + priceWithCurrencyT);
//                    System.out.println(priceWithCurrencyT);
//                    String priceAsText = priceWithCurrencyT.replaceAll( "[$,]", "" );
//                    priceAsText.replace("On Sale:", "");
//                    priceAsText.replaceAll(" ", "");
//                    priceAsText.replaceAll("\\\\n", "");
//                    priceAsText = priceAsText.replace("\r\n", "");
//                    
//                    //double priceAsNumber3 = Double.parseDouble(priceAsText);
//                    System.out.println("Price: " + priceAsText);
//                    
//                    Writetofile(priceAsText, fw, bw, titleString);
//                    
//                  //getting item name:
//                    GetItemTitle(titleString, titleString);
//            	}
            	
            	
            	
            	//alternate amazon way 
//            	if(price1 == null)  
//            	{
//            	    Elements price2 = ((Element) document).select(".price-current-label"); 
//                    String priceWithCurrency = price2.text();
//                    System.out.println(priceWithCurrency);
//                    System.out.println(priceWithCurrency);
//                    String priceAsText = priceWithCurrency.replaceAll( "[$,]", "" );
//                    // double priceAsNumber = Double.parseDouble(priceAsText);
//                    System.out.println("Price: " + priceAsText);
//                    
//                    Writetofile(priceAsText, file, fw, bw);
//            	}

//            	else
//            	{
//                Elements price2 = ((Element) document).select(".a-row a-spacing-top-mini"); //element vs elements?
//                String priceWithCurrency = price2.text();
//                System.out.println(priceWithCurrency);
//                System.out.println(priceWithCurrency);
//                String priceAsText = priceWithCurrency.replaceAll( "[$,]", "" );
//                double priceAsNumber = Double.parseDouble(priceAsText);
//                System.out.println("Price: " + priceAsNumber);
//                
//                Writetofile(priceAsNumber, file, fw, bw);
//                
//               
//                
//            	}
            	//IF THERE IS AN ERROR IDENTIFIYING THE CORRECT WEBPAGE
            	else
            	{
            		Writetofile("ERROR 404", fw, bw, titleString);
            		
            		//getting item name:
                    GetItemTitle(titleString, titleString);
            	}

        } 
        // this happens after the end of the for loop but before the end of the try catch crap
        bw.close();
        fwItem.close();// this happens after the end of the for loop but before the end of the try catch crap
        
	}
        	catch (IOException e2) 
        	{
            e2.printStackTrace();
        	}
        
		workingLabel.setVisible(false);
		JOptionPane.showMessageDialog(mainFrame, "All prices are recorded");
        return file;
    }
	//this method will pass in 
	public void GetItemTitle(String ItemTitle, String titleString)
	{
//		try 
//		{	
//		// if file doesnt exists, then create it
//		if (!storedItemTitleFile.exists()) 
//			{
//			storedItemTitleFile.createNewFile();
//			}
//		
//		Writetofile(ItemTitle, fwItem, bwItem, titleString);
//		}
//		catch (IOException e2) 
//    	{
//        e2.printStackTrace();
//    	}
		
	}
	
	//copies all data from new file to old file 
	public void CopyMethod()
	{
		FileReader fr;
		FileWriter fw;
        
		try {
            fr = new FileReader(newfile);
            fw = new FileWriter(oldfile);
            fw.flush();
            int c = fr.read();
            while(c!=-1) 
            {
                fw.write(c);
                c = fr.read();
            }
            fr.close();
    		fw.close();
        } catch(IOException e) 
        	{
            e.printStackTrace();
        	}
		
		
        
    }
    public static void close(Closeable stream) 
    {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch(IOException e) {
            //...
        }
    }
	
	
	public void Writetofile(String priceAsText, FileWriter fw, BufferedWriter bw, String titleString)
	{
		try {
			
			priceAsText.replaceAll("\\\\n", "");
            priceAsText.replaceAll("\\\\t", "");
            priceAsText.replaceAll("\\\\r", "");

			
			bw.write(String.valueOf(titleString + "\t" + priceAsText));
			bw.newLine();
			
			System.out.println( titleString + "\t" + priceAsText);

		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		Object control = e.getSource();
		String currenturl;
		
		if (control == AddUrlTolistButton)
		{
			StoredUrlTextBox.setText(StoredUrlTextBox.getText() + namefield.getText()  + "\n");
			currenturl = namefield.getText();
			urlList.add(currenturl);
			namefield.setText("");
		}
		
		if (control == Go)
		{
			newfile = new File("C:\\Users\\masterclay\\Desktop\\URLPriceFinderFolder\\newdata.txt");
			getPrice(newfile);
		}
		if (control == clearvalues)
		{
			namefield.setText("");
			StoredUrlTextBox.setText("");
			urlList.clear();
			JOptionPane.showMessageDialog(mainFrame, "All Values Cleared");
		}
		if (control == savenewtooldButton)
		{
			
			//creating the old file
			oldfile = new File("C:\\Users\\masterclay\\Desktop\\URLPriceFinderFolder\\Laptop1betaOLDDATA.txt");
			try 
			{
			//checking to make sure the old exists or otherwise creating it
			if (!oldfile.exists()) 
				{
				oldfile.createNewFile();
				}
			}
			catch (IOException e2) 
			{
				e2.printStackTrace();
			}
			
			CopyMethod();
			JOptionPane.showMessageDialog(mainFrame, "Saved all current PRICES saved as prices in old file");
		}
		
		if (control == storeURLsAsTextButton)
{
		int replaced = JOptionPane.showConfirmDialog(mainFrame, "Override the old URLs?");
		
		switch (replaced) 
		{
		case JOptionPane.YES_OPTION:
			       
			
		
			
			StoredURLsFile = new File("C:\\Users\\masterclay\\Desktop\\URLPriceFinderFolder\\StoredUrls.txt");
			
			FileReader fr;
			FileWriter fw;
			
	        
			try {
				if (!StoredURLsFile.exists()) 
				{
				StoredURLsFile.createNewFile();
				}
				
	           
	            fw = new FileWriter(StoredURLsFile);
	            BufferedWriter bw = new BufferedWriter(fw);
	            for (String url : urlList)
				{
	            	bw.write(url);
	            	bw.newLine();
	            	
				}
	            bw.close();
	            JOptionPane.showMessageDialog(mainFrame, "All current URLs were just stored as URLs in Stored URLs File");
			}
			catch (IOException e3) 
			{
				e3.printStackTrace();
			}
			
			break;
			default: 
				;
		}
			
}
		//if user wants to retrieve URLs from the stored URL file
		if (control == RetrieveURLs)
		{
			
			
			urlList.clear();
			StoredUrlTextBox.setText("");
			try 
			{
	           
	           Scanner input = new Scanner(StoredURLsFile);
	           while (input.hasNext() == true)
	           {
	        	   String urltext = input.nextLine();
	        	   urlList.add(urltext);
	        	   StoredUrlTextBox.setText(StoredUrlTextBox.getText() +  urltext + "\n");
	           }
	           input.close();	           
	            JOptionPane.showMessageDialog(mainFrame, "All URLs retrieved from URL file");
			}
			catch (IOException e3) 
			{
				e3.printStackTrace();
			}
			
		}
	
}
}
