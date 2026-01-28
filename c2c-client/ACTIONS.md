# C2C Actions & Payloads

This document lists required payload fields for each action used by the WS/CSM flow.

## COMMAND actions (client -> server)

### ROOM_CREATE
- payload: `null` (no fields)

### JOIN_REQUEST
- payload:
  - `roomId` (string)
  - `nickName` (string)

### JOIN_APPROVE
- payload:
  - `roomId` (string)
  - `requestedUserId` (string)
  - `approved` (boolean)

### JOIN
- payload:
  - `roomId` (string)
  - `nickName` (string)

### LEAVE
- payload:
  - `roomId` (string)

### CLIENT_MESSAGE
- payload:
  - `roomId` (string)
  - `message` (string)

### CONN_CLOSED
- payload: `null` (no fields)

### HEARTBEAT
- payload: `null` (no fields)

## NOTIFY/MESSAGE event payloads (server -> client)

### JOIN (NOTIFY)
- payload:
  - `userId` (string)
  - `nickname` (string)

### JOIN_REQUEST (NOTIFY to owner)
- payload:
  - `requestedUserId` (string)
  - `nickname` (string)
  - `roomId` (string)

### JOIN_APPROVE (NOTIFY to requester)
- payload:
  - `requestedUserId` (string)
  - `roomId` (string)
  - `approved` (boolean)

### LEAVE (NOTIFY)
- payload:
  - `userId` (string)
  - `nickname` (string)
  - `newOwnerId` (string, optional)

### CLIENT_MESSAGE (MESSAGE)
- payload:
  - `roomId` (string)
  - `userId` (string)
  - `message` (string)
  - `nickname` (string)

## RESULT payloads (server -> client)

### ROOM_CREATE
- payload:
  - `roomId` (string)
  - `ownerId` (string)
  - `createdAt` (string)

### JOIN
- payload:
  - `roomId` (string)
  - `ownerId` (string)
  - `entries` (array of `{ userId, nickname }`)

### JOIN_REQUEST / JOIN_APPROVE / LEAVE / CLIENT_MESSAGE
- payload: mirrors the request-specific payload described above
