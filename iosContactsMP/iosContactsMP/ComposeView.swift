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
import MapKit

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

// LEAVE FOR REFERENCE
// class Coordinator: NSObject, MKMapViewDelegate {
// // https://stackoverflow.com/questions/68030024/cant-add-tap-gesture-recognizer-to-swiftui-mkmapview-uiviewrepresentable
//     var control: MapView
//     let sfCoord = CLLocationCoordinate2D(latitude: 37.7749, longitude: -122.4194)

//     init(_ control: MapView) {
//         self.control = control
//     }

//     //Delegate function to listen for annotation selection on your map
//     func mapView(_ mapView: MKMapView, didSelect view: MKAnnotationView) {
//         if let annotation = view.annotation {
//             //Process your annotation here
//             print("Annotation selected")
//         }
//     }
//
//     func mapView(_ mapView: MKMapView, didUpdate userLocation: MKUserLocation) {
//         mapView.centerCoordinate = userLocation.location!.coordinate
//     }
//
//     func mapViewDidFinishLoadingMap(_ mapView: MKMapView) {
//         print("Map loaded")
//     }
//
//     @objc func addAnnotationOnTapGesture(sender: UITapGestureRecognizer) {
//         if sender.state == .ended {
//             print("in addAnnotationOnTapGesture")
//             let point = sender.location(in: control.myMapView)
//             print("point is \(point)")
//             let coordinate = control.myMapView?.convert(point, toCoordinateFrom: control.myMapView)
//             print("coordinate?.latitude is \(String(describing: coordinate?.latitude))")
//             let annotation = MKPointAnnotation()
//             annotation.coordinate = coordinate ?? sfCoord
//             annotation.title = "Start"
//             control.myMapView?.addAnnotation(annotation)
//         }
//     }
// }
//
// struct Donator {
//     let name: String
//     let car: String
//     let coordinates: CLLocationCoordinate2D
// }
//
// struct MapKitView: UIViewRepresentable {
//
//     typealias Context = UIViewRepresentableContext<MapKitView>
//
//     func updateUIView(_ uiView: MKMapView, context: Context) {
//     }
//
//     func makeUIView(context: Context) -> MKMapView {
//         let map = MKMapView()
//         map.delegate = context.coordinator
//         let annotation = MKPointAnnotation()
//
//         let donator = Donator(name: "Philipp", car: "BMW", coordinates: CLLocationCoordinate2D(latitude: 48.2082, longitude: 16.3738))
//         annotation.coordinate = donator.coordinates
//         annotation.title = donator.name
//         annotation.subtitle = donator.car
//         map.addAnnotation(annotation)
//         return map
//     }
//
//     //Coordinator code
//     func makeCoordinator() -> Coordinator {
//         Coordinator()
//     }
// }
