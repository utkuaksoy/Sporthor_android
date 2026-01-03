//
//  MenuContracts.swift
//  Sporthor
//
//  Created by derTurke on 15.06.2025.
//
//

import Foundation

protocol MenuPresenterProtocol: BasePresenterProtocol {
    var view: MenuPresenterDelegate? { get set }
    var interactor: MenuInteractorProtocol { get set }
    var router: MenuRouterProtocol { get set }
    var menus: [MenuModel] { get set }
    var tempMenu: [MenuModel] { get set }
    
    func viewDidLoad()
    func didTappedBarButton(_ type: BarButtonItemType)
    func openMenu(_ menu: MenuModel)
}

protocol MenuPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
}

protocol MenuInteractorProtocol: BaseInteractorProtocol {
    var delegate: MenuInteractorDelegate? { get set }
    func getMenu() async
}

protocol MenuInteractorDelegate: BaseInteractorDelegate {
    func didGetMenu(_ menus: [MenuModel])
}

protocol MenuRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: MenuRoutes)
}

enum MenuRoutes {
    case back
    case webView(title: String, url: String)
    case createClub
    case updateClub
    case trainingGroup
    case trainingGroupList
    case coachList
    case openSubMenu(menu: [MenuModel], title: String)
    case navigationBack
}
