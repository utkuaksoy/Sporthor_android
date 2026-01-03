//
//  SendClubAuthorizationLetterContracts.swift
//  Sporthor
//
//  Created by derTurke on 20.05.2025.
//
//

import Foundation

protocol SendClubAuthorizationLetterPresenterProtocol: BasePresenterProtocol {
    var view: SendClubAuthorizationLetterPresenterDelegate? { get set }
    var interactor: SendClubAuthorizationLetterInteractorProtocol { get set }
    var router: SendClubAuthorizationLetterRouterProtocol { get set }
    var files: [FileModel] { get set }
    var sportClub: SportClub { get set }
    
    func viewDidLoad()
    func didTappedCKButton(_ tag: Int)
    func didTappedAddNewDocument()
    func didTappedUploadDocumentAt(_ indexPath: IndexPath?, isDelete: Bool)
    func copyFile(_ url: URL)
    func openTermsOfUse()
    func openPrivacyPolicy()
}

protocol SendClubAuthorizationLetterPresenterDelegate: BasePresenterDelegate {
    func didSetTitleAndDescriptionText(_ title: String, _ description: String)
    func prepareUI()
    func reloadData()
    func openDocumentPicker()
}

protocol SendClubAuthorizationLetterInteractorProtocol: BaseInteractorProtocol {
    var delegate: SendClubAuthorizationLetterInteractorDelegate? { get set }
    func updateSportClubFiles(_ request: [String: Any]) async
    func removeSportClubFiles(_ request: [String: Any]) async
    func uploadFile(url: URL) async
}

protocol SendClubAuthorizationLetterInteractorDelegate: BaseInteractorDelegate {
    func didSportClub(_ sportClub: SportClub?)
    func didUploadFile(_ filePath: String)
}

protocol SendClubAuthorizationLetterRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SendClubAuthorizationLetterRoutes)
}

enum SendClubAuthorizationLetterRoutes {
    case successSendClubAuthorizationLetter(sportClub: SportClub)
    case home
    case createTrainingGroup(teams: [TeamItemModel])
    case webView(title: String, url: String)
}
