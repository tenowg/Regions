---
layout: default
title: Regions - Downloads
---

## Direct Download
Direct download is required for Server administrators that wish or need to include this plugin on their server for another
plugin to utilize.

On it's own Regions does very little for a server, it requires plugins to use it's functionality.

### Recommended (Jenkins)
There are no real Recommended Builds yet, as Spout isn't ready for public use.

### Development (Jenkins)
The development builds can be found on my Jenkins: <a href="https://tenowg.ci.cloudbees.com/job/RAZs">Jenkins Page</a>

### Source Code (Github)
You can download source from github: <a href="https://github.com/tenowg/Regions/zipball/master">.ZIP</a> or <a href="https://github.com/tenowg/Regions/tarball/master">.TAR.GZ</a>

## Maven Integration
You can include Regions in your plugin by adding the following Maven dependency and repository to your plugin.

**Do Not Shade** Regions into your plugin, this will cause issues with server, and will limit what can be done. Require administrators to install this plugin on their server for optimum use.

<pre><code>&lt;repository>
    &lt;id>demgel-repo&lt;/id>
    &lt;url>http://repository-tenowg.forge.cloudbees.com/snapshot/&lt;/url>
&lt;/repository>

&lt;dependency>
    &lt;groupId>com.thedemgel&lt;/groupId>
    &lt;artifactId>Regions&lt;/artifactId>
    &lt;version>1.0-SNAPSHOT&lt;/version>
&lt;/dependency>
</code></pre>
