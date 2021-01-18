export GPG_TTY=$(tty)
mvn versions:set -DnewVersion=1.0.1-M1 -DgenerateBackupPoms=false
mvn versions:commit
mvn clean deploy -Ppro
