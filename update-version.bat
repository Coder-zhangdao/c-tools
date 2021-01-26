export GPG_TTY=$(tty)
mvn versions:set -DnewVersion=1.1.0-SNAPSHOT -DgenerateBackupPoms=false
mvn versions:commit
mvn clean deploy -Ppro
