//
//  ProfileEditContracts.swift
//  Sporthor
//
//  Created by derTurke on 9.04.2025.
//
//

import UIKit
import CoreLocation

protocol ProfileEditPresenterProtocol: BasePresenterProtocol {
    var view: ProfileEditPresenterDelegate? { get set }
    var interactor: ProfileEditInteractorProtocol { get set }
    var router: ProfileEditRouterProtocol { get set }
    var profileSummaryResponse: GetProfileSummaryResponse? { get set }
    var branchesAttributesForm: [ProfileSummaryTextFieldRow] { get set }
    
    func viewDidLoad()
    func changeTag(_ id: String)
    func textDidChange(_ text: String, for indexPath: IndexPath)
    func getBranches()
    func didTappedContinueButton()
    func checkCameraPermission()
    func checkPhotoLibraryPermission()
    func uploadImage(_ image: UIImage)
    func didUpdateLocations(locations: [CLLocation])
    func updateRoles()
}

protocol ProfileEditPresenterDelegate: BasePresenterDelegate {
    func prepareUI()
    func reloadData()
    func presentCamera()
    func presentPhotoLibrary()
    func showPermissionAlert(for type: PermissionType)
    func textFieldDisabled(at indexPath: IndexPath)
    func checkLocationPermission()
}

protocol ProfileEditInteractorProtocol: BaseInteractorProtocol {
    var delegate: ProfileEditInteractorDelegate? { get set }
    
    func getProfileSummary() async
    func getBranches() async
    func getBranchAttributes(request: [String: Any]) async
    func updateProfileSummary(_ request: [String: Any]) async
    func uploadImage(_ image: UIImage) async
    func updateProfileImage(_ request: [String : Any]) async
}

protocol ProfileEditInteractorDelegate: BaseInteractorDelegate {
    func didGetProfileSummary(_ response: GetProfileSummaryResponse)
    func didGetBranches(_ response: [ProfileSummaryHighlightsBranch])
    func didGetBranchAttributes(_ response: ProfileSummaryHighlightsBranchAttributes)
    func didUpdateProfileSummary()
    func didUploadSuccess(filePath: String)
}

protocol ProfileEditRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ProfileEditRoutes)
}

enum ProfileEditRoutes {
    case selection(title: String, model: [SelectionModel], delegate: ProfileEditAddBranchDelegate?)
    case back
    case updateRole
}

enum ProfileEditSection: Int, CaseIterable {
    case profileImage = 0
    case profileInfoTitle
    case profileInfo
    case teamInfo
    case attributesTitle
    case attributes
    case attributesForm
}

enum ProfileEditAttributes: Int, CaseIterable {
    case volleyball = 0
    case football
    case basketball
    case none
}

protocol ProfileEditDelegate: AnyObject {
    func didSuccessProfileEdit()
}
