seiso
=====

[![Build Status](https://travis-ci.org/ExpediaDotCom/seiso.svg?branch=master)](https://travis-ci.org/ExpediaDotCom/seiso)

Devops data integration repository.

Please see the [Seiso project site](http://expediadotcom.github.io/seiso/) for project information and documentation.

Set up your dev environment
===========================

There are two options available here. The first is a manual setup, where you install Java, MySQL, RabbitMQ and the app yourself. The other is via Vagrant.

Manual setup
------------

**Installation.** You'll need to install the following:

* Java 8
* MySQL 5.6.x
  * Create the Seiso database schema by running `src/main/sql/seiso-schema.sql`.
  * For now, you can load sample data by running `src/main/sql/seiso-data-sample.sql`. We may replace this with a non-SQL data importer at some point in the future.
* RabbitMQ
  * For visibility we recommend installing the RabbitMQ management plugin.

**Configuration.** Create copies of the following files from `conf-sample` and modify them as appropriate:

* `application.yml`
* `keystore-dev.jks`
* `log4j.xml`

To run Seiso in development mode, you can place them directly in `src/main/resources`.

Vagrant setup
-------------

TODO

Build Seiso
===========

Build Seiso using Gradle:

    $ ./gradlew clean build

Run Seiso
=========

Again we use Gradle:

    $ ./gradlew bootRun

Point your HTML5-enabled browser to **https://localhost:8443** or whatever scheme/port combo you chose during configuration. You should see a home page with a list of services.
