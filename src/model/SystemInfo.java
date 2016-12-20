package model;

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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;



public class SystemInfo {
	
	static Path filePath;	
	static ObservableList<SystemInfoEntryProperty> systemInfoEntryObservableList;

	public SystemInfo(Path filePath) {
		SystemInfo.filePath = filePath;
		systemInfoEntryObservableList = FXCollections.observableArrayList();
	}

	public static ObservableList<SystemInfoEntryProperty> getSystemInfoEntryObservableList() {
		return systemInfoEntryObservableList;
	}

	public void writeSystemInfoFile(String id) {
		ArrayList<String> browsers;
		systemInfoEntryObservableList.add(new SystemInfoEntryProperty(new SystemInfoEntry("OS Name", System.getProperty("os.name"))));
		systemInfoEntryObservableList.add(new SystemInfoEntryProperty(new SystemInfoEntry("OS Version", System.getProperty("os.version"))));
		systemInfoEntryObservableList.add(new SystemInfoEntryProperty(new SystemInfoEntry("OS Architecture", System.getProperty("os.arch"))));
		browsers = getBrowsers();
		for (int i = 0; i < browsers.size(); i++) {
			systemInfoEntryObservableList.add(new SystemInfoEntryProperty(new SystemInfoEntry("Browser " + String.valueOf(i+1), browsers.get(i))));			
		}
		
		
		
		
		
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
				
				writer.close();
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
		
	}
	
	
	public void openOrCreateSystemInfoFile() {
		File sysInfoFile = new File(filePath.toString());
		try {
			sysInfoFile.delete();
			sysInfoFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
    
    /**
     *  Function gets machine's installed browsers.
     *  (Supported browsers: Chrome, Firefox, Opera, Chromium, Internet Explorer)
     *  @return String of the installed browsers
     */
    public static ArrayList<String> getBrowsers(){
        
    	ArrayList<String> browsers = new ArrayList<String>();
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("linux")){
            String tmp;
            tmp = unixTermOut("google-chrome --version");
            if (tmp != null)
                browsers.add(tmp);
            tmp = unixTermOut("firefox --version");
            if (tmp != null)
                browsers.add(tmp);
            String opVer = unixTermOut("opera --version");
            if (opVer != null){
                tmp = "Opera " + unixTermOut("opera --version");
                browsers.add(tmp);
            }
            tmp = unixTermOut("chromium-browser --version");
            if (tmp != null)
                browsers.add(tmp);
        }
        else if(os.contains("windows")) {
            
            // IE
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
        else if(os.contains("mac")){
        
        }
        else {
            browsers.add("Unrecognized OS");
        }
        return browsers;
    }
 
    
    /**
     *  Function creates a linux bash and returns its output.
     *  @param cmd  Command to launch
     *  @return     Command's result
     */
    private static String unixTermOut(String cmd){
        String[] args = new String[] {"/bin/bash", "-c", cmd};
            String out = "";
            try {
                Process proc = new ProcessBuilder(args).start();
                BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                out = br.readLine();
            } catch (IOException ex) {
            	ex.printStackTrace();
            }
            return out;
    }


	
	
}
