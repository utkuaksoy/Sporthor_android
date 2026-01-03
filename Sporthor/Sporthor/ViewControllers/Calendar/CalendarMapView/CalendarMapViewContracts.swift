//
//  CalendarMapViewContracts.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import Foundation
import MapKit

protocol CalendarMapViewPresenterProtocol: BasePresenterProtocol {
    var view: CalendarMapViewPresenterDelegate? { get set }
    var interactor: CalendarMapViewInteractorProtocol { get set }
    var router: CalendarMapViewRouterProtocol { get set }
    
    func viewDidLoad()
    func didUpdateLocation(_ locations: [CLLocation])
    func locationError()
    func searchBarTextDidEndEditing(_ text: String)
    func didTappedSubmitButton()
    func addPinToMap(_ coordinate: CLLocationCoordinate2D)
}

protocol CalendarMapViewPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func didSetLocationManager()
    func setRegion(_ region: MKCoordinateRegion)
    func addPinToMap(at coordinate: CLLocationCoordinate2D, title: String?)
    func didStopUpdatingLocation()
    
}

protocol CalendarMapViewInteractorProtocol: BaseInteractorProtocol {
    var delegate: CalendarMapViewInteractorDelegate? { get set }
}

protocol CalendarMapViewInteractorDelegate: BaseInteractorDelegate {
}

protocol CalendarMapViewRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CalendarMapViewRoutes)
}

enum CalendarMapViewRoutes {
    case dismiss(delegate: CalendarMapViewDelegate?,
                 placemark: CLPlacemark)
}

protocol CalendarMapViewDelegate: AnyObject {
    func didSelectMapViewLocation(_ placemark: CLPlacemark)
}
