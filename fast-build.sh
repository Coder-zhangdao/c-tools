 mvn -o clean package -D"gpg.skip=true" -Dmaven.test.skip=true install:install -Ppro
 mvn clean package -Dmaven.test.skip=true deploy -Ppro
