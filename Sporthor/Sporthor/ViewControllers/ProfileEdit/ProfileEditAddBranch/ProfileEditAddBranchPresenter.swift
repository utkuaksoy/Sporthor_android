//
//  ProfileEditAddBranchPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.04.2025.
//
//

import Foundation

final class ProfileEditAddBranchPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ProfileEditAddBranchPresenterDelegate? {
        get { return self.baseView as? ProfileEditAddBranchPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ProfileEditAddBranchInteractorProtocol {
        get { return self.baseInteractor as! ProfileEditAddBranchInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ProfileEditAddBranchRouterProtocol {
        get { return self.baseRouter as! ProfileEditAddBranchRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ProfileEditAddBranchPresenterDelegate,
         interactor: ProfileEditAddBranchInteractorProtocol,
         router: ProfileEditAddBranchRouterProtocol,
         delegate: ProfileEditAddBranchDelegate? = nil,
         title: String = "",
         model: [SelectionModel] = [],
         isSingleSelection: Bool = false) {
        self.profileEditAddBranchDelegate = delegate
        self.title = title
        self.model = model
        self.isSingleSelection = isSingleSelection
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    private weak var profileEditAddBranchDelegate: ProfileEditAddBranchDelegate?
    private var title: String = ""
    var model: [SelectionModel] = []
    private var isSingleSelection: Bool = false
}

// MARK: - ProfileEditAddBranchPresenterProtocol
extension ProfileEditAddBranchPresenter: ProfileEditAddBranchPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle(title)
        view?.prepareNavigationBar()
        view?.prepareUI()
    }
    
    private func navigate(_ routes: ProfileEditAddBranchRoutes) {
        router.handleRouter(routes)
    }
    
    func didSelectItemAt(_ indexPath: IndexPath) {
        if isSingleSelection {
            for index in model.indices {
                model[index].isSelected = false
            }
        }
        
        var item = model[indexPath.item]
        item.isSelected = !item.isSelected
        model[indexPath.item] = item
        view?.reloadData()
    }
    
    func didTappedContinueButton() {
        navigate(.dismiss(delegate: profileEditAddBranchDelegate, model: model))
    }
    
    func didTappedCloseButton() {
        navigate(.dismiss(delegate: nil, model: nil))
    }
}

// MARK: - ProfileEditAddBranchInteractorDelegate
extension ProfileEditAddBranchPresenter: ProfileEditAddBranchInteractorDelegate {

}
