//
//  ProfileEditViewController.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import UIKit
import ComponentKit
import PhotosUI
import BarVisibilityKit
import CommonKit

final class ProfileEditViewController: BaseViewController, TabBarVisibility {
    // MARK: - VIPER Variables
    var presenter: ProfileEditPresenterProtocol {
        get { return self.basePresenter as! ProfileEditPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.separatorStyle = .none
        tableView.allowsSelection = false
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.backgroundColor = .clear
        tableView.removeEmptyCell()
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Değişiklikleri Kaydet",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 1)
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private var backButton: UIBarButtonItem {
        let backImage = Asset.chevronLeftIcon.image.withRenderingMode(.alwaysOriginal)
        return UIBarButtonItem(
            image: backImage,
            style: .plain,
            target: self,
            action: #selector(didTappedBackButton)
        )
    }
    
    private lazy var locationManager: CLLocationManager = {
        let locationManager = CLLocationManager()
        locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        locationManager.delegate = self
        return locationManager
    }()
    
    // MARK: - Members
    private var isNavigationAndTabbarHidden: Bool {
        return navigationController?.viewControllers.count == 1
    }
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        configureTabBarVisibility(at: .willAppear(isHidden: true))
        configureNavigationBarVisibility(at: .willAppear(isHidden: false))
        setUpNavigationBar()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        configureTabBarVisibility(at: .didAppear(isHidden: true))
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        configureTabBarVisibility(at: .willDisappear)
        configureNavigationBarVisibility(at: .willDisappear)
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        configureTabBarVisibility(at: .didDisappear)
    }
    
    // MARK: - Custom Methods
    @objc
    private func didTappedBackButton() {
        navigationController?.popViewController(animated: true)
    }
}

// MARK: - ProfileEditPresenterDelegate
extension ProfileEditViewController: ProfileEditPresenterDelegate {
    func prepareUI() {
        setUpNavigationBar()
        view.addSubview(tableView)
        view.addSubview(continueButton)
        
        NSLayoutConstraint.activate([
            tableView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            continueButton.topAnchor.constraint(equalTo: tableView.bottomAnchor, constant: 16),
            continueButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32),
            continueButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 24),
            continueButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -24),
            continueButton.heightAnchor.constraint(equalToConstant: 46)
        ])
        
        if let navCon = navigationController as? CustomNavigationController {
            navCon.isTextRightBarButtonItem = (
                "Rol Güncelle",
                DesignKitColorName.contentStrong900.color,
                .body04Compact
            )
        }
    }
    
    func setUpNavigationBar() {
        guard !isNavigationAndTabbarHidden else {
            navigationController?.setNavigationBarHidden(isNavigationAndTabbarHidden, animated: false)
            return
        }
        configureNavigationBar()
    }
    
    private func configureNavigationBar() {
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        
        appearance.titleTextAttributes = [
            .foregroundColor: DesignKitColorName.contentStrong900.color,
            .font: UIFont.bold03Compact
        ]
        appearance.shadowColor = .clear
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
        navigationItem.leftBarButtonItem = backButton
        if let navCon = navigationController as? CustomNavigationController {
            navCon.customDelegate = self
        }
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
    
    func presentCamera() {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .camera
        imagePicker.delegate = self
        present(imagePicker, animated: true)
    }
    
    func presentPhotoLibrary() {
        var config = PHPickerConfiguration()
        config.filter = .images
        config.selectionLimit = 1
        
        let picker = PHPickerViewController(configuration: config)
        picker.delegate = self
        present(picker, animated: true)
    }
    
    func showPermissionAlert(for type: PermissionType) {
        let alertController = UIAlertController(
            title: "İzin Gerekli",
            message: "\(type.rawValue) erişimi için ayarlardan izin vermeniz gerekmektedir.",
            preferredStyle: .alert
        )
        
        let settingsAction = UIAlertAction(title: "Ayarlar", style: .default) { _ in
            if let settingsURL = URL(string: UIApplication.openSettingsURLString) {
                UIApplication.shared.open(settingsURL)
            }
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(settingsAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true)
    }
    
    func textFieldDisabled(at indexPath: IndexPath) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            guard let cell = tableView.cellForRow(at: indexPath) as? HorizontalTitleTextFieldTableViewCell else {
                return
            }
            cell.updateEnabled(false)
        }
    }
    
    func checkLocationPermission() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            switch locationManager.authorizationStatus {
            case .authorizedWhenInUse, .authorizedAlways:
                locationManager.startUpdatingLocation()
            default:
                locationManager.requestWhenInUseAuthorization()
            }
        }
    }
}

extension ProfileEditViewController: UITableViewDataSource, UITableViewDelegate {
    func numberOfSections(in tableView: UITableView) -> Int {
        return 7
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch ProfileEditSection(rawValue: section) {
        case .profileImage:
            return 1
        case .profileInfoTitle:
            return 1
        case .profileInfo:
            return presenter.profileSummaryResponse?.profileInfo?.row?.count ?? 0
        case .teamInfo:
            return 1
        case .attributesTitle:
            return 1
        case .attributes:
            return 1
        case .attributesForm:
            return presenter.branchesAttributesForm.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch ProfileEditSection(rawValue: indexPath.section) {
        case .profileImage:
            let cell = ProfileImageEditTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      image: presenter.profileSummaryResponse?.profileImage ?? "",
                      buttonTitle: "Profil fotoğrafını güncelle")
            return cell
        case .profileInfoTitle:
            let cell = TitleTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(title: presenter.profileSummaryResponse?.profileInfo?.title ?? "")
            return cell
        case .profileInfo:
            let cell = HorizontalTitleTextFieldTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bindForRow(delegate: self,
                            model: presenter.profileSummaryResponse?.profileInfo?.row?[indexPath.row],
                            indexPath: indexPath)
            return cell
        case .teamInfo:
            let cell = ProfileEditTeamInformationTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(with: presenter.profileSummaryResponse?.teamInfo,
                      infoImage: Asset.info.name)
            return cell
        case .attributesTitle:
            let cell = TitleTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(title: presenter.profileSummaryResponse?.highlights?.title ?? "")
            return cell
        case .attributes:
            let cell = ProfileEditSelectableBranchesTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bindForBranchModel(delegate: self,
                                    with: presenter.profileSummaryResponse?.highlights?.branches)
            return cell
        case .attributesForm:
            let cell = HorizontalTitleTextFieldTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bindForRow(delegate: self,
                            model: presenter.branchesAttributesForm[indexPath.row],
                            indexPath: indexPath)
            return cell
        default:
            return UITableViewCell()
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = UIView()
        switch ProfileEditSection(rawValue: section) {
        case .teamInfo, .attributesTitle:
            view.backgroundColor = DesignKitColorName.backgroundSoft200.color
        default:
            view.backgroundColor = .clear
        }
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        
        switch ProfileEditSection(rawValue: section) {
        case .teamInfo, .attributesTitle:
            return 3
        case .attributesForm:
            return 16
        default:
            return 0
        }
    }
}

// MARK: - CKButtonDelegate
extension ProfileEditViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedContinueButton()
    }
}

// MARK: - ProfileImageEditTableViewCellDelegate
extension ProfileEditViewController: ProfileImageEditTableViewCellDelegate {
    func didTappedUpdateProfileImageButton() {
        presentImagePickerOptions()
    }
}

// MARK: - HorizontalTitleTextFieldTableViewCellDelegate
extension ProfileEditViewController: HorizontalTitleTextFieldTableViewCellDelegate {
    func horizontalTextFieldDidEndEditing(text: String,
                                          tag: Int,
                                          indexPath: IndexPath) {
        presenter.textDidChange(text, for: indexPath)
    }
}

// MARK: - CKSelectableBranchesViewDelegate
extension ProfileEditViewController: CKSelectableBranchesViewDelegate {
    func tagButtonClicked(_ id: String) {
        presenter.changeTag(id)
    }
    
    func addButtonClicked() {
        presenter.getBranches()
    }
}

extension ProfileEditViewController {
    func presentImagePickerOptions() {
        let alertController = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        
        let cameraAction = UIAlertAction(title: "Kamera", style: .default) { [weak self] _ in
            guard let self else { return }
            self.presenter.checkCameraPermission()
        }
        
        let galleryAction = UIAlertAction(title: "Galeri", style: .default) { [weak self] _ in
            guard let self else { return }
            self.presenter.checkPhotoLibraryPermission()
        }
        
        let cancelAction = UIAlertAction(title: "İptal", style: .cancel)
        
        alertController.addAction(cameraAction)
        alertController.addAction(galleryAction)
        alertController.addAction(cancelAction)
        
        present(alertController, animated: true)
    }
}

// MARK: - UIImagePickerControllerDelegate, UINavigationControllerDelegate
extension ProfileEditViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        picker.dismiss(animated: true)
        if let image = info[.originalImage] as? UIImage {
            presenter.uploadImage(image)
        }
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true)
    }
}

// MARK: - PHPickerViewControllerDelegate

extension ProfileEditViewController: PHPickerViewControllerDelegate {
    func picker(_ picker: PHPickerViewController, didFinishPicking results: [PHPickerResult]) {
        picker.dismiss(animated: true)
        
        guard let result = results.first else { return }
        
        result.itemProvider.loadObject(ofClass: UIImage.self) { [weak self] object, error in
            guard let self = self else { return }
            if let image = object as? UIImage {
                DispatchQueue.main.async {
                    self.presenter.uploadImage(image)
                }
            }
        }
    }
}

// MARK: - CLLocationManagerDelegate
extension ProfileEditViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        presenter.didUpdateLocations(locations: locations)
    }
    
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        if manager.authorizationStatus == .authorizedWhenInUse || manager.authorizationStatus == .authorizedAlways {
            BaseHelper.shared.showIndicator()
            manager.startUpdatingLocation()
        }
    }
}

extension ProfileEditViewController: CustomNavigationControllerDelegate {
    func didTapButton(type: BarButtonItemType) {
        presenter.updateRoles()
    }
}
