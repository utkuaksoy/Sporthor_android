//
//  SuccessCreateClubPresenter.swift
//  Sporthor
//
//  Created by derTurke on 19.05.2025.
//
//

import Foundation

final class SuccessCreateClubPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: SuccessCreateClubPresenterDelegate? {
        get { return self.baseView as? SuccessCreateClubPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: SuccessCreateClubInteractorProtocol {
        get { return self.baseInteractor as! SuccessCreateClubInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: SuccessCreateClubRouterProtocol {
        get { return self.baseRouter as! SuccessCreateClubRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: SuccessCreateClubPresenterDelegate,
         interactor: SuccessCreateClubInteractorProtocol,
         router: SuccessCreateClubRouterProtocol,
         delegate: SuccessCreateClubDelegate?,
         sportClub: SportClub?,
         infoTitle: String,
         infoDescription: String) {
        super.init()
        self.successCreateClubDelegate = delegate
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.sportClub = sportClub
        self.infoTitle = infoTitle
        self.infoDescription = infoDescription
    }
    
    private weak var successCreateClubDelegate: SuccessCreateClubDelegate?
    private var sportClub: SportClub?
    private var infoTitle: String = ""
    private var infoDescription: String = ""
}

// MARK: - SuccessCreateClubPresenterProtocol
extension SuccessCreateClubPresenter: SuccessCreateClubPresenterProtocol {
    func viewDidLoad() {
        view?.prepareUI()
        view?.prepareTeam(
            image: sportClub?.logo ?? "",
            name: sportClub?.clubName ?? ""
        )
        view?.prepareSuccessHeaderAndDescription(
            header: infoTitle,
            description: infoDescription
        )
    }
    
    private func navigate(_ routes: SuccessCreateClubRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedCKButton(_ tag: Int) {
        switch tag {
        case 0:
            navigate(.sendAuthorizationLetter(delegate: successCreateClubDelegate))
        case 1:
            navigate(.skip(delegate: successCreateClubDelegate))
        default:
            break
        }
    }
}

// MARK: - SuccessCreateClubInteractorDelegate
extension SuccessCreateClubPresenter: SuccessCreateClubInteractorDelegate {
    
}
