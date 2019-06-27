package com.github.qianniancc.q2a;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class Q2A extends JavaPlugin implements Listener{
	
	private String[] list={"����ÿͻ�����ʾ��ƷID"};
	private String[] request={"�ͻ��˿�ݼ�Alt+F3"};
	private boolean debug=false;
	private String[] errors={"Exception","[Server thread/ERROR]"};
	private String[] warns={"[Server thread/WARN]"};
	
	public static String txt2String(File file) throws FileNotFoundException{
        String echo = "";
        Scanner in = new Scanner(file);
        try{
            while (in.hasNextLine()) {
            	
                String str = in.nextLine();
                if(str.contains("A detailed walkthrough of the error, its code path and all known details is as follows")){
            		break;
            	}
            	echo+=str+"\n";
            }
            
            in.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return echo;
    }
	
	public void onEnable()
	{
	    this.getServer().getPluginManager().registerEvents(this, this);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		
		
		if(!sender.isOp()){
			sender.sendMessage("��c��l�������OP����ʹ�ñ����");
			return false;
		}
	    if (cmd.getName().equals("q2a")) {
	    	if(args.length>0){
	    		
	    		if(args[0].equals("encode")){
	    			
	    			
	    			if(System.getProperty("os.name").toLowerCase().contains("win".toLowerCase())){
	    				sender.sendMessage("������룺ANSI/GB2312/GBK");
	    			}else{
	    				sender.sendMessage("������룺UTF-8/Unicode");
	    			}
	    			
	    		}
	    		
	    		if(args[0].equals("debug")){
	    			
	    			if(debug){
	    				debug=false;
	    				sender.sendMessage("DEBUG�Ѿ��ر�");
	    			}else{
	    				debug=true;
	    				sender.sendMessage("DEBUG�Ѿ�����");
	    			}
	    			
	    		}
	    		
	    		if(args[0].equals("crash")){
	    			
	    			try {
						File path = new File("crash-reports");
						File[] files = path.listFiles();
		    			Arrays.sort(files, new Comparator<File>() {
		    			   @Override
		    			   public int compare(File file1, File file2) {
		    			      return (int)(file2.lastModified()-file1.lastModified());
		    			   }
		    			});
		    			sender.sendMessage(txt2String(files[0]));
		    		
					} catch (Exception e) {
						sender.sendMessage("��c��l����������Mod����û�б�����");
						return false;
					}
	    		
	    			
	    		}
	    		
	    		if(args[0].equals("error")){
	    			
	    			String echo="";
	    			try {
	    	            Scanner in = new Scanner(new File("logs/latest.log"));
	    	 
	    	            while (in.hasNextLine()) {
	    	                String str = in.nextLine();
	    	                for(int i=0;i<errors.length;i++){
	    	                	if(str.toLowerCase().contains(errors[i].toLowerCase())){
	    	                		if(echo.contains(str)){
	    	                			break;
	    	                		}
		    	                	echo+=str+"\n";
		    	                	break;
		    	                }
	    	                }
	    	                
	    	            }
	    	            
	    	            in.close();
	    	        } catch (Exception e) {
	    	            sender.sendMessage("��c��l���ش�������ϵ������");
	    	        }
	    			
	    			sender.sendMessage(echo);
	    		}
	    		
	    		if(args[0].equals("warn")){
	    			
	    			String echo="";
	    			try {
	    	            Scanner in = new Scanner(new File("logs/latest.log"));
	    	 
	    	            while (in.hasNextLine()) {
	    	                String str = in.nextLine();
	    	                for(int i=0;i<warns.length;i++){
	    	                	if(str.toLowerCase().contains(warns[i].toLowerCase())){
	    	                		if(echo.contains(str)){
	    	                			break;
	    	                		}
		    	                	echo+=str+"\n";
		    	                	break;
		    	                }
	    	                }
	    	                
	    	            }
	    	            
	    	            in.close();
	    	        } catch (FileNotFoundException e) {
	    	            e.printStackTrace();
	    	        }
	    			
	    			sender.sendMessage(echo);
	    		}
	    		
	    		
	    		if(args[0].equals("list")){
	    			
	    			if(args.length==1){
	    				for(int i=0;i<list.length;i++){
		    				sender.sendMessage((i+1)+"��"+list[i]);
		    			}
	    			}else{
	    				Pattern pattern = Pattern.compile("[0-9]*");
	    				if(pattern.matcher(args[1]).matches()){
	    					try {
								sender.sendMessage("�ʣ�"+list[Integer.valueOf(args[1])-1]);
								sender.sendMessage("��"+request[Integer.valueOf(args[1])-1]);
							} catch (Exception e) {
								sender.sendMessage("��c��l�������");
							}
	    				}else{
	    					sender.sendMessage("��c��l��������");
	    				}
	    				
	    			}	
	    		}
	    		if(args[0].equals("pl")){
	    			
	    			if(args.length==1){
    					
    					this.getServer().dispatchCommand(sender, "pl");	
	    				
	    			}else{
	    				try {
							String v=this.getServer().getPluginManager().getPlugin(args[1]).getDescription().getVersion();
							sender.sendMessage("�����"+args[1]);
							sender.sendMessage("�汾"+v);
	    				} catch (Exception e) {
							sender.sendMessage("��c��l�������");
							sender.sendMessage("��c��l�����Ҫ���Сд������ͬ������");
						}	
	    			}
	    		}
	    	}else{
	    		this.help(sender);
	    	} 
	    }
		return false;
	}
	@EventHandler
	public void onCmd(PlayerCommandPreprocessEvent e)
	{
		if(!e.getPlayer().isOp()){
			return;
		}
		if(debug){
			String cmd = new String(e.getMessage());
			cmd = cmd.toLowerCase();
			if (cmd.startsWith("/")){
				cmd = cmd.substring(1).trim();
			}
			String [] arr = cmd.split("\\s+");
			e.getPlayer().sendMessage("ָ�"+e.getMessage());
			try {
				String pl=this.getServer().getPluginCommand(arr[0]).getPlugin().getName();
				e.getPlayer().sendMessage("���������"+pl);
				String v=this.getServer().getPluginManager().getPlugin(pl).getDescription().getVersion();
				e.getPlayer().sendMessage("����汾��"+v);
				
			} catch (Exception e1) {
				e.getPlayer().sendMessage("��ָ��δ�������");
			}
		}
	}
	public void help(CommandSender sender) {
		sender.sendMessage("[Q2A] -> Question & Answer");
		sender.sendMessage("/q2a encode�鿴�������");
		sender.sendMessage("/q2a list�鿴�����б�");
		sender.sendMessage("/q2a list <���>�鿴���");
		sender.sendMessage("/q2a pl�鿴����б�");
		sender.sendMessage("/q2a pl <���Ӣ����>�鿴����汾");
		sender.sendMessage("/q2a debug����ָ����Դ(��/��)");
		sender.sendMessage("/q2a error�������˴���");
		sender.sendMessage("/q2a warn�������˾���");
		sender.sendMessage("/q2a crash�鿴����Mod������־");
	}
}
