---
title: "Bus XOpenPilot : Proposition d'API et de format de messages"
author:
  - Timothée Jourde
  - Laetitia Leoni
date: lundi 27 février
---

Le projet XOpenPilot vise entre autres à développer un bus de communication suivant un modèle client/serveur.
Le bus est un serveur sur lequel se connectent des clients émetteurs (capteurs) déposants des messages sur le bus, ou des clients récepteurs récupérant ces messages.

Ce document présente une proposition d'API de communication entre le bus et ses clients (émetteurs et récepteurs) décrite en Java.

Ce document présente dans une seconde partie une proposition de format pour les messages échangés entre le bus et ses clients.
  
# API client (émetteur et récepteur)


```java
// RemoteBus.java
package fr.ubordeaux.xopenpilot.libbus.client;

public class RemoteBus
{
   public RemoteBus(String hostname, int port)
   {
   }

   public Sender registerSender(String senderClass, String senderName)
   {
      return new Sender();
   }

   
   /* Méthode de découverte (pour la réception) */
   /*
     Exemples d'appels :
     
     listSenders(null, null);      // liste tout les Sender
     listSenders("GPS", null);     // liste les Sender de classe "GPS"
     listSenders(null, "device");  // liste les Sender de nom "device"
     listSenders("GPS", "device"); // liste les Sender de classe "GPS" ET de nom "device"
    */
   public SenderInfoClient[] listSenders(String senderClass, String senderName)
   {
      return new SenderInfoClient[0];
   }
}

```


---------------------------------------


```java
// Sender.java
package fr.ubordeaux.xopenpilot.libbus.client;

import fr.ubordeaux.xopenpilot.libbus.JSON;

public class Sender
{
   public void deregister()
   {
   }

   public void sendMessage(JSON messageContent)
   {
   }
}
```


---------------------------------------


```java
// SenderInfoClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

public class SenderInfoClient
{
   public String getSenderName()
   {
      return "senderName";
   }

   public String getSenderClass()
   {
      return "senderClass";
   }

   public MessageClient getNextMessage()
   {
      return new MessageClient();
   }

   public MessageClient getLastMessage()
   {
      return new MessageClient();
   }
}
```

---------------------------------------


```java
// MessageClient.java
package fr.ubordeaux.xopenpilot.libbus.client;

import java.util.Date;

public class MessageClient
{
   public Date getDate()
   {
      return new Date();
   }

   public JSON getContent()
   {
      return new JSON();
   }
}
```


---------------------------------------

# API serveur


```java
// Bus.java
package fr.ubordeaux.xopenpilot.libbus.server;

import fr.ubordeaux.xopenpilot.libbus.JSON;

/*
  Une implémentation de bus doit implémenter cette interface.
 */
public interface Bus
{
   /*
     Retourne un identifiant unique.
    */
   public int  registerSender(String senderClass, String senderName);
   public void deregisterSender(int senderId);

   /*
     Invoqué lors de la réception d'un message par un Sender.
    */
   public void messageReceived(int senderId, JSON messageContent);

   
   /*
     Voir RemoteBus.java
    */
   public SenderInfoServer[] listSenders(String senderClass, String senderName);

   /*
     Voir SenderInfoClient.java
    */
   public MessageServer getMessage(int senderId, int messageId);
   public MessageServer getLastMessage(int senderId);
}
```


---------------------------------------


```java
// SenderInfoServer.java
package fr.ubordeaux.xopenpilot.libbus.server;

public class SenderInfoServer
{
   public String getSenderName()
   {
      return "senderName";
   }

   public String getSenderClass()
   {
      return "senderClass";
   }

   public int getSenderId()
   {
      return 0;
   }

   public int getLastMessageId()
   {
      return 0;
   }
}
```


---------------------------------------


```java
// MessageServer.java
package fr.ubordeaux.xopenpilot.libbus.server;

import java.util.Date;

public class MessageServer
{
   public Date getDate()
   {
      return new Date();
   }

   public JSON getContent()
   {
      return new JSON();
   }

   public int getId()
   {
      return 0;
   }
}
```


---------------------------------------


# Format des messages

Les messages sont intégralement au format JSON (encodé en ASCII).

## Enregistrement d'un nouvel émetteur
Client vers serveur :
```json
{ "type": "register", "class": "senderClass", "name": "senderName" }
```
Réponse du serveur :
```json
{ "type": "register", "id": 1234 }
```

## Désenregistrement d'un émetteur
Client vers serveur :
```json
{ "type": "deregister", "id": 1234 }
```

## Envoi d'un message (émetteur)
Client vers serveur :
```json
{ "type": "send", "id": 1234, "content": JSON_DATA }
```

## Découverte des émetteurs
Client (récepteur) vers serveur :
```json
{ "type": "list", "class": "senderClass", "name": "senderName" }
```
Réponse du serveur :
```json
{ "type": "list",
  "results": [
     { "class": "senderClass", "name": "senderName", "id": 1, "lastMessageId": 0 },
     { "class": "senderClass", "name": "senderName", "id": 2, "lastMessageId": 0 },
     { "class": "senderClass", "name": "senderName", "id": 3, "lastMessageId": 0 },
     ...
]}
```

## Récupération d'un message (getNextMessage)
Client (récepteur) vers serveur :
```json
{ "type": "get", "senderId": 1234, "messageId": 0 }
```
Réponse du serveur :
```json
{ "type": "get", "date": DATE, "content": JSON_DATA, "messageId": 0 }
```
où DATE correspond au nombre de millisecondes depuis le 1er janvier 1970, 00:00:00 GMT.

## Récupération d'un message (getLastMessage)
Client (récepteur) vers serveur :
```json
{ "type": "getLast", "senderId": 1234 }
```
Réponse du serveur :
```json
{ "type": "get", "date": DATE, "content": JSON_DATA, "messageId": 0 }
```

## Réponse du serveur en cas de requête invalide ou d'erreur
```json
{ "type": "error", "error": "errorId", "explanation": "error explanation message" }
```
