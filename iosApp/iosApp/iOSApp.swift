import SwiftUI
import ComposeApp
import FirebaseCore
import FirebaseMessaging

class AppDelegate: NSObject, UIApplicationDelegate {

    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {

        // Initialize Firebase - required for push notifications
        FirebaseApp.configure()

        // Initialize KMPNotifier for iOS
        // showPushNotification: When false, foreground push notifications won't be shown
        // askNotificationPermissionOnStart: Automatically ask for notification permission
        // notificationSoundName: Custom sound file name (nil for default)
        NotifierManager.shared.initialize(
            configuration: NotificationPlatformConfigurationIos(
                showPushNotification: true,
                askNotificationPermissionOnStart: true,
                notificationSoundName: nil
            )
        )

        return true
    }

    // Required for push notifications - set APNs token for Firebase Messaging
    func application(_ application: UIApplication,
                     didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }

    // Required for receiving push notification payload data
    func application(_ application: UIApplication,
                     didReceiveRemoteNotification userInfo: [AnyHashable: Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
    }
}

@main
struct iOSApp: App {

    // Connect AppDelegate to SwiftUI App lifecycle
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
