//
//  ProfileEditAddBranchContracts.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import Foundation

protocol ProfileEditAddBranchPresenterProtocol: BasePresenterProtocol {
    var view: ProfileEditAddBranchPresenterDelegate? { get set }
    var interactor: ProfileEditAddBranchInteractorProtocol { get set }
    var router: ProfileEditAddBranchRouterProtocol { get set }
    var model: [SelectionModel] { get set }
    
    func viewDidLoad()
    func didSelectItemAt(_ indexPath: IndexPath)
    func didTappedContinueButton()
    func didTappedCloseButton()
}

protocol ProfileEditAddBranchPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
}

protocol ProfileEditAddBranchInteractorProtocol: BaseInteractorProtocol {
    var delegate: ProfileEditAddBranchInteractorDelegate? { get set }
}

protocol ProfileEditAddBranchInteractorDelegate: BaseInteractorDelegate {
}

protocol ProfileEditAddBranchRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: ProfileEditAddBranchRoutes)
}

enum ProfileEditAddBranchRoutes {
    case dismiss(delegate: ProfileEditAddBranchDelegate? = nil, model: [SelectionModel]? = nil)
}

protocol ProfileEditAddBranchDelegate: AnyObject {
    func didSelectItems(_ items: [SelectionModel])
}
