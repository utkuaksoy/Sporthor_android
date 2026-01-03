//
//  CalendarMapViewPresenter.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import Foundation
import MapKit

final class CalendarMapViewPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CalendarMapViewPresenterDelegate? {
        get { return self.baseView as? CalendarMapViewPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CalendarMapViewInteractorProtocol {
        get { return self.baseInteractor as! CalendarMapViewInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CalendarMapViewRouterProtocol {
        get { return self.baseRouter as! CalendarMapViewRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: CalendarMapViewPresenterDelegate,
         interactor: CalendarMapViewInteractorProtocol,
         router: CalendarMapViewRouterProtocol,
         delegate: CalendarMapViewDelegate?,
         placemark: CLPlacemark?) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.calendarMapViewDelegate = delegate
        self.placemark = placemark
    }
    
    private weak var calendarMapViewDelegate: CalendarMapViewDelegate?
    private var placemark: CLPlacemark?
}

// MARK: - CalendarMapViewPresenterProtocol
extension CalendarMapViewPresenter: CalendarMapViewPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        if let placemark,
            let coordinate = placemark.location?.coordinate {
            view?.addPinToMap(at: coordinate,
                              title: placemark.name ?? "")
        } else {
            view?.didSetLocationManager()
        }
    }
    
    private func navigate(_ routes: CalendarMapViewRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didUpdateLocation(_ locations: [CLLocation]) {
        guard let location = locations.last else { return }
        let region = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: 1000, longitudinalMeters: 1000)
        view?.setRegion(region)
        
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
            guard let self = self,
                  let placemark = placemarks?.first else { return }
            let title = placemark.name ?? "Bulunduğunuz Konum"
            self.placemark = placemark
            self.view?.addPinToMap(at: location.coordinate,
                                   title: title)
            self.view?.didStopUpdatingLocation()
        }
    }
    
    func locationError() {
        showAlert(type: .error,
                  message: "Konum alınamadı. Lütfen konum servislerini kontrol edin.")
    }
    
    func searchBarTextDidEndEditing(_ text: String) {
        let request = MKLocalSearch.Request()
        request.naturalLanguageQuery = text
        let search = MKLocalSearch(request: request)
        search.start { [weak self] response, error in
            guard let self = self else { return }
            if let mapItem = response?.mapItems.first, let coordinate = mapItem.placemark.location?.coordinate {
                self.placemark = mapItem.placemark
                self.view?.addPinToMap(at: coordinate, title: mapItem.name ?? text)
                
                let region = MKCoordinateRegion(center: coordinate, latitudinalMeters: 1000, longitudinalMeters: 1000)
                self.view?.setRegion(region)
            } else {
                let geocoder = CLGeocoder()
                geocoder.geocodeAddressString(text) { [weak self] placemarks, error in
                    guard let self = self,
                          let placemark = placemarks?.first,
                          let location = placemark.location else { return }
                    self.placemark = placemark
                    self.view?.addPinToMap(at: location.coordinate, title: placemark.name ?? text)
                    
                    let region = MKCoordinateRegion(center: location.coordinate, latitudinalMeters: 1000, longitudinalMeters: 1000)
                    self.view?.setRegion(region)
                }
            }
        }
    }
    
    func didTappedSubmitButton() {
        guard let placemark else {
            showAlert(type: .warning,
                      message: "Lütfen harita üzerinden bir konum seçiniz.")
            return
        }
        navigate(.dismiss(delegate: calendarMapViewDelegate,
                          placemark: placemark))
    }
    
    func addPinToMap(_ coordinate: CLLocationCoordinate2D) {
        let location = CLLocation(latitude: coordinate.latitude, longitude: coordinate.longitude)
        let geocoder = CLGeocoder()
        geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
            guard let self = self,
                  let placemark = placemarks?.first else { return }
            let title = placemark.areasOfInterest?.first ?? placemark.name ?? "Bulunduğunuz Konum"
            self.placemark = placemark
            self.view?.addPinToMap(at: coordinate, title: title)
        }
    }
}

// MARK: - CalendarMapViewInteractorDelegate
extension CalendarMapViewPresenter: CalendarMapViewInteractorDelegate {}
