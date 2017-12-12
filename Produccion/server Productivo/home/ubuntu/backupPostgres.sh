#!/bin/sh
#          ##
##                                                                          ##
### ====================================================================== ###
# Este script soluciona mi problemilla
# Script template para demonios
sudo -u postgres pg_dump -U postgres  -w -f  /home/ubuntu/backups/dumpBDInstituto.sql -d InstitutoProduccion
exit
