# Colored-be

![Preview](https://github.com/col0redby/colored-be/blob/gif-preview/media/ColoredGif.gif)
### To run locally

##### Start mysql
```shell script
$ docker run -d -p 3310:3306 -e MYSQL_ROOT_PASSWORD=root mysql --default-authentication-plugin=mysql_native_password
```

##### Create Sqitch user
```sql
CREATE USER '******'@'%' IDENTIFIED BY '******';

GRANT ALL PRIVILEGES ON *.* TO '******'@'%';
```

##### Setup mysql client
Place created above creds into ~/.my.cnf like
```shell script
[client]
user     = ******
password = ******
```

##### Apply migrations
```shell script
$ cd sqitch
$ sqitch deploy --target Dev
```

##### Build docker image
```shell script
$ cd colored-be
$ sbt docker:publishLocal
```

##### Start application
You may be need to change ```aws.credentialsProvider.config``` and ```aws.credentialsProvider.credentials``` in 
```application-local.conf``` with your ```$HOME``` prefix.

```shell script
$ cd colored-be
$ docker run -p 8070:8070 -v $(pwd)/src/main/resources/:$(pwd)/src/main/resources/ -v $HOME/.aws:$HOME/.aws colored-be:0.1.0-SNAPSHOT --config-file $(pwd)/src/main/resources/application-local.conf
```

> Note, you must have an aws credentials placed here ```$HOME/.aws``` since we are using ```ProfileCredentialsProvider``` for aws sdk. 
