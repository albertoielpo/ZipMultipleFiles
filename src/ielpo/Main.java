package ielpo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author Alberto Ielpo
 * @version 1.0_20161211_1535
 * @category Utility
 * Zip Multiple Files
 *
 */
public class Main {
	
	/**
	 * 
	 * @param root
	 * @param currentFilesToZip
	 * @param zipname
	 * @param bufferLength
	 * @param sleeptime
	 */
	private static void zipFiles(String root, List<String> currentFilesToZip, String zipname,int bufferLength, int sleeptime ) {
		System.out.println("--> zipFiles");
		String zipFile = root + File.separator + zipname + ".zip";
		System.out.println("Creating zip: " + zipFile);

		List<String> fileToZipNoDir = new ArrayList<String>();

		for(String currentFile : currentFilesToZip){
			/* 
			 * Are allowed only files into the zip 
			 */
			File checkDir = new File(root + File.separator + currentFile);
			if(!checkDir.isDirectory()){
				fileToZipNoDir.add(root + File.separator + currentFile);
				System.out.println("-> " + root + File.separator + currentFile);
			}else{
				System.out.println("-> " + currentFile + " is a directory and cannot be included");
			}
		}
		
		String[] finalFiles = (String[]) fileToZipNoDir.toArray(new String[fileToZipNoDir.size()]);
				
		try {
	        // create byte buffer
	        byte[] buffer = new byte[bufferLength];
	        FileOutputStream fos = new FileOutputStream(zipFile);
	        ZipOutputStream zos = new ZipOutputStream(fos);
	
	        for (int i=0; i < finalFiles.length; i++) {
	        	File srcFile = new File(finalFiles[i]);
	        	FileInputStream fis = new FileInputStream(srcFile);
	        	// begin writing a new ZIP entry, positions the stream to the start of the entry data
	        	zos.putNextEntry(new ZipEntry(srcFile.getName()));
	
	        	int length;
	        	
	        	/*
	        	 * sleeptime is introduced to reduce the CPU consumption
	        	 * sleeptime = 0 => max CPU consumption
	        	 * sleeptime = 1 => around 50% CPU consumption
	        	 */
	        	if(sleeptime == 0){
		        	while ((length = fis.read(buffer)) > 0) {
		        		zos.write(buffer, 0, length);
		        	}
	        	}else{
		        	while ((length = fis.read(buffer)) > 0) {
		        		zos.write(buffer, 0, length);
		        		try{
		        			Thread.sleep(sleeptime);
		        		}catch(Exception e){
		        			
		        		}
		        	}
	        	}
	        	
	        	zos.closeEntry();
	
	        	// close the InputStream
	            fis.close();
	        }
	        // close the ZipOutputStream
        	zos.close();
		} catch (IOException ioe) {
			System.out.println("Error creating zip file: " + ioe);
        }		
	}
	
	/**
	 * 
	 * @param dir
	 * @param fileIntoZip
	 * @param zipname
	 * @param startCounterValue
	 * @param bufferLength
	 * @param sleeptime
	 */
	private static void listAndZip(File dir, int fileIntoZip, String zipname, int startCounterValue, int bufferLength, int sleeptime){
	
		String root = dir.getPath();
		String[] files = dir.list();
		Arrays.sort(files);
			
		int totalFiles = files.length;	//Total files into a directory
		if(fileIntoZip > totalFiles){
			fileIntoZip = totalFiles;
		}

		int totalZipNumber = totalFiles / fileIntoZip;
		int modTotalZipNumber = totalFiles % fileIntoZip;
		int k=0;
		int counterValue = startCounterValue;
		
		for(int i=0; i<totalZipNumber; i++){
			List<String> currentFilesToZip = new ArrayList<String>();
			for(int j=0; j<fileIntoZip; j++, k++){
				currentFilesToZip.add(files[k]);
			}
			
			/* I'm gonna zip the files */
			zipFiles(root, currentFilesToZip, zipname + counterValue, bufferLength, sleeptime);
			counterValue++;
		}
		
		if(modTotalZipNumber > 0){
			List<String> currentFilesToZip = new ArrayList<String>();
			for(;k<files.length;k++){
				currentFilesToZip.add(files[k]);
			}
			zipFiles(root, currentFilesToZip, zipname + counterValue, bufferLength, sleeptime);

		}
				
	}
	

	/**
	 * 
	 * @param args
	 * * args[0] = directory => working dir (mandatory)
	 * * args[1] = files quantity => numbers of files per zip (mandatory)
	 * * args[2] = zip name => output name (mandatory)
	 * * args[3] = start counter value => default 0
	 * * args[4] = buffer length => default 16 KB buffer (16384)
	 * * args[5] = sleep time => To reduce CPU consumption (default 1ms)
	 */
	public static void main(String[] args) {
		System.out.println("Start: " + new Date());
		File dir;
		int fileIntoZip = 0;
		String zipname = "";
		
		if(args.length > 2
				&& !"".equalsIgnoreCase(args[0])
				&& !"".equalsIgnoreCase(args[1])
				&& !"".equalsIgnoreCase(args[2])){

			
			dir = new File(args[0]);
			
			if(dir.isDirectory()){
				try{
					fileIntoZip = Integer.valueOf(args[1]);
					if(args[2] != null && args[2].length()>0){
						zipname = args[2];
						int startCounterValue = 0;
						int bufferLength = 16384; //16KB buffer
						int sleeptime = 1;	//1ms sleeptime
						
						if(args.length >3 && args[3] != null){
							startCounterValue = Integer.valueOf(args[3]);
							if(args.length >4 && args[4] != null){
								bufferLength = Integer.valueOf(args[4]);
								if(args.length >5 && args[5] != null){
									sleeptime = Integer.valueOf(args[5]);
								}
							}
							
						}
						
						listAndZip(dir, fileIntoZip, zipname, startCounterValue, bufferLength, sleeptime);
						
					}else{
						System.out.println(zipname + " is not a valid zip name");
					}
					
				}catch(NumberFormatException e){
					System.out.println(args[1] + " is not a valid number");
					
				}
								
			}else{
				System.out.println(dir.getAbsolutePath() + " is not a valid directory");
			}
			
		}else{
			System.out.println("HELP:> java -jar ZipMultipleFiles.jar (WORKING_DIR) (FILES_NUMBER) (ZIP_NAME) [START_COUNTER_VALUE] [BUFFER_LENGTH] [SLEEP_TIME]");
		}
		
		System.out.println("End: " + new Date());

	}

}
