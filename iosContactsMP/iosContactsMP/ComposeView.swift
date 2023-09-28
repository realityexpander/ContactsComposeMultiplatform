//
//  ComposeView.swift
//  iosContactsMP
//
//  Created by Philipp Lackner on 25.06.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import SwiftUI
import GoogleMaps

struct ComposeView: UIViewControllerRepresentable {
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {
    }
    
    func makeUIViewController(context: Context) -> some UIViewController {
        // Load the Google maps API key from the AppSecrets.plist file
        let filePath = Bundle.main.path(forResource: "AppSecrets", ofType: "plist")!
        let plist = NSDictionary(contentsOfFile: filePath)!
        let googleMapsApiKey = plist["GOOGLE_MAPS_API_KEY"] as! String
        GMSServices.provideAPIKey(googleMapsApiKey)
        GMSServices.setMetalRendererEnabled(true)

        return MainViewControllerKt.MainViewController()
    }

//    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
//        debugger()
//        GMSServices.provideAPIKey("123456")
//        return true
//    }
}
