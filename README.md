koka-util
=========

Utility classes for java

Usage
=====

Add the repo to your settings

    <repository>
      <id>kokalabs-bintray</id>
      <url>https://dl.bintray.com/kokalabs/maven/</url>
      <snapshots>
        <enabled>false</enabled>
      </snapshots>
    </repository>

Adds the Bill of Materials (BoM) to your dependencyManagement

    <dependency>
      <groupId>kokalabs.util</groupId>
      <artifactId>koka-util-bom</artifactId>
      <version>0.1-6</version>
      <scope>import</scope>
    </dependency>
    
Finally, add the dependency that you need.  For example, for IO

    <dependency>
      <groupId>${project.groupId}</groupId>
      <artifactId>koka-util-io</artifactId>
    </dependency>

Install
=======

    git clone git://github.com/KokaLabs/koka-util.git
    mvn clean install -f koka-util/pom.xml -s koka-util/settings.xml -T 1C
