

import java.io.*; 
import java.net.*; 
import java.util.Scanner; 

// Client class 
public class Client 
{ 
	public static void main(String[] args) throws IOException 
	{ 
		try
		{ 
			Scanner scn = new Scanner(System.in); 
			System.out.println("Enter server ip address");
			// getting localhost ip 
			String ip=scn.nextLine();
			


			Socket	s = new Socket(ip, 5056); 

	
			// obtaining input and out streams 
			DataInputStream dis = new DataInputStream(s.getInputStream()); 
			DataOutputStream dos = new DataOutputStream(s.getOutputStream()); 
			String reply="";
			Thread wr=new write(s,dos,dis);
			wr.start();	
			Thread r=new  read(s,dis,dos,wr);
			r.start();
					
			// the following loop performs the exchange of 
			// information between client and client handler 
			
	
		}catch(Exception e){ 
			e.printStackTrace(); 
		} 
	}

	
}

class read extends Thread{
	Socket s;
	final DataInputStream dis;
	private DataOutputStream dos;
	private Thread wr; 
	

	public read(Socket s2, DataInputStream dis, DataOutputStream dos, Thread wr) {
		this.s=s2;
		this.dis=dis;
		this.dos=dos;
		this.wr=wr;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{ 
		while (true) 
		{ 

			try {
			
				String reply=dis.readUTF();
				if(reply.equals("Block")) {
					 System.out.println("Connection Lost...You are removed by admin...");
					 s.close(); 
						System.exit(0);
			}
				if(reply.equals("Write")) {
					 write();
			}
				
				System.out.println(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
	}
}

	private void write() throws IOException {
		Scanner in=new Scanner(System.in); 
	  wr.suspend();
		String compname=dis.readUTF();
		System.out.println(dis.readUTF());
		compname=compname+".txt";
		File file=new File(compname);
		if(!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fout=new FileOutputStream(file);
		DataOutputStream out=new DataOutputStream(fout);
		String c=dis.readUTF();
		out.writeUTF(c);
		out=new DataOutputStream(fout);
		System.out.println("Currently :\n"+c);
		
		System.out.println("Start Editting :\n(Type over to stop)\n");
		wr.resume();
	
			 
		

	}


}


class write extends Thread{
	Socket s;
	final DataOutputStream dos;
	private DataInputStream dis; 
	public write(Socket  s,DataOutputStream dos, DataInputStream dis) {
		this.s=s;
		this.dos=dos;
		this.dis=dis;
	}

	@Override
	public void run() 
	{ 
		while (true) 
		{ 
		try {
			dos.writeUTF(new Scanner(System.in).nextLine());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			break;
			
		}
		
	}
		
	}
}
 