//
//  LocationViewController.swift
//  Sporthor
//
//  Created by derTurke on 7.05.2025.
//
//

import UIKit
import ComponentKit
import CoreLocation

final class LocationViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: LocationPresenterProtocol {
        get { return self.basePresenter as! LocationPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(delegate: self,
                                    textColor: DesignKitColorName.contentStrong900.color,
                                    placeholder: "Ara",
                                    placeholderColor: DesignKitColorName.contentSoft600.color,
                                    backgroundColor: DesignKitColorName.backgroundWeak100.color,
                                    cornerRadius: 23,
                                    borderWidth: 1,
                                    font: .body04Compact,
                                    image: Asset.searchbarSearch.image,
                                    clearImage: Asset.searchbarClose.image)
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        return searchBar
    }()
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.delegate = self
        tableView.dataSource = self
        tableView.backgroundColor = .clear
        tableView.removeEmptyCell()
        tableView.separatorStyle = .none
        tableView.allowsSelectionDuringEditing = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        return tableView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - LocationPresenterDelegate
extension LocationViewController: LocationPresenterDelegate {
    override func didSetBackgroundColor(_ color: UIColor) {
        view.backgroundColor = color
    }
    
    func prepareNavigationBar() {
        if let nav = navigationController as? CustomNavigationController {
            nav.customDelegate = self
            nav.isTextRightBarButtonItem = (
                "Bitti",
                presenter.isDarkTheme ? .white : DesignKitColorName.contentStrong900.color,
                .bold03Compact
            )
            nav.navigationBar.titleTextAttributes = [
                .foregroundColor: presenter.isDarkTheme ? UIColor.white : DesignKitColorName.contentStrong900.color,
                .font: UIFont.bold03Compact
            ]
        }
    }
    
    func prepareUI() {
        view.addSubview(searchBar)
        view.addSubview(tableView)
        
        NSLayoutConstraint.activate([
            searchBar.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 8),
            searchBar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            searchBar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            searchBar.heightAnchor.constraint(equalToConstant: 46),
            
            tableView.topAnchor.constraint(equalTo: searchBar.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            tableView.bottomAnchor.constraint(equalTo: view.bottomAnchor)
        ])
    }
    
    func checkLocationPermission() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            presenter.locationManager.delegate = self
            switch presenter.locationManager.authorizationStatus {
            case .authorizedWhenInUse, .authorizedAlways:
                presenter.locationManager.startUpdatingLocation()
            case .denied:
                presenter.deniedLocation()
            default:
                presenter.locationManager.requestWhenInUseAuthorization()
            }
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - UITableViewDataSource
extension LocationViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return presenter.items.isEmpty ? 1 : presenter.items.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if presenter.items.isEmpty {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(image: Asset.warning.image, description: "Sonuç Yok")
            return cell
        } else {
            let cell = LocationTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(with: presenter.items[indexPath.row],
                      isDarkTheme: presenter.isDarkTheme)
            return cell
        }
    }
}

// MARK: - UITableViewDelegate
extension LocationViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
}

// MARK: - CustomNavigationControllerDelegate
extension LocationViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        switch type {
        case .textRight:
            presenter.didTappedTextRight()
        default:
            break
        }
    }
}

// MARK: - CKSearchBarDelegate
extension LocationViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.searchPlaces(query: text)
    }
}

// MARK: - CLLocationManagerDelegate
extension LocationViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        presenter.nearbyPlaces(locations: locations)
    }
}
