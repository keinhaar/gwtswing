import java.io.File;
import java.io.IOException;

import de.exware.nobuto.java.JavaBuilder;
import de.exware.nobuto.maven.Maven;
import de.exware.nobuto.maven.MavenDependency;
import de.exware.nobuto.utils.Utilities;

public class Build extends JavaBuilder
{
    private static final String PROJECTNAME = "de.exware.gwtswing";
    
    private File jarFile = new File("dist/" + PROJECTNAME + ".jar");
    private File sourceJarFile = new File("dist/" + PROJECTNAME + "-sources.jar");
    
    public Build() throws IOException, InterruptedException
    {
        super();
        Maven maven = Maven.getDefaultinstance();
        maven.addRepository("file://" + maven.getLocalRepo() + "/");
    }

    @Override
    public void dist() throws Exception
    {
        super.dist();
        jarFile.getParentFile().mkdir();
        jar(jarFile.getAbsolutePath(), "out", null);
    }
    
    public void installToMaven() throws Exception
    {
        clean();
        dist();
        createSourceJar();
        Maven.getDefaultinstance().installJar(jarFile, "de.exware", PROJECTNAME, getVersion());
        Maven.getDefaultinstance().installSourceJar(sourceJarFile, "de.exware", PROJECTNAME, getVersion());
    }

    public void createSourceJar() throws IOException
    {
        File file = new File("tmp/sourceJar");
        Utilities.delete(file, true);
        Utilities.copy("source/java", file, true);
        jar(sourceJarFile.getAbsolutePath(), file, null);
    }
    
    @Override
    public void clean() throws IOException
    {
        Utilities.delete(new File("dist"));
        Utilities.delete(new File("out"));
    }

    @Override
    public void compile() throws Exception
    {      
        addSources("source/java");
        addDependency(new MavenDependency("de.exware", "de.exware.gplatform", "1.0"));
        super.compile();
    }
}
