import java.io.*;
import java.net.*;

import javax.swing.*;
import javax.sound.midi.*;
import java.util.Random;
import java.awt.*; 
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
		
	static String path = "";
	static Sequencer sequencer;
	static int areas;
	static String[] SoundList;
	static String[] ZoneList;
	static String accountname;
	static String displayname;
	static JFrame frame;
	static JTextArea debugbox = new JTextArea("Enter text");
	static String soundaddy = "http://ivalicemud.com/soundlist.txt";
	static String zoneaddy = "http://ivalicemud.com/maplocations";
	static String walkicon = "sabin";
	static String soundfile;
	static String lastfile;
	static String inputstring;
	static JLabel maplabel = new JLabel();
	static JLabel maplabel2 = new JLabel();
	static JLabel maplabel3 = new JLabel();
	static JLabel maplabel4 = new JLabel();
	static JLabel walklabel = new JLabel();
	static ImageIcon mapicon = new ImageIcon(path+"data\\ivm001");
		
	static JPanel mappanel;
	static JPanel mappanel2;
	static JPanel mappanel3;
	static JPanel mappanel4;
	static ImageIcon walk1;
	static ImageIcon walk2;
	
	public static void main(String[] args) 
    throws MalformedURLException, IOException, InterruptedException 
{
	checkinstance();	
	setup();
	loadsettings();
		
	while (true) {
		fetch();
		
	}
		
		
	
    }
	
	public static void parsefetch() {
		if (inputstring.endsWith(".are")) {	    	//Area Midi
			String[] line = inputstring.split(" ");
	    	for (int i = 0; i < areas -1; i++) {
	    		String[] area = SoundList[i].split(" ");
	    			if(area[0].equals(line[1])){ //Match
	    				print("Playing " + area[1] );
	    				PlayMidi(area[1]); //MidiPlayer.main(area[1]);
	    				
	    				break;
    				} else {  }
	    		
	    	}

	    }else if (inputstring.endsWith(".mid")) {    	//Specific Midi
	    	String[] line = inputstring.split(" ");
	    	if (line[1].equals("victory.mid")) { PlayMidi("victory"+ rand(5) +".mid"); return; };
	    	if (line[1].equals("battle.mid")) {  PlayMidi("battle"+ rand(7) +".mid"); return; };
	    	PlayMidi(line[1]);
	    }else if (inputstring.endsWith("QUIT")) { 
	    	msgbox("Now closing Ivalice:Companion.");
	    	System.exit(0);
	    }else if (inputstring.startsWith("p mov")) {	    	//Movie
	    }else if (inputstring.startsWith("p ooo")) {	    //Overworld Map
	    	PlayMidi("over.mid");
	    	String[] line = inputstring.split(" ");
	        int vnum = Integer.parseInt(line[2]);
	    	DrawMap(vnum);
	    }else if (inputstring.startsWith("p uuu")) {	    	// Undeworld Map
	    	PlayMidi("underground.mid");
	    }else { msgbox("Unknown String Input: " + inputstring);    }
}
	public static int rand(int num) {
		Random numbers = new Random();
		int roll = numbers.nextInt(num) + 1;
		return roll;
	}
	
    @SuppressWarnings("deprecation")
	public static void fetch()  
    {
    	String addy = ("http://ivalicemud.com/ivc/" + accountname + ".txt");
    	URL url = null;
		try {
			url = new URL(addy);
		} catch (MalformedURLException e) {
			msgbox("Unable to connect to Ivalice Server. Now exiting IV:C.");
			System.exit(0);
			e.printStackTrace();
		}
    	URLConnection connection = null;
		try {
			connection = url.openConnection();
		} catch (IOException e) {
			msgbox("Connection Timeout. Now exiting IV:C.");
			System.exit(0);
			e.printStackTrace();
		}
    	
        DataInputStream in = null;
		try {
			in = new DataInputStream(connection.getInputStream());
		} catch (IOException e) {
			msgbox("Invalid account name entered. Closing IV:C.");
			System.exit(0);
			e.printStackTrace();
		}
        String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if(line.equals(inputstring)) {
        		
        }else{
        	inputstring = line;
        	print(inputstring);
        	parsefetch();
        }
        if(walklabel.getIcon().equals(walk1)) {walklabel.setIcon(walk2); } else { walklabel.setIcon(walk1);}
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }

    @SuppressWarnings("deprecation")
	public static void setup() throws IOException, InterruptedException {
        if (accountname == null) { 
    	accountname = JOptionPane.showInputDialog(null, "Please enter your account name: ",  "Account Login", 1);
        }
           if( accountname==null || accountname.length() <= 0 ) {msgbox("No account name entered. Exiting IV:C."); System.exit(0);}
           accountname = accountname.toLowerCase();
           
           //Create Frame
        frame = new JFrame("Ivalice:Companion");
        frame.setSize(288, 288);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Add Map Panel
        mappanel = new JPanel();
        mappanel.setLayout(null);
        mappanel.setBackground(Color.black);
        mappanel2 = new JPanel();
        mappanel2.setLayout(null);
        mappanel2.setBackground(Color.orange);

        mappanel3 = new JPanel();
        mappanel3.setLayout(null);
        mappanel3.setBackground(Color.MAGENTA);

        mappanel4 = new JPanel();
        mappanel4.setLayout(null);
        mappanel4.setBackground(Color.red);

        mapicon = new ImageIcon(path+"data\\ivm001");
               
        walk1 = new ImageIcon(path+"data\\tiles\\"+walkicon+".gif");
        walk2 = new ImageIcon(path+"data\\tiles\\"+walkicon+".gif");
        walklabel.setIcon(walk1);

        //Get Sound List -
    	URL url = new URL(soundaddy);
    	URLConnection connection = url.openConnection();
        DataInputStream in = new DataInputStream(connection.getInputStream());
        SoundList = new String[300];
        for(areas = 0; areas < 10000; areas++) {
            String line = in.readLine();
            if (line == null) break;
            SoundList[areas] = line;
            print(line);
        }
        // Load Map Zones
        
        
        url = new URL(zoneaddy);
    	connection = url.openConnection();
        in = new DataInputStream(connection.getInputStream());
        ZoneList = new String[2000];
        
        for(int i = 0; i < 10000; i++) {
            String line = in.readLine();
            if (line == null) break;
            ZoneList[i] = line;
            String[] line2 = line.split(" ");
			PrintMap(Integer.parseInt(line2[0]), Integer.parseInt(line2[1]));
        }
        //frame.add(debugbox);
        frame.add(walklabel);
        maplabel.setIcon(mapicon);
        maplabel2.setIcon(mapicon);
        maplabel3.setIcon(mapicon);
        maplabel4.setIcon(mapicon);
        mappanel.add(maplabel);
        mappanel2.add(maplabel2);
        mappanel3.add(maplabel3);
        mappanel4.add(maplabel4);
        
        frame.add(mappanel);
        frame.add(mappanel2);
        frame.add(mappanel3);
        frame.add(mappanel4);

        mappanel.setSize(2566,2144);
        mappanel2.setSize(2566,2144);
        mappanel3.setSize(2566,2144);
        mappanel4.setSize(2566,2144);
        
        maplabel.setSize(2566,2144);
        maplabel.setLocation(1,1);

        maplabel2.setSize(2566,2144);
        maplabel2.setLocation(1,1);

        maplabel3.setSize(2566,2144);
        maplabel3.setLocation(1,1);

        maplabel4.setSize(2566,2144);
        maplabel4.setLocation(1,1);

        mappanel.setAutoscrolls(false);
        mappanel2.setAutoscrolls(false);
        mappanel3.setAutoscrolls(false);
        mappanel4.setAutoscrolls(false);

        walklabel.setBounds((288/2)-16,(288/2)-16,32,32);
        walklabel.setVisible(true);
        DrawMap(28272);
        frame.setVisible(true);
        debugbox.setText("Ivalice:Companion - Ready.");
      
    }
    
    public static void DrawMap(double vnum) {

        vnum = (vnum - 26000) + 80;
        double big = (vnum * 1000) / 80;
        double x = Math.floor(big / 1000);
        double remain = big - (x * 1000);
        double y = 1 + ((80) * remain) / 1000;	
        
        double top = -18 - ((y - 5) * 32);
        double left = -46 - ((x - 5) * 32);

        mappanel.setLocation((int)top,(int)left);
        mappanel.setSize(2566,2144);
        mappanel2.setSize(2566,2144);
        mappanel3.setSize(2566,2144);
        mappanel4.setSize(2566,2144);

        if ( mappanel.getX() >= -18)    { mappanel2.setLocation(mappanel.getX()-mappanel.getWidth()+5, mappanel.getY()); } 
        if ( mappanel.getX() <= -2258 ) { mappanel2.setLocation(mappanel.getX()+mappanel.getWidth()-5, mappanel.getY());}        
        if ( mappanel.getY() >= -14) 	{ mappanel3.setLocation(mappanel.getX(),mappanel.getY() - mappanel.getHeight()+63);}
        if (mappanel.getY() <= -1870 )  { mappanel3.setLocation(mappanel.getX(),mappanel.getY()+mappanel.getHeight()-63); }

        if (mappanel.getY() <= -1870 && mappanel.getX() <= -2258) {
        	mappanel4.setLocation(mappanel.getX()+mappanel.getWidth()-5,mappanel.getY()+mappanel.getHeight()-63);
        	}

        if (mappanel.getY() <= -1870 && mappanel.getX() >= -18) { //Bottom Left
        	mappanel4.setLocation(mappanel.getX()-mappanel.getWidth(),mappanel.getY()+mappanel.getHeight()-63);
        }

        if ( mappanel.getX() >= -18 && mappanel.getY() >= -14 ) {
        	mappanel4.setLocation(mappanel.getX()-mappanel.getWidth()+5,mappanel.getY()-mappanel.getHeight()+63);        	
        }
        
        if ( mappanel.getX() <= -2258 && mappanel.getY() >= -14 ) {
        	mappanel4.setLocation(mappanel.getX()+mappanel.getWidth()-5,mappanel.getY()-mappanel.getHeight());        	
        }
        walklabel.setBounds((288/2)-33,(288/2)-42,32,32);
        frame.setTitle("Ivalice:Companion " + (int)x + ":" + (int)y);
    }
    
public static void PrintMap( int loc, int whichicon ){

		ImageIcon pic = new ImageIcon(path+"data\\zone\\"+Integer.toString(whichicon)+".gif");
		JLabel zonelabel = new JLabel();
		zonelabel.setIcon(pic);

		int vnum = (loc - 26000) + 80;
        double big = (vnum * 1000) / 80;
        double x = Math.floor(big / 1000);
        double remain = big - (x * 1000);
        double y = 1 + ((80) * remain) / 1000;	
        
        int top = (int) ((y - 1) * 32);
        int left = (int) ((x) * 32)-10;
        if (pic.getIconWidth() >= 60) { top -= 16; }
        if (pic.getIconHeight() >= 60) {left -= 32;}
	        
	        mappanel.add(zonelabel);
	        zonelabel.setBounds( top, left,pic.getIconWidth(),pic.getIconHeight());
	        zonelabel.setLocation(top,left);
	        zonelabel.setVisible(true);
	        print("Loading Icon "+whichicon+" at Location "+loc+" ("+top+":"+left+")");

}

    public static void PlayMidi(String filename) {
    	try {
    		if ( sequencer == null ) { sequencer = MidiSystem.getSequencer();}
    		//soundfile = System.getProperty("user.dir")+"\\data\\"+filename;
    		soundfile = "http://ivalicemud.com/music/"+filename;
    		if (soundfile.equals(lastfile)) { return;} 
            //Sequence sequence = MidiSystem.getSequence(new File(soundfile)); 
    		Sequence sequence = MidiSystem.getSequence(new URL(soundfile));
            if(sequencer.isRunning()) { sequencer.stop(); sequencer.close(); }
             sequencer.open();
            sequencer.setSequence(sequence);
        
            // Start playing
            sequencer.start();
            sequencer.setLoopCount(1000);
            lastfile = soundfile;
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } catch (MidiUnavailableException e) {
        } catch (InvalidMidiDataException e) {
        }

    }
  
    
    public static void checkinstance() {
    	 try{        
    	        ServerSocket socket = new ServerSocket(9999, 10, InetAddress.getLocalHost());
    	        print(socket.toString());
    	        
    	    }catch(java.net.BindException b){
    	    	msgbox("There is another instance of IV:C already running.");
    	        System.exit(0);
    	    }catch( Exception e ) { 
    	        print(e.toString());
    	    }
    
    }
    public static void msgbox(String msg) {
    	JOptionPane.showMessageDialog(null,msg);
    }
    
	public static void print(String line) {
		System.out.println(line);
		debugbox.setText(line);
		
	}
 
	public static void loadsettings() {
		
	}
	
	public static void savesettings() {
	}
	
}