FROM java:8-jre
MAINTAINER sczyh30 <root@sczyh30.com>

ENV AQ_DIST_FILE  target/universal/samsara-aquarius-0.7.6.zip
ENV AQUARIUS_HOME /usr/aquarius
ENV AQ_WORK_DIR /usr/aquarius/samsara-aquarius-0.7.6/bin

EXPOSE 9000

COPY $AQ_DIST_FILE $AQUARIUS_HOME/

RUN cd /usr/aquarius \
    && unzip samsara-aquarius-0.7.6.zip

RUN rm -rf $AQUARIUS_HOME/samsara-aquarius-0.7.6.zip

WORKDIR $AQ_WORK_DIR
CMD ["./samsara-aquarius"]
