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
	
	private String[] list={"如何让客户端显示物品ID"};
	private String[] request={"客户端快捷键Alt+F3"};
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
			sender.sendMessage("§c§l你必须是OP才能使用本插件");
			return false;
		}
	    if (cmd.getName().equals("q2a")) {
	    	if(args.length>0){
	    		
	    		if(args[0].equals("encode")){
	    			
	    			
	    			if(System.getProperty("os.name").toLowerCase().contains("win".toLowerCase())){
	    				sender.sendMessage("所需编码：ANSI/GB2312/GBK");
	    			}else{
	    				sender.sendMessage("所需编码：UTF-8/Unicode");
	    			}
	    			
	    		}
	    		
	    		if(args[0].equals("debug")){
	    			
	    			if(debug){
	    				debug=false;
	    				sender.sendMessage("DEBUG已经关闭");
	    			}else{
	    				debug=true;
	    				sender.sendMessage("DEBUG已经开启");
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
						sender.sendMessage("§c§l服务器不是Mod服或没有崩溃过");
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
	    	            sender.sendMessage("§c§l严重错误，请联系开发者");
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
		    				sender.sendMessage((i+1)+"、"+list[i]);
		    			}
	    			}else{
	    				Pattern pattern = Pattern.compile("[0-9]*");
	    				if(pattern.matcher(args[1]).matches()){
	    					try {
								sender.sendMessage("问："+list[Integer.valueOf(args[1])-1]);
								sender.sendMessage("答："+request[Integer.valueOf(args[1])-1]);
							} catch (Exception e) {
								sender.sendMessage("§c§l该项不存在");
							}
	    				}else{
	    					sender.sendMessage("§c§l参数错误");
	    				}
	    				
	    			}	
	    		}
	    		if(args[0].equals("pl")){
	    			
	    			if(args.length==1){
    					
    					this.getServer().dispatchCommand(sender, "pl");	
	    				
	    			}else{
	    				try {
							String v=this.getServer().getPluginManager().getPlugin(args[1]).getDescription().getVersion();
							sender.sendMessage("插件名"+args[1]);
							sender.sendMessage("版本"+v);
	    				} catch (Exception e) {
							sender.sendMessage("§c§l该项不存在");
							sender.sendMessage("§c§l插件名要求大小写必须相同，请检查");
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
			e.getPlayer().sendMessage("指令："+e.getMessage());
			try {
				String pl=this.getServer().getPluginCommand(arr[0]).getPlugin().getName();
				e.getPlayer().sendMessage("触发插件："+pl);
				String v=this.getServer().getPluginManager().getPlugin(pl).getDescription().getVersion();
				e.getPlayer().sendMessage("插件版本："+v);
				
			} catch (Exception e1) {
				e.getPlayer().sendMessage("该指令未触发插件");
			}
		}
	}
	public void help(CommandSender sender) {
		sender.sendMessage("[Q2A] -> Question & Answer");
		sender.sendMessage("/q2a encode查看所需编码");
		sender.sendMessage("/q2a list查看问题列表");
		sender.sendMessage("/q2a list <序号>查看解答");
		sender.sendMessage("/q2a pl查看插件列表");
		sender.sendMessage("/q2a pl <插件英文名>查看插件版本");
		sender.sendMessage("/q2a debug监听指令来源(开/关)");
		sender.sendMessage("/q2a error输出服务端错误");
		sender.sendMessage("/q2a warn输出服务端警告");
		sender.sendMessage("/q2a crash查看最新Mod崩溃日志");
	}
}
