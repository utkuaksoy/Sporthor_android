//
//  PersonsPermissionPresenter.swift
//  Sporthor
//
//  Created by derTurke on 23.02.2025.
//
//

import Foundation

final class PersonsPermissionPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: PersonsPermissionPresenterDelegate? {
        get { return self.baseView as? PersonsPermissionPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: PersonsPermissionInteractorProtocol {
        get { return self.baseInteractor as! PersonsPermissionInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: PersonsPermissionRouterProtocol {
        get { return self.baseRouter as! PersonsPermissionRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: PersonsPermissionPresenterDelegate,
         interactor: PersonsPermissionInteractorProtocol,
         router: PersonsPermissionRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - PersonsPermissionPresenterProtocol
extension PersonsPermissionPresenter: PersonsPermissionPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitleAndDescriptionText(DesignKitL10n.PersonsPermission.title,
                                            DesignKitL10n.PersonsPermission.description)
        view?.prepareUI()
    }
    
    private func navigate(_ routes: PersonsPermissionRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedButton(_ tag: Int) {
        if ApplicationContext.shared.isSelectedCoach || ApplicationContext.shared.isSelectedClubOfficial {
            navigate(.authenticationTeam)
        } else {
            navigate(.home)
        }        
    }
}

// MARK: - PersonsPermissionInteractorDelegate
extension PersonsPermissionPresenter: PersonsPermissionInteractorDelegate {

}
