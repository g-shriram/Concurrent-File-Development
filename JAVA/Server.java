
import java.io.*; 
import java.text.*; 
import java.util.*; 
import java.net.*; 

class creater extends Thread{
	Scanner in=new Scanner(System.in);
	String fname;
	int no,i;
	List<String> message,user;
	List<Integer> userthread;
	HashMap<Integer,String> comp,para;
	HashMap<Integer,ClientHandler>threadobject;
    HashMap<Integer,ArrayList<String>>temppara;
	HashMap<Integer,Integer>write,read;
	HashMap<Integer,String>writeuser,readuser;
	creater(){
		comp=new HashMap<Integer,String>();
		para=new HashMap<Integer,String>();
		temppara=new HashMap<Integer,ArrayList<String>>();
		threadobject=new HashMap<Integer,ClientHandler>();
		write=new HashMap<Integer,Integer>();
		writeuser=new HashMap<Integer,String>();
		read=new HashMap<Integer,Integer>();
		message=new ArrayList<String>();
		user=new ArrayList<String>();
		userthread=new ArrayList<Integer>();
		System.out.println("Enter file name : ");
		fname=in.next();
		System.out.println("Enter no of components : ");
		no=in.nextInt();
		for(i=0;i<no;i++)
		{
			read.put(i, 0);
			write.put(i, 0);
			comp.put(i, "");
			temppara.put(i,new ArrayList<String>());
			}
		
		for(i=0;i<no;i++) {
			System.out.println("Enter name of component :"+(i+1));
			comp.put(i, in.next());
		}
		
		
		
	}
	
	void viewcomp(DataOutputStream dos, DataInputStream dis) throws IOException {
		String text="File : "+fname+"\nComponents : \n";
		
		for(i=0;i<no;i++) {
			text=text+(i+1)+" - "+comp.get(i)+'\n';
		}
		
		dos.writeUTF(text);dos.flush();
		
	}
	
	@Override
	public void run() 
	{ 
	String command;
	try {
		while (true) 
		{ 
			command=in.next();
			switch(command) {
			case "man":displaycommands();break;
			case "users":displayuser();break;
			case "deleteuser":deleteuser();break;
			case "viewrequest":req();break;
			case "viewcomp":viewcom();break;
			case "writefile":writefile();break;
			case "readfile":readfile();break;
			default:System.out.println("Invalid command");
			}
				} 
		
	}
		catch(Exception e) {
			e.printStackTrace();
		}
	
	}

	private void displaycommands() {
		System.out.println("users - To Display users");
		System.out.println("deleteuser - To remove users");
		System.out.println("viewrequest - To commit/discard pull requests");
		System.out.println("viewcomp - To Display current components");
		System.out.println("writefile - To update components into file");
		System.out.println("readfile - To view current file");
		
	}

	private void readfile() {
		DataInputStream dout=null;
		try {
		FileInputStream fout=new FileInputStream(fname+".txt");
		dout=new DataInputStream(fout);
		}catch(Exception e) {
			System.out.println("File not created yet...");
		}
		try {
			String x;
			while(( x=dout.readUTF())!=null ) {
				System.out.println(x);
			}
		}
		catch(Exception e) {
			
		}
		
	}

	private void writefile() throws IOException {
		FileOutputStream fout=new FileOutputStream(fname+".txt");
		DataOutputStream dout=new DataOutputStream(fout);
	  dout.writeUTF("File name : "+fname+"\n\n");
		  for(i=0;i<no;i++) {
			  dout.writeUTF("Component : "+comp.get(i)+"\n");
			try {  dout.writeUTF(para.get(i));}
			catch(Exception e) {break;}
		  }
		
	}

	private void viewcom() {
		int ch;
		for(ch=0;ch<no;ch++) {
		    System.out.println("Component - "+(ch+1)+"\n"+para.get(ch));
		}
		
	}

	private void req() {
		for(i=0;i<no;i++) {
			if(temppara.get(i).size()!=0) {
				for(int j=0;j<temppara.get(i).size();j++) {
					System.out.println("Component "+(j+1));
					System.out.println("Submitted : "+temppara.get(i).get(j));
					System.out.println("Changes : ");
					
					String text=para.get(i),text1=temppara.get(i).get(j);
					
					if(text!=null) {
					for(int k=0;k<text.length() && k<text1.length();k++) {
						if(text.charAt(k)!=text1.charAt(k)) {
							System.out.println(text.charAt(k)+"\t :"+text1.charAt(k));
						}
					}
					}
					else {
						for(int k=0;k<text1.length();k++) {
							
								System.out.println(text1.charAt(k));
							
						}
					}
					System.out.println("Press 1 to commit...\n2 to discard...");
					int ch=in.nextInt();
							if(ch==1) {
								para.put(i, text1);
								temppara.get(i).remove(j);
								System.out.println("\nSuccessfully comitted !");
							}
							else {
								temppara.get(i).remove(j);
								System.out.println("\nSuccessfully discarded !");
							}
				}
			}
		}
		
	}

	private void deleteuser() throws IOException {
		try{
			System.out.println("Enter user name : ");
			int n=99999;
			String nam=in.next();
			for(i=0;i<user.size();i++) {
				if(user.get(i).equals(nam)) {
					n=i;
				}
			}	
			
			if(n==99999)
			{
				System.out.println("User  "+nam+" is not found...");
				return;
			}
			
		threadobject.get(userthread.get(n)).dos.writeUTF("Block");
		   
		threadobject.get(userthread.get(n)).s.close();
		threadobject.get(userthread.get(n)).dis.close();
		threadobject.get(userthread.get(n)).dos.close();
			threadobject.remove(userthread.get(n));
			user.remove(n);
			userthread.remove(n);
	System.out.println("User  "+nam+" is removed...");
		}
		catch(Exception e) {
			System.out.println("User not found...");
		}
	}

	private void displayuser() {
		
		for(i=0;i<user.size();i++) {
			System.out.println("Id : "+(i+1)+"\tName : "+user.get(i)+"\tThread : "+userthread.get(i));
		}
		
	}
	 void senduser(DataOutputStream dos, DataInputStream dis) throws IOException {
			String users="";
			for(i=0;i<user.size();i++) {
				users=users+"\nId : "+(i+1)+"\tName : "+user.get(i)+"\tThread : "+userthread.get(i);
			}
			
			
	             dos.writeUTF(users);dos.flush();
	             
	             
	}
	public void readcomp(DataInputStream dis, DataOutputStream dos,int j) throws IOException {

	
		 String text=null;
			
			text=para.get(j);
					    
		if(text==null)
			text="Component not yet written...";
	
	
		
		dos.writeUTF(text+"\nEnter any key to finish reading...");
		String flush=dis.readUTF();
	}

	public void writecomp(DataInputStream dis, DataOutputStream dos, int j, String name) throws IOException {
		dos.writeUTF("Write");
		dos.writeUTF(comp.get(j));
		dos.writeUTF("Temp file for component is created ...\nU can view changes in file.");dos.flush();
		if(para.get(j)!=null)
			dos.writeUTF(para.get(j));
		else
			dos.writeUTF("");
		String x=dis.readUTF(),y ="";
		while(!x.equals("Over")) {
			y=y+x;
			x=dis.readUTF();
		}
		dos.writeUTF("Type commit to upload ur changes...");
			
		while(!dis.readUTF().equals("commit"))
			dos.writeUTF("Please commit your changes before doing any task.");
		temppara.get(j).add(y);
		
		System.out.println("\nNew pull request from :"+name);
		dos.writeUTF("Committed Successfully !");

		
	}

	public void message(DataInputStream dis, DataOutputStream dos,String name) throws IOException {
		dos.writeUTF("Enter user name :");dos.flush();
		String nam=dis.readUTF();
		int  n=99999;
		for(i=0;i<user.size();i++) {
			if(user.get(i).equals(nam)) {
				n=i;
			}
		}	
		
		if(n==99999)
		{
			dos.writeUTF("User  "+nam+" is not found...");dos.flush();
			return;
		}
		
		try {
			dos.writeUTF("Enter your  message : ");dos.flush();	
		threadobject.get(userthread.get(n)).dos.writeUTF("Message : "+dis.readUTF()+"\tfrom : "+name);
		System.out.println("Message sent  from : "+name+" to : "+user.get(n));
		dos.writeUTF("Message sent...");dos.flush();
		}catch(Exception e) {
			dos.writeUTF("Message not sent...");dos.flush();
		}
	}
}

// Server class 
public class Server 
{ 
	
	public static void main(String[] args) throws IOException 
	{ 
		
		creater c=new creater();
		c.start();
		
		InetAddress ip ;
		ip=InetAddress.getLocalHost();
		System.out.println("Hostname : "+ip.getHostName());
		System.out.println("Ip : "+ip);
		// server is listening on port 5056 
		ServerSocket ss = new ServerSocket(5056); 
		System.out.println("System ready ..."); 
		
		// running infinite loop for getting 
		// client request 
		while (true) 
		{ 
			Socket s = null; 
			
			try
			{ 
				// socket object to receive incoming client requests 
				s = ss.accept(); 
				
				
				
				// obtaining input and out streams 
				DataInputStream dis = new DataInputStream(s.getInputStream()); 
				DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
				

				// create a new thread object 
				Thread t = new ClientHandler(s, dis, dos,c); 

				// Invoking the start() method 
				t.start(); 
				
			} 
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
	} 
} 

// ClientHandler class 
class ClientHandler extends Thread 
{ 
	DateFormat fordate = new SimpleDateFormat("yyyy/MM/dd"); 
	DateFormat fortime = new SimpleDateFormat("hh:mm:ss"); 
	final DataInputStream dis; 
	final DataOutputStream dos; 
	final Socket s; 
	String name;
	int seckey;
	creater c;
	// Constructor 
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, creater c) 
	{ 
		this.s = s; 
		this.dis = dis; 
		this.dos = dos;
		this.c=c;
	} 
	
	void exit(){
		try
		{ 
			// closing resources 
			this.dis.close(); 
			this.dos.close(); 
			
		}catch(IOException e){ 
			e.printStackTrace(); 
		} 
	}

	@Override
	public void run() 
	{ 
		String received; 
		String toreturn; 
	 
			try { 
				InetAddress ip = InetAddress.getByName("localhost"); 
				dos.writeUTF("Connected to "+ip+"\nYour Name Please ?");dos.flush();
				name=dis.readUTF();
				System.out.println("Assigning new thread for this client : "+name); 
				dos.writeUTF("Welcome "+name+"\nEnter secret code to verify : ");dos.flush();
			
				seckey=new Random().nextInt()/100000;
				if(seckey<0)
					seckey=seckey*-1;
				System.out.println("Secret code for "+name+" : "+seckey);
				if(seckey!=Integer.valueOf(dis.readUTF())){
					System.out.println("Authentication failed for "+name);
					dos.writeUTF("Authentication failed... ");dos.flush();
					s.close();
					this.dis.close(); 
					this.dos.close(); 
					stop();
				}
				System.out.println("Authentication Succeeded for "+name);
				   System.out.println(name + " joined the team...");
				c.user.add(name);
				c.userthread.add((int)getId());
				c.threadobject.put((int)getId(),this);
				// Ask user what he wants 
				int choice;
				do {
					dos.writeUTF("\nPress 1 to view component of file...\nPress 2 to read component...\nPress 3 to write component...\nPress 4 to view peers\nPress 5 to message...\nPress 0 to exit ");
					dos.flush();
					choice=Integer.valueOf(dis.readUTF());
				
					
				switch(choice) {
				case 1:c.viewcomp(dos,dis);break;
				case 2:
					   dos.writeUTF("Enter component no : ");dos.flush();
					   int j=Integer.valueOf(dis.readUTF())-1;
						if(j>=c.no) {
							dos.writeUTF("Component not found...\n");
							
							break;
						}
				
					   if(c.write.get(j)==1) {
						   dos.writeUTF("The component is under development by "+c.writeuser.get(j)+"\nPress any key to continue...");
						 
				     	   break;
					   }
					   System.out.println(name+" reading component "+(j+1));
					   c.read.put(j,c.read.get(j)+1);
					   c.readcomp(dis,dos,j);
					   c.read.put(j,c.read.get(j)-1);
					   System.out.println(name+" finished reading component "+(j+1));
					   break;
				case 3:
				   	 dos.writeUTF("Enter component no : ");dos.flush();
					   j=Integer.valueOf(dis.readUTF())-1;
					   if(j>=c.no) {
							dos.writeUTF("Component not found...\n");
						
							break;
						}
					   if(c.write.get(j)==1) {
						   dos.writeUTF("The component is under development by "+c.writeuser.get(j)+"...");
						
				     	   break;
					   }
					   if(c.read.get(j)>0) {
						   dos.writeUTF("The component is under read by someone...");
						  
				     	   break;
					   }
					   c.write.put(j,1);
					   System.out.println(name+" writing component "+(j+1));
					   c.writeuser.put(j,name);
					   c.writecomp(dis,dos,j,name);
					   c.write.put(j,0);
					   System.out.println(name+" finished writing component "+(j+1));
					   break;
				case 4:c.senduser(dos,dis);break;
				case 5:c.message(dis,dos,name);break;
				case 0:break;
				default:dos.writeUTF("Enter valid option...");dos.flush();
				}
				}while(choice!=0);
				
				// receive the answer from client 
				
				
			} catch (IOException e) { 
				e.printStackTrace(); 
			} 
		exit();
		
		
	} 
} 