FROM ubuntu:latest
MAINTAINER Sven Ruppert <sven.ruppert@gmail.com>
LABEL JCON2017 PWA Vaadin UI

USER root
WORKDIR /app

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get install -y curl
RUN apt-get install -y sudo

RUN curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
RUN sudo apt-get install -y nodejs
RUN npm install -g lighthouse
RUN apt-get install -y chromium-browser

RUN apt-get install -y software-properties-common
RUN add-apt-repository -y ppa:webupd8team/java
RUN apt-get update
RUN echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
RUN apt-get install -y oracle-java8-installer

RUN useradd -ms /bin/bash lighthouse
USER lighthouse

RUN mkdir /home/lighthouse/report
WORKDIR /home/lighthouse/report

ENTRYPOINT nohup java -jar fat.jar & \
           lighthouse --chrome-flags="--headless --no-sandbox" ${lighthouse_url}


#           RUN apt-get install -y xvfb
#           ENV DISPLAY=1.5
#           ENV TMP_PROFILE_DIR=$(mktemp -d -t lighthouse.XXXXXXXXXX)
#           RUN mkdir /tmp/lighthouse
#           RUN chmod 777 /tmp/lighthouse
#           ENV TMP_PROFILE_DIR=/tmp/lighthouse
#
#           ENTRYPOINT xvfb-run --server-args='-screen 0, 1024x768x16' \
#                          chromium-browser --user-data-dir=$TMP_PROFILE_DIR \
#                          --start-maximized \
#                          --disable-namespace-sandbox \
#                          --no-sandbox \
#                          --no-first-run \
#                          --remote-debugging-port=9222 "about:blank" && \
#                      lighthouse --port=9222 https://github.com
