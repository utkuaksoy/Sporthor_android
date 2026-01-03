//
//  SelectionContracts.swift
//  Sporthor
//
//  Created by derTurke on 14.04.2025.
//
//

import Foundation

protocol SelectionPresenterProtocol: BasePresenterProtocol {
    var view: SelectionPresenterDelegate? { get set }
    var interactor: SelectionInteractorProtocol { get set }
    var router: SelectionRouterProtocol { get set }
    var model: [SelectionModel] { get set }
    
    func viewDidLoad()
    func didSelectItemAt(_ indexPath: IndexPath)
}

protocol SelectionPresenterDelegate: BasePresenterDelegate {
    func didSetCustomTitle(_ title: String)
    func prepareUI()
}

protocol SelectionInteractorProtocol: BaseInteractorProtocol {
    var delegate: SelectionInteractorDelegate? { get set }
}

protocol SelectionInteractorDelegate: BaseInteractorDelegate {
}

protocol SelectionRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SelectionRoutes)
}

enum SelectionRoutes {

}

protocol SelectionViewDelegate: AnyObject {
    func didSelectionItem(with model: SelectionModel)
}
