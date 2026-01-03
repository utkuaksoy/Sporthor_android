//
//  LocationPresenter.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import Foundation
import CommonKit
import CoreLocation
import MapKit

final class LocationPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: LocationPresenterDelegate? {
        get { return self.baseView as? LocationPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: LocationInteractorProtocol {
        get { return self.baseInteractor as! LocationInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: LocationRouterProtocol {
        get { return self.baseRouter as! LocationRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: LocationPresenterDelegate,
         interactor: LocationInteractorProtocol,
         router: LocationRouterProtocol,
         delegate: LocationDelegate?,
         isDarkTheme: Bool = true,
         isPlacemark: Bool) {
        super.init()
        self.locationDelegate = delegate
        self.isDarkTheme = isDarkTheme
        self.isPlacemark = isPlacemark
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var items: [MKMapItem] = []
    private var isFoundNearbyPlace: Bool = false
    private var searchWorkItem: DispatchWorkItem?
    var locationManager: CLLocationManager = CLLocationManager()
    private var locations: [CLLocation] = []
    private weak var locationDelegate: LocationDelegate?
    var isDarkTheme: Bool = true
    private var isPlacemark: Bool = false
}

// MARK: - LocationPresenterProtocol
extension LocationPresenter: LocationPresenterProtocol {
    func viewDidLoad() {
        view?.checkLocationPermission()
        view?.didSetBackgroundColor(isDarkTheme ? .black : .white)
        view?.didSetTitle("Konumlar")
        view?.prepareNavigationBar()
        view?.prepareUI()
    }
    
    private func navigate(_ routes: LocationRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedTextRight() {
        navigate(.dismiss(delegate: nil, name: ""))
    }
    
    func deniedLocation() {
        navigate(.showAlertController(BaseHelper.shared.showPermissionAlert(for: .location)))
    }
    
    func nearbyPlaces(locations: [CLLocation]) {
        guard let coordinate = locations.first?.coordinate, !isFoundNearbyPlace else { return }
        locationManager.stopUpdatingLocation()
        self.locations = locations
        let categories: [MKPointOfInterestCategory] = [
            .stadium, .fitnessCenter, .nationalPark, .park, .parking, .restaurant, .cafe, .foodMarket, .school, .university
        ]
        
        let filter = MKPointOfInterestFilter(including: categories)
        let request = MKLocalPointsOfInterestRequest(center: coordinate, radius: 5000)
        request.pointOfInterestFilter = filter

        let search = MKLocalSearch(request: request)
        search.start { [weak self] response, error in
            guard let self, let mapItems = response?.mapItems else { return }
            self.items = mapItems
            self.view?.reloadData()
            isFoundNearbyPlace = true
        }
    }
    
    func searchPlaces(query: String) {
        searchWorkItem?.cancel()
        
        guard !query.isEmpty else {
            isFoundNearbyPlace = false
            nearbyPlaces(locations: locations)
            return
        }
        
        let workItem = DispatchWorkItem { [weak self] in
            guard let self = self else { return }
            self.search(query)
        }
        
        searchWorkItem = workItem
        DispatchQueue.global().asyncAfter(deadline: .now() + 0.5, execute: workItem)
    }
    
    private func search(_ query: String) {
        guard let userLocation = locationManager.location?.coordinate else { return }
        
        let request = MKLocalSearch.Request()
        request.naturalLanguageQuery = query
        request.region = MKCoordinateRegion(center: userLocation,
                                            latitudinalMeters: 20000,
                                            longitudinalMeters: 20000)

        let search = MKLocalSearch(request: request)
        search.start { [weak self] response, error in
            guard let self,
                  let mapItems = response?.mapItems, !mapItems.isEmpty else { return }
            self.items = mapItems
            self.view?.reloadData()
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let selectedItem = items[safe: indexPath.row],
              let name = selectedItem.name,
              !name.isEmpty else { return }
        if isPlacemark {
            navigate(.dismissPlacemark(delegate: locationDelegate,
                                       placemark: selectedItem.placemark))
        } else {
            navigate(.dismiss(delegate: locationDelegate, name: name))
        }
    }
}

// MARK: - LocationInteractorDelegate
extension LocationPresenter: LocationInteractorDelegate {

}
