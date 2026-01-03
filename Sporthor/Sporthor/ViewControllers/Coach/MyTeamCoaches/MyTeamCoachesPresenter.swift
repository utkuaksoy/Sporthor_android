//
//  MyTeamCoachesPresenter.swift
//  Sporthor
//
//  Created by derTurke on 21.07.2025.
//
//

import Foundation

extension Notification.Name {
    static let didAddCoachAtTeam = Notification.Name("didAddCoachAtTeam")
}

final class MyTeamCoachesPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: MyTeamCoachesPresenterDelegate? {
        get { return self.baseView as? MyTeamCoachesPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: MyTeamCoachesInteractorProtocol {
        get { return self.baseInteractor as! MyTeamCoachesInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: MyTeamCoachesRouterProtocol {
        get { return self.baseRouter as! MyTeamCoachesRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self)
    }
    
    // MARK: - Initialize
    init(view: MyTeamCoachesPresenterDelegate,
         interactor: MyTeamCoachesInteractorProtocol,
         router: MyTeamCoachesRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var clubs: [GetClubsAndDetailClub] = []
}

// MARK: - MyTeamCoachesPresenterProtocol
extension MyTeamCoachesPresenter: MyTeamCoachesPresenterProtocol {
    func viewDidLoad() {
        NotificationCenter.default.addObserver(self,
                                               selector: #selector(handleDidAddCoachAtTeam),
                                               name: .didAddCoachAtTeam,
                                               object: nil)
        view?.didSetTitle("Antrenörlerim")
        view?.prepareNavigationBar()
        view?.prepareUI()
        getClubsAndDetails()
    }
    
    
    
    private func getClubsAndDetails() {
        let request: [String: Any] = [:]
        Task { @MainActor in
            await interactor.getClubsAndDetails(request)
        }
    }
    
    private func navigate(_ routes: MyTeamCoachesRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        default:
            break
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let model = clubs[safe: indexPath.row] else { return }
        navigate(.detail(model: model))
    }
    
    @objc private func handleDidAddCoachAtTeam() {
        getClubsAndDetails()
    }
}

// MARK: - MyTeamCoachesInteractorDelegate
extension MyTeamCoachesPresenter: MyTeamCoachesInteractorDelegate {
    func didGetClubsAndDetails(_ clubs: [GetClubsAndDetailClub]) {
        self.clubs = clubs
        view?.reloadData()
    }
}
