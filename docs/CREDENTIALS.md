Getting Necessary Credemtials
=============================

## Sonatype

Sign up for a [Sonatype account](https://issues.sonatype.org/secure/Signup!default.jspa)  using your Coursera email address.
To be added to the open source Coursera organization, you need to file a ticket like [this one](https://issues.sonatype.org/browse/OSSRH-35833),
and to get some current member of the organization to vouch for you, by commenting on the ticket.

Having done that you then add the following to your `~/.sbt/0.13/plugins/gpg.sbt` file:
```
  addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")
```
Add your Sonatype credentials to SBT by adding the following to `~/.sbt/0.13/sonatype.sbt`
```
credentials += Credentials("Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        "<sonatype_username>",
        "<sonatype_password>")
```

You can sign in with this account at [Sonatype's Nexus Repository](https://oss.sonatype.org/#welcome) to see both the active releases,
and the staged releases.

## PGP

You need a key integrated with sbt for signing. 

Install gpg with:
```
brew install gpg
```
If you already have a key, it is likely not in the right place 
(e.g. in `.gnupg` and not `.sbt/gpg`).

If you wish to generate a new key, the following 
will also upload your PGP.
Run SBT and enter the following on the SBT prompt
```
set pgpReadOnly := false
pgp-cmd gen-key
pgp-cmd list-keys
pgp-cmd send-key <your_username_here@coursera.org> hkp://pool.sks-keyservers.net
```

## Bintray

Sign up for a [bintray open source account](https://bintray.com/signup/oss) 
using your coursera email address as well,
this can be with your github account, for example. 
If needed, use some artificial organization id like my-name-coursera during the signup workflow. 

Add your public key to [your profile](https://bintray.com/profile/edit).

After you have an account, 
ask admin (currently Jeremy, David G., Matt) 
to add you to the Coursera organization as an admin.

Create an API key in the [web console](https://bintray.com/profile/edit). 
Add it to `~/.bintray/.credentials` 

```
realm = Bintray API Realm
host = api.bintray.com
user = ***HERE***
password = ***SECRET***
```

## Artifactory

The scripts assume you have your `~/.artifactory_credentials` file correctly configured.
