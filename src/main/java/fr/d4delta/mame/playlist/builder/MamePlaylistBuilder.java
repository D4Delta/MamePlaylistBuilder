/*
   Copyright (C) 2017 D4Delta
   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 3 of the License, or
   (at your option) any later version.
   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software Foundation,
   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
*/


package fr.d4delta.mame.playlist.builder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author delta
 */
public class MamePlaylistBuilder {
    final static String MAME_LPL = "MAME.lpl";
    
    final static String LPL = 
        "DETECT" + System.lineSeparator() +
        "DETECT" + System.lineSeparator() +
        "00000000|crc" + System.lineSeparator() +
        MAME_LPL;
    
    final static String NAME_DB = "NamesDB.properties";
    
    
    public static void main(String[] args) {
        if(args.length >= 2 && args[0].equalsIgnoreCase("buildNameDB")) {
            createNameDB();
        } else {
            createPlaylist();
        }
    }
    
    static void createPlaylist() {
        JOptionPane.showMessageDialog(null, "Please select your MAME rom directory.");
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File mameRomsDir = chooser.getSelectedFile();
        
        if(mameRomsDir == null) {
            return;
        }
        
        File output = new File(MAME_LPL);
        JOptionPane.showMessageDialog(null, "The playlist will be created after you close this message." + System.lineSeparator() + "It will be saved to: " + output.getAbsolutePath() + System.lineSeparator() + "This may take a while, depending on how much rom you own; You will get a notification when it's done.");
        
        Properties namesDB = new Properties();
        try(InputStream inputStream = MamePlaylistBuilder.class.getResourceAsStream("/" + NAME_DB); PrintWriter writer = new PrintWriter(output)) {
            namesDB.load(inputStream);
            File[] files = mameRomsDir.listFiles((File pathname) -> !pathname.isDirectory());
            for(File f: files) {
                String romName = f.getName();
                int dotIndex = romName.lastIndexOf(".");
                romName = romName.substring(0, dotIndex);
                String fullName = namesDB.getProperty(romName);
                if(fullName != null) {
                    writer.println(f.getAbsolutePath());
                    writer.println(fullName);
                    writer.println(LPL);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong. Here's some details:" + System.lineSeparator() + getStacktrace(ex));
        }
        JOptionPane.showMessageDialog(null, "Done! The playlist has been saved to: " + output.getAbsolutePath());
    }
    
    static void createNameDB() {
        JFileChooser chooser = new JFileChooser();
        JOptionPane.showMessageDialog(null, "Select the mame.xml");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.showOpenDialog(null);
        File mameXMLFile = chooser.getSelectedFile();
        if(mameXMLFile == null) {
            return;
        }
        File output = new File(NAME_DB);
        JOptionPane.showMessageDialog(null, "Name Database will be generated after you close this message." + System.lineSeparator() + "Name Database will be saved in " + output.getAbsolutePath() + "." + System.lineSeparator() + "This will take a while, and I recommend increasing JVM's heap size to avoid swapping.");
        
        //Start doing actual stuff
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try(PrintWriter writer = new PrintWriter(output, "UTF-8")) {
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            Document mameXML = builder.parse(mameXMLFile);
            Element root = mameXML.getDocumentElement();
            NodeList machineList = root.getElementsByTagName("machine");
            HashMap<String, String> romName2Name = new HashMap<>();
            
            for(int i = 0; i < machineList.getLength(); i++) {
                Element machine = (Element)machineList.item(i);
                writer.println(machine.getAttribute("name") + "=" + machine.getElementsByTagName("description").item(0).getTextContent());
            }
            
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something went wrong. Here's some details:" + System.lineSeparator() + getStacktrace(ex));
        }
        JOptionPane.showMessageDialog(null, "Done. The Name database has been saved to: " + output.getAbsolutePath());
    }
    
    static String getStacktrace(Exception ex) {
        try(StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
            ex.printStackTrace(pw);
            return sw.toString();
        } catch (IOException ex1) {
            return "Could not convert exception stacktrace to string.";
        }
        
    }
}
