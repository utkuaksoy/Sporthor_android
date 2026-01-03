//
//  LocationContracts.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import UIKit
import CoreLocation
import MapKit

protocol LocationPresenterProtocol: BasePresenterProtocol {
    var view: LocationPresenterDelegate? { get set }
    var interactor: LocationInteractorProtocol { get set }
    var router: LocationRouterProtocol { get set }
    var items: [MKMapItem] { get set }
    var locationManager: CLLocationManager { get set }
    var isDarkTheme: Bool { get set }
    
    func viewDidLoad()
    func didTappedTextRight()
    func deniedLocation()
    func nearbyPlaces(locations: [CLLocation])
    func searchPlaces(query: String)
    func didSelectRowAt(_ indexPath: IndexPath)
}

protocol LocationPresenterDelegate: BasePresenterDelegate {
    func checkLocationPermission()
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
}

protocol LocationInteractorProtocol: BaseInteractorProtocol {
    var delegate: LocationInteractorDelegate? { get set }
}

protocol LocationInteractorDelegate: BaseInteractorDelegate {
}

protocol LocationRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: LocationRoutes)
}

enum LocationRoutes {
    case dismiss(delegate: LocationDelegate?, name: String)
    case dismissPlacemark(delegate: LocationDelegate?, placemark: CLPlacemark)
    case showAlertController(_ alertController: UIAlertController)
}

protocol LocationDelegate: AnyObject {
    func didSelectLocation(name: String)
    func didSelectPlacemark(placemark: CLPlacemark)
}

extension LocationDelegate {
    func didSelectLocation(name: String) {}
    func didSelectPlacemark(placemark: CLPlacemark) {}
}
