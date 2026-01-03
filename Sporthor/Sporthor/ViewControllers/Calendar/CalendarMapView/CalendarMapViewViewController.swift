//
//  CalendarMapViewViewController.swift
//  Sporthor
//
//  Created by derTurke on 9.06.2025.
//
//

import UIKit
import ComponentKit
import MapKit
import PanModal
import CoreLocation

final class CalendarMapViewViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarMapViewPresenterProtocol {
        get { return self.basePresenter as! CalendarMapViewPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineContentView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        view.translatesAutoresizingMaskIntoConstraints = false
        view.layer.zPosition = 1
        view.isUserInteractionEnabled = true
        let panGesture = UIPanGestureRecognizer(target: self, action: #selector(handlePan(_:)))
        view.addGestureRecognizer(panGesture)
        return view
    }()
    
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        view.layer.zPosition = 2
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(text: "Lokasyon", textColor: .black, textAlignment: .center , font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        label.layer.zPosition = 1
        return label
    }()
    
    private lazy var mapView: MKMapView = {
        let mapView = MKMapView()
        mapView.delegate = self
        mapView.translatesAutoresizingMaskIntoConstraints = false
        mapView.isUserInteractionEnabled = true
        let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleMapTap(_:)))
        mapView.addGestureRecognizer(tapGesture)
        return mapView
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(
            delegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Adres Ara",
            placeholderColor: DesignKitColorName.contentStrong900.color,
            backgroundColor: .white,
            cornerRadius: 22,
            borderWidth: 1,
            borderColor: DesignKitColorName.borderStrong900.color,
            selectedBorderColor: DesignKitColorName.borderStrong900.color,
            font: .body04Compact,
            image: Asset.searchbarSearch.image,
            clearImage: Asset.searchbarClose.image
        )
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        searchBar.layer.zPosition = 99
        return searchBar
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Konumu Al",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    // MARK: - Members
    private let locationManager = CLLocationManager()
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
    @objc private func handlePan(_ gesture: UIPanGestureRecognizer) {
        let translation = gesture.translation(in: view)
        
        switch gesture.state {
        case .changed:
            // Yalnızca aşağıya sürüklemeye izin ver
            if translation.y > 0 {
                view.transform = CGAffineTransform(translationX: 0, y: translation.y)
            }

        case .ended, .cancelled:
            if translation.y > 150 {
                UIView.animate(withDuration: 0.2, animations: {
                    self.view.transform = CGAffineTransform(translationX: 0, y: self.view.frame.height)
                }, completion: { _ in
                    self.dismiss(animated: false)
                })
            } else {
                UIView.animate(withDuration: 0.2) {
                    self.view.transform = .identity
                }
            }

        default:
            break
        }
    }
    
    @objc private func handleMapTap(_ gesture: UITapGestureRecognizer) {
        let point = gesture.location(in: mapView)
        let coordinate = mapView.convert(point, toCoordinateFrom: mapView)
        presenter.addPinToMap(coordinate)
    }
}

// MARK: - CalendarMapViewPresenterDelegate
extension CalendarMapViewViewController: CalendarMapViewPresenterDelegate {
    func prepareUI() {
        view.backgroundColor = .clear
        view.addSubview(mapView)
        scrollLineContentView.addSubview(scrollLineView)
        view.addSubview(scrollLineContentView)
        view.addSubview(titleLabel)
        view.addSubview(searchBar)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineContentView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            scrollLineContentView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            scrollLineContentView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            scrollLineContentView.heightAnchor.constraint(equalToConstant: 30),
            
            scrollLineView.topAnchor.constraint(equalTo: scrollLineContentView.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: scrollLineContentView.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineContentView.bottomAnchor),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            searchBar.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 10),
            searchBar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            searchBar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            searchBar.heightAnchor.constraint(equalToConstant: 44),
            
            mapView.topAnchor.constraint(equalTo: view.topAnchor),
            mapView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            mapView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            mapView.bottomAnchor.constraint(equalTo: view.bottomAnchor),
            
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16)
        ])
    }
    
    func didSetLocationManager() {
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.requestWhenInUseAuthorization()
        locationManager.startUpdatingLocation()
    }
    
    func setRegion(_ region: MKCoordinateRegion) {
        mapView.setRegion(region, animated: true)
    }
    
    func addPinToMap(at coordinate: CLLocationCoordinate2D, title: String? = nil) {
        mapView.removeAnnotations(mapView.annotations)
        
        let annotation = MKPointAnnotation()
        annotation.coordinate = coordinate
        annotation.title = title
        mapView.addAnnotation(annotation)
        
        let region = MKCoordinateRegion(center: coordinate, latitudinalMeters: 1000, longitudinalMeters: 1000)
        mapView.setRegion(region, animated: true)
    }
    
    func didStopUpdatingLocation() {
        locationManager.stopUpdatingLocation()
    }
}

// MARK: - PanModalPresentable
extension CalendarMapViewViewController: PanModalPresentable {
    var allowsExtendedPanScrolling: Bool {
        return true
    }
    
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var longFormHeight: PanModalHeight {
        return .maxHeight
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }
    
    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
}

// MARK: - MKMapViewDelegate
extension CalendarMapViewViewController: MKMapViewDelegate {
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        guard !(annotation is MKUserLocation) else { return nil }

        let identifier = "customPin"
        var view = mapView.dequeueReusableAnnotationView(withIdentifier: identifier)
        if view == nil {
            let customView = UIView(frame: CGRect(x: 0, y: 0, width: 48, height: 48))
            customView.backgroundColor = DesignKitColorName.backgroundPrimaryGreen.color
            customView.layer.cornerRadius = 24
            customView.clipsToBounds = true

            let imageView = UIImageView(image: Asset.pin.image)
            imageView.contentMode = .scaleAspectFit
            imageView.frame = CGRect(x: 15, y: 14, width: 18, height: 20)
            customView.addSubview(imageView)

            let renderer = UIGraphicsImageRenderer(size: customView.bounds.size)
            let image = renderer.image { ctx in
                customView.layer.render(in: ctx.cgContext)
            }

            view = MKAnnotationView(annotation: annotation, reuseIdentifier: identifier)
            view?.canShowCallout = true
            view?.image = image
            view?.frame.size = CGSize(width: 48, height: 48)
            view?.centerOffset = CGPoint(x: 0, y: -24)
        } else {
            view?.annotation = annotation
        }
        return view
    }
}

// MARK: - CKSearchBarDelegate
extension CalendarMapViewViewController: CKSearchBarDelegate {
    func searchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String) {
        presenter.searchBarTextDidEndEditing(text)
    }
}

// MARK: - CLLocationManagerDelegate
extension CalendarMapViewViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        if status == .authorizedWhenInUse || status == .authorizedAlways {
            locationManager.startUpdatingLocation()
        }
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        presenter.didUpdateLocation(locations)
    }
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        presenter.locationError()
    }
}

// MARK: - CKButtonDelegate
extension CalendarMapViewViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedSubmitButton()
    }
}
