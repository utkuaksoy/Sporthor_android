//
//  AboutPresenter.swift
//  Sporthor
//
//  Created by derTurke on 22.07.2025.
//
//

import Foundation

final class AboutPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: AboutPresenterDelegate? {
        get { return self.baseView as? AboutPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: AboutInteractorProtocol {
        get { return self.baseInteractor as! AboutInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: AboutRouterProtocol {
        get { return self.baseRouter as! AboutRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: AboutPresenterDelegate,
         interactor: AboutInteractorProtocol,
         router: AboutRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
}

// MARK: - AboutPresenterProtocol
extension AboutPresenter: AboutPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle("Hakkında")
        view?.prepareUI()
    }
    
    private func navigate(_ routes: AboutRoutes) {
        router.handleRouter(routes)
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
}

// MARK: - AboutInteractorDelegate
extension AboutPresenter: AboutInteractorDelegate {

}
