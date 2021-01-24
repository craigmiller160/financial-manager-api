# Financial Manager API

This is the backend service for the Financial Manager application.

## Authentication

This app uses the `sso-oauth2-server` for all authentication. Please don't forget to setup the DB schema from the `oauth2-utils` library.

## Client Secret Setup

The production Client Secret needs to be stored in a Kubernetes secret.

```
kubectl create secret generic financial-manager-api-client-secret --from-literal=client-secret=######
```

## Running Locally

Use the run script, not the normal maven command:

```
sh run.sh
```