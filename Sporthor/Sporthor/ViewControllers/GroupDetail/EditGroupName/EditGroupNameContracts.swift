//
//  EditGroupNameContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 12.04.2025.
//
//

import UIKit

protocol EditGroupNamePresenterProtocol: BasePresenterProtocol {
    var view: EditGroupNamePresenterDelegate? { get set }
    var interactor: EditGroupNameInteractorProtocol { get set }
    var router: EditGroupNameRouterProtocol { get set }
    
    func viewDidLoad()
    func saveButtonTapped(name: String?)
    func uploadImage(with image: UIImage)
    func updateProfileImage(imagePath: String)
}

protocol EditGroupNamePresenterDelegate: BasePresenterDelegate {
    func updateUI(with response: EditGroupNameResponse)
}

protocol EditGroupNameInteractorProtocol: BaseInteractorProtocol {
    var delegate: EditGroupNameInteractorDelegate? { get set }
    func fetchGroupSummary(groupId: String) async
    func updateGroupName(groupId: String, name: String?, icon: String?) async
    func uploadImage(_ image: UIImage) async
}

protocol EditGroupNameInteractorDelegate: BaseInteractorDelegate {
    func didFetchGroupSummary(_ response: EditGroupNameResponse)
    func didUpdateGroupName()
    func didFailure(_ error: Error)
    func didUploadSuccess(filePath: String)
}

protocol EditGroupNameRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: EditGroupNameRoutes)
}

enum EditGroupNameRoutes {
    case dismiss
}
