package com.luo.util;

import com.luo.lang.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * resolve the resource location to files
 */
public abstract class ResourceUtils {


    private static final String CLASSPATH_URL_PREFIX = "classpath:";

    private static final String FILE_URL_PREFIX = "file:";
    private static final String JAR_URL_PREFIX = "jar:";
    private static final String WAR_URL_PREFIX = "war:";

    private static final String URL_PROTOCOL_FILe = "file";
    private static final String URL_PROTOCOL_JAR = "jar";
    private static final String URL_PROTOCOL_WAR = "war";
    private static final String URL_PROTOCOL_ZIP = "zip";

    //from websphere
    private static final String URL_PROTOCOL_WAJAR = "wsjar";
    //from Jboss
    private static final String URL_PROTOCOL_VFZIP = "vfzip";
    //a general Jboss vfs file resource
    private static final String URL_PROTOCOL_VFSFILE = "vfsfile";
    //a general Jboss vfs resourCE
    private static final String URL_PROTOCOL_VFS = "vfs";

    private static final String JAR_FILE_EXTENSION = ".jar";

    private static final String JAR_URL_SEPARATOR = "!/";

    private static final String WAR_URL_SEPARATOR = "*/";


    //---
    public static boolean isUrl(@Nullable String resourceLocation) {
        //-1 if null, false
        if (resourceLocation == null) {
            return false;
        }
        //-2 a special "classpath" pseudo URL
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            return true;
        }

        //-3 a standard url
        try {
            new URL(resourceLocation);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    public static URL getURL(String resourceLocation) throws FileNotFoundException {
        //-1 prefix
        Assert.notNull(resourceLocation, "resource location must not be null");
        //-2 if it is a classpath resource
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            //-3 drop off the prefix: classpath:  get the resource path
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            //-4 use cl to get resource by the resource path
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            //-5 ex handle
            if (url == null) {
                String description = "class path resource [" + path + "]";
                throw new FileNotFoundException(description +
                        "can't be resolved to a URL because it does not exist");
            }

            return url;
        }

        //-6 not class path resource
        try {
            //-7 try url
            return new URL(resourceLocation);
        } catch (MalformedURLException e) {
            //-8 no url -> it is a file resource

            try {
                return new File(resourceLocation).toURI().toURL();
            } catch (MalformedURLException ex) {
                throw new FileNotFoundException("Resource location [" + resourceLocation +
                        "] is neither a URL not a well-formed file path");
            }
        }
    }


    public static File getFile(URI resourceUri, String description) throws FileNotFoundException {

        Assert.notNull(resourceUri, "Resource URI must not be null");

        if (!URL_PROTOCOL_FILe.equals(resourceUri.getScheme())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path " +
                    "because it does not reside in the file system: " + resourceUri);

        }

        return new File(resourceUri.getSchemeSpecificPart());

    }

    public static URI toURI(String location) throws URISyntaxException {
        return new URI(StringUtils.replace(location, " ", "%20"));
    }

    public static URI toURI(URL url) throws URISyntaxException {
        return toURI(url.toString());
    }

    /**
     * get file by url and desc
     *
     * @param resourceUrl url
     * @param description desc
     * @return file
     * @throws FileNotFoundException
     */
    public static File getFile(URL resourceUrl, String description) throws FileNotFoundException {
        //-1 pre-check
        Assert.notNull(resourceUrl, "Resource URL must not be null");
        if (!URL_PROTOCOL_FILe.equals(resourceUrl.getProtocol())) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path " +
                    "because it does not reside in the file system: " + resourceUrl);

        }

        //-2 use uri to create a file
        try {
            return new File(toURI(resourceUrl).getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            //-3 use url's file property to create a file
            return new File(resourceUrl.getFile());
        }

    }

    public static File getFile(URL resourceUrl) throws FileNotFoundException {
        return getFile(resourceUrl, "URL");
    }

    /**
     * get file by resource location
     * use method :
     * 1.  public static File getFile(URL resourceUrl)
     * 2.  public static File getFile(URL resourceUrl, String description)
     *
     * @param resourceLocation
     * @return
     * @throws FileNotFoundException
     */
    public static File getFile(String resourceLocation) throws FileNotFoundException {
        //-1 prefix
        Assert.notNull(resourceLocation, "resource location must not be null");
        //-2 if it is a classpath resource
        if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
            //-3 drop off the prefix: classpath:  get the resource path
            String path = resourceLocation.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader cl = ClassUtils.getDefaultClassLoader();
            String description = "class path resource [" + path + "]";
            //-4 use cl to get resource by the resource path
            URL url = (cl != null ? cl.getResource(path) : ClassLoader.getSystemResource(path));
            //-5 ex handle
            if (url == null) {
                throw new FileNotFoundException(description +
                        "can't be resolved to a URL because it does not exist");
            }

            return getFile(url, description);
        }

        //-6 not class path resource
        try {
            //-7 try url
            return getFile(new URL(resourceLocation));
        } catch (MalformedURLException e) {
            //-8 no url -> it is a file resource
            return new File(resourceLocation);
        }
    }


}
