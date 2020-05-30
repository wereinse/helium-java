#!/bin/sh

date >> ~/status
echo "post-install.sh ..." >> ~/status

# copy vscode files
# TODO - add .vscode files to docs
#mkdir -p .vscode && cp docs/vscode-template/* .vscode

# source the bashrc-append from the repo
# you can add project specific settings to .bashrc-append and 
# they will be added for every user that clones the repo with Codespaces
# including keys or secrets could be a SECURITY RISK
echo "" >> ~/.bashrc
echo ". ${PWD}/.devcontainer/.bashrc-append" >> ~/.bashrc

date >> ~/status
echo "Updating Packages ..." >> ~/status

DEBIAN_FRONTEND=noninteractive
sudo apt-get update

date >> ~/status
echo "Installing basics ..." >> ~/status

sudo apt-get install -y --no-install-recommends apt-utils dialog curl git

date >> ~/status
echo "Installing mvn ..." >> ~/status

sudo apt-get install -y maven

DEBIAN_FRONTEND=dialog

date >> ~/status
echo "Done" >> ~/status
