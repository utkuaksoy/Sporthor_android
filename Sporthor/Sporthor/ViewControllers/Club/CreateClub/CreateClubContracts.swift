//
//  CreateClubContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import UIKit

protocol CreateClubPresenterProtocol: BasePresenterProtocol {
    var view: CreateClubPresenterDelegate? { get set }
    var interactor: CreateClubInteractorProtocol { get set }
    var router: CreateClubRouterProtocol { get set }
    var clubName: String { get set }
    var location: String { get set }
    var fullAddress: String { get set }
    var foundationDate: String? { get set }
    var selectedBranch: NameValueDetailModel? { get set }
    
    func viewDidLoad()
    func viewWillAppear()
    func viewWillDisappear()
    func presentImagePickerOptions()
    func presentCamera(_ imagePickerViewController: UIImagePickerController)
    func didTapSelection(_ tag: Int)
    func textFieldDidEndEditing(_ text: String, tag: Int)
    func didTappedContinueButton()
    func uploadImage(_ image: UIImage)
}

protocol CreateClubPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func prepareUI()
    func presentCamera(sourceType: UIImagePickerController.SourceType)
    func reloadData()
    func setEditClubImage(_ url: String)
    func setContinueButtonTitle(_ title: String)
    func setNavigationBarHidden(_ isHidden: Bool)
}

protocol CreateClubInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreateClubInteractorDelegate? { get set }
    func uploadImage(_ image: UIImage) async
    func deleteImage(filePath: String) async
    func addSportClub(_ request: [String: Any], isEdit: Bool) async
}

protocol CreateClubInteractorDelegate: BaseInteractorDelegate {
    func didUploadImage(filePath: String)
    func didDeleteImage()
    func didAddSportClub(_ sportClub: SportClub?)
}

protocol CreateClubRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreateClubRoutes)
}

enum CreateClubRoutes {
    case showAlertController(_ alertController: UIAlertController)
    case camera(imagePickerController: UIImagePickerController)
    case location(delegate: LocationDelegate)
    case successCreateClub(delegate: SuccessCreateClubDelegate,
                           sportClub: SportClub,
                           infoTitle: String,
                           infoDescription: String)
    case sendClubAuthorizationLetter(sportClub: SportClub)
    case home
    case createTrainingGroup
    case selection(title: String,
                   model: [SelectionModel],
                   delegate: ProfileEditAddBranchDelegate?,
                   isSingleSelection: Bool)
}
