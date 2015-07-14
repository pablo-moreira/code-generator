package br.com.atos.cg;



import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ClassUtils;
import org.apache.velocity.util.ExceptionUtils;

public class CgResourceLoader extends ResourceLoader
{

    public void init( ExtendedProperties configuration)
    {
        if (log.isTraceEnabled())
        {
            log.trace("ClasspathResourceLoader : initialization complete.");
        }
    }

    /**
     * Get an InputStream so that the Runtime can build a
     * template with it.
     *
     * @param name name of template to get
     * @return InputStream containing the template
     * @throws ResourceNotFoundException if template not found
     *         in  classpath.
     */
    public InputStream getResourceStream( String name ) throws ResourceNotFoundException {
        
    	InputStream result = null;

        if (StringUtils.isEmpty(name))
        {
            throw new ResourceNotFoundException ("No template name provided");
        }

        /**
         * look for resource in thread classloader first (e.g. WEB-INF\lib in
         * a servlet container) then fall back to the system classloader.
         */

        
        

        return result;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#isSourceModified(org.apache.velocity.runtime.resource.Resource)
     */
    public boolean isSourceModified(Resource resource)
    {
        return false;
    }

    /**
     * @see org.apache.velocity.runtime.resource.loader.ResourceLoader#getLastModified(org.apache.velocity.runtime.resource.Resource)
     */
    public long getLastModified(Resource resource)
    {
        return 0;
    }
}