#!/bin/bash

###
# #%L
# Camle logging for BEAST and CS-Studio messages
###

# The mapping definition for the Indexes associated with the BEAST


#Create the Index
curl -XPUT 'http://localhost:9200/alarms_eng'
#Set the mapping
curl -XPUT 'http://localhost:9200/alarms_eng/_mapping/BEAST' -d'
{
  "BEAST" : {
        "properties" : {
          "APPLICATION-ID" : {
            "type" : "string"
          },
          "CONFIG" : {
            "type" : "string"
          },
          "CURRENT_SEVERITY" : {
            "type" : "string"
          },
          "CURRENT_STATUS" : {
            "type" : "string"
          },
          "EVENTTIME" : {
            "type" : "date",
            "format" : "yyyy-MM-dd HH:mm:ss.SSS"
          },
          "HOST" : {
            "type" : "string"
          },
          "NAME" : {
            "type" : "string",
            "index" : "not_analyzed"
          },
          "SEVERITY" : {
            "type" : "string"
          },
          "STATUS" : {
            "type" : "string"
          },
          "TEXT" : {
            "type" : "string"
          },
          "TYPE" : {
            "type" : "string"
          },
          "USER" : {
            "type" : "string"
          },
          "VALUE" : {
            "type" : "string"
          }
        }
      }
}'


#Create the Index
curl -XPUT 'http://localhost:9200/alarms_opr'
#Set the mapping
curl -XPUT 'http://localhost:9200/alarms_opr/_mapping/BEAST' -d'
{
  "BEAST" : {
        "properties" : {
          "APPLICATION-ID" : {
            "type" : "string"
          },
          "CONFIG" : {
            "type" : "string"
          },
          "CURRENT_SEVERITY" : {
            "type" : "string"
          },
          "CURRENT_STATUS" : {
            "type" : "string"
          },
          "EVENTTIME" : {
            "type" : "date",
            "format" : "yyyy-MM-dd HH:mm:ss.SSS"
          },
          "HOST" : {
            "type" : "string"
          },
          "NAME" : {
            "type" : "string",
            "index" : "not_analyzed"
          },
          "SEVERITY" : {
            "type" : "string"
          },
          "STATUS" : {
            "type" : "string"
          },
          "TEXT" : {
            "type" : "string"
          },
          "TYPE" : {
            "type" : "string"
          },
          "USER" : {
            "type" : "string"
          },
          "VALUE" : {
            "type" : "string"
          }
        }
      }
}'
