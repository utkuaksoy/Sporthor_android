//
//  MenuPresenter.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

final class MenuPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: MenuPresenterDelegate? {
        get { return self.baseView as? MenuPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: MenuInteractorProtocol {
        get { return self.baseInteractor as! MenuInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: MenuRouterProtocol {
        get { return self.baseRouter as! MenuRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Initialize
    init(view: MenuPresenterDelegate,
         interactor: MenuInteractorProtocol,
         router: MenuRouterProtocol,
         menu: [MenuModel] = [],
         isSubMenu: Bool = false,
         subMenuTitle: String = "") {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
        self.menus = menu
        self.tempMenu = menu
        self.isSubMenu = isSubMenu
        self.subMenuTitle = subMenuTitle
    }
    
    var menus: [MenuModel] = []
    var tempMenu: [MenuModel] = []
    var subMenuTitle: String = ""
    var isSubMenu: Bool = false
}

// MARK: - MenuPresenterProtocol
extension MenuPresenter: MenuPresenterProtocol {
    func viewDidLoad() {
        view?.didSetTitle(isSubMenu ? subMenuTitle + " Menü" : "Hızlı Menü")
        view?.prepareNavigationBar()
        view?.prepareUI()
        if !isSubMenu {
            getMenu()
        }
    }
    
    private func navigate(_ routes: MenuRoutes) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.router.handleRouter(routes)
        }
    }
    
    func didTappedBarButton(_ type: BarButtonItemType) {
        switch type {
        case .back:
            navigate(isSubMenu ? .navigationBack : .back)
        default:
            break
        }
    }
    
    private func getMenu() {
        Task { @MainActor in
            await interactor.getMenu()
        }
    }
    
    func openMenu(_ menu: MenuModel) {
        if menu.mainMenu {
            navigate(.openSubMenu(menu: menu.subMenus, title: menu.name))
        } else {
            switch menu.menuKey {
            case .webRedirect:
                navigate(.webView(title: menu.name,
                                  url: menu.url))
            case .mainMenu:
                break
            case .updateClub:
                navigate(.updateClub)
            case .addDocument:
                break
            case .generateClub:
                navigate(.createClub)
            case .trainingGroup:
                navigate(.trainingGroup)
            case .sporterClub:
                break
            case .trainingGroupUsers:
                break
            case .trainingGroupEdit:
                navigate(.trainingGroupList)
            case .coachList:
                navigate(.coachList)
            }
        }
    }
}

// MARK: - MenuInteractorDelegate
extension MenuPresenter: MenuInteractorDelegate {
    func didGetMenu(_ menus: [MenuModel]) {
        self.menus = menus
        self.tempMenu = menus.filter { $0.mainMenu }
        if tempMenu.isEmpty {
            self.tempMenu = menus.flatMap { $0.subMenus }
        }
        view?.reloadData()
    }
}
