package koka.util.xml.resolver.xerces;

import java.io.File;

import org.apache.xerces.util.XMLCatalogResolver;

public final class XMLCatalogResolvers {
  private XMLCatalogResolvers() {}

  public static XMLCatalogResolver from(File... catalogs) {
    String[] filePaths = toPaths(catalogs);
    return new XMLCatalogResolver(filePaths);
  }

  private static String[] toPaths(File... from) {
    int len = from.length;

    String[] paths = new String[len];
    for (int i = 0; i < len; ++i) {
      paths[i] = from[i].getPath();
    }
    return paths;
  }
}
