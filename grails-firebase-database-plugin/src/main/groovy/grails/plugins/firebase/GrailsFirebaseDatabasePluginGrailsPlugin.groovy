package grails.plugins.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase
import grails.plugins.Plugin
import groovy.util.logging.Slf4j

@Slf4j
class GrailsFirebaseDatabasePluginGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.2.9 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Grails Firebase Database Plugin" // Headline display name of the plugin
    def author = "Matthew Moss"
    def authorEmail = "mossm@objectcomputing.com"
    def description = '''\
Provides access to the Firebase realtime database.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-firebase-database-plugin"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    // def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    // def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    // def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    // def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    // def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    Closure doWithSpring() { { ->
        def config = grailsApplication.config['grails.plugin.firebase']
        if (!config) {
            log.error 'Firebase database disabled: configuration not found'
            return
        }

        GoogleCredentials credentials
        String credentialsPath = config['credentials']
        if (credentialsPath) {
            def serviceAccount = new FileInputStream(credentialsPath)
            credentials = GoogleCredentials.fromStream(serviceAccount)
        }
        else {
            credentials = GoogleCredentials.applicationDefault
        }
        if (!credentials) {
            log.error 'Firebase database disabled: credentials not found'
            return
        }

        String databaseName = config['databaseName']
        String databaseUrl = config['databaseUrl'] ?: (databaseName ? "https://${databaseName}.firebaseio.com" : null)

        if (!databaseUrl) {
            log.error 'Firebase database disabled: neither `databaseName` nor `databaseUrl` specified in configuration'
            return
        }

        def options = new FirebaseOptions.Builder().
                setCredentials(credentials).
                setDatabaseUrl(databaseUrl)

        // If authOverride is specified (boolean true, map, or otherwise), then setDatabaseAuthVariableOverride will be
        // called and will limit privileges accordingly. If unspecified or false, then the override will not be called
        // and the application will have full access (i.e. security rules are bypassed entirely).

        def authOverride = config['authOverride']
        if (authOverride) {
            if (authOverride instanceof Map<String, Object>) {
                // Limit privileges according to the provided Map (e.g. [uid: 'my-service-worker']).
                options.setDatabaseAuthVariableOverride(authOverride)
            }
            else {
                // Limit privileges as that of an unauthorized user.
                options.setDatabaseAuthVariableOverride(null)

                // Warn if value unexpected (at this point, not boolean true).
                if (!(authOverride instanceof Boolean)) {
                    // TODO: WARNING
                }
            }
        }

        FirebaseApp.initializeApp(options.build())

        firebaseDatabase(FirebaseDatabase) { bean ->
            bean.factoryMethod = 'getInstance'
            bean.scope = 'singleton'
        }
    } }

    void doWithDynamicMethods() {
        // TODO Implement registering dynamic methods to classes (optional)
    }

    void doWithApplicationContext() {
        // TODO Implement post initialization spring config (optional)
    }

    void onChange(Map<String, Object> event) {
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
