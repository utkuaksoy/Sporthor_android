//
//  ListClubPresenter.swift
//  Sporthor
//
//  Created by derTurke on 3.07.2025.
//
//

import Foundation
import ComponentKit

final class ListClubPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: ListClubPresenterDelegate? {
        get { return self.baseView as? ListClubPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: ListClubInteractorProtocol {
        get { return self.baseInteractor as! ListClubInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: ListClubRouterProtocol {
        get { return self.baseRouter as! ListClubRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: ListClubPresenterDelegate,
         interactor: ListClubInteractorProtocol,
         router: ListClubRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    var sportClubs: [SportClub] = []
    var isDeleted: Bool = false
}

// MARK: - ListClubPresenterProtocol
extension ListClubPresenter: ListClubPresenterProtocol {
    func viewDidLoad() {
        view?.prepareNavigationBar()
        view?.didSetTitle("Kulüp Seç")
        view?.prepareUI()
        getSportClub()
    }
    
    func getSportClub() {
        Task { @MainActor in
            await interactor.getSportClub()
        }
    }
    
    private func navigate(_ routes: ListClubRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedNavigationButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(.back)
        case .textRight:
            unSelectedSportClubs()
            isDeleted.toggle()
            view?.changeDeleteSubmitButtonHiddenState(!isDeleted)
            view?.reloadData()
        default:
            break
        }
    }
    
    func didSelectRowAt(_ indexPath: IndexPath) {
        guard let sportClub = sportClubs[safe: indexPath.row] else { return }
        
        if isDeleted {
            unSelectedSportClubs()
            sportClubs[indexPath.row].isSelected = !sportClubs[indexPath.row].isSelected
            view?.reloadData()
        } else {
            navigate(.editSportClub(sportClub: sportClub))
        }
    }
    
    private func unSelectedSportClubs() {
        for index in sportClubs.indices {
            sportClubs[index].isSelected = false
        }
    }
    
    func ckButtonDidTap(tag: Int) {
        switch tag {
        case 1:
            guard let selectedSportClub = sportClubs.first(where: { $0.isSelected }) else {
                showAlert(type: .warning, message: "Silme işlemi yapabilmek için 1 adet kulüp seçmelisiniz!")
                return
            }
            showCKDefaultAlert(
                delegate: self,
                message: selectedSportClub.clubName + " isimli takımınızı silmek istediğinize emin misiniz?",
                okTitle: "Evet",
                cancelTitle: "Hayır")
        default:
            break
        }
    }
    
    private func removeSportClub() {
        guard let selectedSportClub = sportClubs.first(where: { $0.isSelected }) else { return }
        let request: [String: Any] = ["clubId": selectedSportClub.clubId]
        Task { @MainActor in
            await interactor.removeSportClub(request)
        }
    }
}

// MARK: - ListClubInteractorDelegate
extension ListClubPresenter: ListClubInteractorDelegate {
    func didGetSportClub(_ sportClubs: [SportClub]) {
        self.sportClubs = sportClubs
        view?.changeDeleteSubmitButtonHiddenState(true)
        view?.reloadData()
    }
    
    func didRemoveSportClub() {
        sportClubs.removeAll(where: { $0.isSelected })
        if sportClubs.isEmpty {
            view?.changeDeleteSubmitButtonHiddenState(true)
        }
        view?.reloadData()
    }
}

extension ListClubPresenter: CKDefaultAlertDelegate {
    func ckDefaultAlertDidTapOK() {
        removeSportClub()
    }
}
