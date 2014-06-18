#Google Cloud Messaging (GCM) Demo app

This repo contains an end-to-end demo of GCM, including a server-side implementation as well as a gcm client (Android app) implementation. See the [docs](http://developer.android.com/google/gcm/index.html) on [developer.android.com](http://developer.android.com/index.html) for information regarding the setup that needs to be done on the GCM side.

##Server-side

In this case, the server-side is implemented in Scala using [Play](http://www.playframework.com), because…well, just because. Uses the GCM REST api to send messages to clients. To run the app, you'll need a GCM API key, which can be passed in via the `gcm.api.key` property. By default, Play will look for an environment property named `GCM_API_KEY`. Uses H2 by default, but you can override this by specifying your own `db.default.url` and `db.default.driver` (tho' no guarantees that it will work with every database - I've only tested it with H2 and PostgreSQL).

##Service endpoints

The following REST-ish endpoint are exposed by the service (there's a RAML file in gcm-server that has the schema of the servce - purely there for documentation purposes):

###Create a new session

	POST /sessions/:id
	{
		"gcm_id":"a gcm registration id",
		"os_version": "android os version",
		"app_version": "app version"
	}

###Retrieve a session

	GET /sessions/:id

###Delete a session

	DELETE /sessions/:id

###Post a message 

	POST /sessions/:id/message
	{
		"content":"hello GCM!"
	}

##Running the server

To start the server (with H2):

	$ start -Dgcm.api.key=<your GCM api key>

##GCM client

The GCM client is implemented as an Android application, which registers with GCM, then submits the GCM registration ID, along with a session identifier to the server-side (see above). The build takes two parameters, `gcmSenderId` and `appServerUrl` to specify the GCM sender id and the url of the application server (again, above). 

To build the apk:

	gradle packageDebug -PgcmSenderId=<your GCM sender id> -PappServerUrl=<the url for your server>


