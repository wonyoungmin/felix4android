package com.knokode.osgi.maven.android;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;


/**
 * Creates a classes.dex file
 * 
 * dx --dex --output=classes.dex <>
 * 
 * @goal dx
 * 
 * @phase install
 */
public class DxMojo extends BaseAndroidMojo
{
    /**
     * artifact to process
     * 
     * @parameter
     * @required 
     */
    private String artifact;    
    
    /**
     * output file name
     * 
     * @parameter 
     * @required
     */
    private String output;

    
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		String tmpClasses = "classes.dex";
		
		new File(this.output).mkdirs();
		Artifact foundArtifact = findArtifact(this.artifact);
		
		File newFile = new File(this.output+File.separator+foundArtifact.getFile().getName());
		try {
			FileUtils.copyFile(foundArtifact.getFile(), newFile);
		} catch (IOException e1) {
			throw new MojoFailureException("Cannot execute the dx command", e1);
		}
	
		String command = "dx --dex --output=" + tmpClasses + " "+newFile.getAbsolutePath();
		getLog().info(command);
        try 
        {
			Process child = Runtime.getRuntime().exec(command);
			waitForFile(tmpClasses);
		} 
        catch (Throwable e) 
		{
			throw new MojoFailureException("Cannot execute the dx command", e);
		}
   
        
		command = "aapt add "+newFile.getAbsolutePath()+ " " +tmpClasses;
		getLog().info(command);
        try 
        {
			Process child = Runtime.getRuntime().exec(command);
			Thread.sleep(1000l);
		} 
        catch (Throwable e) 
		{
			throw new MojoFailureException("Cannot execute the dx command", e);
		}
        
        
        new File(tmpClasses).delete();
	}


	private void waitForFile(String tmpClasses) 
	{
		long timeout = 60000l;
		long wait = 500l;
		
		while (timeout > 0)
		{
			if (new File(tmpClasses).exists())
				return;
			timeout -= wait;
			
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		getLog().error("Could not create file "+tmpClasses+" in a minute. The build process is probably failed");
		
	}    
}
