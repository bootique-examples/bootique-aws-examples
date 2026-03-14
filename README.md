[![verify](https://github.com/bootique-examples/bootique-aws-examples/actions/workflows/verify.yml/badge.svg)](https://github.com/bootique-examples/bootique-aws-examples/actions/workflows/verify.yml)

# Bootique 4.x AWS Examples

A simple example of configuring and using an AWS S3 client in a [Bootique](http://bootique.io) app.

Different Git branches contain example code for different versions of Bootique:
* [4.x](https://github.com/bootique-examples/bootique-aws-examples/tree/4.x)
* [3.x](https://github.com/bootique-examples/bootique-aws-examples/tree/3.x)
* [2.x](https://github.com/bootique-examples/bootique-aws-examples/tree/2.x)
* [1.x](https://github.com/bootique-examples/bootique-aws-examples/tree/1.x)


## Prerequisites

To build and run the project, ensure you have the following installed on your machine:

* Docker
* Java 21 or newer
* Maven
* Access to an S3 bucket on AWS (for testing)

and then follow these steps:

## Checkout
```
git clone git@github.com:bootique-examples/bootique-aws-examples.git
cd bootique-aws-examples
```

## Build and package

Run the following command to build the code, run the tests and package the app:
```
mvn clean package
```

## Run

The following command prints a help message with supported options:

```bash  
java -jar target/bootique-aws-examples-4.0.jar
```

Get an Amazon account that you can play with. Take note of the access and secret keys. Copy `config.sample.yml` file 
to `config.yml`. Put both keys in and the Bucket default region in `config.yml`. (Make sure the region matches the 
bucket location). To list the bucket contents run the `--list` command:

```
java -jar target/bootique-aws-examples-4.0.jar -c config.yml -l -b mybucket
```

To store some text in a file in a bucket, run `--send-text-to-s3` command:

```
java -jar target/bootique-aws-examples-4.0.jar -c config.yml -s \
   -b mybucket -t "hello aws" -p 'somefolder/myfile.txt'
```

You can rerun the list command again to check that the file got created.
