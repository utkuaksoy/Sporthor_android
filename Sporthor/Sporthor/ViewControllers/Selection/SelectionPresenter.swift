//
//  SelectionPresenter.swift
//  Sporthor
//
//  Created by derTurke on 14.04.2025.
//
//

import Foundation

final class SelectionPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SelectionPresenterDelegate? {
        get { return self.baseView as? SelectionPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SelectionInteractorProtocol {
        get { return self.baseInteractor as! SelectionInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SelectionRouterProtocol {
        get { return self.baseRouter as! SelectionRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SelectionPresenterDelegate,
         interactor: SelectionInteractorProtocol,
         router: SelectionRouterProtocol,
         title: String,
         model: [SelectionModel],
         delegate: SelectionViewDelegate? = nil) {
        self.title = title
        self.model = model
        self.delegate = delegate
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    var title: String
    var model: [SelectionModel]
    weak var delegate: SelectionViewDelegate?
}

// MARK: - SelectionPresenterProtocol
extension SelectionPresenter: SelectionPresenterProtocol {
    func viewDidLoad() {
        view?.didSetCustomTitle(title)
        view?.prepareUI()
    }
    
    private func navigate(_ routes: SelectionRoutes) {
        router.handleRouter(routes)
    }
    
    func didSelectItemAt(_ indexPath: IndexPath) {
        delegate?.didSelectionItem(with: model[indexPath.row])
    }
}

// MARK: - SelectionInteractorDelegate
extension SelectionPresenter: SelectionInteractorDelegate {

}
