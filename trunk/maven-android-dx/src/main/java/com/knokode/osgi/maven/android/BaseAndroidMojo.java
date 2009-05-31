package com.knokode.osgi.maven.android;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

public abstract class BaseAndroidMojo extends AbstractMojo 
{
	 /**
     * The artifact factory to use.
     * 
     * @component
     * @required
     * @readonly
     */
    ArtifactFactory artifactFactory;
    
    /**
     * Used to look up Artifacts in the remote repository.
     * 
     * @parameter expression="${component.org.apache.maven.artifact.resolver.ArtifactResolver}"
     * @required
     * @readonly
     */
    protected org.apache.maven.artifact.resolver.ArtifactResolver resolver;
    
    /**
     * List of Remote Repositories used by the resolver
     * 
     * @parameter expression="${project.remoteArtifactRepositories}"
     * @readonly
     * @required
     */
    protected java.util.List remoteRepos; 
    
    /**
     * Location of the local repository.
     * 
     * @parameter expression="${localRepository}"
     * @readonly
     * @required
     */
    protected org.apache.maven.artifact.repository.ArtifactRepository local;        

	public BaseAndroidMojo() {
		super();
	}

	protected Artifact findArtifact(String artifact) throws MojoExecutionException 
	{
		String[] tokens = artifact.split(":");
		if (tokens.length < 3)
		{
			getLog().error("Bundle defined as "+artifact+" doesn't follow the naming convention and will be ignored");
			throw new MojoExecutionException("Cannot proceed");
		}
			
		Artifact foundArtifact = artifactFactory.createArtifact(tokens[0], tokens[1], tokens[2], "compile", "jar");
		
		try {
			resolver.resolve( foundArtifact, remoteRepos, local );
			getLog().info(foundArtifact.getFile()+"");
		} catch (ArtifactResolutionException e) {
			getLog().error("Cannot resolve artifact "+artifact, e);
		} catch (ArtifactNotFoundException e) {
			getLog().error("Cannot find artifact "+ artifact, e);
		}
		return foundArtifact;
	}

}