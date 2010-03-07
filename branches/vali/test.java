//package vali;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.Random;


public class test {
	public static void main(String args[]) throws Exception{
		FileWriter f;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); 
		String str = "";
		(new File("D:/out.txt")).renameTo(new File("D:/out"+(new Random()).nextInt(1000)+".txt"));
		f=new FileWriter("D:/out.txt");
		while (true) { 
			str = in.readLine();
			f.append(str+"\n");
			f.flush();
			if (str.contains("go")) {
				for (int i=2;i<=6;i++){
					System.out.print("move a"+i+"a"+(i+1)+"\n");
					f.write("_move a"+i+"a"+(i+1)+"\n");
					str=in.readLine();
					f.write(str+"\n");
					str=in.readLine();
					f.write(str+"\n");
					
					//System.out.print(str+"\n");
				}
				System.out.print("quit\n");
				f.close();
				return;
//				System.out.print("move g2g3\n");
//				f.append("_m1\n");
//				str = in.readLine();
//				f.append(str+"\n");
//				f.flush();
//				System.out.print("move g3g4\n");
//				f.append("_m1\n");
//				//str = in.readLine();
//				f.append(str+"\n");
//				f.flush();
//				System.out.print("move g4g5\n");
//				f.append("_m1\n");
//				//str = in.readLine();
//				f.append(str+"\n");
//				f.flush();
//				
//				f.close();
//				System.out.print("quit\n");
//				//f.close();
//				break;
//				//return;
			}
			//System.out.println(str);
			if (str.contains("exit"))
				break;
			if (str.contains("quit"))
				break;
			if (str.contains("protover 2"))
				System.out.println("feature usermove=1");
		} 
		//f.close();
	}

//v[0->63] masca de biti
//pentru fiecare pozitie si tip, de exemplu mutare diagonal











}
