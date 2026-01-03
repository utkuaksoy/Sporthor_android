//
//  ProfileEditPresenter.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import Foundation
import AVFoundation
import Photos
import UIKit
import CommonKit
import CoreLocation

final class ProfileEditPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ProfileEditPresenterDelegate? {
        get { return self.baseView as? ProfileEditPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ProfileEditInteractorProtocol {
        get { return self.baseInteractor as! ProfileEditInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ProfileEditRouterProtocol {
        get { return self.baseRouter as! ProfileEditRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ProfileEditPresenterDelegate,
         interactor: ProfileEditInteractorProtocol,
         router: ProfileEditRouterProtocol,
         delegate: ProfileEditDelegate? = nil) {
        profileEditViewDelegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private weak var profileEditViewDelegate: ProfileEditDelegate?
    
    var profileSummaryResponse: GetProfileSummaryResponse?
    var branchesAttributesForm: [ProfileSummaryTextFieldRow] = []
    private var branchId: String = ""
    private var location: String = ""
    private let geocoder = CLGeocoder()
    private var isFoundLocation: Bool = false
}

// MARK: - ProfileEditPresenterProtocol
extension ProfileEditPresenter: ProfileEditPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Profili Düzenle")
        view?.prepareUI()
        getProfileSummary()
    }
    
    private func navigate(_ routes: ProfileEditRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    private func getProfileSummary() {
        Task {
            @MainActor in
            await interactor.getProfileSummary()
        }
    }
    
    func changeTag(_ id: String) {
        guard branchId != id else { return }
        branchId = id
        changeToBranchIsSelected(id: id)
        getProfileBranchesAttributesForm(id)
    }
    
    func textDidChange(_ text: String, for indexPath: IndexPath) {
        switch ProfileEditSection(rawValue: indexPath.section) {
        case .profileInfo:
            profileSummaryResponse?.profileInfo?.row?[indexPath.row].text = text
        case .attributesForm:
            branchesAttributesForm[indexPath.row].text = text
            if let index = profileSummaryResponse?.highlights?.branchesAttributes?.firstIndex(where: { $0.branchId == self.branchId }) {
                profileSummaryResponse?.highlights?.branchesAttributes?[index].branchInfoRow = branchesAttributesForm
            }
        default:
            break
        }
    }
    
    private func changeToBranchIsSelected(id: String) {
        if var branches = profileSummaryResponse?.highlights?.branches {
            for i in branches.indices {
                branches[i].isSelected = false
            }
            if let index = branches.firstIndex(where: { $0.branchId == id }) {
                branches[index].isSelected = true
            }
            profileSummaryResponse?.highlights?.branches = branches
            
            view?.reloadData()
        }
    }
    
    private func getProfileBranchesAttributesForm(_ id: String) {
        if let attribute = profileSummaryResponse?.highlights?.branchesAttributes?.first(where: { $0.branchId == id }) {
            branchesAttributesForm = attribute.branchInfoRow ?? []
            view?.reloadData()
        } else {
            getBranchAttributes(id)
        }
    }
    
    func getBranches() {
        Task {
            @MainActor in
            await interactor.getBranches()
        }
    }
    
    private func getBranchAttributes(_ id: String) {
        let request: [String: Any] = ["branchId": id]
        Task {
            @MainActor in
            await interactor.getBranchAttributes(request: request)
        }
    }
    
    func didTappedContinueButton() {
        var request: [String: Any] = [:]
        
        profileSummaryResponse?.profileInfo?.row?.forEach {
            guard let parameterName = $0.parameterName else { return }
            request[parameterName] = $0.text
        }
        
        if let branchAttributes =  profileSummaryResponse?.highlights?.branchesAttributes, !branchAttributes.isEmpty {
            var attributes: [[String: Any]] = []
            
            profileSummaryResponse?.highlights?.branchesAttributes?.forEach { branch in
                guard let branchId = branch.branchId else { return }
                
                branch.branchInfoRow?.forEach { row in
                    guard let parameterName = row.parameterName,
                          let value = row.text else { return }
                    
                    let attribute: [String: Any] = [
                        "branchId": branchId,
                        "parameterName": parameterName,
                        "value": value
                    ]
                    attributes.append(attribute)
                }
            }
            request["attributes"] = attributes
        }
        
        Task {
            @MainActor in
            await interactor.updateProfileSummary(request)
        }
    }
    
    func checkCameraPermission() {
        switch AVCaptureDevice.authorizationStatus(for: .video) {
        case .authorized:
            view?.presentCamera()
        case .notDetermined:
            AVCaptureDevice.requestAccess(for: .video) { [weak self] granted in
                guard let self else { return }
                if granted {
                    DispatchQueue.main.async {
                        self.view?.presentCamera()
                    }
                }
            }
        default:
            view?.showPermissionAlert(for: .camera)
        }
    }
    
    func checkPhotoLibraryPermission() {
        switch PHPhotoLibrary.authorizationStatus(for: .readWrite) {
        case .authorized:
            view?.presentPhotoLibrary()
        case .notDetermined:
            PHPhotoLibrary.requestAuthorization(for: .readWrite) { [weak self] status in
                guard let self else { return }
                if status == .authorized {
                    DispatchQueue.main.async {
                        self.view?.presentPhotoLibrary()
                    }
                }
            }
        default:
            view?.showPermissionAlert(for: .photoLibrary)
        }
    }
    
    func uploadImage(_ image: UIImage) {
        Task {
            @MainActor in
            await interactor.uploadImage(image)
        }
    }
    
    func didUpdateLocations(locations: [CLLocation]) {
        guard let location = locations.last, !isFoundLocation else { return }
        
        geocoder.reverseGeocodeLocation(location) { [weak self] placemarks, error in
            guard let self else { return }
            
            if let placemark = placemarks?.first {
                let district = placemark.subLocality ?? ""
                let city = placemark.locality ?? ""
                let country = placemark.country ?? ""
                self.location = "\(district), \(city), \(country)"
            }
            isFoundLocation = true
            didSetCityTextField()
        }
    }
    
    private func didSetCityTextField() {
        guard let cityIndex = profileSummaryResponse?.profileInfo?.row?.firstIndex(where: { $0.parameterName == "city" }) else {
            return
        }
        guard profileSummaryResponse?.profileInfo?.row?[cityIndex].text == nil else {
            profileSummaryResponse?.profileInfo?.row?[cityIndex].isEnabled = false
            view?.reloadData()
            DispatchQueue.main.async { [weak self] in
                guard let _ = self else { return }
                BaseHelper.shared.hideIndicator()
            }
            return
        }
        if !location.replacingOccurrences(of: ", ", with: "").isEmpty {
            profileSummaryResponse?.profileInfo?.row?[cityIndex].text = location
        }
        profileSummaryResponse?.profileInfo?.row?[cityIndex].isEnabled = false
        view?.reloadData()
        DispatchQueue.main.async { [weak self] in
            guard let _ = self else { return }
            BaseHelper.shared.hideIndicator()
        }
    }
}

// MARK: - ProfileEditInteractorDelegate
extension ProfileEditPresenter: ProfileEditInteractorDelegate {
    func didGetProfileSummary(_ response: GetProfileSummaryResponse) {
        self.profileSummaryResponse = response
        view?.checkLocationPermission()
        if let branchId = response.highlights?.branches?.first?.branchId {
            changeTag(branchId)
        }
        view?.reloadData()
    }
    
    func didGetBranches(_ response: [ProfileSummaryHighlightsBranch]) {
        let selectedBranches = profileSummaryResponse?.highlights?.branches?.compactMap {
            $0.branchId
        } ?? []
        
        let selectionModel = response.compactMap {
            SelectionModel(
                id: $0.branchId,
                value: $0.branchTitle,
                image: $0.branchImage,
                isSelected: selectedBranches.contains($0.branchId ?? "")
            )
        }
        
        guard !selectionModel.isEmpty else { return }
        navigate(.selection(title: "Branş Ekle", model: selectionModel, delegate: self))
    }
    
    
    func didGetBranchAttributes(_ response: ProfileSummaryHighlightsBranchAttributes) {
        let alreadyExists = profileSummaryResponse?.highlights?.branchesAttributes?.contains(where: { $0.branchId == response.branchId }) ?? false
        
        if !alreadyExists {
            profileSummaryResponse?.highlights?.branchesAttributes?.append(response)
            branchesAttributesForm = response.branchInfoRow ?? []
            
        } else {
            let branchAttributes = profileSummaryResponse?.highlights?.branchesAttributes?.first(where: {
                $0.branchId == response.branchId
            })
            branchesAttributesForm = branchAttributes?.branchInfoRow ?? []
        }
        view?.reloadData()
    }
    
    func didUpdateProfileSummary() {
        showAlert(delegate: self,
                  type: .success,
                  message: "İşleminiz başarıyla gerçekleşmiştir.")
    }
    
    func updateRoles() {
        navigate(.updateRole)
    }
    
    func didUploadSuccess(filePath: String) {
        profileSummaryResponse?.profileImage = filePath
        ApplicationContext.shared.authResponse?.profilePhoto = filePath
        view?.reloadData()
        let request: [String: Any] = ["imageUrl": filePath]
        Task {
            @MainActor in
            await interactor.updateProfileImage(request)
        }
        DispatchQueue.main.async { [weak self] in
            guard let _ = self else { return }
            NotificationCenter.default.post(name: .didChangeProfilePhoto, object: nil)
        }
    }
}

// MARK: - ProfileEditAddBranchDelegate
extension ProfileEditPresenter: ProfileEditAddBranchDelegate {
    func didSelectItems(_ items: [SelectionModel]) {
        if !items.contains(where: { $0.isSelected }) {
            profileSummaryResponse?.highlights?.branchesAttributes?.removeAll()
            branchesAttributesForm = []
            profileSummaryResponse?.highlights?.branches?.removeAll()
        } else {
            for item in items {
                let exists = profileSummaryResponse?.highlights?.branches?.contains(where: { $0.branchId == item.id }) ?? false

                if item.isSelected {
                    if !exists {
                        profileSummaryResponse?.highlights?.branches?.append(ProfileSummaryHighlightsBranch(
                            branchImage: item.image,
                            branchTitle: item.value,
                            branchId: item.id
                        ))
                    }
                } else {
                    profileSummaryResponse?.highlights?.branches?.removeAll(where: { $0.branchId == item.id })
                    profileSummaryResponse?.highlights?.branchesAttributes?.removeAll(where: {$0.branchId == item.id })
                }
            }

            if let branches = profileSummaryResponse?.highlights?.branches,
               !branches.contains(where: { $0.isSelected ?? false }) {
                if self.branchId != branches.first?.branchId {
                    self.branchId = branches.first?.branchId ?? ""
                    profileSummaryResponse?.highlights?.branches?[0].isSelected = true
                    getBranchAttributes(branches.first?.branchId ?? "")
                }
            }
        }

        view?.reloadData()
    }
}

extension ProfileEditPresenter: AlertViewDelegate {
    func didTappedAlertButton(_ tag: Int) {
        navigate(.back)
        profileEditViewDelegate?.didSuccessProfileEdit()
    }
}
